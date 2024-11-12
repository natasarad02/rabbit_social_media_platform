import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';  
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatButtonModule, FormsModule, CommonModule],  // <-- Add FormsModule here
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private router: Router, private auth: AuthService, private userservice: UserService) {}

  onLogin() {
    if (this.username && this.password) {
      const loginData = {
        username : this.username ,
        password : this.password
      }
      this.auth.login(loginData).subscribe({
        next: response => {
          console.log('Signup successful:', response);
          alert("sucessfully logged in");
          this.userservice.getMyInfo().subscribe((response)=> {
            console.log(response);
          });
          console.log(this.auth.getToken());
        },
        error: error => {
          console.error('Signup failed:', error);
        }
      });
      console.log('Logged in with', this.username);
    }
  }

  goBack() {
    this.router.navigate(['/']); // Adjust this to navigate to the desired route
  }

  goToSignUp() {
    this.router.navigate(['/signup']); // Adjust this to navigate to the desired route
  }
}
