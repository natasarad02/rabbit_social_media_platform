import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatButtonModule, FormsModule, CommonModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})


export class SignupComponent {
  name: string = '';
  surname: string = '';
  email: string = '';
  username: string = '';
  password: string = '';
  confirmPassword: string = '';
  address: string = '';

  constructor(private router: Router, private auth: AuthService) {}

  get passwordMismatch(): boolean {
    return !!(this.password && this.confirmPassword && this.password !== this.confirmPassword);
  }  

  onSignup() {
    if (!this.passwordMismatch && this.name && this.surname && this.email && this.username && this.password && this.address) {
      const signupData = {
        firstname: this.name,  
        lastname: this.surname, 
        email: this.email,
        username: this.username,
        password: this.password,
        address: this.address
      };
      
      // auth
      this.auth.signup(signupData).subscribe({
        next: response => {
          console.log('Signup successful:', response);
          alert("sucessfully made a profile");
          this.router.navigate(['/login']); 
        },
        error: error => {
          console.error('Signup failed:', error);
        }
      });

      console.log('Signup details:', this.name, this.surname, this.email, this.username, this.address);
    }
  }

  goBack() {
    this.router.navigate(['/']);
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
