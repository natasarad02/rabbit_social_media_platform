import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post-service.service';
import { PostViewDTO } from '../../models/PostViewDTO.model';
import { ProfileTrendDTO } from '../../models/ProfileTrendDTO.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-trends',
  templateUrl: './trends.component.html',
  styleUrl: './trends.component.css'
})
export class TrendsComponent implements OnInit {
  totalPosts: number = 0;
  postsLastWeek: number = 0;
  top10Posts: PostViewDTO[] = [];
  top5Posts: PostViewDTO[] = [];
  trendingProfiles: ProfileTrendDTO[] = [];
  displayedPosts: PostViewDTO[] = [];

  selectedTrend: string = 'top10';

  constructor(private trendService: PostService, private router: Router) {}

  ngOnInit(): void {
    this.fetchAlwaysVisibleData();
    this.fetchTrendData();
  }

  fetchAlwaysVisibleData(): void {
    this.trendService.getNumberOfPosts().subscribe((count) => (this.totalPosts = count));
    this.trendService.getNumberOfPostsInLastMonth().subscribe((count) => (this.postsLastWeek = count));
  }

  fetchTrendData(): void {
    this.trendService.getTop10MostLikedPosts().subscribe((posts) => {
      this.top10Posts = posts;
      if (this.selectedTrend === 'top10') this.displayedPosts = posts;
    });

    this.trendService.findTop5MostLikedPostsInLast7Days().subscribe((posts) => {
      this.top5Posts = posts;
      if (this.selectedTrend === 'top5') this.displayedPosts = posts;
    });

    this.trendService.findProfilesWithMostLikesGivenInLast7Days().subscribe((profiles) => {
      this.trendingProfiles = profiles;
    });
    alert
  }

  onTrendSelectionChange(): void {
    if (this.selectedTrend === 'top10') {
      this.displayedPosts = this.top10Posts;
    } else if (this.selectedTrend === 'top5') {
      this.displayedPosts = this.top5Posts;
    }
  }

  showProfileId(id: number){
    this.router.navigate(['profile', id]);
  }

  setTrend(trend: string): void {
    this.selectedTrend = trend;
  
    if (trend === 'top10') {
      this.displayedPosts = this.top10Posts;
    } else if (trend === 'top5') {
      this.displayedPosts = this.top5Posts;
    }
  }
  
}