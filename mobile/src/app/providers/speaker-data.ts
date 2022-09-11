import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { CacheService } from "ionic-cache";
import { Observable } from "rxjs";
import { map } from 'rxjs/operators';
import { Speaker } from "../data/speaker";

@Injectable()
export class SpeakerData {
    constructor(
        private http: HttpClient,
        private cache: CacheService) { }

    getSpeakers(): Observable<Speaker[]> {
        return this.cache.loadFromObservable("speakers",
            this.http.get("https://vg3eqhj2s7.execute-api.eu-central-1.amazonaws.com/prod/speakers"));
    }

    getSpeakerById(speakerId: string): Observable<Speaker> {
        return this.getSpeakers()
            .pipe(map(speakers => speakers.find(speaker => speaker.id == speakerId)));
    }
    
    getSpeakersByIds(speakerIds: string[]): Observable<Speaker[]> {
        return this.getSpeakers()
            .pipe(map(speakers => speakers.filter(speaker => speakerIds.indexOf(speaker.id) !== -1)));
      }
  
}