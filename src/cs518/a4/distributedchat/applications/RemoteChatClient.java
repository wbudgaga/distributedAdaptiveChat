package cs518.a4.distributedchat.applications;

import java.io.IOException;
import java.util.ArrayList;

import cs518.a4.distributedchat.communication.ConnectionManager;
import cs518.a4.distributedchat.core.ChatClient;
import cs518.a4.distributedchat.core.ChatGroup;
import cs518.a4.distributedchat.core.Remote;
import cs518.a4.distributedchat.gui.MainGUI;
import cs518.a4.distributedchat.wireformates.ClientInfo;
import cs518.a4.distributedchat.wireformates.DelMember;
import cs518.a4.distributedchat.wireformates.GroupMembers;
import cs518.a4.distributedchat.wireformates.GroupMerging;
import cs518.a4.distributedchat.wireformates.GroupSize;
import cs518.a4.distributedchat.wireformates.Message;
import cs518.a4.distributedchat.wireformates.NewMember;

public class RemoteChatClient extends Remote implements ChatClient{
	private int 	groupID;
	private String 	nodeID;

	public RemoteChatClient(String id, String host, int port, int groupID) {
		super(host,port);
		this.nodeID = id;
		setGroupID(groupID);
	}
	
	public boolean isAlive(){
		return ConnectionManager.isAlive(getHost(), getPort());
	}

	private boolean sendGroupMembers(GroupMembers groupMembersMSG, ArrayList<RemoteChatClient> members) throws IOException{
		groupMembersMSG.setGroupID(members.get(0).groupID);
		for (RemoteChatClient member:members)
			groupMembersMSG.addMember(member.getClientInfo());
		
		sendMessage(groupMembersMSG);
		return true;
	}
	public boolean setGroupMembers(ArrayList<RemoteChatClient> members) throws IOException{
		GroupMembers groupMembersMSG = new GroupMembers();
		return sendGroupMembers(groupMembersMSG,members);
	}
	
	public boolean addGroupMembers(ArrayList<RemoteChatClient> members) throws IOException{
		GroupMerging groupMembersMSG = new GroupMerging();
		return sendGroupMembers(groupMembersMSG,members);
	}
	
	public void setNewGroupSize(int size) throws IOException{
		GroupSize msg = new GroupSize();
		msg.setGroupID(getGroupID());
		msg.setGroupSize(size);
		sendMessage(msg);
	}
	public void newMemberNotification(RemoteChatClient newMember) throws IOException{
		NewMember newMemberMSG = new NewMember();
		newMemberMSG.setClientInfo(newMember.getClientInfo());
		sendMessage(newMemberMSG);
	}

	public void leavingMemberNotification(ClientInfo departedMember) throws IOException{
		DelMember delMemberMSG = new DelMember();
		delMemberMSG.setClientInfo(departedMember);
		sendMessage(delMemberMSG);
	}
	
	public static  RemoteChatClient getInstance(ClientInfo client) {
		RemoteChatClient remoteChatClient = new RemoteChatClient(client.getClientID(),client.getHost(),client.getPortNum(), client.getGroupID());
		remoteChatClient.setGroupID(client.getGroupID());
		return remoteChatClient;
	}

	public ClientInfo getClientInfo() {
		ClientInfo client = new ClientInfo();
		client.setClientID(getNodeID());
		client.setHost(getHost());
		client.setPortNum(getPort());
		client.setGroupID(getGroupID());
		return client;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public int getGroupID() {
		return groupID;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	@Override
	public boolean run(int threadPoolSize) {return true;}
	@Override
	public ArrayList<RemoteChatClient> getMembers() {return null;}
	@Override
	public void sendGroupData(String data) {}
	@Override
	public void broadcastData(String data) {}
	@Override
	public void setMainGUI(MainGUI mainGUI) {}
	@Override
	public void newMemberGroupNotification() {}
	@Override
	public void cleanGroup() {}
	@Override
	public void refreshChatGroup() {}
	@Override
	public void dataReceivedNotifying(String senderID, String data) {}
	@Override
	public void sendGroupMSG(Message data) throws IOException {}
	@Override
	public void setRemoteChatServer(RemoteChatServer remoteChatServer) {}
	@Override
	public void updateGroupActivity(ChatGroup chatGroup) {}
	@Override
	public void setGroupSize(int size) {}

	@Override
	public int getGroupSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}
