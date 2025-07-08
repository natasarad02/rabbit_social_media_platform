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
        username: this.username,
        password: this.password
      };
      this.auth.login(loginData).subscribe({
        next: (response) => {
          console.log('Login successful:', response);
          alert('Successfully logged in');
          console.log(this.auth.getToken());
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          console.error('Login failed:', error);

          if (error.status === 401) {
            alert('Invalid username or password. Please try again.');
          } else if (error.status === 403) {
            alert('Account not activated. Please activate your account to log in.');
          } else if (error.status === 429) {
            alert(error.error || 'Too many login attempts. Please try again later.');
          } else {
            alert('An unexpected error occurred. Please try again later.');
          }
        }
      });
      console.log('Logged in with', this.username);
    } else {
      alert('Please enter both username and password.');
    }
  }

  spamLogin() {
    const loginData = {
      username: 'marko',
      password: 'lozinka'
    };

    for (let i = 0; i < 6; i++) {
      this.auth.login(loginData).subscribe({
        next: (res) => console.log('✔️ Success', res),
        error: (err) => {
          console.error('❌ Error', err);
          if (err.status === 429) {
            alert('Previše pokušaja. Sačekajte malo pre nove prijave.');
          }
        }
      });
    }
  }


  

  goBack() {
    this.router.navigate(['/']); // Adjust this to navigate to the desired route
  }

  goToSignUp() {
    this.router.navigate(['/signup']); // Adjust this to navigate to the desired route
  }
}
