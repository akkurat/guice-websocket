import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component'



@NgModule({
  declarations: [LoginComponent],
  imports: [ CommonModule, FormsModule],
  exports: [LoginComponent]
})
export class LoginModule { }

export { LoginComponent } 

