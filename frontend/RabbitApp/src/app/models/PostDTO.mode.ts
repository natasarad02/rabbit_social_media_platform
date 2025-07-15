import { CommentDTO } from "./CommentDTO.model";
import { LocationDTO } from "./LocationDTO.model";
import { ProfileDTO } from "./ProfileDTO.model";

export interface PostDTO{
    id?: number;
    description: string;
    picture: string;
    postedTime: Date;
    deleted: boolean;
    likeCount: number;
    comments: CommentDTO[];
    address: string;
    latitude: number;
    longitude: number;
    imageBase64: string;
    
    
   
}