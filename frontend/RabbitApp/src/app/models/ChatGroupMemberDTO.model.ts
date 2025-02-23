import { ProfileDTO } from "./ProfileDTO.model";

export interface ChatGroupMemberDTO{
    id: number;
    chatGroupId: number;
    profileId: number;
    joinDate: string;
   
}