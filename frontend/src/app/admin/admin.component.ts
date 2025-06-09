import { Component } from '@angular/core';
import {NgIf} from '@angular/common';
import {UserService} from '../services/user.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss'],
})
export class AdminComponent {
  userName: string | null = null;

  constructor(private userService: UserService) {
    this.userService.userDetails$.subscribe(user => {
      this.userName = user?.fullName ?? null;
    });
  }

}
