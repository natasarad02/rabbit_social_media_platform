import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';  
import { CommonModule } from '@angular/common';

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

  constructor(private router: Router) {}

  onLogin() {
    if (this.username && this.password) {
      // Your login logic here, e.g., call an authentication service.
      console.log('Logged in with', this.username, this.password);
    }
  }

  goBack() {
    this.router.navigate(['/']); // Adjust this to navigate to the desired route
  }

  goToSignUp() {
    this.router.navigate(['/signup']); // Adjust this to navigate to the desired route
  }
}
