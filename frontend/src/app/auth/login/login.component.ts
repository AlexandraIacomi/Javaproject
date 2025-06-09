import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import {UserService} from '../../services/user.service';


interface LoginUserDto {
  email: string;
  password: string;
}

interface LoginResponseDto {
  token: string;
  expirationTime: number;
  fullName: string;
  roles: string[];
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, HttpClientModule, NgIf],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  loginUser: LoginUserDto = {
    email: '',
    password: '',
  };

  constructor(
    private http: HttpClient,
    private router: Router,
    private userService: UserService
  ) {}

  onSubmit() {
    this.http.post<LoginResponseDto>('http://localhost:8080/auth/login', this.loginUser).subscribe({
      next: (response) => {
        sessionStorage.setItem('authToken', response.token);
        this.userService.setUserDetails(
          {
            fullName: response.fullName,
            expirationTime: response.expirationTime,
            roles: response.roles
          },
          response.token
        );

        this.router.navigate(['/welcome']);
      },
      error: (err) => {
        alert('Login failed: ' + (err.error?.message || err.statusText));
      },
    });
  }
}
