import { IonicModule } from '@ionic/angular';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TwitterPage } from './twitter.page';

import { TwitterPageRoutingModule } from './twitter-routing.module';

@NgModule({
  imports: [
    IonicModule,
    CommonModule,
    FormsModule,
    TwitterPageRoutingModule
  ],
  declarations: [TwitterPage]
})
export class TwitterPageModule {}
