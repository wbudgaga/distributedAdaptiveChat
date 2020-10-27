package cs518.a4.distributedchat.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.weaver.Iterators.Getter;

import cs518.a4.distributedchat.applications.RemoteChatClient;
import cs518.a4.distributedchat.publishsubscribe.Context;
import cs518.a4.distributedchat.threadpool.ThreadPoolManager;
import cs518.a4.distributedchat.wireformates.Data;
import cs518.a4.distributedchat.wireformates.Forward;
import cs518.a4.distributedchat.wireformates.Message;

public class ChatGroupsManager {
	private ConcurrentHashMap<String,Integer> groupMember 	= new ConcurrentHashMap<String,Integer>(); // <memberID,groupID>
	private  ConcurrentHashMap<Integer,ChatGroup> groups 	= new ConcurrentHashMap<Integer,ChatGroup>(); // <groupID,group>
	private GroupsMonitor groupsMonitor;
	
	public ChatGroupsManager (){
	}
	
	public GroupsMonitor getGroupsMonitor(){
		if(groupsMonitor==null){
			groupsMonitor 				= (GroupsMonitor) Context.getInstance().getBean("groupsMonitor");//new GroupsMonitor();
			groupsMonitor.setChatGroupsManager(this);
		}
		return groupsMonitor;
	}


	public synchronized ChatGroup getMemberChatGroup(String memberID){
		Integer groupID 				= groupMember.get(memberID);
		if (groupID == null)
			return null;
		return getChatGroup(groupID);
	}
	
	public ChatGroup getChatGroup(int groupID){
		return groups.get(groupID);
	}
	
	public synchronized ArrayList<RemoteChatClient> getGroupMembers(String memberID){
		ChatGroup chatGroup 				= getMemberChatGroup(memberID);
		if (chatGroup == null)
			return null;
		
		return chatGroup.getMembers();
	}

	public synchronized ChatGroup addMember(RemoteChatClient remoteChatClient){
		ChatGroup chatGroup 				= getMemberChatGroup(remoteChatClient.getNodeID());
		if (chatGroup != null)
			 return null;
		return addNewMember(remoteChatClient);
	}
	
	private ChatGroup addNewMember(RemoteChatClient remoteChatClient){
		ChatGroup chatGroup 				= getSmallestChatGroup();
		if (chatGroup == null || chatGroup.isFull()){
			chatGroup 				= createChatGroup(generateNewGroupID());
		}
		return addNewMember(chatGroup, remoteChatClient);
	}

	public ChatGroup addNewMember(ChatGroup chatGroup, RemoteChatClient remoteChatClient){
		chatGroup.addMember(remoteChatClient);
		groupMember.put(remoteChatClient.getNodeID(),chatGroup.getGroupID());
		return chatGroup;
	}
	
	private int generateNewGroupID(){
		for(int i = 1;;++i){
			if (!groups.containsKey(i))
				return i;
		}
	}
	
	public ChatGroup createChatGroup(int groupID){
		ChatGroup chatGroup 				= (ChatGroup) Context.getInstance().getBean("chatGroup");// new ChatGroup(groupID,this);
		chatGroup.init(groupID,this);
		groups.put(groupID, chatGroup);
		return chatGroup;
	}
	
	private ChatGroup getSmallestChatGroup(){
		ChatGroup smallestGroup 			= null;
		for(ChatGroup group:groups.values())
			if (smallestGroup == null || smallestGroup.getNumberOfMembers() > group.getNumberOfMembers())
				smallestGroup 			= group;
		return smallestGroup;
	}
		
	public synchronized Integer removeMember(String memberID){
		return groupMember.remove(memberID);
	}

	public synchronized ChatGroup removeChatGroup(int groupID){
		return groups.remove(groupID);
	}

	protected synchronized void removeFailedMembers() throws IOException{
		for(ChatGroup group:groups.values()){
			group.removeFailedMembers();
			if (group.getNumberOfMembers()==0)
				removeChatGroup(group.getGroupID());
		}
	}
	
	public void publishAllMembers() {
		for(ChatGroup group:groups.values())
			group.publishMembersEvent();
	}

	public void broadCastMessage(Message message, int senderGroupID) throws IOException {
		for(ChatGroup group:groups.values())
			if (group.getGroupID() != senderGroupID)
				group.sendMessageToOneMember(message);
	}
	
	public synchronized void broadCastData(Data data) throws IOException {
		Integer senderGroupID = groupMember.get(data.getSender().getClientID());
		data.getSender().setClientID("Group#"+senderGroupID+":"+data.getSender().getClientID());
		Forward fwd = data.getForward();
		broadCastMessage(fwd,senderGroupID);
	}
	
	public void updateGroupActivity(int groupID, int numOfSentMessages) {
		groupsMonitor.incrNumOfMessages(groupID, numOfSentMessages);
	}

	public void merge2Groups(int gID1, int gID2) throws IOException{
		ChatGroup group1 = getChatGroup(gID1);
		ChatGroup group2 = getChatGroup(gID2);
		if (group2.getMaxSize()< group1.getMaxSize()){
			ChatGroup tmp 	= group1;
			group1 			= group2;
			group2			= tmp;
		}
		
		for(RemoteChatClient receiverMember:group2.getMembers()){
			receiverMember.addGroupMembers(group1.getMembers());
		}
		
		
		for(RemoteChatClient member:group1.getMembers()){
			group2.addMember(group1.removeMember(member.getNodeID()));
		}
		removeChatGroup(group1.getGroupID());
	}
	
	public ConcurrentHashMap<Integer, ChatGroup> getGroups() {
		return groups;
	}

}
