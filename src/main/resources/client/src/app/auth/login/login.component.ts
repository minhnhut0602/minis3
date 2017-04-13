import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { LoginModel } from './login.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model: LoginModel = new LoginModel('', '');

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
        if (this.authService.isLoggedIn()) {
            this.router.navigateByUrl('dashboard');
        }
    }

    login() {
      console.log(this.model);
        if (this.authService.login(this.model.username, this.model.password)) {
            this.router.navigateByUrl('dashboard');
        }
    }

    logout() {
        this.authService.logout();
        this.router.navigateByUrl('');
    }


}
