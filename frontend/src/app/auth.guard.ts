import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import {AuthService} from './services/auth.service';
import {UserService} from './services/user.service';


@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private userService: UserService, private router: Router) {}

  canActivate(): boolean {
    const user = this.userService.getUserFromStorage();

    if (!user) {
      this.router.navigate(['/login']);
      return false;
    }

    if (user.roles.includes('ROLE_ADMIN')) {
      return true;
    }

    this.router.navigate(['/unauthorized']);
    return false;
  }
}
