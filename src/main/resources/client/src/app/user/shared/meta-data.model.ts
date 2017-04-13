export class MetaData {
    public Id: string;
    public name: string;
    public size: number;
    public timeCreated: Date;
    public updated: Date;
    public md5Hash: string;
    public cacheControl: string;
    public contentDisposition: string;
    public contentEncoding: string;
    public contentLanguage: string;
    public contentType: string;
    public customMetaData: Map<string, string>;
}
