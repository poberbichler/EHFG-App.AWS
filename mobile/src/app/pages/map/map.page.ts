import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Platform } from '@ionic/angular';
import { PointData } from 'src/app/providers/point-data';
import { GoogleMaps, LatLng } from "@ionic-native/google-maps";

declare const google: any;

@Component({
  selector: 'app-map',
  templateUrl: 'map.page.html',
  styleUrls: ['map.page.scss']
})
export class MapPage implements OnInit {

  constructor(
    private platform: Platform,
    private pointData: PointData,
    private googleMaps: GoogleMaps) { }

  private markers: any[] = [];

  @ViewChild('map') mapElement: ElementRef;

  ngOnInit(): void {
    window.addEventListener('map:category-changed', (event: CustomEvent) => {
      this.markers.filter(marker => marker.category === event.detail.name)
        .forEach(marker => marker.setVisible(event.detail.toggled));
    });
  }

  ionViewDidEnter() {
    if (this.isNative()) {
      this.platform.ready().then(() => {
        let map = this.createNativeMap();
        this.pointData.getPoints().subscribe(data => this.createNativeMarker(data, map));
        //TODOthis.workaroundSideMenu(map);
      });
    } else {
      let map = new google.maps.Map(this.mapElement.nativeElement, {
        center: { lat: 47.170329, lng: 13.103852 },
        zoom: 16
      });

      this.pointData.getPoints().subscribe(points => {
        points.forEach(point => {
          let marker = new google.maps.Marker({
            position: { lat: point.coordinate.latitude, lng: point.coordinate.longitude },
            map: map,
            icon: `assets/img/markers/${point.category.cssClass ? point.category.cssClass + '-' : ''}marker.png`,
            category: point.category.name
          });

          marker.addListener('click', () => {
            let infoWindow = new google.maps.InfoWindow({
              content: `<p>${point.description}</p>`
            });

            infoWindow.open(map, marker);
          });

          this.markers.push(marker);
        });
      });
    }
  }

  private createNativeMap() {
    return this.googleMaps.create(this.mapElement.nativeElement, {
      controls: {
        compass: true,
        myLocationButton: true,
        indoorPicker: true,
        zoom: true
      },
      gestures: {
        scroll: true,
        tilt: true,
        rotate: true,
        zoom: true
      },
      camera: {
        target: { lat: 47.170329, lng: 13.103852 },
        zoom: 16
      }
    });
  }

  private createNativeMarker(input: any, map: any) {
    input.json().forEach(point => {
      map.addMarker({
        icon: `assets/img/markers/${point.category.cssClass ? point.category.cssClass + '-' : ''}marker.png`,
        position: new LatLng(point.coordinate.latitude, point.coordinate.longitude),
        title: point.name,
        snippet: point.descriptionNative
      }).then(marker => {
        marker.set('category', point.category.name);
        this.markers.push(marker);
      });
    });
  }

  private isNative() {
    return this.platform.is('cordova');
  }
}
