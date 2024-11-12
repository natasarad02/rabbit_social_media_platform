import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post-service.service';
import { PostViewDTO } from '../../models/PostViewDTO.model';
import { Router } from '@angular/router';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { UserService } from '../../services/user.service';

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

  constructor(private postService: PostService, private router: Router, private userService: UserService){}


  ngOnInit(): void {
    this.loadUser();
    this.postService.getAllPosts().subscribe(
      (response) => {
        this.posts = response;
        console.log(this.posts);

        this.posts.forEach(post => {
        })

        this.posts.forEach(post => {
          if(post.picture.includes("/images"))
           {
             post.picture = this.imageStartPath + post.picture;
             
           } 
        });
      },
      (error) => {
        console.error('Error loading profiles', error);
      }
    );
      this.loadLikedPosts();
      
  }

  loadUser()
  {
    this.userService.getUserProfile().subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.loggedProfile = data;
          this.profileId = this.loggedProfile.id;
        } else {
          console.log('No profile found or token expired');
        }
      },
      (error) => {
        console.error('Error loading profile:', error);
      }
    );
  }



  loadLikedPosts()
  {
      this.postService.getLikedPosts(this.profileId).subscribe(
        (response) => {
          this.likeIds = response;
        },
        (error) => {
          console.error('Error loading profiles', error);
        }
      );
  }

  deletePost(id: number): void {
    const confirmed = window.confirm('Are you sure you want to delete this post?');
  
    if (confirmed) {
      this.postService.deletePost(id).subscribe(() => {
        console.log(this.posts);
        this.ngOnInit();
      },
      (error) => {
        console.error('Error delete', error);
      });
    }
  }

  likePost(postId: number): void {
    this.postService.likePost(this.profileId, postId).subscribe(()=> {
      this.ngOnInit();
      console.log("success")
    },
    (error) => {
      console.error('Error liking', error);
    })
  }

  goToUpdate(id: number)
  {
    this.router.navigate([`/update-post/${id}`]);
  }

  





 }
