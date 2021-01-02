import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CheckUserService {

  constructor(private http: HttpClient) { }

  isLoggedIn(): Promise<string>{
    return (this.http.get("/api/user").toPromise() as Promise<{name:string}>)
    .then( v => v?.name)
  }
}
