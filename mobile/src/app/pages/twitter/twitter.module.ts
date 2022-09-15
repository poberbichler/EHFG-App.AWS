import { IonicModule } from '@ionic/angular';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TwitterPage } from './twitter.page';

import { TwitterPageRoutingModule } from './twitter-routing.module';
import { TrustHtmlPipe } from 'src/components/trust-html/trust-html.pipe';

@NgModule({
  imports: [
    IonicModule,
    CommonModule,
    FormsModule,
    TwitterPageRoutingModule
  ],
  declarations: [
    TwitterPage,
    TrustHtmlPipe
  ]
})
export class TwitterPageModule {}
