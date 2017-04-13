const HOST_URL = 'http://localhost:8080';
const BASE_API: string = HOST_URL + '/api/v1';
const LOGIN_URL: string = HOST_URL + '/oauth/token';

function joinUrls(url1, url2) {
    return url1 + url2;
}

export class UserUrls {
    private static base: string = BASE_API + '/users';
    private static login: string = LOGIN_URL;
    private static register: string = joinUrls(UserUrls.base, '/register');
    private static checkUsername: string = joinUrls(UserUrls.base, '/isavailable');
    public static getLoginUrl() {return UserUrls.login; }
    public static getRegisterUrl() {return UserUrls.register; }
    public static getCheckUsername() {return UserUrls.checkUsername; }
}

export class FileUrls {
    private static base: string = BASE_API + '/files';
    private static sendReceive: string = joinUrls(FileUrls.base, '/');
    public static getUploadDownloadUrl() { return FileUrls.sendReceive; }
}

export class MetaUrls {
    private static base: string = BASE_API + '/meta';
    private static url: string = joinUrls(MetaUrls.base, '/');
    public static getUrl() { return MetaUrls.url; }
}
