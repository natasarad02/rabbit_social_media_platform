import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post-service.service';
import { PostViewDTO } from '../../models/PostViewDTO.model';
import { Router } from '@angular/router';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';

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

  constructor(
    private postService: PostService, 
    private router: Router, 
    private userService: UserService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.loadUser();
    this.postService.getAllPosts().subscribe(
      (response) => {
        this.posts = response;
        console.log(this.posts);

        this.posts.forEach(post => {
          if (post.picture.includes("/images")) {
            post.picture = this.imageStartPath + post.picture;
          }
        });
      },
      (error) => {
        console.error('Error loading profiles', error);
      }
    );
  }

  loadUser(): void {
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.loggedProfile = data;
          this.profileId = this.loggedProfile.id;
          this.loadLikedPosts();  // Load liked posts only if user is logged in
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
      this.showLoginAlert();  // Show alert if user is not logged in
    }
  }

  commentOnPost(postId: number): void {
    if (this.loggedProfile) {
      // Logic for authenticated users to comment on the post.
      console.log(`Commenting on post with ID: ${postId}`);
      // Here, you might open a comment dialog or redirect to a comment form.
    } else {
      this.showLoginAlert();  // Show alert if user is not logged in
    }
  }

  showCreatorInfo(post: PostViewDTO): void {
    const creatorInfo = `
      <strong>Name:</strong> ${post.profile?.name || 'N/A'}<br>
      <strong>Surname:</strong> ${post.profile?.surname || 'N/A'}<br>
      <strong>Email:</strong> ${post.profile?.email || 'N/A'}
    `;
  
    Swal.fire({
      title: 'Creator Information',
      html: creatorInfo,
      icon: 'info',
      confirmButtonText: 'Close'
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
}
