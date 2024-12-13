import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { Router } from '@angular/router';
import { PostService } from '../../services/post-service.service';
import { PostViewDTO } from '../../models/PostViewDTO.model';
import { ProfileService } from '../../services/profile-service.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent implements OnInit {

  likeIds: number[] = [];
  posts: PostViewDTO[] = [];
  followers: ProfileDTO[] = []; //sve koje ulogovan korisnik prati
  imageStartPath: string = 'http://localhost:8080';

  constructor(private userService: UserService, private authService: AuthService,
     private router: Router, private postService: PostService, private profileService: ProfileService){}

  loggedProfile: ProfileDTO | null = null;
  ngOnInit(): void {
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.loggedProfile = data;
          this.loadLikedPosts();
          this.loadFollowers(data.id);
        } else {
          console.log('No profile found or token expired');
        }
      },
      (error) => {
        console.error('Error loading profile:', error);
      }
    );
  
  }

  navigateToProfile() {
    if (this.loggedProfile && this.loggedProfile.role === 'Administrator') {
      this.router.navigate(['view-profile']);
    } else if(this.loggedProfile) {
      this.router.navigate(['profile', this.loggedProfile.id]);
    }
  }

  navigateToPost() {
    this.router.navigate(['view-posts']);
  }

  logOut()
  {
    this.authService.logout();
    this.ngOnInit();
  }

  logIn()
  {
    this.router.navigate(['/login']);
  }

  loadLikedPosts(): void {
    if (this.loggedProfile) {
      this.postService.getLikedPosts(this.loggedProfile.id).subscribe(
        (response) => {
          this.likeIds = response;
          console.log(this.likeIds);
        },
        (error) => {
          console.error('Error loading liked posts', error);
        }
      );
    }
  }

  //dobavlja sve koje prati
  loadFollowers(profileId: number): void {
    this.profileService.getFollowers(profileId).subscribe({
      next: (followers) => {
        this.followers = followers;
        console.log(followers);
        this.followers.forEach(follower => {
          this.getPosts(follower.id);
        });
      },
      error: (err) => {
        console.error('Error fetching followers:', err);
      }
    });
  }

  //dobavi sve postove za profile koje treba
  getPosts(profileId: number): void {
    this.postService.getPostsForProfile(profileId).subscribe(
      (response) => {
        // Add posts for the follower to the posts array
        const newPosts = response.map(post => {
          if (post.picture && post.picture.includes('/images')) {
            post.picture = this.imageStartPath + post.picture;
          }
          return post;
        });

        this.posts = [...this.posts, ...newPosts];  // Merge posts into the existing array
        console.log('Posts loaded for profile', profileId, this.posts);
      },
      (error) => {
        console.error('Error loading posts for profile', profileId, error);
      }
    );
  }

}
