import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class TwitterData {
    constructor(private http: HttpClient) {}

    getTweetPage(page: number): Observable<any> {
        return this.http.get(`https://vg3eqhj2s7.execute-api.eu-central-1.amazonaws.com/prod/twitter/page/${page}`)
    }

    getInitialTweets(): Observable<any> {
        return this.getTweetPage(0);
    }

    getNewerTweets(tweetId: string): Observable<any> {
        return this.http.get(`https://vg3eqhj2s7.execute-api.eu-central-1.amazonaws.com/prod/twitter/update/id/${tweetId}`);
    }
}