import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { asyncScheduler, interval, Observable, ReplaySubject, scheduled, Subject, timer } from 'rxjs';
import { debounce, debounceTime } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CheckUserService {

  requestQueue = new Subject()
  subject = new ReplaySubject<string>(1)
  constructor(private http: HttpClient) { 
    this.requestQueue.pipe(debounceTime(1000)).subscribe( () => this._isLoggedIn() )
    timer(0,60e3).subscribe(this.requestQueue)
  }


  isLoggedIn(): Observable<string>{
    return this.subject.asObservable()
  }
  private _isLoggedIn() {
    return (this.http.get("/api/user").toPromise() as Promise<{name:string}>)
    .then( v => this.subject.next(v?.name) )
  }

  logout() {
    this.subject.next(null)
    return this.http.post("/api/logout",{}).toPromise()
    .then(r => {this.requestQueue.next();return r})
  }

}
