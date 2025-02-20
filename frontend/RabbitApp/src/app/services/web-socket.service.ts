import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private stompClient!: Client;

  constructor() {}

  connect() {
    const socket = new SockJS('http://localhost:8080/socket');
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (msg) => console.log(msg), // Optional: Debugging logs
      reconnectDelay: 5000, // Auto-reconnect after 5 seconds
    });

    this.stompClient.onConnect = (frame) => {
      console.log('Connected: ', frame);
      
      // Subscribe to messages
      this.stompClient.subscribe('/socket-publisher/messages', (message: Message) => {
        console.log('Received:', message.body);
      });
    };

    this.stompClient.activate(); 
  }

  sendMessage(destination: string, message: any) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: `/socket-subscriber${destination}`,
        body: JSON.stringify(message),
      });
    } else {
      console.error('STOMP Client is not connected.');
    }
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
      console.log('Disconnected');
    }
  }
}
