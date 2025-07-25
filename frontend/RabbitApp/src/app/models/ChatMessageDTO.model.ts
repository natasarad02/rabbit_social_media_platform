import { ChatGroupDTO } from "./ChatGroupDTO.model";
import { ProfileDTO } from "./ProfileDTO.model";

export interface ChatMessageDTO {
    id: number;
    message: string;
    sender: number;
    receiver: number;
    chatGroup: number;
    timeStamp: string;
}