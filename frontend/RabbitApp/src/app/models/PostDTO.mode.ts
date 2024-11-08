import { CommentDTO } from "./CommentDTO.model";

export interface PostDTO{
    id: number;
    description: string;
    picture: string;
    postedTime: Date;
    deleted: boolean;
    likeCount: number;
    comments: CommentDTO[];
}