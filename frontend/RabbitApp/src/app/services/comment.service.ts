import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CommentDTO } from '../models/CommentDTO.model';
import { Observable } from "rxjs";
@Injectable({
  providedIn: 'root'
})
export class CommentService {


    private apiUrl = 'http://localhost:8080/api/comments';
      constructor(private http: HttpClient) { }

      createComment(postId: number, profileId: number, comment: CommentDTO): Observable<CommentDTO>{
        const url = `${this.apiUrl}?idPost=${postId}&idProfile=${profileId}`;
        return this.http.post<CommentDTO>(url, comment);
    }
}
