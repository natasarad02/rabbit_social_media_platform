import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProfileDTO } from '../../../models/ProfileDTO.model';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfileService } from '../../../services/profile-service.service';

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.css']
})
export class ProfileInfoComponent implements OnInit{
  
  @Input() user: any;
  @Input() isLoggedInUser: boolean = false;
  showModal: boolean = false;

  editedUser: any = {};
  newPassword: string = '';
  confirmPassword: string = '';
  passwordsDoNotMatch: boolean = false;
  allFieldsFilled: boolean = true;
  loggedProfile: ProfileDTO | null = null;
  profileId: number = -1;
  isFollowed: boolean = false;
  following: ProfileDTO[] = []; 
  followers: ProfileDTO[] = []; 
  loggedUserFollowing: ProfileDTO[] = [];
  followingNum: number = 0;
  followersNum: number = 0;
  requestedProfileId: number = -1;
  //modal sa followers and following
  isModalOpen: boolean = false;
  modalTitle: string = '';
  displayedList: ProfileDTO[] = [];
  @Output() updated: EventEmitter<void> = new EventEmitter<void>();

  constructor(private userService: UserService, private auth: AuthService, private route: ActivatedRoute,
     private profileService: ProfileService, private router: Router){}
    
    
  ngOnInit(): void {
    this.loadUser();
  }

  getFollowers() {
    if(this.requestedProfileId){
      this.profileService.getFollowers(this.requestedProfileId).subscribe(
        (data: ProfileDTO[]) => {
            this.followers = data; 
            this.followersNum = data.length;
            console.log(this.followers);
            
        },
        (error) => {
            console.error('Error fetching followers:', error);
        }
    );
   }    
  }

  getFollowing() {
    if(this.requestedProfileId)
      this.profileService.getFollowing(this.requestedProfileId).subscribe(
          (data: ProfileDTO[]) => {
              this.following = data; // Populate the following array
              this.followingNum = data.length; // Update the count
              console.log(this.following);
              
               
              
          },
          (error) => {
              console.error('Error fetching following:', error);
          }
      );
  }


  loadUser(): void {
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.loggedProfile = data;
          this.profileId = this.loggedProfile.id;  
          this.checkisLoggedInUser();
        } else {
          console.log('No profile found or token expired');
          this.profileId = -1;
        }
      },
      (error) => {
        console.error('Error loading profile:', error);
        this.auth.logout();
        this.profileId = -1;
      }
    );
  }

  checkisLoggedInUser(){
    const idParam = this.route.snapshot.paramMap.get('id');
    this.requestedProfileId = idParam ? parseInt(idParam, 10) : -1; 
    this.isLoggedInUser = this.requestedProfileId === this.loggedProfile?.id;    
    this.getFollowers();
    this.getFollowing();
    if(!this.isLoggedInUser)
    {
      this.loadFollowers(this.profileId, this.requestedProfileId);
    }
  }

  openEditModal() {
    this.editedUser = { ...this.user }; 
    this.newPassword = '';
    this.confirmPassword = '';
    this.passwordsDoNotMatch = false;
    this.allFieldsFilled = true;
    this.showModal = true;    
  }

  closeEditModal() {
    this.showModal = false;
  }

  saveChanges() {
    this.passwordsDoNotMatch = this.newPassword !== this.confirmPassword;
    this.allFieldsFilled = !!(
      this.editedUser.name &&
      this.editedUser.surname &&
      this.editedUser.email &&
      this.editedUser.address &&
      (!this.newPassword || this.confirmPassword)
    );

    if (this.passwordsDoNotMatch || !this.allFieldsFilled) {
      return;
    }

    console.log('Saving changes:', this.editedUser, this.newPassword);
    this.user = { ...this.editedUser }; 
    this.closeEditModal();
  } 

  //dodato pracenje i otpracivanje
  followUser() {

    this.profileService.followProfile(this.profileId, this.requestedProfileId).subscribe({
      next: () => {
        console.log('User followed successfully!');
        this.ngOnInit();
      },
      error: (err) => {
        console.error('Error while following user:', err);
      }
    });
  }


  unfollowUser()
  {
    this.profileService.unfollowProfile(this.profileId, this.requestedProfileId).subscribe({
      next: () => {
        console.log('User followed successfully!');
        this.ngOnInit();
      },
      error: (err) => {
        console.error('Error while following user:', err);
      }
    });

  }

  //funkcija koja dobavlja sve koje ulogovan korisnik prati 
  loadFollowers(profileId: number, requestedProfileId: number): void {
    this.profileService.getFollowing(profileId).subscribe({
      next: (followers) => {
        this.loggedUserFollowing = followers;
        this.isFollowed = this.loggedUserFollowing.some(follower => follower.id === requestedProfileId);
        console.log('Following loaded:', this.loggedUserFollowing);
      },
      error: (err) => {
        console.error('Error fetching followers:', err);
      }
    });
  }

  //ovo je za followers and following modal
  showModalFollow(type: 'followers' | 'following'): void {
    this.isModalOpen = true;
    if (type === 'followers') {
      this.modalTitle = 'Followers';
      this.displayedList = this.followers;
    } else {
      this.modalTitle = 'Following';
      this.displayedList = this.following;
    }
  }

  closeModalFollow(): void {
    this.isModalOpen = false;
    this.displayedList = [];
  }

  navigateToUser(userId: number) : void {
    this.closeModalFollow();
    this.router.navigate([`/profile/${userId}`]).then(() => {
      this.ngOnInit(); 
      this.refreshPostData();
    });
    
  }

  private refreshPostData(): void {
    this.updated.emit();
  }

  



}
