import { ChatGroupMemberDTO } from "./ChatGroupMemberDTO.model";
import { ChatMessageDTO } from "./ChatMessageDTO.model";
import { ProfileDTO } from "./ProfileDTO.model";

export interface ChatGroupDTO{
    id: number;
    name: string;
    admin: number;
    members: number[];
   
}