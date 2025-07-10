// src/app/map-posts/map-posts.component.ts

import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core'; // Changed imports
import { HttpClient } from '@angular/common/http';
import * as L from 'leaflet';
import { PostService } from '../../services/post-service.service';
import { PostViewDTO } from '../../models/PostViewDTO.model';

interface NominatimResponse {
  lat: string;
  lon: string;
  display_name: string;
}

@Component({
  selector: 'app-map-posts',
  templateUrl: './map-posts.component.html',
  styleUrls: ['./map-posts.component.scss']
})
export class MapPostsComponent implements OnInit, OnDestroy {
  private addressToDisplay = 'FTN, Novi Sad';
  public mapId = `map-posts-${Math.random().toString(36).substring(2)}`;
  private map: L.Map | null = null;
  private readonly nominatimUrl = 'https://nominatim.openstreetmap.org/search?format=json&q=';
  posts: PostViewDTO[] = [];
  filteredPosts: PostViewDTO[] = [];
  imageStartPath: string = 'http://localhost:8080';
  private postsLayerGroup: L.LayerGroup | null = null;

  public currentCenterLocation: L.LatLng | null = null;

  @Output() centerLocationChanged = new EventEmitter<L.LatLng>();


  constructor(private http: HttpClient,
              private postService: PostService) {}

  ngOnInit(): void {
    if (this.addressToDisplay) {
      this.geocodeAndInitializeMap();
      this.loadPosts();
    }    
  }

  private geocodeAndInitializeMap(): void {
    const encodedAddress = encodeURIComponent(this.addressToDisplay);
    this.http.get<any[]>(`${this.nominatimUrl}${encodedAddress}&limit=1`).subscribe(
      (response) => {
        if (response && response.length > 0) {
          const lat = parseFloat(response[0].lat);
          const lon = parseFloat(response[0].lon);
          this.initMap(lat, lon);
        }
      }
    );
  }

  private initMap(lat: number, lon: number): void {
    if (this.map) {
      this.map.setView([lat, lon]);
      return;
    }

    this.map = L.map(this.mapId, {
      center: [lat, lon],
      zoom: 16
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });
    this.postsLayerGroup = L.layerGroup().addTo(this.map);
    tiles.addTo(this.map);

    this.currentCenterLocation = this.map.getCenter();
    this.centerLocationChanged.emit(this.currentCenterLocation);
    console.log('Početna lokacija centra:', this.currentCenterLocation);

    this.map.on('moveend', () => {
      this.currentCenterLocation = this.map!.getCenter(); // Uzimamo novi centar
      this.centerLocationChanged.emit(this.currentCenterLocation); // Šaljemo ga napolje
      this.filterPostsByLocation(); 
      console.log('Novi centar mape (lat, lng):', this.currentCenterLocation.lat, this.currentCenterLocation.lng);
    });
  }

  private filterPostsByLocation(): void {
    const radiusInMeters = 1000; // 1km

    // Proveravamo da li imamo sve potrebne podatke za filtriranje
    if (!this.currentCenterLocation || !this.posts || this.posts.length === 0) {
      this.filteredPosts = []; // Ako nešto fali, lista filtriranih je prazna
      return;
    }
    
    this.filteredPosts = this.posts.filter(post => {
      if (post.latitude == null || post.longitude == null) {
        return false;
      }

      const postPoint = L.latLng(post.latitude, post.longitude);
      const distance = this.currentCenterLocation!.distanceTo(postPoint);
      return distance <= radiusInMeters;
    });

    console.log(`Pronađeno ${this.filteredPosts.length} postova unutar 1km.`);
    this.updateMarkersOnMap();
  }

  
  private updateMarkersOnMap(): void {
    if (!this.postsLayerGroup) {
      return;
    }

    this.postsLayerGroup.clearLayers();

    this.filteredPosts.forEach(post => {
      if (post.latitude == null || post.longitude == null) {
        return;
      }

      const marker = L.marker([post.latitude, post.longitude]);

      const popupContent = `
        <div class="post-popup">
          <img src="${post.picture}" alt="Post image" class="popup-image">
          <p class="popup-description">${post.description}</p>
        </div>
      `;

      marker.bindPopup(popupContent);
      this.postsLayerGroup?.addLayer(marker);
    });
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.remove();
    }
  }



loadPosts() {
  this.postService.getAllPosts().subscribe(
    (response) => {
      this.posts = response;
      console.log(this.posts);

      this.posts.forEach(post => {
        if (post.picture && post.picture.includes("/images")) {
          post.picture = this.imageStartPath + post.picture;
        }
      });
      
      this.filterPostsByLocation(); 
    },
    (error: any) => {
      console.error('Error loading posts', error);
    }
  );
}

  
}