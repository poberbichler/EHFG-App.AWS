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
    console.log("infinite scroll triggered...", event);
    this.twitterData.getTweetPage(this.tweetData.currentPage + 1).subscribe(tweetData => {
      this.tweetData = tweetData;
      this.tweets = this.tweets.concat(tweetData.data);
      event.target.complete();
    });
  }

  doRefresh(event) {
    console.log('refresh triggered...');
    this.twitterData.getNewerTweets(this.tweets[0].id).subscribe(newerTweets => {
      this.tweets = newerTweets.concat(this.tweets);
      event.target.complete();
    });
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
