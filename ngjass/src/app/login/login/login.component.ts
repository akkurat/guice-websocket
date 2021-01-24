import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CheckUserService } from '../check-user.service';

@Component({
  selector: 'jas-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements AfterViewInit {

    name = 'gagi'
    password = 'password'

  constructor(private http: HttpClient, private router: Router, private loginService: CheckUserService) { 
    console.log("Construct")
  }
  ngAfterViewInit(): void {
    this.loginService.isLoggedIn().subscribe(uname => {if(uname){this.router.navigateByUrl('/')}})
  }


  login() {
    // TODO: use service rather
    const authHeader = {authorization : "Basic "
        + btoa(this.name + ":" + this.password)
    } ;
    this.http.get("/api/user", {headers: authHeader}).subscribe(
      o => {this.loginService.requestQueue.next(); console.log(o); this.router.navigate([''])},
      e => console.log(e)
      )
  }
  register() {
    this.http.post("/api/registration", {username: this.name, password: this.password}).subscribe(
      o => {console.log(o); this.router.navigate([''])},
      e => console.log(e)
      )
  }

}
