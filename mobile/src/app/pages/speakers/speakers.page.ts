import { Component } from '@angular/core';
import { Speaker } from 'src/app/data/speaker';
import { SpeakerData } from 'src/app/providers/speaker-data';

@Component({
  selector: 'app-speakers',
  templateUrl: 'speakers.page.html',
  styleUrls: ['speakers.page.scss']
})
export class SpeakersPage {

  constructor(private speakerData: SpeakerData) { }

  speakers: Speaker[] = [];

  ionViewDidEnter() {
    this.speakerData.getSpeakers()
      .subscribe(speakers => this.speakers = speakers);
  }
}
