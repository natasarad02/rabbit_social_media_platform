import { Component, OnInit } from '@angular/core';
import { PostDTO } from '../../models/PostDTO.mode';
import { LocationDTO } from '../../models/LocationDTO.model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CreatePostService } from '../../services/create-post.service';
import { ProfileDTO } from '../../models/ProfileDTO.model';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent implements OnInit {

  newPost: PostDTO = {
    id: 0,
    description: '',
    picture: '',
    postedTime:  new Date('2024-12-01T00:00'),
    deleted: false,
    likeCount: 0,
    comments: [],
    address: '',
    longitude: 0,
    latitude: 0,
    imageBase64: ''
   

  }

  imageBase64: string = '';
  

  path: string = '/images/4e8abe61-bfbe-4d07-b61b-f398d289dd35.jpg';
  //imageBeginningPath: string = 'http://localhost:8080';
  // Fiksiran profil dok se ne doda login
  //uploadedImage: string = '';// = 'http://localhost:8080/images/4e8abe61-bfbe-4d07-b61b-f398d289dd35.jpg';
  profile: ProfileDTO = {
    id: 1,
    name: 'Marko',
    surname: 'Markovic',
    email: 'marko@example.com',
    password: 'aaa',
    role: 'User',
    address: 'Karadjordjeva 45'

  }
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

  constructor(private service: CreatePostService){}
  ngOnInit(): void {
    //this.uploadedImage = 'http://localhost:8080/images/4e8abe61-bfbe-4d07-b61b-f398d289dd35.jpg';

    
  }

  createPost(): void {
    console.log(this.location);

    this.newPost = {
      description: this.postForm.get('description')?.value || '',
      picture: this.postForm.get('image')?.value || '',
      postedTime:  new Date(),
      deleted: false,
      likeCount: 0,
      comments: [],
      address: this.location.address,
      longitude: this.location.longitude,
      latitude: this.location.latitude,
      imageBase64: this.imageBase64
    
     
    }

    

    console.log("Location");
    console.log(this.location);


    //this.newPost.location = this.location;
    // treba dodati profile id kad budemo imali logovanje
   // this.newPost.profile = 1;
    console.log("Post");
    console.log(this.newPost);
    this.service.createPost(this.newPost, this.profile.id).subscribe({
      next: (result: PostDTO) => {
        alert("Post created successfully");
        this.path = result.picture;
        window.location.reload();
       // this.showImage(this.path);
      },
      error: (err: any) => {
        alert("Error creating post.");
      }
    });

    
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
       /* this.imageBase64 = reader.result as string;
        this.keypointForm.patchValue({
          imageBase64: this.imageBase64
        });*/
        this.imageBase64 = reader.result as string;
        this.postForm.patchValue({
          image: this.imageBase64
        })

     

    };
    reader.readAsDataURL(file); 

}

/*showImage(path: string) : void
{
   alert(this.imageBeginningPath + path);
   this.uploadedImage =  this.imageBeginningPath + this.path;
   
}*/








}
