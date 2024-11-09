import { CommentDTO } from "./CommentDTO.model";

export interface PostDTO {
    id: number;
    description: string;
    picture: string;
    likeCount: number;
    followingCount: number;
    comments: CommentDTO[];
    profileId: number;
}


