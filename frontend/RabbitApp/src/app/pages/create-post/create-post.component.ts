import { Component, OnInit } from '@angular/core';
import { PostDTO } from '../../models/PostDTO.mode';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent implements OnInit {

  posts: PostDTO[] =[];

  ngOnInit(): void {
    
  }



}
