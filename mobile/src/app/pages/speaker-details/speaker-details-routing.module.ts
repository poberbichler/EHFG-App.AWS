import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SpeakerDetailsPage } from './speaker-details.page';

const routes: Routes = [
  {
    path: '',
    component: SpeakerDetailsPage,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SpeakerDetailsPageRoutingModule {}
