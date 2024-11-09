import { Component, AfterViewInit, Output, EventEmitter } from '@angular/core';
import * as L from 'leaflet';
import { MapService } from '../../services/map.service';
import { LocationDTO } from '../../models/LocationDTO.model';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements AfterViewInit{
  private map: any;
  private currentMarker: L.Marker | null = null; 

  @Output() locationChanged = new EventEmitter<LocationDTO>();

  newLocation: LocationDTO = {
    id: 0,
    longitude: 0,
    latitude: 0,
    address: '',
    number: '',
    deleted: false
  };

  constructor(private mapService: MapService) {}

  private initMap(): void {
    this.map = L.map('map', {
      center: [45.2396, 19.8227],
      zoom: 13,
    });

    const tiles = L.tileLayer(
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      {
        maxZoom: 18,
        minZoom: 3,
        attribution:
          '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }
    );
    tiles.addTo(this.map);

    this.registerOnClick();
  }

  ngAfterViewInit(): void {
    let DefaultIcon = L.icon({
      iconUrl: 'bunnylocation.svg',
      iconSize: [45, 45],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32]
    });

    L.Marker.prototype.options.icon = DefaultIcon;
    this.initMap();
  }

  registerOnClick(): void {
    this.map.on('click', (e: any) => {
      const coord = e.latlng;
      const lat: number = coord.lat;
      const lng: number = coord.lng;

      if (this.currentMarker) {
        this.map.removeLayer(this.currentMarker);
      }


     

      console.log(
        'You clicked the map at latitude: ' + lat + ' and longitude: ' + lng
      );
      this.currentMarker = new L.Marker([lat, lng]).addTo(this.map);

      this.mapService.reverseSearch(lat, lng).subscribe((res) => {
        const address = res.address;
        const houseNumber = address.house_number;
        const road = address.road;
        const city = address.city;   
        const country = address.country;

      
        const locationAddress: string = road + ', ' + houseNumber + ', ' + city + ', ' + country;
        const locationNumber = houseNumber;

        this.newLocation.address = locationAddress;
        this.newLocation.latitude = lat;
        this.newLocation.longitude = lng;
        this.newLocation.number = locationNumber;

        
        this.locationChanged.emit(this.newLocation);

       
       
      });

     
    });
  }


}
