import { Injectable } from '@angular/core';
import { Headers, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/Rx';

import { AuthService } from '../auth.service';
import { RegisterModel } from './register.model';
import { HttpService, UserUrls } from '../../shared';


@Injectable()
export class RegisterService {

  constructor(
    private httpService: HttpService,
    private authService: AuthService
  ) { }

  register(model: RegisterModel) {
    const body = JSON.stringify(model);
    const header: Headers = new Headers();
    //header.append();
    const options: RequestOptions = new RequestOptions();
    //options.headers = header;
    this.httpService.post(UserUrls.getRegisterUrl(), body, options)
        .map(res => {
          console.log(res);
          if (res.status === 200) {
            this.authService.login(model.username, model.password);
          }
        }).catch(this.handleError).subscribe();
  }

  private handleError(error: Response) {
        console.error(error);
        return Observable.throw(error.json() || 'Server error');
    }

}
