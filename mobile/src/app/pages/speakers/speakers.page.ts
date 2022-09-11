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
  private allSpeakers: Speaker[];

  ngOnInit() {
    this.speakerData.getSpeakers()
      .subscribe(speakers => {
        this.speakers = speakers;
        this.allSpeakers = speakers;
      });
  }

  filterSpeakers(event: any ): void {
    let filterTerm = event.target.value;

    if (filterTerm && filterTerm.trim()) {
      this.speakers = this.allSpeakers
        .filter(speaker => speaker.fullName.toLowerCase().indexOf(filterTerm.toLowerCase()) > -1)
    } else {
      this.speakers = this.allSpeakers;
    }
  }
}
