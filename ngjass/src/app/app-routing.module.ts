import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SpringconnectComponent } from './springconnect/springconnect.component';

import { LoginactivateGuard } from './login/loginactivate.guard'
import { GameComponent } from './game/game.component';

const routes: Routes = [
  { path: 'game', component: SpringconnectComponent, canActivate: [LoginactivateGuard] },
  { path: 'play/:id', component: GameComponent  },
  { path: 'login', component: LoginComponent },
  { path: '',   redirectTo: '/game', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
