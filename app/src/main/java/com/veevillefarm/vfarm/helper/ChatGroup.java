package com.veevillefarm.vfarm.helper;

import java.util.List;

/**
 * Created by Prashant C on 08/01/19.
 */
public class ChatGroup {

    public String groupName,latestMessage;
    public List<ChatContact> contacts;
    public ChatGroup(String groupName,String latestMessage,List<ChatContact> contacts){
        this.groupName = groupName;
        this.contacts = contacts;
        this.latestMessage = latestMessage;
    }
}
