package cs518.a4.distributedchat.gui;

import java.io.IOException;

import cs518.a4.distributedchat.core.GUIChatClient;

public class GUIFacade {
	private GUIChatClient chatClient;

	public GUIFacade(GUIChatClient chatClient){
		this.chatClient = chatClient;
	}
	
	public String[] getMembersIDs(){
		return chatClient.getMembersIDs();
	}
	
	public String getMemberID(){
		return chatClient.getMemberID();
	}
	
	public int getGroupID() {
		return chatClient.getGroupID();
	}
	
	public int getGroupSize() {
		return chatClient.getGroupSize();
	}

	public void sendGroupData(String data) {
		chatClient.sendGroupData(data);
	}

	public void broadcastData(String data){
		chatClient.broadcastData(data);
	}
}
