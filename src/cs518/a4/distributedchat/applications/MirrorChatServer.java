package cs518.a4.distributedchat.applications;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import cs518.a4.distributedchat.core.ChatGroup;
import cs518.a4.distributedchat.core.ChatGroupsManager;
import cs518.a4.distributedchat.core.ChatServer;
import cs518.a4.distributedchat.core.Node;
import cs518.a4.distributedchat.handler.MirrorChatServerHandler;
import cs518.a4.distributedchat.wireformates.ClientInfo;
import cs518.a4.distributedchat.wireformates.NewChatServer;

public class MirrorChatServer extends Node implements ChatServer{
	private ChatGroupsManager chatGroupsManager;
	private String 	mainChatServerHost;
	private int  	mainChatServerPort;
	
	public MirrorChatServer(String mainChatServerHost,int mainChatServerPort,ChatGroupsManager chatGroupsManager) throws UnknownHostException {
		super("mirrorChatServer",-1);
		this.chatGroupsManager 	= new ChatGroupsManager();
		this.mainChatServerHost = mainChatServerHost;
		this.mainChatServerPort = mainChatServerPort;
	}
	
	public void addMember(RemoteChatClient memebr){
		ChatGroup group = chatGroupsManager.getChatGroup(memebr.getGroupID());
		if (group == null)
			group = addChatGroup(memebr.getGroupID());
		chatGroupsManager.addNewMember(group, memebr);
	}
	
	public void removeMember(RemoteChatClient memebr){
		ChatGroup group = chatGroupsManager.getChatGroup(memebr.getGroupID());
		
		group.removeMember(memebr.getNodeID());
		if (group.getNumberOfMembers()==0)
			removeChatGroup(group.getGroupID());
	}

	public ChatGroup addChatGroup(int groupID){
		return chatGroupsManager.createChatGroup(groupID);
	}
	
	public void removeChatGroup(Integer groupID){
		chatGroupsManager.removeChatGroup(groupID);
	}

	public void run(int threadpoolSize) throws InstantiationException, IllegalAccessException, IOException, InterruptedException{
		setHandler(new MirrorChatServerHandler(this));
		try {
			connect(mainChatServerHost,mainChatServerPort);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	
	public ChatServer nextRun() throws InstantiationException, IllegalAccessException, IOException, InterruptedException{
		MainChatServer ChatServer  = new MainChatServer(mainChatServerPort-1,mainChatServerPort, chatGroupsManager);
		ChatServer.run(Setting.SERVER_THREADPOOL_SIZE);
		announceChatServer();
		return ChatServer;
	}

	public void announceChatServer() throws IOException{
		NewChatServer message = new NewChatServer();
		message.setClientInfo(getNodeInfo());
		chatGroupsManager.broadCastMessage(message, -1);
	}


	public ClientInfo getNodeInfo() throws UnknownHostException {
		ClientInfo node = new ClientInfo();
		node.setClientID("Chat Server");
		node.setHost(InetAddress.getLocalHost().getHostName());
		node.setPortNum(mainChatServerPort -1);
		node.setGroupID(-1);
		return node;
	}

	public void changeGroupSize(int groupID, int size){
		chatGroupsManager.getChatGroup(groupID).setMaxSize(size);
	}

	public void test(){
		for(Integer gid:chatGroupsManager.getGroups().keySet()){
			System.out.println("GroupID: "+gid);
			ChatGroup gr =chatGroupsManager.getChatGroup(gid);
			for(RemoteChatClient rc:gr.getMembers())
				System.out.print(rc.getNodeID()+"   ");
			System.out.println();
		}
			
	}
}
