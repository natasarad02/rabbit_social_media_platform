import { Component, Input, OnInit } from '@angular/core';
import { ProfileDTO } from '../../../models/ProfileDTO.model';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute } from '@angular/router';

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

  constructor(private userService: UserService, private auth: AuthService, private route: ActivatedRoute){}
    
  ngOnInit(): void {
    this.loadUser();
    this.checkisLoggedInUser();
  }

  loadUser(): void {
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.loggedProfile = data;
          this.profileId = this.loggedProfile.id;  
          this.checkisLoggedInUser()
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
    const requestedProfileId = idParam ? parseInt(idParam, 10) : null; 
    this.isLoggedInUser = requestedProfileId === this.loggedProfile?.id;
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

  followUser() {
    console.log('Follow button clicked!');
    // Handle follow logic here
  }
}
