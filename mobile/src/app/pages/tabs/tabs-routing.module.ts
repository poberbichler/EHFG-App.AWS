import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TabsPage } from './tabs.page';

const routes: Routes = [
  {
    path: '',
    component: TabsPage,
    children: [
      {
        path: 'twitter',
        loadChildren: () => import('../twitter/twitter.module').then(m => m.TwitterPageModule)
      },
      {
        path: 'sessions',
        children: [
          {
            path: '',
            loadChildren: () => import('../sessions/sessions.module').then(m => m.SessionsPageModule)
          },
          {
            path: ':sessionId',
            loadChildren: () => import('../session-details/session-details.module').then(m => m.SessionsDetailsPageModule)
            
          }
        ]
      },
      {
        path: 'speakers',
        children: [
          {
            path: '',
            loadChildren: () => import('../speakers/speakers.module').then(m => m.SpeakersPageModule)
          },
          {
            path: ':speakerId',
            loadChildren: () => import('../speaker-details/speaker-details.module').then(m => m.SpeakerDetailsPageModule)
          }

        ]
      },
      {
        path: 'map',
        loadChildren: () => import('../map/map.module').then(m => m.SpeakersPageModule)
      },
      {
        path: '',
        redirectTo: '/twitter',
        pathMatch: 'full'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class TabsPageRoutingModule {}
