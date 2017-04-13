import { Injectable } from '@angular/core';
import { Headers, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/Rx';

import { HttpService, UserUrls } from '../shared/';

@Injectable()
export class AuthService {

    constructor(private httpService: HttpService) { }

    login(userName: string, password: string): boolean {
        const body = 'password=' + password + '&username=' + userName + '&grant_type=password';
        const headers: Headers = this.httpService.getLoginHeaders();
        const options = new RequestOptions({ headers: headers });
        this.httpService.post(UserUrls.getLoginUrl(), body, options)
            .map(this.extractToken)
            .catch(this.handleError)
            .subscribe();
        return this.isLoggedIn();
    }

    extractToken(res: Response | any) {
        if (res.status === 200) {
            const response = res.json();
            localStorage.setItem('token', response['access_token']);
        }
    }

    logout(): any {
        localStorage.removeItem('token');
    }

    getToken(): any {
        return localStorage.getItem('token');
    }

    isLoggedIn(): boolean {
        return this.getToken() !== null;
    }

    private handleError(error: Response) {
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }
}

export let AUTH_PROVIDERS: Array<any> = [
    { provide: AuthService, useClass: AuthService }
];
