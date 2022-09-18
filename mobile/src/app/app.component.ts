import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent {
  constructor(private router: Router) {}

    hideRetweets: boolean = true;

  showRetweetsChanged(event: CustomEvent) {
    this.hideRetweets = event.detail.value;
    window.dispatchEvent(new CustomEvent('twitter:show-retweets', {detail: {value: this.hideRetweets}}));
  }

  get tweetPageActive() {
    return this.router.url === '/twitter';
  }
}
