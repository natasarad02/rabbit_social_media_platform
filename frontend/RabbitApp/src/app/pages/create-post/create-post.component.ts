import { Component, OnInit } from '@angular/core';
import { PostDTO } from '../../models/PostDTO.mode';
import { LocationDTO } from '../../models/LocationDTO.model';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent implements OnInit {

  posts: PostDTO[] =[];
  location: LocationDTO = {
    id: 0,
    longitude: 0,
    latitude: 0,
    address: '',
    number: 0,
    deleted: false
  };

  ngOnInit(): void {
    
  }

  setLocation(newLocation: LocationDTO)
  {
    
    this.location = newLocation;
   
  }



}
