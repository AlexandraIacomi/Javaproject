import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { MenubarModule } from 'primeng/menubar';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { InputTextModule } from 'primeng/inputtext';
import { RippleModule } from 'primeng/ripple';
import { MenuItem } from 'primeng/api';
import { NgIf, NgClass } from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  imports: [NgIf, MenubarModule, ButtonModule, BadgeModule, AvatarModule, InputTextModule, RippleModule, RouterLink],
})
export class HeaderComponent {
  userName: string | null = null;
  isAdmin = false;
  items: MenuItem[] = [];

  constructor(private userService: UserService) {
    this.userService.userDetails$.subscribe(user => {
      this.userName = user?.fullName ?? null;
      this.isAdmin = Array.isArray(user?.roles) && user.roles.includes('ROLE_ADMIN');
      this.buildMenu();
    });
  }

  buildMenu() {
    this.items = [
      { label: 'Home', routerLink: '/' },

      ...(!this.isAdmin
        ? [{ label: 'Products', routerLink: '/products', icon: 'pi pi-box' }]
        : []),


      ...(this.isAdmin
        ? [
          { label: 'Admin Products', routerLink: '/admin/products', icon: 'pi pi-cog' },
          { label: 'Categories', routerLink: '/admin/categories', icon: 'pi pi-tags' }
        ]
        : []),

      ...(!this.userName
        ? [
          { label: 'Login', routerLink: '/login' },
          { label: 'Register', routerLink: '/signup' }
        ]
        : [])
    ];
  }

  logout() {
    this.userService.clearUserDetails();
  }
}
