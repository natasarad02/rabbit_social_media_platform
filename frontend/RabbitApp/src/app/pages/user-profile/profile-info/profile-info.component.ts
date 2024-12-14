import { Component, Input, OnInit } from '@angular/core';
import { ProfileDTO } from '../../../models/ProfileDTO.model';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute } from '@angular/router';
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
<<<<<<< HEAD
  isFollowed: boolean = false;
  followers: ProfileDTO[] = []; //sve koje ulogovan korisnik prati
  requestedProfileId: number = -1;

  constructor(private userService: UserService, private auth: AuthService, private route: ActivatedRoute, private profileService: ProfileService){}
    
  ngOnInit(): void {
    this.loadUser();
   // this.checkisLoggedInUser();
=======
  following: ProfileDTO[] = []; 
  followers: ProfileDTO[] = []; 
  followingNum: number = 0;
  followersNum: number = 0;
  requestedProfileId: number | null= -1;

  constructor(private userService: UserService, private auth: AuthService, private route: ActivatedRoute,
    private profileService: ProfileService
  ){}
    
  ngOnInit(): void {
    this.loadUser();
>>>>>>> a0c085fb7e67284e16bbb91a3b5d412ae06832c7
  }

  getFollowers() {
    if(this.requestedProfileId){
      this.profileService.getFollowers(this.requestedProfileId).subscribe(
        (data: ProfileDTO[]) => {
            this.followers = data; 
            this.followersNum = data.length; 
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
<<<<<<< HEAD
    this.requestedProfileId = idParam ? parseInt(idParam, 10) : -1; 
    this.isLoggedInUser = this.requestedProfileId === this.loggedProfile?.id;
    if(!this.isLoggedInUser)
    {
      this.loadFollowers(this.profileId, this.requestedProfileId);
    }
=======
    this.requestedProfileId = idParam ? parseInt(idParam, 10) : null; 
    this.isLoggedInUser = this.requestedProfileId === this.loggedProfile?.id;    
    this.getFollowers();
    this.getFollowing();
>>>>>>> a0c085fb7e67284e16bbb91a3b5d412ae06832c7
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
    this.profileService.getFollowers(profileId).subscribe({
      next: (followers) => {
        this.followers = followers;
        this.isFollowed = followers.some(follower => follower.id === requestedProfileId);
        console.log('Followers loaded:', this.followers);
      },
      error: (err) => {
        console.error('Error fetching followers:', err);
      }
    });
  }

  



}
