import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { AnalyticsDTO } from "../models/AnalyticsDTO.model";

@Injectable({
    providedIn: 'root',
  })
  export class AnalyticsService {
    private apiUrl = 'http://localhost:8080/api/analytics';
  
    constructor(private http: HttpClient) {}
  
    getAnalyticsData(): Observable<AnalyticsDTO> {
      return this.http.get<AnalyticsDTO>(this.apiUrl);
    }
  }
  