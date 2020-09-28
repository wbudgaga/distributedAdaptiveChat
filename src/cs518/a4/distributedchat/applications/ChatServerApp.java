package cs518.a4.distributedchat.applications;

import java.io.IOException;
import java.net.UnknownHostException;

import cs518.a4.distributedchat.core.ChatGroupsManager;
import cs518.a4.distributedchat.core.ChatServer;
import cs518.a4.distributedchat.publishsubscribe.Context;

public class ChatServerApp {
	private ChatServer 			ChatServer;
	private ChatGroupsManager	chatGroupsManager;
	
	public ChatServerApp(int listeningPort, int mirrorListeningPort) throws UnknownHostException{
		chatGroupsManager 	=  (ChatGroupsManager) Context.getInstance().getBean("chatGroupsManager");
		this.ChatServer 	= new MainChatServer(listeningPort,mirrorListeningPort, chatGroupsManager);
	}
	
	public ChatServerApp(String mainChatServerHost, int  mainChatServerPort) throws UnknownHostException{
		chatGroupsManager 	=  (ChatGroupsManager) Context.getInstance().getBean("chatGroupsManager");
		this.ChatServer 	= new MirrorChatServer(mainChatServerHost,mainChatServerPort, chatGroupsManager);
	}		
	
	public void run(int threadpoolSize) throws InstantiationException, IllegalAccessException, IOException, InterruptedException{
		ChatServer.run(threadpoolSize);
		ChatServer = ChatServer.nextRun();
	}

	public static void main(String args[]) throws InstantiationException, IllegalAccessException, IOException, InterruptedException {
		ChatServerApp chatServer;
		      
		if (args.length < 2) {
			System.err.println("ChatServerApp   Usage:");
			System.err.println("         java  cs518.a4.distributedchat.application.ChatServerApp [PORT1 PORT2] |[CHATSERVER_HOST PORT] ");
		    return;
		}
		try{
			chatServer = new ChatServerApp(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}catch(NumberFormatException e){
			chatServer = new ChatServerApp(args[0], Integer.parseInt(args[1]));
		}
		
		chatServer.run(Setting.SERVER_THREADPOOL_SIZE);
	}

}