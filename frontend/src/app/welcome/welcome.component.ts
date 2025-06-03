import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss'],
})
export class WelcomeComponent {
  fullName: string | null = null;

  constructor() {
    const userDetails = sessionStorage.getItem('userDetails');
    if (userDetails) {
      this.fullName = JSON.parse(userDetails).fullName;
    }
  }
}
