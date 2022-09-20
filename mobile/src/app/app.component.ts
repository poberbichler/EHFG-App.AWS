import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CacheService } from 'ionic-cache';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent {
  constructor(private router: Router, private cacheService: CacheService) {
    this.cacheService.setDefaultTTL(60 * 60); // 1 hour
  }

  hideRetweets: string = 'true';

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

  showRetweetsChanged() {
    window.dispatchEvent(new CustomEvent('twitter:show-retweets', { detail: { value: this.hideRetweets } }));
  }

  get tweetPageActive() {
    return this.router.url === '/twitter';
  }

  get mapPageActive() {
    return this.router.url === '/map';
  }

  categoryToggleChanged(category) {
    window.dispatchEvent(new CustomEvent('map:category-changed', { detail: category }));
  }

  resetData(): void {
    this.cacheService.clearAll().then(() => location.reload());
  }
}
