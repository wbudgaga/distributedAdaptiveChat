package cs518.a4.distributedchat.publishsubscribe;

import java.io.IOException;

import org.springframework.context.ApplicationListener;

import cs518.a4.distributedchat.applications.MainChatServer;
import cs518.a4.distributedchat.core.ChatClient;

public class GroupActivityEventListener implements ApplicationListener<GroupEvent>{
	private ChatClient chatClient;
	
	public void setChatClient(ChatClient chatClient){
		this.chatClient = chatClient;
	}
	
	public ChatClient getChatClient(){
		return chatClient;
	}

	@Override
	public void onApplicationEvent(GroupEvent event) {
		if (chatClient!=null)
			chatClient.updateGroupActivity(event.getChatGroup());		
	}

}
