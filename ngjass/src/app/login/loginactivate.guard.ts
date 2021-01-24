import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CheckUserService } from './check-user.service';

@Injectable({
  providedIn: 'root'
})
export class LoginactivateGuard implements CanActivate {
  constructor( private userService: CheckUserService, private router: Router)
  {}
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> {
      return this.userService.isLoggedIn().pipe(map(v => {
        if(!v) 
        {this.router.navigate(['login']); return false}
        return true;
      }))
  }
  
}
