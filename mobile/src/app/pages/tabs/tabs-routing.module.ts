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
        loadChildren: () => import('../sessions/sessions.module').then(m => m.SessionsPageModule)
      },
      {
        path: 'speakers',
        loadChildren: () => import('../speakers/speakers.module').then(m => m.SpeakersPageModule)
      },
      {
        path: '',
        redirectTo: '/twitter',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '',
    redirectTo: '/twitter',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class TabsPageRoutingModule {}
