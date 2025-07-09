import { Component, Input, OnInit } from '@angular/core';
import { ProfileDTO } from '../../../models/ProfileDTO.model';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute } from '@angular/router';
import { ProfileService } from '../../../services/profile-service.service';
import Swal from 'sweetalert2'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.css']
})
export class ProfileInfoComponent implements OnInit {
  
  @Input() user: any;
  @Input() isLoggedInUser: boolean = false;

  // Modals
  showEditProfileModal: boolean = false;
  showChangePasswordModal: boolean = false;

  // Edit Profile
  editedUser: any = {};
  allFieldsFilled: boolean = true;

  // Change Password
  newPassword: string = '';
  confirmPassword: string = '';
  passwordsDoNotMatch: boolean = false;

  // Followers and Following
  loggedProfile: ProfileDTO | null = null;
  profileId: number = -1;
  following: ProfileDTO[] = []; 
  followers: ProfileDTO[] = []; 
  followingNum: number = 0;
  followersNum: number = 0;
  requestedProfileId: number | null = -1;

  showFollowListModal = false;
  followListTitle = '';
  selectedFollowList: ProfileDTO[] = [];

  constructor(
    private userService: UserService, 
    private auth: AuthService, 
    private route: ActivatedRoute,
    private profileService: ProfileService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      this.requestedProfileId = idParam ? parseInt(idParam, 10) : null;
      this.loadRequestedProfile();
      this.loadUser();
      
    });
  }


  // Load the logged-in user's profile
  loadUser(): void {    
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);          
          this.loggedProfile = data;
          this.profileId = this.loggedProfile.id;  
          this.checkIsLoggedInUser();
        } else {
          console.log('No profile found or token expired');
          alert('No profile found or token expired');
          this.profileId = -1;
        }
      },
      (error) => {
        console.error('Error loading profile:', error);
        alert('Error loading profile:'+ error.message);
        this.auth.logout();
        this.profileId = -1;
      }
    );
  }

  loadRequestedProfile(): void {
  if (this.requestedProfileId) {
    this.profileService.getProfile(this.requestedProfileId).subscribe(
      (data) => {
        this.user = data;
        this.checkIsLoggedInUser();
        this.getFollowers();
        this.getFollowing();
      },
      (error) => {
        console.error('Error loading requested profile:', error);
      }
    );
  }
}


  openFollowList(type: 'followers' | 'following'): void {
    this.followListTitle = type === 'followers' ? 'Followers' : 'Following';
    this.selectedFollowList = type === 'followers' ? this.followers : this.following;
    this.showFollowListModal = true;
  }

  closeFollowListModal(): void {
    this.showFollowListModal = false;
  }

  goToProfile(userId: number): void {
    this.closeFollowListModal();
    this.router.navigate(['/profile', userId]);
  }


  // Check if the currently viewed profile is the logged-in user's profile
  checkIsLoggedInUser() {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.requestedProfileId = idParam ? parseInt(idParam, 10) : null; 
    this.isLoggedInUser = this.requestedProfileId === this.loggedProfile?.id;
    this.getFollowers();
    this.getFollowing();
  }

  // Get followers
  getFollowers() {
    if (this.requestedProfileId) {
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

  // Get following
  getFollowing() {
    if (this.requestedProfileId) {
      this.profileService.getFollowing(this.requestedProfileId).subscribe(
        (data: ProfileDTO[]) => {
          this.following = data;
          this.followingNum = data.length;
        },
        (error) => {
          console.error('Error fetching following:', error);
        }
      );
    }
  }

  // Open modals
  openEditModal() {
    this.editedUser = { ...this.user };
    this.allFieldsFilled = true;
    this.showEditProfileModal = true;
  }

  openChangePasswordModal() {
    this.newPassword = '';
    this.confirmPassword = '';
    this.passwordsDoNotMatch = false;
    this.showChangePasswordModal = true;
  }

  // Close modals
  closeEditProfileModal() {
    this.showEditProfileModal = false;
  }

  closeChangePasswordModal() {
    this.showChangePasswordModal = false;
  }

  // Save profile changes
  saveChanges() {
    this.allFieldsFilled = !!(
      this.editedUser.name &&
      this.editedUser.surname &&
      this.editedUser.address
    );
  
    if (!this.allFieldsFilled) {
      return;
    }
  
    console.log('Saving changes:', {
      name: this.editedUser.name,
      surname: this.editedUser.surname,
      address: this.editedUser.address
    });
  
    this.profileService.updateProfile(this.editedUser).subscribe({
      next: () => {
        this.user.name = this.editedUser.name;
        this.user.surname = this.editedUser.surname;
        this.user.address = this.editedUser.address;
  
        this.closeEditProfileModal();
      },
      error: (err) => {
        Swal.fire({
          title: 'Error!',
          text: 'Failed to update the profile. Please try again later.',
          icon: 'error',
          confirmButtonText: 'OK'
        });
  
        console.error('Profile update failed:', err);
      }
    });
  }
  

  // Save new password
  savePassword() {
    this.passwordsDoNotMatch = this.newPassword !== this.confirmPassword;
  
    if (this.passwordsDoNotMatch || !this.newPassword || !this.confirmPassword) {
      return;
    }
  
    console.log('Changing password to:', this.newPassword);
  
    this.profileService.updatePassword(this.profileId, this.newPassword).subscribe({
      next: () => {
        // Success handler
        Swal.fire({
          title: 'Success!',
          text: 'Your password has been updated successfully.',
          icon: 'success',
          confirmButtonText: 'OK'
        });
        this.closeChangePasswordModal();
      },
      error: (err) => {
        // Error handler
        Swal.fire({
          title: 'Error!',
          text: 'Please try again later!',
          icon: 'error',
          confirmButtonText: 'OK'
        });
        console.error('Password update failed:', err);
      }
    });
  }
  

  updatePassword(): void {
    const profileId = this.profileId;
    const newPassword = this.newPassword;
  
    this.profileService.updatePassword(profileId, newPassword).subscribe(
      response => {
        console.log('Password update response:', response);
        alert(response);
      },
      error => {
        console.error('Error updating password:', error);
        alert('Password update failed');
      }
    );
  }
  
  // Component code for updating profile
  updateProfileInfo(): void {
     
    this.profileService.updateProfile(this.editedUser).subscribe(
      response => {
        console.log('Profile update response:', response);
        alert(response);
      },
      error => {
        console.error('Error updating profile:', error);
        alert('Profile update failed');
      }
    );
  }

  followUser() {
    console.log('Follow button clicked!');
    // Handle follow logic here
  }
}
