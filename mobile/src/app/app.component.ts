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
  
  readonly mapCategories = [{
    "name": "Venues",
    "cssClass": "",
    "imageUrl": "",
    "toggled": true
  }, {
    "name": "Hotels",
    "cssClass": "warning",
    "imageUrl": "",
    "toggled": true
  }, {
    "name": "Suggested Restaurants",
    "cssClass": "success",
    "imageUrl": "",
    "toggled": true
  }];

  showRetweetsChanged(event: CustomEvent) {
    this.hideRetweets = event.detail.value;
    window.dispatchEvent(new CustomEvent('twitter:show-retweets', {detail: {value: this.hideRetweets}}));
  }

  get tweetPageActive() {
    return this.router.url === '/twitter';
  }

  get mapPageActive() {
    return this.router.url === '/map';
  }

  categoryToggleChanged(event: CustomEvent) {
    console.log(event);
    window.dispatchEvent(new CustomEvent('map:category-changed', {detail: event}));
  }
}
