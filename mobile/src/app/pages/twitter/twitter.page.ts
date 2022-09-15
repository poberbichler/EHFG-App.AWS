import { Component } from '@angular/core';
import { TwitterData } from 'src/app/providers/twitter-data';

@Component({
  selector: 'app-twitter',
  templateUrl: 'twitter.page.html',
  styleUrls: ['twitter.page.scss']
})
export class TwitterPage {
  
  constructor(private twitterData: TwitterData) {}
  
  tweets: any[];
  tweetData: any;
  
  ngOnInit(): void {
    this.twitterData.getInitialTweets().subscribe(tweetData => {
      this.tweets = tweetData.data;
      this.tweetData = tweetData;
    });
  }

  loadData(event) {
    console.log(event);
  }

  get hasMoreTweets(): boolean {
    if (this.tweetData) {
      return this.tweetData.morePages;
    }
    return false;
  }

  get currentHashtag(): string {
    if(this.tweetData) {
      return this.tweetData.currentHashtag;
    }

    return "";
  }
}
