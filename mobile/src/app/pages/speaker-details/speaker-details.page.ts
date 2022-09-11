import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Session } from 'src/app/data/session';
import { Speaker } from 'src/app/data/speaker';
import { SessionData } from 'src/app/providers/session-data';
import { SpeakerData } from 'src/app/providers/speaker-data';

@Component({
  selector: 'app-speaker-details',
  templateUrl: 'speaker-details.page.html',
  styleUrls: ['speaker-details.page.scss']
})
export class SpeakerDetailsPage {
  speaker: Speaker = new Speaker();
  sessions: Session[] = []

  constructor(
    private speakerData: SpeakerData,
    private sessionData: SessionData,
    private route: ActivatedRoute) { }

  ionViewDidEnter() {
    this.speakerData.getSpeakerById(this.route.snapshot.paramMap.get('speakerId'))
      .subscribe(speaker => {
        this.speaker = speaker;
        this.sessionData.getSessionsForSpeaker(this.speaker).subscribe(sessions => {
          this.sessions = sessions;
        });
      });
  }
}
