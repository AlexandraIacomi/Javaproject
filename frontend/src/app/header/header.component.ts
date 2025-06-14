// src/app/header/header.component.ts
import { Component, OnDestroy } from '@angular/core';
import { UserService } from '../services/user.service';
import { CartService } from '../services/cart.service';
import { MenubarModule } from 'primeng/menubar';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { InputTextModule } from 'primeng/inputtext';
import { RippleModule } from 'primeng/ripple';
import { MenuItem } from 'primeng/api';
import { NgIf, NgClass } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  imports: [NgIf, MenubarModule, ButtonModule, BadgeModule, AvatarModule, InputTextModule, RippleModule, RouterLink, NgClass],
})
export class HeaderComponent implements OnDestroy {
  userName: string | null = null;
  isAdmin = false;
  items: MenuItem[] = [];
  cartItemCount: number = 0;
  private subscriptions: Subscription[] = [];

  constructor(
    private userService: UserService,
    private cartService: CartService
  ) {
    this.subscriptions.push(
      this.userService.userDetails$.subscribe(user => {
        this.userName = user?.fullName ?? null;
        this.isAdmin = Array.isArray(user?.roles) && user.roles.includes('ROLE_ADMIN');
        this.buildMenu();
      })
    );

    this.subscriptions.push(
      this.cartService.cartItemCount$.subscribe(count => {
        this.cartItemCount = count;
      })
    );
  }


  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  buildMenu() {
    this.items = [
      { label: 'Home', routerLink: '/' },

      ...(!this.isAdmin ? [
        { label: 'Products', routerLink: '/products', icon: 'pi pi-box' }
      ] : []),

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
