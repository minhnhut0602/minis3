import { Injectable } from '@angular/core';
import { Http, Headers, Response, Request, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/Rx';

import { HttpService, MetaUrls } from '../../shared';

@Injectable()
export class FileListService {

    constructor(private httpService: Http) { }

    getAll() {
        const headers = new Headers();
        headers.append('Authorization', 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsid2ViYXBpIl0sInVzZXJfbmFtZSI6InVuYW1lIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTQ5MjEwNjU4NywiYXV0aG9yaXRpZXMiOlsiQURNSU4iXSwianRpIjoiZDYxNDM2MWYtOWFjZi00NGY0LTllNmEtZTljNDhhOGRiOTQ3IiwiY2xpZW50X2lkIjoiYW5ndWxhciJ9.5Oko-YuzZTWeu3f2ksTBPVbNpUr0KT_q1RwecrA3ido');
        const options = new RequestOptions(headers);
        return this.httpService.get("http://localhost:8080/api/v1/meta/", options)
                    .map(res => console.log(res.json()))
                    .catch(this.handleError);
    }

    private handleError(error: Response) {
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }
}
