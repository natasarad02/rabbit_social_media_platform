import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ProfileViewDTO } from "../models/ProfileViewDTO.model";
import { PaginatedResponse } from "../models/Pagebale.model";
import { PostViewDTO } from "../models/PostViewDTO.model";
import { PostDTO } from "../models/PostDTO.mode";
import { CommentDTO } from "../models/CommentDTO.model";
import { PostAdDTO } from "../models/PostAdDTO.model";

@Injectable({
    providedIn: 'root'
})

export class PostService {
    private apiUrl = 'http://localhost:8080/api/posts';
    constructor(private http: HttpClient) { }

    getAllPosts(): Observable<PostViewDTO[]> {
        return this.http.get<PostViewDTO[]>(this.apiUrl);
    }

    getPostsForProfile(profileId: number): Observable<PostViewDTO[]> {
        return this.http.get<PostViewDTO[]>(`${this.apiUrl}/forProfile/${profileId}`);
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

    dislikePost(idProfile: number, idPost: number): Observable<void>
    {
        const params = new HttpParams()
        .set('profileId', idProfile)
        .set('postId', idPost);
        return this.http.post<void>(`${this.apiUrl}/dislike`, null, { params });
    }

    getLikedPosts(idProfile: number): Observable<number[]>
    {
        return this.http.get<number[]>(`${this.apiUrl}/likes`, {
            params: new HttpParams().set('profileId', idProfile)
        });
    }

    getPostById(id: number): Observable<PostViewDTO> {
        const url = `${this.apiUrl}/id/${id}`;
        return this.http.get<PostViewDTO>(url);
      }

      updatePost(postDTO: PostViewDTO): Observable<PostViewDTO> {
        return this.http.put<PostViewDTO>(this.apiUrl, postDTO);
    }


    sendForAds(post: PostAdDTO): Observable<PostAdDTO>{
        const url = "http://localhost:8080/api/post_ads/send_ad";
        return this.http.post<PostAdDTO>(url, post);
    }

    
    

    

}