import { Component, EventEmitter, Input, OnInit, Output, OnChanges, SimpleChanges } from '@angular/core';
import { ProfileDTO } from '../../../models/ProfileDTO.model';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfileService } from '../../../services/profile-service.service';
import Swal from 'sweetalert2'; 

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.css']
})
export class ProfileInfoComponent implements OnInit {
  
  @Input() user: any;
  @Input() isLoggedInUser: boolean = false;
  @Output() updated: EventEmitter<void> = new EventEmitter<void>();

  // Modals
  showEditProfileModal: boolean = false;
  showChangePasswordModal: boolean = false;
  showFollowListModal = false;
  followListTitle = '';
  selectedFollowList: ProfileDTO[] = [];

  // Edit Profile
  editedUser: any = {};
  allFieldsFilled: boolean = true;

  // Change Password
  newPassword: string = '';
  confirmPassword: string = '';
  passwordsDoNotMatch: boolean = false;

  // User & Follower State
  loggedProfile: ProfileDTO | null = null;
  profileId: number = -1; // Logged-in user's ID
  requestedProfileId: number | null = null; // ID of profile being viewed
  isFollowed: boolean = false;
  followers: ProfileDTO[] = []; 
  following: ProfileDTO[] = []; 
  followingNum: number = 0;
  followersNum: number = 0;

  constructor(
    private userService: UserService, 
    private auth: AuthService, 
    private route: ActivatedRoute,
    private profileService: ProfileService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // This subscription ensures the component reloads when the route parameter changes
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      this.requestedProfileId = idParam ? parseInt(idParam, 10) : null;
      
      // Load both the logged-in user and the requested profile data
      this.loadUser();
      this.loadRequestedProfile();
    });
  }

  // Load the logged-in user's profile to know who "we" are
  loadUser(): void {    
    this.userService.getUserProfile().subscribe({
      next: (data) => {
        if (data) {
          this.loggedProfile = data;
          this.profileId = this.loggedProfile.id;  
          this.checkIfViewingOwnProfile();
        } else {
          console.log('No profile found for logged-in user or token expired');
        }
      },
      error: (error) => {
        console.error('Error loading logged-in user profile:', error);
        this.auth.logout();
      }
    });
  }

  // Load the profile for the user ID in the URL
  loadRequestedProfile(): void {
    if (this.requestedProfileId) {
      this.profileService.getProfile(this.requestedProfileId).subscribe({
        next: (data) => {
          this.user = data;
          this.getFollowers();
          this.getFollowing();
          this.checkIfViewingOwnProfile();
        },
        error: (error) => {
          console.error('Error loading requested profile:', error);
        }
      });
    }
  }

  // Check if the currently viewed profile is the logged-in user's profile
  checkIfViewingOwnProfile() {
    // Return if we don't have both IDs yet
    if (this.requestedProfileId === null || !this.loggedProfile) {
      return;
    }
    
    this.isLoggedInUser = this.requestedProfileId === this.loggedProfile.id;

    // If we are viewing someone else's profile, check if we follow them
    if (!this.isLoggedInUser) {
      this.checkIfFollowed();
    }
  }
  
  // Check if the logged-in user follows the currently viewed profile
  checkIfFollowed(): void {
    if (this.profileId !== -1 && this.requestedProfileId !== null) {
      this.profileService.getFollowing(this.profileId).subscribe({
        next: (followingList) => {
          this.isFollowed = followingList.some(profile => profile.id === this.requestedProfileId);
        },
        error: (err) => console.error('Error fetching logged-in user following list:', err)
      });
    }
  }

  // Get followers for the viewed profile
  getFollowers() {
    if (this.requestedProfileId) {
      this.profileService.getFollowers(this.requestedProfileId).subscribe({
        next: (data) => {
          this.followers = data; 
          this.followersNum = data.length; 
        },
        error: (error) => console.error('Error fetching followers:', error)
      });
    }
  }

  // Get following for the viewed profile
  getFollowing() {
    if (this.requestedProfileId) {
      this.profileService.getFollowing(this.requestedProfileId).subscribe({
        next: (data) => {
          this.following = data;
          this.followingNum = data.length;
        },
        error: (error) => console.error('Error fetching following:', error)
      });
    }
  }

  // Follow/Unfollow Actions
  followUser() {
    if (this.profileId === -1 || this.requestedProfileId === null) return;
    this.profileService.followProfile(this.profileId, this.requestedProfileId).subscribe({
      next: () => {
        this.isFollowed = true;
        this.getFollowers(); // Refresh follower count
      },
      error: (err) => console.error('Error while following user:', err)
    });
  }

  unfollowUser() {
    if (this.profileId === -1 || this.requestedProfileId === null) return;
    this.profileService.unfollowProfile(this.profileId, this.requestedProfileId).subscribe({
      next: () => {
        this.isFollowed = false;
        this.getFollowers(); // Refresh follower count
      },
      error: (err) => console.error('Error while unfollowing user:', err)
    });
  }

  // Modal Controls
  openEditModal() {
    this.editedUser = { ...this.user };
    this.allFieldsFilled = true;
    this.showEditProfileModal = true;
  }

  closeEditProfileModal() {
    this.showEditProfileModal = false;
  }

  openChangePasswordModal() {
    this.newPassword = '';
    this.confirmPassword = '';
    this.passwordsDoNotMatch = false;
    this.showChangePasswordModal = true;
  }

  closeChangePasswordModal() {
    this.showChangePasswordModal = false;
  }
  
  openFollowList(type: 'followers' | 'following'): void {
    this.followListTitle = type.charAt(0).toUpperCase() + type.slice(1);
    this.selectedFollowList = type === 'followers' ? this.followers : this.following;
    this.showFollowListModal = true;
  }

  closeFollowListModal(): void {
    this.showFollowListModal = false;
  }

  // Save Actions
  saveChanges() {
    this.allFieldsFilled = !!(this.editedUser.name && this.editedUser.surname && this.editedUser.address);
    if (!this.allFieldsFilled) return;

    this.profileService.updateProfile(this.editedUser).subscribe({
      next: () => {
        this.user = { ...this.user, ...this.editedUser }; // Update local user object
        this.closeEditProfileModal();
        Swal.fire('Success!', 'Your profile has been updated.', 'success');
      },
      error: (err) => {
        Swal.fire('Error!', 'Failed to update the profile. Please try again.', 'error');
        console.error('Profile update failed:', err);
      }
    });
  }

  savePassword() {
    this.passwordsDoNotMatch = this.newPassword !== this.confirmPassword;
    if (this.passwordsDoNotMatch || !this.newPassword || !this.confirmPassword) {
      return;
    }

    this.profileService.updatePassword(this.profileId, this.newPassword).subscribe({
      next: () => {
        Swal.fire('Success!', 'Your password has been updated successfully.', 'success');
        this.closeChangePasswordModal();
      },
      error: (err) => {
        Swal.fire('Error!', 'Failed to update your password. Please try again.', 'error');
        console.error('Password update failed:', err);
      }
    });
  }

  // Navigation and Refresh
  goToProfile(userId: number): void {
    this.closeFollowListModal();
    this.router.navigate(['/profile', userId]).then(() => {
      this.refreshParentPosts();
    });
  }

  private refreshParentPosts(): void {
    this.updated.emit();
  }
}