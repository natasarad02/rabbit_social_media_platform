import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { ChatMessageDTO } from '../models/ChatMessageDTO.model';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private stompClient!: Client;
  private privateMessageSubject = new BehaviorSubject<ChatMessageDTO | null>(null);
  private groupMessageSubject = new BehaviorSubject<ChatMessageDTO | null>(null);
  // Mapa za čuvanje subjekata za svaku grupu
  private groupMessageSubjects: Map<number, Subject<ChatMessageDTO>> = new Map();
  private connected = false;

  constructor() {}

  connect(userId: number, groupIds: number[]) {
    if (this.stompClient && this.stompClient.connected) return;

    const socketUrl = 'http://localhost:8080/socket';

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      connectHeaders: {
        'user-id': userId.toString() // Dodajte ID korisnika kao header
      },
      debug: (msg) => console.log(msg),
      reconnectDelay: 5000, // Automatski pokušaj ponovnog povezivanja nakon 5 sekundi
      onConnect: (frame) => {
        console.log('Connected: ', frame);
        this.connected = true;

        // Pretplata na privatne poruke
        this.stompClient.subscribe(`/user/${userId}/queue/messages`, (message: IMessage) => {
          const chatMessage: ChatMessageDTO = JSON.parse(message.body);
          this.privateMessageSubject.next(chatMessage);
        });

        // Pretplata na grupne poruke (svaka grupa posebno)
        groupIds.forEach(groupId => {
          // Kreiramo novi Subject za svaku grupu ako ne postoji
          if (!this.groupMessageSubjects.has(groupId)) {
            this.groupMessageSubjects.set(groupId, new Subject<ChatMessageDTO>());
          }
          
          // Pretplata za svaku grupu posebno
          this.stompClient.subscribe(`/topic/group/${groupId}`, (message: IMessage) => {
            const chatMessage: ChatMessageDTO = JSON.parse(message.body);
            console.log(`Primljena grupna poruka za grupu ${groupId}:`, chatMessage);
            
            // Šaljemo poruku samo kroz subject za tu grupu
            const subject = this.groupMessageSubjects.get(groupId);
            if (subject) {
              subject.next(chatMessage);
            }
            
            // Takođe šaljemo kroz glavni groupMessageSubject za kompatibilnost
            this.groupMessageSubject.next(chatMessage);
          });
        });
      },
      onWebSocketError: (error) => {
        console.error('WebSocket Error:', error);
      },
    });

    this.stompClient.activate();
  }

  sendPrivateMessage(message: ChatMessageDTO) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: '/socket-subscriber/send',
        body: JSON.stringify(message),
      });
    } else {
      console.error('STOMP Client is not connected.');
    }
  }

  sendGroupMessage(groupId: number, message: ChatMessageDTO) {
    if (this.stompClient && this.stompClient.connected) {
      // Proveravamo da li je groupId ispravno postavljen
      if (message.chatGroup !== groupId) {
        message.chatGroup = groupId;
      }
      
      console.log(`Šaljem grupnu poruku za grupu ${groupId}:`, message);
      
      this.stompClient.publish({
        destination: `/socket-subscriber/send`,
        body: JSON.stringify(message),
      });
    } else {
      console.error('STOMP Client is not connected.');
    }
  }

  getPrivateMessages(): Observable<ChatMessageDTO | null> {
    return this.privateMessageSubject.asObservable();
  }

  // Nova metoda - vraća poruke samo za određenu grupu
  getGroupMessagesForGroup(groupId: number): Observable<ChatMessageDTO> {
    if (!this.groupMessageSubjects.has(groupId)) {
      this.groupMessageSubjects.set(groupId, new Subject<ChatMessageDTO>());
    }
    
    return this.groupMessageSubjects.get(groupId)!.asObservable();
  }
  
  // Originalna metoda za kompatibilnost sa postojećim kodom
  getGroupMessages(): Observable<ChatMessageDTO | null> {
    return this.groupMessageSubject.asObservable();
  }
}