import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { CacheService } from "ionic-cache";
import { Observable } from "rxjs";

@Injectable()
export class SpeakerData {
    constructor(
        private http: HttpClient,
        private cache: CacheService) { }

    getSpeakers(): Observable<any[]> {
        return this.cache.loadFromObservable("speakers",
            this.http.get("https://vg3eqhj2s7.execute-api.eu-central-1.amazonaws.com/prod/speakers"));
    }
}