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

  showAllSessions: boolean = true;

  constructor(
    private sessionData: SessionData,
  ) { }

  ionViewDidEnter() {
    this.sessionData.getSessions().subscribe(data => {
      this.dayMap = data;
      this.days = Object.keys(data);
    });
  }

  updateSessions(): void {
    console.log('showAllSessions', this.showAllSessions);
    if (this.showAllSessions === true) {
      Object.keys(this.dayMap).forEach(key => {
        this.dayMap[key].hidden = false;
      });
    }

    else {
      Object.keys(this.dayMap).forEach(key => {
        let day = this.dayMap[key];

        let showDay: boolean = false;
        day.sessions.forEach(session => {
          showDay = showDay || (session.favourite !== undefined && session.favourite === true);
        });

        day.hidden = !showDay;
      });
    }
  }
}
