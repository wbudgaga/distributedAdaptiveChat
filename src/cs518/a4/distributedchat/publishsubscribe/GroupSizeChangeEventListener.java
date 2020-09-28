package cs518.a4.distributedchat.publishsubscribe;

import java.io.IOException;

import org.springframework.context.ApplicationListener;

import cs518.a4.distributedchat.applications.MainChatServer;

public class GroupSizeChangeEventListener implements ApplicationListener<GroupSizeEvent>{
	private MainChatServer chatServer;
	
	public void setMainChatServer(MainChatServer chatServer){
		this.chatServer = chatServer;
	}
	
	public MainChatServer getMainChatServer(){
		return chatServer;
	}

	@Override
	public void onApplicationEvent(GroupSizeEvent event) {
		try {
			if (chatServer!=null)
				chatServer.updateGroupSize(event.getChatGroup().getGroupID(), event.getChatGroup().getMaxSize());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
