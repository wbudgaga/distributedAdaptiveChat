package cs518.a4.distributedchat.handler;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import cs518.a4.distributedchat.applications.RemoteChatClient;
import cs518.a4.distributedchat.applications.RemoteChatServer;
import cs518.a4.distributedchat.core.ChatClient;
import cs518.a4.distributedchat.util.Helper;
import cs518.a4.distributedchat.wireformates.ByteStream;
import cs518.a4.distributedchat.wireformates.ClientInfo;
import cs518.a4.distributedchat.wireformates.Data;
import cs518.a4.distributedchat.wireformates.DelMember;
import cs518.a4.distributedchat.wireformates.Forward;
import cs518.a4.distributedchat.wireformates.GroupMembers;
import cs518.a4.distributedchat.wireformates.GroupMerging;
import cs518.a4.distributedchat.wireformates.GroupSize;
import cs518.a4.distributedchat.wireformates.NewChatServer;
import cs518.a4.distributedchat.wireformates.NewMember;
import cs518.a4.distributedchat.wireformates.TestData;

// This class handles all messages that received at the chat client
public class ChatClientHandler extends MessageHandler{
	private ChatClient chatClient;
	
	public ChatClientHandler(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	private void addNewMember(ClientInfo  newMember) throws IOException{
		if (chatClient.getNodeID().compareTo(newMember.getClientID())!=0){
			chatClient.newMemberNotification(RemoteChatClient.getInstance(newMember));
		}
	}

	private void addGroupMembers(ArrayList<ClientInfo> members) throws IOException{
		for(ClientInfo member:members){
			addNewMember(member);
		}
		chatClient.newMemberGroupNotification();
		chatClient.refreshChatGroup();
	}
	
	//It handles GroupMembers message received from chat server. After group members received and 
	// added, refreshChatGroup() method is called to update the current group list
	public void handleGroupMembers(Socket link, GroupMembers groupMembers) throws IOException {
		chatClient.cleanGroup();
		chatClient.setGroupID(groupMembers.getGroupID());
		addGroupMembers(groupMembers.getMembersList());
	}
	
	public void handleGroupMerging(Socket link, GroupMerging groupMerging) throws IOException {
		addGroupMembers(groupMerging.getMembersList());
	}

	//It handles the received text message(data) by calling dataReceivedNotifying() that shows the text in text area
	public void handleData(Socket link, Data data) {
		chatClient.dataReceivedNotifying(data.getSender().getClientID(), data.getText());
	}
		
	public void handleForwardData(Socket link, Forward forwardData) throws IOException {
		Data dataMSG = forwardData.getData();
		handleData(link, dataMSG);
		chatClient.sendGroupMSG(dataMSG);
	}
	
	public void handleTestData(Socket link, TestData testData) throws NoSuchAlgorithmException, IOException {
		String hashCodeOfReceivedData 	= Helper.fromBytes(ByteStream.StringToByteArray(testData.getText()));
		chatClient.dataReceivedNotifying(testData.getSender().getClientID(),hashCodeOfReceivedData); 
		RemoteChatClient.getInstance(testData.getSender()).sendData(chatClient.getClientInfo(), hashCodeOfReceivedData);
	}

	//It handles NewMember message that includes data of new member joined to the group.
	//It adds the new member and calls refreshChatGroup() to reflect the new update on the GUI.
	public void handleNewMember(Socket link, NewMember newMember) throws IOException {
		addNewMember(newMember.getClientInfo());
		chatClient.setGroupID(newMember.getClientInfo().getGroupID());
		chatClient.refreshChatGroup();
	}
	
	//It handles DelMember message that includes ID of deleted member.
	//It removes that member and calls refreshChatGroup() to reflect the new update on the GUI.
	public void handleDelMember(Socket link, DelMember deletedMember) throws IOException {
		chatClient.leavingMemberNotification(deletedMember.getClientInfo());
		chatClient.refreshChatGroup();
	}
	
	public void handleNewChatServer(Socket link, NewChatServer newChatServer) throws IOException {
		chatClient.setRemoteChatServer(new RemoteChatServer(newChatServer.getClientInfo().getHost(),newChatServer.getClientInfo().getPortNum()));
		if (newChatServer.getClientInfo().getGroupID()!=chatClient.getGroupID()){
			newChatServer.getClientInfo().setGroupID(chatClient.getGroupID());
			chatClient.sendGroupMSG(newChatServer);
		}
	}
	public void handleGroupSize(Socket link, GroupSize groupSize) throws IOException {
		chatClient.setGroupSize(groupSize.getGroupSize());
		if (groupSize.getGroupID()!=-1){
			groupSize.setGroupID(-1);
			chatClient.sendGroupMSG(groupSize);
		}
	}
	
}
