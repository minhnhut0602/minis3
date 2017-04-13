import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from 'app/auth';
import { RegisterService } from './register.service';
import { RegisterModel } from './register.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  message;
  model: RegisterModel = new RegisterModel('', '', '', '', '');

  constructor(
    private authService: AuthService,
    private router: Router,
    private registerService: RegisterService
  ) { }

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
            this.router.navigateByUrl('dashboard');
        }
  }

  register() {
    this.registerService.register(this.model);
  }

}
