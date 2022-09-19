import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { CacheService } from "ionic-cache";
import { Observable } from "rxjs";
import { ConferenceDay } from "../data/conferenceday";
import { Speaker } from "../data/speaker";
import { Session } from "../data/session";
import { map, tap } from "rxjs/operators";
import { Storage } from "@ionic/storage-angular";


@Injectable()
export class SessionData {
    constructor(
        private http: HttpClient,
        private cache: CacheService,
        private storage: Storage) { }

    private readonly FAVOURITE_SESSION: string = "favouriteSessionIds";

    getSessions(): Observable<Map<string, ConferenceDay>> {
        return this.cache.loadFromObservable("sessions",
            this.http.get("https://vg3eqhj2s7.execute-api.eu-central-1.amazonaws.com/prod/sessions"))
            .pipe(tap(data => {
                this.updateFavouriteSessions(data)
            }));
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

    getFavouriteSessions(): Promise<string[]> {
        return this.storage.get(this.FAVOURITE_SESSION).then(arrayFromStorage => {
            return Promise.resolve(arrayFromStorage || []);
        });
    }

    private updateFavouriteSessions(data: Map<string, ConferenceDay>): void {
        this.getFavouriteSessions().then(favouriteSessions => {
            Object.keys(data).map(key => data[key].sessions)
                .reduce((x, y) => x.concat(y), []) // flatMap
                .forEach(session => session.favourite = favouriteSessions.indexOf(session.id) !== -1);
        });
    }

    isFavouriteSession(session: Session): Promise<boolean> {
        return this.getFavouriteSessions()
            .then(favouriteSessions => favouriteSessions.indexOf(session.id) !== -1);
    }

    toggleFavouriteSession(sessionId: string): Promise<boolean> {
        return this.getFavouriteSessions().then(favouriteSessions => {
            let index = favouriteSessions.indexOf(sessionId);

            if (index === -1) {
                favouriteSessions.push(sessionId);
                this.storage.set(this.FAVOURITE_SESSION, favouriteSessions);
                return true;
            } else {
                favouriteSessions.splice(index, 1);
                this.storage.set(this.FAVOURITE_SESSION, favouriteSessions);
                return false;
            }
        });
    }
}