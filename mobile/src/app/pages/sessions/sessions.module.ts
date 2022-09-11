import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SessionsPage } from './sessions.page';

import { SessionsPageRoutingModule } from './sessions-routing.module';

@NgModule({
  imports: [
    IonicModule,
    CommonModule,
    FormsModule,
    SessionsPageRoutingModule
  ],
  declarations: [SessionsPage]
})
export class SessionsPageModule {}
