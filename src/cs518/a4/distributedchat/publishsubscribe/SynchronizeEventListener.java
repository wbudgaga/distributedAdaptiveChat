package cs518.a4.distributedchat.publishsubscribe;

import org.springframework.context.ApplicationListener;

import cs518.a4.distributedchat.applications.MainChatServer;

public class SynchronizeEventListener implements ApplicationListener<SynchronizeEvent>{
	private MainChatServer chatServer;
	
	public void setMainChatServer(MainChatServer chatServer){
		this.chatServer = chatServer;
	}
	
	public MainChatServer getMainChatServer(){
		return chatServer;
	}

	@Override
	public void onApplicationEvent(SynchronizeEvent event) {
		chatServer.MirrorChatServerSynchronize();		
	}

}
