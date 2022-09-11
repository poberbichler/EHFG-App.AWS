import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { CacheService } from "ionic-cache";
import { Observable } from "rxjs";
import { ConferenceDay } from "../data/conferenceday";
import { Speaker } from "../data/speaker";
import { Session } from "../data/session";
import { map, mergeMap } from "rxjs/operators";


@Injectable()
export class SessionData {

    constructor(
        private http: HttpClient,
        private cache: CacheService) { }

    getSessions(): Observable<Map<string, ConferenceDay>> {
        return this.cache.loadFromObservable("sessions",
            this.http.get("https://vg3eqhj2s7.execute-api.eu-central-1.amazonaws.com/prod/sessions"));
    }

    getSessionById(sessionId: string): Observable<Session> {
        return this.getSessions()
            .pipe(map(data => Object.keys(data)
                .flatMap(key => data[key].sessions)
                .find(session => session.id == sessionId)));
    }

    getSessionsForSpeaker(speaker: Speaker): Observable<Session[]> {
        return this.getSessions()
            .pipe(map(data => Object.keys(data)
                .flatMap(key => data[key].sessions)
                .filter((session: Session) => session.speakers.indexOf(speaker.id) !== -1)
            ));
    }
}