import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MapPage } from './map.page';

import { MapPageRoutingModule } from './map-routing.module';
import { MapModalPageModule } from '../map-modal/map-modal.module';

import { GoogleMaps } from "@ionic-native/google-maps";

@NgModule({
  imports: [
    IonicModule,
    CommonModule,
    FormsModule,
    RouterModule.forChild([{ path: '', component: MapPage }]),
    MapPageRoutingModule,
    MapModalPageModule
  ],
  declarations: [
    MapPage
  ],
  providers: [
    GoogleMaps
  ]
})
export class MapPageModule { }
