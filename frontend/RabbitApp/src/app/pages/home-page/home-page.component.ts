import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent implements OnInit {

  constructor(private userService: UserService, private authService: AuthService, private router: Router){}

  loggedProfile: ProfileDTO | null = null;
  ngOnInit(): void {
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.loggedProfile = data;
        } else {
          console.log('No profile found or token expired');
        }
      },
      (error) => {
        console.error('Error loading profile:', error);
      }
    );
  
  }

  navigateToProfile() {
    if (this.loggedProfile && this.loggedProfile.role === 'Administrator') {
      this.router.navigate(['view-profile']);
    } else {
      this.router.navigate(['my-profile']);
    }
  }

  navigateToPost() {
    if (this.loggedProfile) {
      this.router.navigate(['view-posts']);
    } else {
      console.log("please log in");
    }
  }

  logOut()
  {
    this.authService.logout();
    this.ngOnInit();
  }

  logIn()
  {
    this.router.navigate(['/login']);
  }

}
