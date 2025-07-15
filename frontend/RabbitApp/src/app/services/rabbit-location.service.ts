import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RabbitLocation } from '../models/rabbit-location.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RabbitLocationService {
  private apiUrl = 'http://localhost:8080/api/rabbit-locations';

  constructor(private http: HttpClient) { }

  getAllRabbitLocations(): Observable<RabbitLocation[]> {
    console.log(`Fetching rabbit locations from: ${this.apiUrl}`);
    return this.http.get<RabbitLocation[]>(this.apiUrl);
  }
}