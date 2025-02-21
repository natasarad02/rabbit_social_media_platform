import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';
import { ChatService } from '../../services/chat.service';
import { ChatMessageDTO } from '../../models/ChatMessageDTO.model';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit{

  messages: ChatMessageDTO[] = [];
  newMessage: string = '';
  currentUser!: ProfileDTO;
  receiver!: ProfileDTO;

  constructor(private webSocketService: WebSocketService, private chatService: ChatService, private userService: UserService) {}

  ngOnInit() {
    this.loadUser();
    this.webSocketService.connect();
    

    // Slušanje novih poruka
    this.webSocketService.getMessages().subscribe((message) => {
      if (message) {
        this.messages.push(message);
      }
    });
  }

  loadUser(): void {
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.currentUser = data;
        } else {
          console.log('No profile found or token expired');

        }
      },
      (error) => {
        console.error('Error loading profile:', error);
      }
    );
  }

  sendMessage() {
    if (this.newMessage.trim()) {
      const message: ChatMessageDTO = {
        id: 0,
        message: this.newMessage,
        sender: this.currentUser,
        receiver: this.receiver,
        chatGroup: null,
        timeStamp: new Date()
      };

      this.webSocketService.sendMessage('/socket-subscriber/send', message);
      this.newMessage = '';
    }
  }

}
