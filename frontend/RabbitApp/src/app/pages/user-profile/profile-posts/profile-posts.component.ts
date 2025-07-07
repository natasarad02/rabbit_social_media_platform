import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { PostViewDTO } from '../../../models/PostViewDTO.model';
import { ProfileDTO } from '../../../models/ProfileDTO.model';
import { PostService } from '../../../services/post-service.service';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-profile-posts',
  templateUrl: './profile-posts.component.html',
  styleUrls: ['./profile-posts.component.css']
})
export class ProfilePostsComponent implements OnInit {
  @Input() userId!: number; 
  posts: PostViewDTO[] = [];
  loggedProfile: ProfileDTO | null = null; 
  imageStartPath: string = 'http://localhost:8080';
  
  constructor(
    private postService: PostService, 
    private router: Router, 
    private userService: UserService,
    private auth: AuthService
  ) {}

  ngOnInit() {
    this.loadPosts();    
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['userId'] && !changes['userId'].firstChange) {
      this.loadPosts();
    }
  }

  loadPosts() {
    this.postService.getAllPosts().subscribe(
      (response) => {
        this.posts = response;
        this.posts = response.filter(post => post.profile?.id === this.userId);
        console.log(this.posts);
  
        this.posts.sort((a, b) => {
          const aTime = new Date(Date.UTC(
            a.postedTime[0], 
            a.postedTime[1] - 1, 
            a.postedTime[2], 
            a.postedTime[3], 
            a.postedTime[4], 
            a.postedTime[5], 
            a.postedTime[6]  
          ));
  
          const bTime = new Date(Date.UTC(
            b.postedTime[0], 
            b.postedTime[1] - 1, 
            b.postedTime[2], 
            b.postedTime[3], 
            b.postedTime[4], 
            b.postedTime[5], 
            b.postedTime[6]  
          ));
  
          return bTime.getTime() - aTime.getTime(); 
        });
  
        this.posts.forEach(post => {
          if (post.picture.includes("/images")) {
            post.picture = this.imageStartPath + post.picture;
          }
        });
      },
      (error) => {
        console.error('Error loading posts', error);
      }
    );
  }
}
