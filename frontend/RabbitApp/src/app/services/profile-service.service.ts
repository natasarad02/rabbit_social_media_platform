import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ProfileViewDTO } from "../models/ProfileViewDTO.model";
import { ProfileDTO } from "../models/ProfileDTO.model";
import { PaginatedResponse } from "../models/Pagebale.model";

@Injectable({
    providedIn: 'root'
})

export class ProfileService {
    private apiUrl = 'http://localhost:8080/api/profiles';
    constructor(private http: HttpClient) { }

    getAllProfiles(): Observable<ProfileViewDTO[]> {
        return this.http.get<ProfileViewDTO[]>(`${this.apiUrl}/all`);
    }

    getProfile(id: number): Observable<ProfileDTO | null>{
        return this.http.get<ProfileDTO>(`${this.apiUrl}/`+id);
    }

    getFollowers(id: number): Observable<ProfileDTO[]>{
        return this.http.get<ProfileDTO[]>(`${this.apiUrl}/followers/`+id);
    }

    getFollowing(id: number): Observable<ProfileDTO[]>{
        return this.http.get<ProfileDTO[]>(`${this.apiUrl}/following/`+id);
    }

    getPaginatedProfiles(page: number, size: number, idProfile: number[]): Observable<PaginatedResponse<ProfileViewDTO>> {
        const params = {
            page: page.toString(),
            size: size.toString(),
            profileIds: idProfile.join(',')
        };
        
        return this.http.get<PaginatedResponse<ProfileViewDTO>>(`${this.apiUrl}/allPaged`, { params });
    }

    followProfile(profileId: number, followedProfileId: number): Observable<void> {
        const params = new HttpParams()
          .set('profileId', profileId.toString())
          .set('followedProfileId', followedProfileId.toString());
        return this.http.post<void>(`${this.apiUrl}/follow`, null, { params });
      }
    
      unfollowProfile(profileId: number, followedProfileId: number): Observable<void> {
        const params = new HttpParams()
          .set('profileId', profileId.toString())
          .set('followedProfileId', followedProfileId.toString());
        return this.http.post<void>(`${this.apiUrl}/unfollow`, null, { params });
      }
    
      



}