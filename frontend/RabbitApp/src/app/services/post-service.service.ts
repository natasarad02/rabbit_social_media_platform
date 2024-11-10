import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ProfileViewDTO } from "../models/ProfileViewDTO.model";
import { PaginatedResponse } from "../models/Pagebale.model";
import { PostDTO } from "../models/PostDTO.model";

@Injectable({
    providedIn: 'root'
})

export class PostService {
    private apiUrl = 'http://localhost:8080/api/posts';
    constructor(private http: HttpClient) { }

    getAllPosts(): Observable<PostDTO[]> {
        return this.http.get<PostDTO[]>(this.apiUrl);
    }

    deletePost(id: number): Observable<void>
    {
        return this.http.put<void>(`${this.apiUrl}/delete/${id}`, {});
    }

    likePost(idProfile: number, idPost: number): Observable<void>
    {
        const params = new HttpParams()
        .set('profileId', idProfile)
        .set('postId', idPost);
        return this.http.post<void>(`${this.apiUrl}/like`, null, { params });
    }

    getLikedPosts(idProfile: number): Observable<number[]>
    {
        return this.http.get<number[]>(`${this.apiUrl}/likes`, {
            params: new HttpParams().set('profileId', idProfile)
        });
    }

    

}