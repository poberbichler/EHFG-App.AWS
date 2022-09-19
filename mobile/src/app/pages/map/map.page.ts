import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MenuController, ModalController, Platform } from '@ionic/angular';
import { PointData } from 'src/app/providers/point-data';
import { GoogleMap } from '@capacitor/google-maps';
import { MapModalPage } from '../map-modal/map-modal.page';

declare const google: any;

@Component({
  selector: 'app-map',
  templateUrl: 'map.page.html',
  styleUrls: ['map.page.scss']
})
export class MapPage implements OnInit {

  constructor(
    private pointData: PointData,
    private modalController: ModalController) { }

  private markers: any[] = [];
  private hiddenMarkers: any[] = [];

  private map: GoogleMap;

  ngOnInit(): void {
    window.addEventListener('map:category-changed', (event: CustomEvent) => {
      if (event.detail.toggled) {
        let markersToAdd = this.hiddenMarkers
          .filter(marker => {
            return marker.category.name === event.detail.name
          });

          markersToAdd.forEach(marker => {
            this.map.addMarker({
              coordinate: { lat: marker.coordinate.latitude, lng: marker.coordinate.longitude },
              iconUrl: `assets/img/markers/${marker.category.cssClass ? marker.category.cssClass + '-' : ''}marker.png`
            }).then(markerId => this.markers.push({ ...marker, markerId: markerId,  }))
          });
      } else {
        let markersToRemove = this.markers
          .filter(marker => marker.category.name === event.detail.name);
        
        if (markersToRemove.length !== 0) {
          this.map.removeMarkers(markersToRemove.map(marker => marker.markerId));
        }

        this.hiddenMarkers.push(...markersToRemove);
        this.markers = this.markers.filter(marker => marker.category.name !== event.detail.name);
      }
    });
  }

  ionViewDidEnter(): void {
    const mapConfig = {
      center: {
        lat: 47.170329,
        lng: 13.103852,
      },
      zoom: 16,
      androidLiteMode: false,
    };

    const mapOptions = {
      id: "ehfg-map",
      apiKey: "AIzaSyDVVBimV3mdTV3V2kYCr5qpp9otxBO30D0",
      config: mapConfig,
      element: document.getElementById('map'),
    };

    GoogleMap.create(mapOptions).then(map => {
      this.map = map;

      map.setOnMarkerClickListener(marker => {
        this.modalController.create({
          component: MapModalPage,
          breakpoints: [0, 0.3, 0.5],
          initialBreakpoint: 0.3,
          componentProps: {
            marker: this.markers.find(it => it.markerId === marker.markerId)
          }
        }).then(modal => {
          modal.present();
        });
      });

      this.pointData.getPoints().subscribe(points => {
        points.forEach(point => {
          const data = {
            coordinate: { lat: point.coordinate.latitude, lng: point.coordinate.longitude },
            iconUrl: `assets/img/markers/${point.category.cssClass ? point.category.cssClass + '-' : ''}marker.png`
          };

          map.addMarker(data)
            .then(markerId => this.markers.push({ markerId: markerId, ...point }));
        });
      });
    });
  }
}
