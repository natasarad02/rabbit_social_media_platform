import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { LocationDTO } from '../models/LocationDTO.model';
import { PostDTO } from '../models/PostDTO.mode';
@Injectable({
  providedIn: 'root'
})
export class CreatePostService {

  private locationUrl = 'http://localhost:8080/api/locations';
  private postUrl = 'http://localhost:8080/api/posts';
  constructor(private http: HttpClient) { }

  createLocation(location: LocationDTO) : Observable<LocationDTO>{
    return this.http.post<LocationDTO>(this.locationUrl + '/savelocation', location);
  }

  createPost(post: PostDTO, profileId: number) : Observable<PostDTO>{
    return this.http.post<PostDTO>(this.postUrl + '/createpost/' + profileId, post);
  }
}
