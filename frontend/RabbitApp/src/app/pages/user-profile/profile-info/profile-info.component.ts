<<<<<<< HEAD
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
=======
import { Component, Input, OnInit } from '@angular/core';
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
import { ProfileDTO } from '../../../models/ProfileDTO.model';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute } from '@angular/router';
import { ProfileService } from '../../../services/profile-service.service';
<<<<<<< HEAD
import Swal from 'sweetalert2';
import { forkJoin } from 'rxjs'; // We'll use forkJoin for parallel requests
=======
import Swal from 'sweetalert2'; 
import { Router } from '@angular/router';
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.css']
})
export class ProfileInfoComponent implements OnInit {
  
<<<<<<< HEAD
  // The user object for the profile being viewed. No longer an @Input.
  user: ProfileDTO | null = null; 
  
  // We no longer need isLoggedInUser as an @Input, we calculate it
  isLoggedInUser: boolean = false;
  @Output() updated: EventEmitter<void> = new EventEmitter<void>();
=======
  @Input() user: any;
  @Input() isLoggedInUser: boolean = false;
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)

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
<<<<<<< HEAD
  requestedProfileId: number | null = null;
  isFollowed: boolean = false;
  followers: ProfileDTO[] = []; 
=======
  profileId: number = -1;
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
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
<<<<<<< HEAD
    // This subscription re-runs the logic whenever the URL parameter changes
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.requestedProfileId = parseInt(idParam, 10);
        this.loadInitialData(); // Call a single method to load everything
=======
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
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
      }
    );
  }

<<<<<<< HEAD
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

=======
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
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
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

<<<<<<< HEAD
  closeFollowListModal(): void {
    this.showFollowListModal = false;
  }

=======
  // Save profile changes
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
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
<<<<<<< HEAD
      next: (updatedProfile) => {
        if (this.user && updatedProfile) {
          this.user = this.editedUser;
        }
=======
      next: () => {
        this.user.name = this.editedUser.name;
        this.user.surname = this.editedUser.surname;
        this.user.address = this.editedUser.address;
  
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
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
<<<<<<< HEAD

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
=======
  
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
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
  }
  

<<<<<<< HEAD
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
=======
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
>>>>>>> parent of 503dbad (Merge branch 'student2' into merging)
