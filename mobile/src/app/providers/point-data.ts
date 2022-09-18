import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { CacheService } from "ionic-cache";
import { Observable } from "rxjs";

@Injectable()
export class PointData {
    constructor(
        private http: HttpClient,
        private cache: CacheService) { }

    getPoints(): Observable<any[]> {
        return this.cache.loadFromObservable("points",
        this.http.get("https://vg3eqhj2s7.execute-api.eu-central-1.amazonaws.com/prod/points"));
    }
}