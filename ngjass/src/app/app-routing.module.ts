import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from '@/login'

import { LoginactivateGuard } from './login/loginactivate.guard'
import { GameComponent } from '@/game'
import { LobbyComponent } from '@/lobby'

const routes: Routes = [
  { path: 'lobby', component: LobbyComponent, canActivate: [LoginactivateGuard] },
  { path: 'play/:id', component: GameComponent, canActivate: [LoginactivateGuard] },
  { path: 'login', component: LoginComponent },
  { path: '',   redirectTo: '/lobby', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
