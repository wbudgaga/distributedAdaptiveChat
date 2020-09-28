package cs518.a4.distributedchat.publishsubscribe;

import cs518.a4.distributedchat.applications.MainChatServer;
import cs518.a4.distributedchat.core.ChatClient;

public class Observer {
	
	public static void register(MainChatServer chatServer) {
		MemberOutEventListener memberOutEventListener = (MemberOutEventListener) Context.getInstance().getBean("memberOutEventListener");
		memberOutEventListener.setMainChatServer(chatServer);
		
		MemberInEventListener memberinEventListener = (MemberInEventListener) Context.getInstance().getBean("memberInEventListener");
		memberinEventListener.setMainChatServer(chatServer);
		
		SynchronizeEventListener synchronizeEventListener = (SynchronizeEventListener) Context.getInstance().getBean("synchronizeEventListener");
		synchronizeEventListener.setMainChatServer(chatServer);
		
		GroupSizeChangeEventListener groupSizeChangeEventListener = (GroupSizeChangeEventListener) Context.getInstance().getBean("groupSizeChangeEventListener");
		groupSizeChangeEventListener.setMainChatServer(chatServer);
	}

	
	public static void register(ChatClient chatClient) {
		GroupActivityEventListener groupActivityEventListener = (GroupActivityEventListener) Context.getInstance().getBean("groupActivityEventListener");
		groupActivityEventListener.setChatClient(chatClient);

	}

}
