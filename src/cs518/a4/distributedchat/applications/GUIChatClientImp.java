package cs518.a4.distributedchat.applications;

import java.io.IOException;
import java.util.ArrayList;

import cs518.a4.distributedchat.core.ChatClient;
import cs518.a4.distributedchat.core.GUIChatClient;

// This class is an implementation of Client interface which is the only part that can be seen by the gui model
public class GUIChatClientImp implements GUIChatClient{
	private ChatClient	chatClient;
	
	public GUIChatClientImp(ChatClient chatClient){
		this.chatClient 			= chatClient;
	}

	@Override
	public String[] getMembersIDs() {
		ArrayList<RemoteChatClient>  members 	= chatClient.getMembers();
		String[] membersList 			= new String[members.size()];
		for (int i = 0; i <  members.size()  ; ++i)
			membersList[i] 			= members.get(i).getNodeID();
		
		return membersList;
	}

	@Override
	public String getMemberID() {
		return chatClient.getNodeID();
	}

	@Override
	public int getGroupID() {
		return chatClient.getGroupID();
	}

	@Override
	public void sendGroupData(String data) {
		try {
			chatClient.sendGroupData(data);
		} catch (IOException e) {
		}
	}

	@Override
	public void broadcastData(String data)  {
		chatClient.broadcastData(data);
	}
	public int getGroupSize()  {
		return chatClient.getGroupSize();
	}
}
