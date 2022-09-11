import { IonicModule } from '@ionic/angular';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { SessionDetailsPage } from './session-details.page';
import { SessionDetailsPageRoutingModule } from './session-details-routing.module';

@NgModule({
  imports: [
    IonicModule,
    CommonModule,
    FormsModule,
    SessionDetailsPageRoutingModule
  ],
  declarations: [SessionDetailsPage]
})
export class SessionsDetailsPageModule { }
