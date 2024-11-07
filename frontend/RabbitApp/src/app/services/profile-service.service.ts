import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ProfileViewDTO } from "../models/ProfileViewDTO.model";
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

    getPaginatedProfiles(page: number, size: number): Observable<PaginatedResponse<ProfileViewDTO>> {
        const params = {
            page: page.toString(),
            size: size.toString()
        };
        
        return this.http.get<PaginatedResponse<ProfileViewDTO>>(`${this.apiUrl}/allPaged`, { params });
    }

}