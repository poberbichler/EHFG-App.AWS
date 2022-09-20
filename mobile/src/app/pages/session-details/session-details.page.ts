import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NavController } from '@ionic/angular';
import { Session } from 'src/app/data/session';
import { Speaker } from 'src/app/data/speaker';
import { SessionData } from 'src/app/providers/session-data';
import { SpeakerData } from 'src/app/providers/speaker-data';

@Component({
  selector: 'app-session-details',
  templateUrl: 'session-details.page.html',
  styleUrls: ['session-details.page.scss']
})
export class SessionDetailsPage {
  session: Session = new Session();
  speakers: Speaker[] = [];

  constructor(
    private sessionData: SessionData,
    private speakerData: SpeakerData,
    private route: ActivatedRoute,
    private navCtrl: NavController
  ) { }

  ionViewDidEnter() {
    this.sessionData.getSessionById(this.route.snapshot.paramMap.get('sessionId'))
      .subscribe(session => {
        this.session = session;
        this.speakerData.getSpeakersByIds(this.session.speakers)
          .subscribe(speakers => this.speakers = speakers);

          this.sessionData.isFavouriteSession(session)
            .then(isFavourite => session.favourite = isFavourite);
      });
  }

  toggleFavouriteSession(): void {
    this.session.favourite = !this.session.favourite;
    this.sessionData.toggleFavouriteSession(this.session.id)
      .then(result => this.session.favourite = result);
  }
}
