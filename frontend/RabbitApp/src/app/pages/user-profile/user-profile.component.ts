import { Component, Input, OnInit } from '@angular/core';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute } from '@angular/router';
import { ProfileService } from '../../services/profile-service.service';
import { errorContext } from 'rxjs/internal/util/errorContext';

@Component({
  selector: 'app-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class ProfileComponent implements OnInit{
   
  loggedProfile: ProfileDTO | null = null;
  requestedProfile: ProfileDTO | null = null;
  profileId: number = -1;
  requestedProfileId: number = -1;

  constructor(private userService: UserService, private auth: AuthService, 
    private profileService: ProfileService, private route: ActivatedRoute){}

  ngOnInit() {
  this.route.paramMap.subscribe(params => {
    const idParam = params.get('id');
    this.requestedProfileId = idParam ? parseInt(idParam, 10) : -1;
    this.loadProfile();
    this.loadUser();
  });
}


  loadProfile(){
    const idParam = this.route.snapshot.paramMap.get('id');
    const requestedProfileId1 = idParam ? parseInt(idParam, 10) : null; 
    if(requestedProfileId1){
      this.profileService.getProfile(requestedProfileId1).subscribe(
        (data) => {
          if (data) {
            console.log(data);
            this.requestedProfile = data;
          this.requestedProfileId = this.requestedProfile.id;
          } else {
            console.log('No profile found or token expired');
            this.profileId = -1;
          }          
        },
        (error) =>{console.error('Error loading profile:', error);}
        )
    }    
  }

  loadUser(): void {
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.loggedProfile = data;
          this.profileId = this.loggedProfile.id;  
          // alert(this.loggedProfile.name);
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

  handleEdit() {
    console.log('Edit button clicked');
    // Handle edit logic here
  }
}