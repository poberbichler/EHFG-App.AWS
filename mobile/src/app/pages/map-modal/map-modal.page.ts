import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-map-modal',
  templateUrl: './map-modal.page.html',
  styleUrls: ['./map-modal.page.scss'],
})
export class MapModalPage implements OnInit {

  constructor(private domSanitizer: DomSanitizer) { }

  @Input()
  marker: any;

  ngOnInit() {
   console.log(this.marker);
  }

  get markerDescription() {
    return this.domSanitizer.bypassSecurityTrustHtml(this.marker.description);
  }
}
