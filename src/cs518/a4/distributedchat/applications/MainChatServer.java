package cs518.a4.distributedchat.applications;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cs518.a4.distributedchat.core.ChatGroup;
import cs518.a4.distributedchat.core.ChatGroupsManager;
import cs518.a4.distributedchat.core.ChatServer;
import cs518.a4.distributedchat.core.GroupsMonitor;
import cs518.a4.distributedchat.core.MembersCheckerTask;
import cs518.a4.distributedchat.core.Node;
import cs518.a4.distributedchat.handler.ChatServerHandler;
import cs518.a4.distributedchat.publishsubscribe.Context;
import cs518.a4.distributedchat.publishsubscribe.MemberInEventListener;
import cs518.a4.distributedchat.publishsubscribe.MemberOutEventListener;
import cs518.a4.distributedchat.publishsubscribe.Observer;
import cs518.a4.distributedchat.publishsubscribe.SynchronizeEventListener;
import cs518.a4.distributedchat.wireformates.ClientInfo;
import cs518.a4.distributedchat.wireformates.Data;

public class MainChatServer extends Node implements ChatServer{
	private ChatGroupsManager chatGroupsManager;
	private RemoteMirrorChatServer	mChatServer;

	public MainChatServer(int clientsPort,int mirrorPort, ChatGroupsManager chatGroupsManager) throws UnknownHostException{
		super("mainServer",clientsPort);
		this.chatGroupsManager 		= chatGroupsManager;
		mChatServer 			= (RemoteMirrorChatServer) Context.getInstance().getBean("remoteMirrorChatServer");
		mChatServer.setPort(mirrorPort);// new RemoteMirrorChatServer(mirrorPort);
	}
	
	public void broadCastData(Data data)  {
		try {
			chatGroupsManager.broadCastData(data);
		} catch (IOException e) {}
	}
	
	public int addMember(RemoteChatClient memebr){
		ChatGroup group			= chatGroupsManager.addMember(memebr);
		if (group == null)
			return -1;
		return group.getGroupID();
	}
	
	public ArrayList<RemoteChatClient> getGroupMembers(String memberID){
		return chatGroupsManager.getGroupMembers(memberID);
	}

	public int getGroupSize(String memberID){
		return chatGroupsManager.getMemberChatGroup(memberID).getMaxSize();
	}

	private void registerListeners(){
		Observer.register(this);
		MembersCheckerTask membersCheckerTask =  (MembersCheckerTask) Context.getInstance().getBean("membersCheckerTask");
		membersCheckerTask.setChatGroupsManager(chatGroupsManager);
		getThreadPoolManager().addTask(membersCheckerTask);
		getThreadPoolManager().addTask(mChatServer);
		
		getThreadPoolManager().addTask(chatGroupsManager.getGroupsMonitor());
	}
	
	public void run(int threadpoolSize) throws InstantiationException, IllegalAccessException, IOException, InterruptedException{
		setHandler(new ChatServerHandler(this));
		if (!startListening(threadpoolSize))
			return;
		registerListeners();
	}
	
	public ChatServer nextRun() throws InstantiationException, IllegalAccessException, IOException, InterruptedException{
		System.out.println("The main Chat Server is running...");
		return null;
	}
	
	public void MirrorChatServerUpdateOutMember(ClientInfo memberInfo) throws IOException{
		mChatServer.removeMember(memberInfo);
	}
	
	public void MirrorChatServerUpdateInMember(ClientInfo memberInfo) {
		try {
			mChatServer.addMember(memberInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void MirrorChatServerSynchronize() {
		chatGroupsManager.publishAllMembers();
	}
	
	public void updateGroupActivity(int groupID, int numOfSentMessages) {
		chatGroupsManager.updateGroupActivity(groupID, numOfSentMessages);
	}
	
	public void updateGroupSize(int groupID, int size) throws IOException {
		mChatServer.changeGroupSize(groupID, size);
	}
}
