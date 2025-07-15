import { Component, OnInit, OnDestroy, Output, EventEmitter, ViewContainerRef, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import * as L from 'leaflet';
import { PostService } from '../../services/post-service.service';
import { PostViewDTO } from '../../models/PostViewDTO.model';
import { PostComponent } from '../post/post.component';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { RabbitLocation } from '../../models/rabbit-location.model';
import { RabbitLocationService } from '../../services/rabbit-location.service';

@Component({
  selector: 'app-map-posts',
  templateUrl: './map-posts.component.html',
  styleUrls: ['./map-posts.component.scss']
})
export class MapPostsComponent implements OnInit, OnDestroy {
  private addressToDisplay = ''; // FTN, Novi Sad
  public mapId = `map-posts-${Math.random().toString(36).substring(2)}`;
  private map: L.Map | null = null;
  private readonly nominatimUrl = 'https://nominatim.openstreetmap.org/search?format=json&q=';
  posts: PostViewDTO[] = [];
  filteredPosts: PostViewDTO[] = [];
  imageStartPath: string = 'http://localhost:8080';
  private postsLayerGroup: L.LayerGroup | null = null;
  public currentCenterLocation: L.LatLng | null = null;
  private viewContainerRef = inject(ViewContainerRef);
  private componentRefs: any[] = [];
  rabbitLocations: RabbitLocation[] = []; 
  filteredRabbitLocations: RabbitLocation[] = []; 
  private rabbitLocationsLayerGroup: L.LayerGroup | null = null;
  rabbitLocationIcon: L.Icon;

  @Output() centerLocationChanged = new EventEmitter<L.LatLng>();


  constructor(private http: HttpClient,
              private postService: PostService,
              private userService: UserService, 
              private auth: AuthService,
              private rabbitLocationService: RabbitLocationService ) {
                console.log('CONSTRUCTOR: MapPostsComponent constructor called.');
                this.rabbitLocationIcon = L.icon({
                  iconUrl: 'vet.png', 
                  iconSize: [32, 32],    
                  iconAnchor: [16, 32],  
                  popupAnchor: [0, -32]  
                });
                console.log('CONSTRUCTOR: rabbitLocationIcon initialized with URL:', this.rabbitLocationIcon.options.iconUrl);
              }


  ngOnInit(): void {
    this.loadUser();
    
  }

  loadUser(): void {    
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.addressToDisplay = data.address;
          this.geocodeAndInitializeMap();
          this.loadPosts();
          this.loadRabbitLocations();
        } else {
          console.log('No profile found or token expired');
          alert('No profile found or token expired');
        }
      },
      (error) => {
        console.error('Error loading profile:', error);
        alert('Error loading profile:'+ error.message);
        this.auth.logout();
      }
    );
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
    // --- NOVO: Dodavanje sloja za markere zečjih lokacija ---
    this.rabbitLocationsLayerGroup = L.layerGroup().addTo(this.map);

    tiles.addTo(this.map);

    this.currentCenterLocation = this.map.getCenter();
    this.centerLocationChanged.emit(this.currentCenterLocation);
    console.log('Početna lokacija centra:', this.currentCenterLocation);

    this.map.on('moveend', () => {
      this.currentCenterLocation = this.map!.getCenter(); // Uzimamo novi centar
      this.centerLocationChanged.emit(this.currentCenterLocation); // Šaljemo ga napolje
      this.filterPostsByLocation();
      this.filterRabbitLocationsByLocation(); // NOVO: Filtriranje i zečjih lokacija
      console.log('Novi centar mape (lat, lng):', this.currentCenterLocation.lat, this.currentCenterLocation.lng);
    });
  }

  loadRabbitLocations() {
    console.log('LOADRABBITLOCATIONS: Starting to load rabbit locations...'); // DODAJTE OVO
    this.rabbitLocationService.getAllRabbitLocations().subscribe(
      (response) => {
        console.log('LOADRABBITLOCATIONS: Successfully received response:', response); // DODAJTE OVO
        this.rabbitLocations = response;
        console.log("Loaded rabbit locations:", this.rabbitLocations);
        this.filterRabbitLocationsByLocation();
      },
      (error: any) => {
        console.error('LOADRABBITLOCATIONS: Error loading rabbit locations:', error); // DODAJTE OVO
        // Opciono: dodajte alert za lakse debagovanje
        // alert('Error loading rabbit locations: ' + error.message);
      }
    );
  }

  private filterRabbitLocationsByLocation(): void {
    const radiusInMeters = 1000; // Koristite isti radijus ili drugi po potrebi

    if (!this.currentCenterLocation || !this.rabbitLocations || this.rabbitLocations.length === 0) {
      this.filteredRabbitLocations = [];
      this.updateRabbitLocationMarkersOnMap(); // Ažuriranje da bi se očistili stari markeri
      return;
    }

    this.filteredRabbitLocations = this.rabbitLocations.filter(location => {
      if (location.latitude == null || location.longitude == null) {
        return false;
      }

      const locationPoint = L.latLng(location.latitude, location.longitude);
      const distance = this.currentCenterLocation!.distanceTo(locationPoint);
      return distance <= radiusInMeters;
    });

    console.log(`Pronađeno ${this.filteredRabbitLocations.length} lokacija za zečeve unutar 1km.`);
    this.updateRabbitLocationMarkersOnMap();
  }

  private updateRabbitLocationMarkersOnMap(): void {
    if (!this.rabbitLocationsLayerGroup) {
      return;
    }

    // Briše prethodne markere za zečje lokacije
    this.rabbitLocationsLayerGroup.clearLayers();

    this.filteredRabbitLocations.forEach(location => {
      if (location.latitude == null || location.longitude == null) {
        return;
      }

      const marker = L.marker([location.latitude, location.longitude], {
            icon: this.rabbitLocationIcon // Koristite prilagođenu ikonu
          });

          // Prikaz naziva lokacije u popup-u
          marker.bindPopup(`<b>${location.name}</b>`);

          // --- IZMENJENE LINIJE: Koristite arrow funkcije ---
          // Opciono: otvorite popup na hover, zatvorite na mouseout
          marker.on('mouseover', (e) => { // Promenjeno u arrow funkciju (e) => { ... }
            (e.target as L.Marker).openPopup(); // Koristite e.target da pristupite markeru i Leaflet tipovanje
          });
          marker.on('mouseout', (e) => { // Promenjeno u arrow funkciju (e) => { ... }
            (e.target as L.Marker).closePopup(); // Koristite e.target da pristupite markeru i Leaflet tipovanje
          });

      this.rabbitLocationsLayerGroup?.addLayer(marker);
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

    // Clear previous markers and component references
    this.postsLayerGroup.clearLayers();
    this.componentRefs.forEach(ref => ref.destroy());
    this.componentRefs = [];

    this.filteredPosts.forEach(post => {
      if (post.latitude == null || post.longitude == null) {
        return;
      }

      const postIcon = L.icon({
        iconUrl: post.picture,
        iconSize: [40, 40],
        iconAnchor: [20, 40],
        popupAnchor: [0, -42],
        className: 'post-marker-icon'
      });

      const marker = L.marker([post.latitude, post.longitude], {
        icon: postIcon
      });

      const componentRef = this.viewContainerRef.createComponent(PostComponent);
      componentRef.instance.post = post;
      const popupWrapper = document.createElement('div');
      popupWrapper.className = 'map-popup-wrapper'; 

      popupWrapper.appendChild(componentRef.location.nativeElement);

      popupWrapper.style.width = '260px';
      popupWrapper.style.maxWidth = '260px';


      marker.bindPopup(popupWrapper, {
        className: 'map-popup-wrapper'
      });

      
      this.componentRefs.push(componentRef); 
      this.postsLayerGroup?.addLayer(marker);
    });
  }

  ngOnDestroy(): void {
    this.componentRefs.forEach(ref => ref.destroy());
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