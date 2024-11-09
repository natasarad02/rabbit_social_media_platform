import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { PostDTO } from '../../models/PostDTO.model';
import { PostService } from '../../services/post-service.service';

@Component({
  selector: 'app-view-posts-registered',
  templateUrl: 'view-posts-registered.component.html',
  styleUrls: ['view-posts-registered.component.css']
})
export class ViewPostsRegisteredComponent implements OnInit {
  posts: PostDTO[] = [];
  profileId: number = 0;

  constructor(private postService: PostService){}


  ngOnInit(): void {
    this.postService.getAllPosts().subscribe(
      (response) => {
        this.posts = response;
        console.log(this.posts);
      },
      (error) => {
        console.error('Error loading profiles', error);
      }
    );

    this.profileId = 1;
  }

  deletePost(id: number): void {
    const confirmed = window.confirm('Are you sure you want to delete this post?');
  
    if (confirmed) {
      this.postService.deletePost(id).subscribe(() => {
        this.ngOnInit();
        console.log("success")
      });
    }
  }

  





 }
