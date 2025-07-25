import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';
import { ChatService } from '../../services/chat.service';
import { ChatMessageDTO } from '../../models/ChatMessageDTO.model';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { UserService } from '../../services/user.service';
import { ProfileViewDTO } from '../../models/ProfileViewDTO.model';
import { ProfileService } from '../../services/profile-service.service';
import { ChatGroupDTO } from '../../models/ChatGroupDTO.model';
import Swal from 'sweetalert2';
import { distinctUntilChanged, Subscription, take } from 'rxjs';

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
  selectedGroupId: number = -1;
  profileUsername: { [key: number]: string } = {}; // Create an object to store usernames by senderId
  chatGroups: ChatGroupDTO[] = [];
  isAdmin: boolean = false;
  selectedGroup!: ChatGroupDTO;
  private privateMessageSubscription!: Subscription;
  private groupMessageSubscription!: Subscription;
  chatGroupIds: number[] = [];

  constructor(private webSocketService: WebSocketService, private chatService: ChatService, private userService: UserService, private profileService: ProfileService) {}

  ngOnInit() {
    this.loadUser();
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
        this.chatGroupIds = []; // Reset array
        this.chatGroups.forEach(grp => {
          this.openProfileFromId(grp.admin);
          this.chatGroupIds.push(grp.id);
        });
        this.connectSocket();
      },
      error: (error) => {
        console.error('Error fetching chat groups:', error);
      }
    });
  }

  connectSocket(): void {
    this.webSocketService.connect(this.currentUser.id, this.chatGroupIds);

    // Slušamo privatne poruke
    if (this.privateMessageSubscription) {
      this.privateMessageSubscription.unsubscribe();
    }
    
    this.privateMessageSubscription = this.webSocketService.getPrivateMessages().subscribe((message) => {
      if (message) {
        // Samo dodajemo poruku ako je trenutno otvoren chat sa pošiljaocem ili primaocem
        if (this.selectedUserId !== -1 && 
            (message.sender === this.selectedUserId || 
             (message.sender === this.currentUser.id && message.receiver === this.selectedUserId))) {
          this.messages.push(message);
        }
      }
    });

    // Otkazujemo prethodne pretplate
    if (this.groupMessageSubscription) {
      this.groupMessageSubscription.unsubscribe();
    }

    // Pretplaćujemo se samo na poruke za trenutno odabranu grupu
    if (this.selectedGroupId !== -1) {
      this.groupMessageSubscription = this.webSocketService
        .getGroupMessagesForGroup(this.selectedGroupId)
        .subscribe((message) => {
          console.log("📥 Primljena poruka za grupu", this.selectedGroupId, ":", message);
          this.messages.push(message);
        });
    }
  }

  sendMessage(): void { 
    if (this.newMessage.trim()) {
      const message: ChatMessageDTO = {
        id: 0,
        message: this.newMessage,
        sender: this.currentUser.id || -1,
        receiver: this.selectedUserId !== -1 ? this.selectedUserId : -1,
        chatGroup: this.selectedGroupId !== -1 ? this.selectedGroupId : -1,
        timeStamp: new Date().toISOString()
      };
      console.log(message);
      if (this.selectedUserId !== -1) {
        // Privatna poruka
        this.webSocketService.sendPrivateMessage(message);
        // Dodajemo našu poruku u listu poruka (jer backend ne vraća našu poruku nazad)
        this.messages.push(message);
      } else if (this.selectedGroupId !== -1) {
        // Grupna poruka
        this.webSocketService.sendGroupMessage(this.selectedGroupId, message);
        // Grupne poruke će doći kroz WebSocket, ne moramo ih dodavati ručno
      }
  
      this.newMessage = '';
    }
  }

  //dobavlja sve profile za pretragu
  loadProfiles(): void {
    this.profileService.getAllProfiles().subscribe({
      next: (followers) => {
        followers.forEach(follower => {
          if (follower.role === 'User' && follower.id !== this.currentUser?.id) {
            const exists = this.allProfiles.some(profile => profile.id === follower.id);
          
          // If the profile doesn't exist, add it to the allProfiles list
          if (!exists) {
            this.allProfiles.push(follower);
          }
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
        this.selectedGroupId = -1;
        this.messages.forEach(message => {
          this.openProfileFromId(message.sender);
        });
        this.isAdmin = false;
        
        // Resetovanje grupne pretplate
        if (this.groupMessageSubscription) {
          this.groupMessageSubscription.unsubscribe();
          this.groupMessageSubscription = null as any;
        }
      },
      (error) => {
        console.error('Error fetching messages:', error);
      }
    );
  }

  openGroupChat(groupNow: ChatGroupDTO): void {
    this.chatService.getGroupMessages(this.currentUser.id, groupNow.id).subscribe(
      (data) => {
        this.messages = [];
        const allMessages = [...data]; // Ne dodajemo this.messages jer je prazno

        // Filtriramo poruke da bismo uklonili duplikate na osnovu id
        this.messages = allMessages.filter((message, index, self) =>
          index === self.findIndex((m) => m.id === message.id)
        );
        console.log("Poruke grupe:", this.messages);
        this.selectedGroup = groupNow;
        this.selectedGroupId = groupNow.id;
        this.selectedUserId = -1;
        this.chatGroups.forEach(group => {
          if(group.id === groupNow.id) {
            if(group.admin === this.currentUser.id) {
              this.isAdmin = true;
            } else {
              this.isAdmin = false;
            }
          }
        });
        
        // Ažuriramo WebSocket pretplatu kada se promeni grupa
        if (this.groupMessageSubscription) {
          this.groupMessageSubscription.unsubscribe();
        }
        
        this.groupMessageSubscription = this.webSocketService
          .getGroupMessagesForGroup(this.selectedGroupId)
          .subscribe((message) => {
            console.log("📥 Primljena poruka za grupu", this.selectedGroupId, ":", message);
            // Proveravamo da li poruka već postoji u nizu pre dodavanja
            const exists = this.messages.some(m => m.id === message.id && message.id !== 0);
            if (!exists) {
              this.messages.push(message);
            }
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

  createGroup() {
    // Launch SweetAlert2 with an input field for the group name
    Swal.fire({
      title: 'Enter Group Name',
      input: 'text',
      inputPlaceholder: 'Group Name',
      showCancelButton: true,
      confirmButtonText: 'Create Group',
      cancelButtonText: 'Cancel',
      preConfirm: (groupName) => {
        // Validate if the user has entered a group name
        if (!groupName) {
          Swal.showValidationMessage('Please enter a group name');
          return false;
        }
  
        // Call the service to create the group
        return this.chatService.createGroup(this.currentUser.id, groupName).subscribe({
          next: () => {
            // Notify user of success
            Swal.fire('Success', 'Group created successfully', 'success');
            this.ngOnInit();
          },
          error: (error) => {
            // Notify user of error
            Swal.fire('Error', 'Failed to create group', 'error');
          }
        });
      }
    });
  }

  openManageUsersDialog(): void {
    const memberIds = new Set(this.selectedGroup.members);
    const members = this.allProfiles.filter(user => memberIds.has(user.id));
    const nonMembers = this.allProfiles.filter(user => !memberIds.has(user.id));

    let htmlContent = `
  <div class="swal-container" style="
    display: flex;
    gap: 20px;
    justify-content: flex-start;
    max-width: 600px;
    font-family: Arial, sans-serif;
  ">
    <div class="swal-section" style="
      flex: 1;
      padding: 10px;
      background: #f8f9fa;
      border-radius: 8px;
      box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
    ">
      <h3 style="
        font-size: 18px;
        color: #333;
        text-align: left;
      ">Members</h3>
      <div class="swal-list" style="
        display: flex;
        flex-direction: column;
        gap: 10px;
        padding: 10px;
      ">
        ${members.length > 0 ? members.map(user => `
          <div class="swal-item" style="
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 8px;
            border-radius: 6px;
            background: white;
            box-shadow: 0px 1px 4px rgba(0, 0, 0, 0.1);
          ">
            <button class="remove-btn" onclick="removeUser(${user.id})" style="
              width: 36px;
              height: 36px;
              border: none;
              border-radius: 50%;
              cursor: pointer;
              font-size: 18px;
              display: flex;
              align-items: center;
              justify-content: center;
              background: #e74c3c;
              color: white;
            ">❌</button>
            <span style="flex: 1;">${user.name} ${user.surname} (${user.username})</span>
          </div>`).join('') : `<p class="empty-msg" style="font-size: 14px; color: gray; text-align: left;">No members</p>`}
      </div>
    </div>

    <div class="swal-section" style="
      flex: 1;
      padding: 10px;
      background: #f8f9fa;
      border-radius: 8px;
      box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
    ">
      <h3 style="
        font-size: 18px;
        color: #333;
        text-align: left;
      ">Non-members</h3>
      <div class="swal-list" style="
        display: flex;
        flex-direction: column;
        gap: 10px;
        padding: 10px;
      ">
        ${nonMembers.length > 0 ? nonMembers.map(user => `
          <div class="swal-item" style="
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 8px;
            border-radius: 6px;
            background: white;
            box-shadow: 0px 1px 4px rgba(0, 0, 0, 0.1);
          ">
            <button class="add-btn" onclick="addUser(${user.id})" style="
              width: 36px;
              height: 36px;
              border: none;
              border-radius: 50%;
              cursor: pointer;
              font-size: 18px;
              display: flex;
              align-items: center;
              justify-content: center;
              background: #2ecc71;
              color: white;
            ">➕</button>
            <span style="flex: 1;">${user.name} ${user.surname} (${user.username})</span>
          </div>`).join('') : `<p class="empty-msg" style="font-size: 14px; color: gray; text-align: left;">No available users</p>`}
      </div>
    </div>
  </div>
`;

    Swal.fire({
      title: `Manage Users in ${this.selectedGroup.name}`,
      html: htmlContent,
      showCloseButton: true,
      showConfirmButton: false,
      didOpen: () => {
        (window as any).removeUser = (userId: number) => this.removeUserFromGroup(userId);
        (window as any).addUser = (userId: number) => this.addUserToGroup(userId);
      }
    });
  }

  addUserToGroup(userId: number): void {
    if (!this.selectedGroup) return;

    this.chatService.addMemberToGroup(this.selectedGroup.id, userId).subscribe(() => {
      Swal.fire('Success', 'User added to the group!', 'success');
      this.selectedGroup.members.push(userId);
      this.ngOnInit();
    });
  }

  removeUserFromGroup(userId: number): void {
    if (!this.selectedGroup) return;

    this.chatService.removeMemberFromGroup(this.selectedGroup.id, userId).subscribe(() => {
      Swal.fire('Success', 'User removed from the group!', 'success');
      this.selectedGroup.members = this.selectedGroup.members.filter(id => id !== userId);
      this.ngOnInit();
    });
  }

  // Čišćenje resursa pri uništavanju komponente
  ngOnDestroy() {
    if (this.privateMessageSubscription) {
      this.privateMessageSubscription.unsubscribe();
    }
    if (this.groupMessageSubscription) {
      this.groupMessageSubscription.unsubscribe();
    }
  }
}