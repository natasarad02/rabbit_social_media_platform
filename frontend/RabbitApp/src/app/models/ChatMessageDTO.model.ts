import { ChatGroupDTO } from "./ChatGroupDTO.model";
import { ProfileDTO } from "./ProfileDTO.model";

export interface ChatMessageDTO {
    id: number;
    message: string;
    sender: ProfileDTO;
    receiver: ProfileDTO | null;
    chatGroup: ChatGroupDTO | null;
    timeStamp: Date;
}