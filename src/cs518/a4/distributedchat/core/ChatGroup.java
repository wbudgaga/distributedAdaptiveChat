package cs518.a4.distributedchat.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cs518.a4.distributedchat.applications.RemoteChatClient;
import cs518.a4.distributedchat.applications.Setting;
import cs518.a4.distributedchat.publishsubscribe.Context;
import cs518.a4.distributedchat.publishsubscribe.MemberInEvent;
import cs518.a4.distributedchat.publishsubscribe.MemberOutEvent;
import cs518.a4.distributedchat.publishsubscribe.Publisher;
import cs518.a4.distributedchat.wireformates.ClientInfo;
import cs518.a4.distributedchat.wireformates.GroupSize;
import cs518.a4.distributedchat.wireformates.Message;

public class ChatGroup{
	private int groupID 						= -1;
	private int maxSize;
	private ChatGroupsManager chatGroupsManager;
	private ConcurrentHashMap<String, RemoteChatClient> members 	= new ConcurrentHashMap<String, RemoteChatClient>();
	
	public ChatGroup(){
		setMaxSize(Setting.GROUP_SIZE);
	}
	
	public void init(int groupID, ChatGroupsManager chatGroupsManager){
		this.groupID 						= groupID;
		this.chatGroupsManager 					= chatGroupsManager;
	}
	
	public synchronized boolean addMember(RemoteChatClient member){
		String memberID 					= member.getNodeID();
		if (isFull() || getMember(memberID) != null)
			return false;
		members.put(m	ember.getNodeID(), member);
		member.setGroupID(getGroupID());
		return true;
	}
	
	public void removeAll(){
		members.clear();
	}
	
	public synchronized RemoteChatClient removeMember(String memberID){
		if (chatGroupsManager != null)
			chatGroupsManager.removeMember(memberID);
		return members.remove(memberID);
	}
	
	public ArrayList<RemoteChatClient> getMembers(){
		ArrayList<RemoteChatClient> membersList 		= new ArrayList<RemoteChatClient>();
		for(RemoteChatClient member:members.values())
			membersList.add(member);
		return membersList;
	}
	
	public ArrayList<RemoteChatClient>  publishMembersEvent(){return getMembers();}

	public void sendDelMemberNotification(RemoteChatClient removedMember) throws IOException {
		 for(RemoteChatClient member:members.values())
			 member.leavingMemberNotification(removedMember.getClientInfo());
	}

	public void sendNewMemberNotification(RemoteChatClient newMember) throws IOException{
		 for(RemoteChatClient member:members.values()){
			 member.newMemberNotification(newMember);
		 }
	}
	
	public synchronized void sendMessageToOneMember(Message msg) throws IOException{			
		 for(RemoteChatClient member:members.values()){
			 member.sendMessage(msg);
			 return ;
		 }
	}


	public void sendMessage(Message msg) throws IOException{
		 for(RemoteChatClient member:members.values())
			 member.sendMessage(msg);
	}

	
	public void sendData(ClientInfo sender ,String data) throws IOException{
		 for(RemoteChatClient member:members.values())
			 member.sendData(sender, data);
	}

	public int getNumberOfMembers(){
		return members.size();
	}
	
	public boolean isFull(){
		return members.size()== getMaxSize();
	}
	
	public RemoteChatClient getMember(String memberID){
		return members.get(memberID);
	}
	public int getGroupID() {
		return groupID;
	}

	public ArrayList<RemoteChatClient> removeFailedMembers() throws IOException{
		ArrayList<RemoteChatClient> deadMembers = new  ArrayList<RemoteChatClient>();
		for(RemoteChatClient member:members.values())
			if (!member.isAlive()){
				deadMembers.add(member);
				System.out.println("The member : "+member.getNodeID()+" has been removed!");
				sendDelMemberNotification(removeMember(member.getNodeID()));
			}
		return deadMembers;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void increaseGroupSize() throws IOException{
		int oldSize = getMaxSize();
		setMaxSize((int)(oldSize + oldSize * Setting.SR + 1));
		getMembers().get(0).setNewGroupSize(getMaxSize());
		System.out.println("Group#"+getGroupID()+": Size has been increased from "+oldSize+" to "+getMaxSize());
	}
	
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
}
