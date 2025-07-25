import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post-service.service';
import { PostViewDTO } from '../../models/PostViewDTO.model';
import { Router } from '@angular/router';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';
import { ImageCacheService } from '../../services/image-cache.service';
import { PostDTO } from '../../models/PostDTO.mode';
import { PostAdDTO } from '../../models/PostAdDTO.model';

@Component({
  selector: 'app-view-posts-registered',
  templateUrl: 'view-posts-registered.component.html',
  styleUrls: ['view-posts-registered.component.css']
})
export class ViewPostsRegisteredComponent implements OnInit {
  posts: PostViewDTO[] = [];
  profileId: number = 1;
  likeIds: number[] = [];
  imageStartPath: string = 'http://localhost:8080';
  loggedProfile: ProfileDTO | null = null;
  cachedImages: { [id: number]: string } = {};

  postAd: PostAdDTO = {} as PostAdDTO;
  constructor(
    private postService: PostService, 
    private router: Router, 
    private userService: UserService,
    private auth: AuthService,
    private imageCacheService: ImageCacheService
  ) {}

  ngOnInit(): void {
    this.loadUser();
    this.postService.getAllPosts().subscribe(
      async (response) => {
        this.posts = response;
  
        // Sort posts by posted time
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
  
        // Prepend image path for posts with relative picture URLs
        this.posts.forEach(post => {
          if (post.picture.includes("/images")) {
            post.picture = this.imageStartPath + post.picture;
          }
        });
  
        // Cache pictures asynchronously
        await this.cachePictures();
      },
      (error) => {
        console.error('Error loading posts', error);
      }
    );
  }
  
  
  
  async cachePictures(): Promise<void> {
    try {
      const fetchPromises = this.posts.map(async (post) => {
        if (!this.cachedImages[post.id]) {
          this.cachedImages[post.id] = await this.imageCacheService.fetchImage(post.picture);
          console.log(`Cached image for post ${post.id}:`, this.cachedImages[post.id]);
        }
      });
      await Promise.all(fetchPromises);
      console.log('All images cached:', this.cachedImages);
    } catch (error) {
      console.error('Error caching images:', error);
    }
  }
  
  

  loadUser(): void {
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.loggedProfile = data;
          this.profileId = this.loggedProfile.id;
          this.loadLikedPosts();  
        } else {
          console.log('No profile found or token expired');
          this.profileId = -1;
        }
      },
      (error) => {
        console.error('Error loading profile:', error);
        this.auth.logout();
        this.profileId = -1;
      }
    );
  }

  showLoginAlert(): void {
    Swal.fire({
      icon: 'warning',
      title: 'Action Not Allowed',
      text: 'You need to log in to like or comment on posts!',
      confirmButtonText: 'OK'
    });
  }

  loadLikedPosts(): void {
    if (this.loggedProfile) {
      this.postService.getLikedPosts(this.profileId).subscribe(
        (response) => {
          this.likeIds = response;
        },
        (error) => {
          console.error('Error loading liked posts', error);
        }
      );
    }
  }

  deletePost(id: number): void {
    const confirmed = window.confirm('Are you sure you want to delete this post?');
  
    if (confirmed) {
      this.postService.deletePost(id).subscribe(() => {
        console.log(this.posts);
        this.ngOnInit();
      },
      (error) => {
        console.error('Error deleting post', error);
      });
    }
  }

  likePost(postId: number): void {
    if (this.loggedProfile) {
      this.postService.likePost(this.profileId, postId).subscribe(() => {
        this.ngOnInit();
        console.log("success");
      },
      (error) => {
        console.error('Error liking post', error);
      });
    } else {
      this.showLoginAlert();  
    }
  }

  dislikePost(postId: number): void {
    if (this.loggedProfile) {
      this.postService.dislikePost(this.profileId, postId).subscribe(() => {
        this.ngOnInit();
        console.log("success");
      },
      (error) => {
        console.error('Error liking post', error);
      });
    } else {
      this.showLoginAlert();  
    }
  }
  

  commentOnPost(postId: number): void {
    if (this.loggedProfile) {
      
      console.log(`Commenting on post with ID: ${postId}`);
     
    } else {
      this.showLoginAlert();  
    }
  }

  showCreatorInfo(post: PostViewDTO): void {
    const creatorInfo = `
      <strong>Ime:</strong> ${post.profile?.name || 'N/A'}<br>
      <strong>Prezime:</strong> ${post.profile?.surname || 'N/A'}<br>
      <strong>Email:</strong> ${post.profile?.email || 'N/A'}
    `;
    
    Swal.fire({
      title: 'Creator Information',
      html: creatorInfo,
      icon: 'info',
      showCancelButton: true,
      confirmButtonText: 'Go to profile',
      cancelButtonText: 'Close',
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33'
    }).then((result) => {
      if (result.isConfirmed && post.profile?.id) {
        window.location.href = `http://localhost:4200/profile/${post.profile.id}`;
      }
    });
  }

  showCommentCreatorInfo(comment: any): void {
    const commentCreatorInfo = `
      <strong>Name:</strong> ${comment.profile?.name || 'N/A'}<br>
      <strong>Username:</strong> ${comment.profile?.username || 'N/A'}<br>
      <strong>Email:</strong> ${comment.profile?.email || 'N/A'}
    `;
  
    Swal.fire({
      title: 'Comment Creator Information',
      html: commentCreatorInfo,
      icon: 'info',
      confirmButtonText: 'Close'
    });
  }
  

  goToUpdate(id: number): void {
    this.router.navigate([`/update-post/${id}`]);
  }

  advertise(post: PostViewDTO): void{

    
    this.postAd.description = post.description || '';
    this.postAd.publishedTime = post.postedTime.join(' ');;
    this.postAd.username = post.profile?.username;
    
    this.postService.sendForAds(this.postAd).subscribe(() => {
     alert("Successfully advertised post by: " + this.postAd.username);
    },
    (error) => {
      console.log('Error advertising post', error);
    });


  }
}
