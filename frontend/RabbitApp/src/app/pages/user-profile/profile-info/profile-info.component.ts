import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ProfileDTO } from '../../../models/ProfileDTO.model';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfileService } from '../../../services/profile-service.service';
import Swal from 'sweetalert2';
import { forkJoin } from 'rxjs'; // We'll use forkJoin for parallel requests

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.css']
})
export class ProfileInfoComponent implements OnInit {
  
  // The user object for the profile being viewed. No longer an @Input.
  user: ProfileDTO | null = null; 
  
  // We no longer need isLoggedInUser as an @Input, we calculate it
  isLoggedInUser: boolean = false;
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
  requestedProfileId: number | null = null;
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
    // This subscription re-runs the logic whenever the URL parameter changes
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.requestedProfileId = parseInt(idParam, 10);
        this.loadInitialData(); // Call a single method to load everything
      }
    });
  }

  // --- REFACTORED DATA LOADING ---
  loadInitialData(): void {
    if (!this.requestedProfileId) return;

    // Fetch the requested profile and the logged-in user's profile in parallel
    // for better performance.
    forkJoin({
      requested: this.profileService.getProfile(this.requestedProfileId),
      loggedIn: this.userService.getUserProfile(),
      followers: this.profileService.getFollowers(this.requestedProfileId),
      following: this.profileService.getFollowing(this.requestedProfileId)
    }).subscribe({
      next: (data) => {
        // Assign all data at once after all requests are complete
        this.user = data.requested;
        this.loggedProfile = data.loggedIn;
        this.followers = data.followers;
        this.following = data.following;

        // Update counts
        this.followersNum = this.followers.length;
        this.followingNum = this.following.length;

        // Now that we have all data, we can safely perform checks
        if (this.user) { // Add this check
          this.isLoggedInUser = this.user.id === this.loggedProfile?.id;
          
          if (!this.isLoggedInUser) {
            this.checkIfFollowed();
          }
        }
        
        if (!this.isLoggedInUser) {
          this.checkIfFollowed();
        }
      },
      error: (err) => {
        console.error("Failed to load profile data", err);
        // Optionally, redirect to a 404 page or show an error message
        this.router.navigate(['/not-found']);
      }
    });
  }
  
  checkIfFollowed(): void {
    // We can now check this locally without another network call, but a call is more robust
    if (this.loggedProfile && this.requestedProfileId) {
        this.profileService.getFollowing(this.loggedProfile.id).subscribe(followingList => {
            this.isFollowed = followingList.some(p => p.id === this.requestedProfileId);
        });
    }
  }

  // Follow/Unfollow Actions
  followUser() {
    if (!this.loggedProfile || !this.requestedProfileId) return;
    this.profileService.followProfile(this.loggedProfile.id, this.requestedProfileId).subscribe({
      next: () => {
        this.isFollowed = true;
        this.followersNum++; // Optimistically update count
        this.refreshData();
      },
      error: (err) => console.error('Error while following user:', err)
    });
  }

  unfollowUser() {
    if (!this.loggedProfile || !this.requestedProfileId) return;
    this.profileService.unfollowProfile(this.loggedProfile.id, this.requestedProfileId).subscribe({
      next: () => {
        this.isFollowed = false;
        this.followersNum--; // Optimistically update count
        this.refreshData();
      },
      error: (err) => console.error('Error while unfollowing user:', err)
    });
  }

  // --- MODAL AND SAVE LOGIC (UNCHANGED) ---

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

  saveChanges() {
    this.allFieldsFilled = !!(this.editedUser.name && this.editedUser.surname && this.editedUser.address);
    if (!this.allFieldsFilled) return;

    this.profileService.updateProfile(this.editedUser).subscribe({
      next: (updatedProfile) => {
        if (this.user && updatedProfile) {
          this.user = this.editedUser;
        }
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

    if(this.loggedProfile) {
        this.profileService.updatePassword(this.loggedProfile.id, this.newPassword).subscribe({
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
  }

  goToProfile(userId: number): void {
    this.closeFollowListModal();
    this.router.navigate(['/profile', userId]);
  }

  refreshData(): void {
    this.updated.emit();
  }

  closeEditModal() {
    this.showEditProfileModal = false;
  }
}