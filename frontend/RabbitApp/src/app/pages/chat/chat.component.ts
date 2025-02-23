import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';
import { ChatService } from '../../services/chat.service';
import { ChatMessageDTO } from '../../models/ChatMessageDTO.model';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { UserService } from '../../services/user.service';
import { ProfileViewDTO } from '../../models/ProfileViewDTO.model';
import { ProfileService } from '../../services/profile-service.service';
import { ChatGroupDTO } from '../../models/ChatGroupDTO.model';

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
  allProfiles: ProfileViewDTO[] = [];
  selectedUserId: number = -1;
  profileUsername: { [key: number]: string } = {}; // Create an object to store usernames by senderId
  chatGroups: ChatGroupDTO[] = [];

  constructor(private webSocketService: WebSocketService, private chatService: ChatService, private userService: UserService, private profileService: ProfileService) {}

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
          this.loadProfiles();
          this.loadChatGroups();
        } else {
          console.log('No profile found or token expired');

        }
      },
      (error) => {
        console.error('Error loading profile:', error);
      }
    );
  }

  loadChatGroups(): void {
    this.chatService.getUserChatGroups(this.currentUser.id).subscribe({
      next: (groups) => {
        this.chatGroups = groups;
        console.log('Chat Groups:', this.chatGroups);
      },
      error: (error) => {
        console.error('Error fetching chat groups:', error);
      }
    });
  }


  sendMessage() : void {
    if (this.newMessage.trim()) {
      const message: ChatMessageDTO = {
        id: 0,
        message: this.newMessage,
        sender: -1,
        receiver: -1,
        chatGroup: -1,
        timeStamp: ''
      };

      this.webSocketService.sendMessage('/socket-subscriber/send', message);
      this.newMessage = '';
    }
  }

  //dobavlja sve profile za pretragu
  loadProfiles(): void {
    this.profileService.getAllProfiles().subscribe({
      next: (followers) => {
        followers.forEach(follower => {
          if (follower.role === 'User' && follower.id !== this.currentUser?.id) {
            this.allProfiles.push(follower);
          }
        });
      },
      error: (err) => {
        console.error('Error fetching profiles:', err);
      }
    });
  }

  openProfileChat(receiverId: number): void {
    this.chatService.getMessages(this.currentUser.id, receiverId).subscribe(
      (data) => {
        this.messages = data;
        console.log(data);
        this.selectedUserId = receiverId;
        this.messages.forEach(message => {
            this.openProfileFromId(message.sender);
          
        });
      },
      (error) => {
        console.error('Error fetching messages:', error);
      }
    );
  }

  openProfileFromId(senderId: number): void {
    // Check if the username for the senderId is already fetched
    if (!this.profileUsername[senderId]) {
      this.profileService.getProfile(senderId).subscribe(
        (data) => {
          this.profileUsername[senderId] = data?.username || 'No username available';
          
        },
        (error) => {
          console.error('Error fetching profile:', error);
        }
      );
    }
  }

}
