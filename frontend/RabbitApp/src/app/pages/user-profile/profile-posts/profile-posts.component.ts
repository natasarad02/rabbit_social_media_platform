import { Component, Input, OnInit, SimpleChanges, OnChanges } from '@angular/core';
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
export class ProfilePostsComponent implements OnInit, OnChanges {
  @Input() userId!: number; 
  posts: PostViewDTO[] = [];
  loggedProfile: ProfileDTO | null = null; 
  imageStartPath: string = 'http://localhost:8080';
  likeIds: number[] = [];
  
  constructor(
    private postService: PostService, 
    private router: Router, 
    private userService: UserService,
    private auth: AuthService
  ) {}

  ngOnInit() {
    this.getLoggedUser();
    this.loadPosts();    
  }

  ngOnChanges(changes: SimpleChanges) {
    // Reload posts if the userId input changes (but not on the initial load)
    if (changes['userId'] && !changes['userId'].firstChange) {
      this.loadPosts();
    }
  }

  loadPosts() {
    this.postService.getAllPosts().subscribe(
      (response) => {
        // Filter posts to show only those belonging to the current profile's user
        this.posts = response.filter(post => post.profile?.id === this.userId);
        
        // Sort posts by posted time, newest first
        this.posts.sort((a, b) => {
          const aTime = new Date(Date.UTC(
            a.postedTime[0], a.postedTime[1] - 1, a.postedTime[2], 
            a.postedTime[3], a.postedTime[4], a.postedTime[5], a.postedTime[6]
          ));
          const bTime = new Date(Date.UTC(
            b.postedTime[0], b.postedTime[1] - 1, b.postedTime[2], 
            b.postedTime[3], b.postedTime[4], b.postedTime[5], b.postedTime[6]
          ));
          return bTime.getTime() - aTime.getTime(); 
        });
  
        // Prepend server path to image URLs
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

  getLoggedUser() {
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          this.loggedProfile = data;
          this.loadLikedPosts(); // After getting the user, load their liked posts
        } else {
          console.log('No profile found or token expired');
        }
      },
      (error) => {
        console.error('Error loading profile:', error);
      }
    );
  }

  loadLikedPosts(): void {
    if (this.loggedProfile) {
      this.postService.getLikedPosts(this.loggedProfile.id).subscribe(
        (response) => {
          this.likeIds = response;
        },
        (error) => {
          console.error('Error loading liked posts', error);
        }
      );
    }
  }
}