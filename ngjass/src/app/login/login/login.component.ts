import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jas-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {

    name: ''
    password: ''

  constructor(private http: HttpClient, private router: Router) { 
    console.log("Construct")
  }

  ngOnInit(): void {
  }

  login() {
    // TODO: use service rather
    const authHeader = {authorization : "Basic "
        + btoa(this.name + ":" + this.password)
    } ;
    this.http.get("/api/user", {headers: authHeader}).subscribe(
      o => {console.log(o); this.router.navigate([''])},
      e => console.log(e)
      )
  }

}
