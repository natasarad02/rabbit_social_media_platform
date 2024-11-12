import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatButtonModule, FormsModule],
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

  constructor(private router: Router) {}

  get passwordMismatch(): boolean {
    return !!(this.password && this.confirmPassword && this.password !== this.confirmPassword);
  }  

  onSignup() {
    if (!this.passwordMismatch && this.name && this.surname && this.email && this.username && this.password && this.address) {
      // Signup logic here, e.g., call an authentication service
      console.log('Signup details:', this.name, this.surname, this.email, this.username, this.address);
    }
  }

  goBack() {
    this.router.navigate(['/']);
  }
}
