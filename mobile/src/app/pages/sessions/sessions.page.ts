import { Component } from '@angular/core';
import { ConferenceDay } from 'src/app/data/conferenceday';
import { SessionData } from 'src/app/providers/session-data';

@Component({
  selector: 'app-sessions',
  templateUrl: 'sessions.page.html',
  styleUrls: ['sessions.page.scss']
})
export class SessionsPage {
  dayMap: Map<string, ConferenceDay>;
  days: string[];

  constructor(
    private sessionData: SessionData,
  ) {}

  ionViewDidEnter() {
    this.sessionData.getSessions().subscribe(data => {
      this.dayMap = data;
      this.days = Object.keys(data);
    });
  }
}
