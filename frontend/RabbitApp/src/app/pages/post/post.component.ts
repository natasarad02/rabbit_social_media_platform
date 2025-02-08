import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../services/post-service.service';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { PostViewDTO } from '../../models/PostViewDTO.model';
import { UserService } from '../../services/user.service';
import { ImageCacheService } from '../../services/image-cache.service';
import { CommentDTO } from '../../models/CommentDTO.model';
import { CommentService } from '../../services/comment.service';

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
  @Output() postUpdated: EventEmitter<void> = new EventEmitter<void>();
  cachedImage: string = '';

  commentClicked: boolean = false;
  commentText: string = '';
  newComment: CommentDTO = {} as CommentDTO;
  constructor(
    private postService: PostService,
    private router: Router,
    private auth: AuthService,
    private userService: UserService,
    private imageCacheService: ImageCacheService,
    private commentService: CommentService
  ) {
    
  }

  ngOnInit(): void {
    console.log(this.post);
    console.log(this.loggedProfile);
    this.cacheImage();
  }

  private async cacheImage(): Promise<void> {
    try {

      this.cachedImage = await this.imageCacheService.fetchImage(this.post.picture);
      console.log(`Image cached for post ${this.post.id}:`, this.cachedImage);
    } catch (error) {
      console.error(`Error caching image for post ${this.post.id}:`, error);
    }
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

  dislikePost(postId: number): void {
    if (this.loggedProfile) {
      this.postService.dislikePost(this.loggedProfile.id, postId).subscribe(() => {
        this.refreshPostData();
        console.log("success");
      },
      (error) => {
        console.error('Error liking post', error);
      });
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
    this.postUpdated.emit();
  }

  commentOnPost(): void {
    if (this.loggedProfile) {
      console.log(`Commenting on post with ID: ${this.post.id}`);
      this.commentClicked = true;
    } else {
      this.showLoginAlert();
    }
  }

  postComment(): void{

    this.newComment.text = this.commentText;
    if(this.loggedProfile)
    {

    
      this.commentService.createComment(this.post.id, this.loggedProfile.id, this.newComment).subscribe(
        () => {
          //console.log(`Post ${this.post.id} deleted successfully`);
          this.refreshPostData();
        },
        (error) => {
          console.error('Error posting comment:', error);
          alert("You posted too many comments in the past hour");
        }
      );

    }
  }

  deletePost(): void {
    const confirmed = window.confirm('Are you sure you want to delete this post?');
    if (confirmed) {
      this.postService.deletePost(this.post.id).subscribe(
        () => {
          //console.log(`Post ${this.post.id} deleted successfully`);
          this.refreshPostData();
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

  navigateToUser(userId: number) : void {
    this.router.navigate([`/profile/${userId}`]).then(() => {
    });
    
  }

  

  
}
