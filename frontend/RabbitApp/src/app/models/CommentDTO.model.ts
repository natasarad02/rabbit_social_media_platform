import { PostDTO } from "./PostDTO.mode";
import { ProfileDTO } from "./ProfileDTO.model";

export interface CommentDTO {
    id: number;
    text: string;
    commentedTime: Date;
    profile: ProfileDTO;
    post: PostDTO;
}
