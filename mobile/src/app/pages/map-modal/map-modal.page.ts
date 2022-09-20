import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-map-modal',
  templateUrl: './map-modal.page.html',
  styleUrls: ['./map-modal.page.scss'],
})
export class MapModalPage {

  constructor(private domSanitizer: DomSanitizer) { }

  @Input()
  marker: any;

  get markerDescription() {
    return this.domSanitizer.bypassSecurityTrustHtml(this.marker.description);
  }
}
