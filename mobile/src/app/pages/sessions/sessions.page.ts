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
    console.log('ngOnInit');
    this.sessionData.getSessions().subscribe(data => {
      this.dayMap = data;
      this.days = Object.keys(data);
    });
  }

  updateSessions(): void {
    console.log('showAllSessions', this.showAllSessions);
    if (this.showAllSessions == 'true') {
      console.log('showing all');
      Object.keys(this.dayMap).forEach(key => {
        this.dayMap[key].hidden = false;
      });
    }

    else {
      console.log('only favs');
      Object.keys(this.dayMap).forEach(key => {
        let day = this.dayMap[key];

        let showDay: boolean = false;
        day.sessions.forEach(session => {
          showDay = showDay || (session.favourite !== undefined && session.favourite === true);
        });

        console.log('updating day', day);
        console.log('day.hidden is', !showDay);
        day.hidden = !showDay;
      });
    }
  }
}
