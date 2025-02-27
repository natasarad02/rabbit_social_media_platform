import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { ChatMessageDTO } from '../models/ChatMessageDTO.model';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private stompClient!: Client;
  private messageSubject = new BehaviorSubject<ChatMessageDTO | null>(null);
  private connected = false;

  constructor() {}

  connect() {
    if (this.connected) return;

    const socketUrl = 'http://localhost:8080/socket';

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      debug: (msg) => console.log(msg),
      reconnectDelay: 5000, // Automatski pokušaj ponovnog povezivanja nakon 5 sekundi
      onConnect: (frame) => {
        console.log('Connected: ', frame);
        this.connected = true;

        // Pretplata na privatne poruke
        this.stompClient.subscribe('/user/queue/messages', (message: IMessage) => {
          const chatMessage: ChatMessageDTO = JSON.parse(message.body);
          this.messageSubject.next(chatMessage);
        });

        // Pretplata na grupne poruke
        this.stompClient.subscribe('/socket-publisher/messages', (message: IMessage) => {
          const chatMessage: ChatMessageDTO = JSON.parse(message.body);
          this.messageSubject.next(chatMessage);
        });
      },
      onWebSocketError: (error) => {
        console.error('WebSocket Error:', error);
      },
    });

    this.stompClient.activate();
  }

  sendMessage(destination: string, message: ChatMessageDTO) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: destination,
        body: JSON.stringify(message),
      });
    } else {
      console.error('STOMP Client is not connected.');
    }
  }

  getMessages(): Observable<ChatMessageDTO | null> {
    return this.messageSubject.asObservable();
  }
}
