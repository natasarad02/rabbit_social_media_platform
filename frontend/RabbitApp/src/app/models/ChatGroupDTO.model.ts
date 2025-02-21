import { ChatMessageDTO } from "./ChatMessageDTO.model";
import { ProfileDTO } from "./ProfileDTO.model";

export interface ChatGroupDTO{
    id: number;
    name: string;
    admin: ProfileDTO;
    members: ProfileDTO[];
    messages: ChatMessageDTO[];
   
}