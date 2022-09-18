import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { IonicStorageModule } from "@ionic/storage-angular";

import { CacheModule } from "ionic-cache";
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SpeakerData } from './providers/speaker-data';
import { SessionData } from './providers/session-data';
import { TwitterData } from './providers/twitter-data';
import { PointData } from './providers/point-data';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule, 
    IonicModule.forRoot(),
    CacheModule.forRoot(),
    IonicStorageModule.forRoot(),
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
    SpeakerData,
    SessionData,
    TwitterData,
    PointData
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
