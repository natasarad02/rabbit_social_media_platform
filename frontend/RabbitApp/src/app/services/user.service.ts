import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {map} from 'rxjs/operators';
import { ProfileDTO } from '../models/ProfileDTO.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  currentUser: ProfileDTO | null = null;
  private apiUrl = 'http://localhost:8080/auth/userFromToken';

  constructor(
    private apiService: ApiService,
    private config: ConfigService,
    private http: HttpClient
  ) {
  }

  getUserProfile(): Observable<ProfileDTO | null> {
    return this.http.get<ProfileDTO>(this.apiUrl); // The interceptor automatically adds the token
  }

}