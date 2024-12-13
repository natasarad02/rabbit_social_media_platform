import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../services/post-service.service';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { PostViewDTO } from '../../models/PostViewDTO.model';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  @Input() post!: PostViewDTO; // The post to display
  @Input() loggedProfile!: ProfileDTO | null; // The logged-in user's profile
  @Input() likeIds: number[] = []; // IDs of liked posts
  imageStartPath: string = 'http://localhost:8080';

  constructor(
    private postService: PostService,
    private router: Router,
    private auth: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    // Initialize any data specific to the PostComponent if needed
  }

  likePost(postId: number): void {
    if (this.loggedProfile) {
      this.postService.likePost(this.loggedProfile.id, postId).subscribe(
        () => {
          console.log(`Post ${postId} liked successfully`);
          this.refreshPostData(); // Refresh the component state
        },
        (error) => {
          console.error('Error liking post:', error);
        }
      );
    } else {
      this.showLoginAlert();
    }
  }

  public showLoginAlert(): void {
    Swal.fire({
      icon: 'warning',
      title: 'Action Not Allowed',
      text: 'You need to log in to like or comment on posts!',
      confirmButtonText: 'OK'
    });
  }

  private refreshPostData(): void {
    // Refresh the component's state; replace this with actual re-fetching logic if needed
    console.log('Refreshing post data...');
  }

  commentOnPost(): void {
    if (this.loggedProfile) {
      console.log(`Commenting on post with ID: ${this.post.id}`);
    } else {
      this.showLoginAlert();
    }
  }

  deletePost(): void {
    const confirmed = window.confirm('Are you sure you want to delete this post?');
    if (confirmed) {
      this.postService.deletePost(this.post.id).subscribe(
        () => {
          console.log(`Post ${this.post.id} deleted successfully`);
        },
        (error) => {
          console.error('Error deleting post:', error);
        }
      );
    }
  }

  showCreatorInfo(): void {
    const creatorInfo = `
      <strong>Name:</strong> ${this.post.profile?.name || 'N/A'}<br>
      <strong>Surname:</strong> ${this.post.profile?.surname || 'N/A'}<br>
      <strong>Email:</strong> ${this.post.profile?.email || 'N/A'}
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

  goToUpdate(): void {
    this.router.navigate([`/update-post/${this.post.id}`]);
  }
}
