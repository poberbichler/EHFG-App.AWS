import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SpeakerDetailsPage } from './speaker-details.page';

import { SpeakerDetailsPageRoutingModule } from './speaker-details-routing.module';

@NgModule({
  imports: [
    IonicModule,
    CommonModule,
    FormsModule,
    RouterModule.forChild([{ path: '', component: SpeakerDetailsPage }]),
    SpeakerDetailsPageRoutingModule
  ],
  declarations: [SpeakerDetailsPage]
})
export class SpeakerDetailsPageModule {}
