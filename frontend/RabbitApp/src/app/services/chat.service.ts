import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChatMessageDTO } from '../models/ChatMessageDTO.model';
import { ChatGroupDTO } from '../models/ChatGroupDTO.model';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private baseUrl = 'http://localhost:8080/api/chat';

  constructor(private http: HttpClient) {}

  getMessages(senderId: number, receiverId: number): Observable<ChatMessageDTO[]> {
    const params = new HttpParams()
      .set('senderId', senderId.toString())
      .set('receiverId', receiverId.toString());

    return this.http.get<ChatMessageDTO[]>(this.baseUrl, { params });
  }

  getUserChatGroups(userId: number): Observable<ChatGroupDTO[]> {
    return this.http.get<ChatGroupDTO[]>(`${this.baseUrl}/user/${userId}`);
  }

  getGroupMessages(userId: number, groupId: number): Observable<ChatMessageDTO[]> {
    return this.http.get<ChatMessageDTO[]>(`${this.baseUrl}/${groupId}/messages/${userId}`);
  }
  createGroup(userId: number, groupName: string): Observable<void> {
    const url = `${this.baseUrl}/createGroup`;
    const params = { userId: userId.toString(), groupName: groupName };
    return this.http.post<void>(url, null, { params: params });
  }

  addMemberToGroup(groupId: number, userId: number): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/${groupId}/add-member/${userId}`, {});
  }

  removeMemberFromGroup(groupId: number, userId: number): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}/${groupId}/remove-member/${userId}`);
  }
}