import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CheckUserService } from './check-user.service';

@Injectable({
  providedIn: 'root'
})
export class LoginactivateGuard implements CanActivate {
  constructor( private userService: CheckUserService, private router: Router)
  {}
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean> {
      return this.userService.isLoggedIn().then(v => {
        if(!v) {this.router.navigate(['login'])}
        return true;
      })
  }
  
}
