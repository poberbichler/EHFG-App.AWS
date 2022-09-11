import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SessionDetailsPage } from './session-details.page';

const routes: Routes = [
  {
    path: '',
    component: SessionDetailsPage,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SessionDetailsPageRoutingModule {}