import { Injectable } from '@angular/core';
import { Http, Headers, Response, Request, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/Rx';

@Injectable()
export class HttpService {

    constructor(
        private http: Http
    ) { }

    public get(url: string, options: RequestOptions): Observable<Response> {
        return this.http.get(url, this.addDeafaultOptionsAndGet(options));
    }

    public post(url: string, body: any, options: RequestOptions): Observable<Response> {
        return this.http.post(url, body, this.addDeafaultOptionsAndGet(options));
    }

    public delete(url: string, options: RequestOptions): Observable<Response> {
        return this.http.delete(url, this.addDeafaultOptionsAndGet(options));
    }

    public put(url: string, body: any, options: RequestOptions): Observable<Response> {
        return this.http.put(url, body, this.addDeafaultOptionsAndGet(options));
    }

    public patch(url: string, body: any, options: RequestOptions): Observable<Response> {
        return this.http.patch(url, body, this.addDeafaultOptionsAndGet(options));
    }

    private addDeafaultOptionsAndGet(options: RequestOptions): RequestOptions {
        if (options == null) {
            options = new RequestOptions();
        }
        if (options.withCredentials) {
            options.merge(new RequestOptions(this.getDefaultHeadersWithToken()));
        }else {
            options.merge(new RequestOptions(this.getDefaultHeaders()));
        }
        return options;
    }

    public getLoginHeaders(): Headers {
        const headers = new Headers({
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': 'Basic ' + btoa('angular:secret')
        });
        return headers;
    }

    private getDefaultHeaders(): Headers {
        const headers = new Headers({
            'Accept': 'application/json'
        });
        return headers;
    }

    private getDefaultHeadersWithToken(): Headers {
        const token = localStorage.getItem('token');
        const headers = new Headers({
            'Authorization': 'Bearer ' + token
        });
        return headers;
    }
}
