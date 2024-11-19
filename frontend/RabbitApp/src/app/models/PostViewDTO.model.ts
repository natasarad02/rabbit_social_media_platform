import { CommentDTO } from "./CommentDTO.model";
import { ProfileDTO } from "./ProfileDTO.model";

export interface PostViewDTO {
    id: number;
    description: string;
    picture: string;
    likeCount: number;
    followingCount: number;
    comments: CommentDTO[];
    profile?: ProfileDTO;
    address: string;
    latitude: number;
    longitude: number;
    imageBase64: string;
    postedTime: [number, number, number, number, number, number, number];
}


