import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent {
  constructor() {}

  hideRetweets: boolean = true;

  showRetweetsChanged(event: CustomEvent) {
    this.hideRetweets = event.detail.value;
    window.dispatchEvent(new CustomEvent('twitter:show-retweets', {detail: {value: this.hideRetweets}}));
  }
}
