import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import {UserService} from '../../services/user.service';

interface RegisterUserDto{
  email: string;
  password: string;
  fullName: string;
  username: string;
}

export interface RegisterResponseDto {
  id: number;
  fullName: string;
  email: string;
  username: string;
  authorities: any[];
}

@Component({
  selector: 'app-register',
  imports: [FormsModule, HttpClientModule, NgIf],
  standalone: true,
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  registerUser: RegisterUserDto = {
    fullName: '',
    email: '',
    username: '',
    password: ''
  };
  constructor(
    private http: HttpClient,
    private router: Router,
    private userService: UserService
  ) {}
  onSubmit() {
    this.http.post<RegisterResponseDto>('http://localhost:8080/auth/signup', this.registerUser)
      .subscribe({
        next: (response) => {
          console.log('Registration successful:', response);
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Registration failed:', error);
          alert('Registration failed: ' + (error.error?.message || error.statusText));
        }
      });
  }
}




