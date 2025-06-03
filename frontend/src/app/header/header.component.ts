import { Component } from '@angular/core';
import {UserService} from '../services/user.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  imports: [NgIf],
})
export class HeaderComponent {
  userName: string | null = null;

  constructor(private userService: UserService) {
    this.userService.userDetails$.subscribe(user => {
      this.userName = user?.fullName ?? null;
    });
  }

  logout() {
    this.userService.clearUserDetails();
  }
}
