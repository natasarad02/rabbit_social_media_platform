import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { PostDTO } from '../../models/PostDTO.mode';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ProfileDTO } from '../../models/ProfileDTO.model';
import { LocationDTO } from '../../models/LocationDTO.model';
import { CreatePostService } from '../../services/create-post.service';
import { PostService } from '../../services/post-service.service';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { PostViewDTO } from '../../models/PostViewDTO.model';

@Component({
  selector: 'app-update-post',
  templateUrl: './update-post.component.html',
  styleUrl: './update-post.component.css',
})
export class UpdatePostComponent implements OnInit {

  postId: number = -1;

  newPost: PostViewDTO = {
    id: 0,
    description: '',
    picture: '',
    likeCount: 0,
    followingCount: 0,   
    comments: [],        
    profile: undefined,   
    address: '',
    longitude: 0,
    latitude: 0,
    imageBase64: '',
    postedTime: [
      new Date().getFullYear(),            
      new Date().getMonth() + 1,           
      new Date().getDate(),                
      new Date().getHours(),               
      new Date().getMinutes(),             
      new Date().getSeconds(),             
      new Date().getMilliseconds()         
    ]
  };

  imageBase64: string = '';
  

  path: string = '/images/4e8abe61-bfbe-4d07-b61b-f398d289dd35.jpg';

  location: LocationDTO = {
    id: 0,
    longitude: 0,
    latitude: 0,
    address: '',
    number: '',
    deleted: false
  };

  postForm = new FormGroup({
    description: new FormControl('', [Validators.required]),
    image: new FormControl('', [Validators.required]),
    address: new FormControl('', [Validators.required])
   
  })

  constructor(private service: CreatePostService, private postService: PostService, private route: ActivatedRoute, private router: Router){}
  ngOnInit(): void {

      this.route.paramMap.subscribe(params => {
      const id = +params.get('id')!; 
      this.postId = id;

      if (this.postId) {
        this.loadPost(this.postId); 
      }
    }); 
  
  }

  loadPost(id: number)
  {
    this.postService.getPostById(id).subscribe(
      (response) => {
        this.newPost = response;
        this.loadLocation();
        this.patchFormValues();
      },
      (error) => {
        console.error('Error loading profiles', error);
      }
    );
  }

  patchFormValues(): void {

    this.postForm.patchValue({
      description: this.newPost.description,
      address: this.location.address,  
      image: this.newPost.picture || ''
    });
  }

  loadLocation()
  {
    this.location.address = this.newPost.address;
    this.location.latitude = this.newPost.latitude;
    this.location.longitude = this.newPost.longitude;
  }

  updatePost(): void {


    
      this.newPost.description = this.postForm.get('description')?.value || '';
      this.newPost.picture = this.postForm.get('image')?.value || '';
      this.newPost.address = this.location.address;
      this.newPost.longitude = this.location.longitude;
      this.newPost.latitude = this.location.latitude;
      this.newPost.imageBase64 = this.imageBase64;

      console.log(this.newPost);
      this.postService.updatePost(this.newPost).subscribe(
        (response) => {
          console.log(response);
          this.path = response.picture;
          this.router.navigate([`/profile/${this.newPost.profile?.id}`]);
        },
        (error) => {
          console.error('Error updating post', error);
          
        }
      );
  } 

  setLocation(newLocation: LocationDTO)
  {
    
    this.location = newLocation;
    this.postForm.patchValue({address:  this.location.address});
   
    
   
  }
  onFileSelected(event: any){
    const file:File = event.target.files[0];
    const reader = new FileReader();
    reader.onload = () => {
        this.imageBase64 = reader.result as string;
        this.postForm.patchValue({
          image: this.imageBase64
        })

     

    };
    reader.readAsDataURL(file); 

} 
 }
