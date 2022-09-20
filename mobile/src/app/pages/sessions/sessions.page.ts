import { Component, OnInit } from '@angular/core';
import { ConferenceDay } from 'src/app/data/conferenceday';
import { SessionData } from 'src/app/providers/session-data';

@Component({
  selector: 'app-sessions',
  templateUrl: 'sessions.page.html',
  styleUrls: ['sessions.page.scss']
})
export class SessionsPage implements OnInit {
  dayMap: Map<string, ConferenceDay>;
  days: string[];

  showAllSessions: string = 'true';

  constructor(
    private sessionData: SessionData,
  ) { }

  ngOnInit(): void {
    this.sessionData.getSessions().subscribe(data => {
      this.dayMap = data;
      this.days = Object.keys(data);
    });
  }

  ionViewDidEnter() {
    this.updateSessions();
  }

  updateSessions(): void {
    this.sessionData.getFavouriteSessions().then(sessionIds => {
      Object.keys(this.dayMap).forEach(dayKey => {
        let day = this.dayMap[dayKey];

        day.sessions.forEach(session => {
          console.log('updating session')
          session.favourite = sessionIds.indexOf(session.id) !== -1;
        });
      });


      if (this.showAllSessions == 'true') {
        Object.keys(this.dayMap).forEach(key => {
          this.dayMap[key].hidden = false;
        });
      } else {
        Object.keys(this.dayMap).forEach(key => {
          let day = this.dayMap[key];

          let showDay: boolean = false;
          day.sessions.forEach(session => {
            showDay = showDay || (session.favourite !== undefined && session.favourite === true);
          });

          day.hidden = !showDay;
        });
      }
    });
  }

}
