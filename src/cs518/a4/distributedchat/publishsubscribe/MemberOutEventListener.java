package cs518.a4.distributedchat.publishsubscribe;

import java.io.IOException;

import org.springframework.context.ApplicationListener;

import cs518.a4.distributedchat.applications.MainChatServer;

public class MemberOutEventListener implements ApplicationListener<MemberOutEvent>{
	private MainChatServer chatServer;
	
	public void setMainChatServer(MainChatServer chatServer){
		this.chatServer = chatServer;
	}
	
	public MainChatServer getMainChatServer(){
		return chatServer;
	}

	@Override
	public void onApplicationEvent(MemberOutEvent event) {
		try {
			if (chatServer!=null)
				chatServer.MirrorChatServerUpdateOutMember(event.getMemberInfo());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
