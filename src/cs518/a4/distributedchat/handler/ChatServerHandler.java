package cs518.a4.distributedchat.handler;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.applications.MainChatServer;
import cs518.a4.distributedchat.applications.RemoteChatClient;
import cs518.a4.distributedchat.wireformates.Data;
import cs518.a4.distributedchat.wireformates.GroupActivity;
import cs518.a4.distributedchat.wireformates.Message;
import cs518.a4.distributedchat.wireformates.RegisterRequest;
import cs518.a4.distributedchat.wireformates.RegisterResponse;
import cs518.a4.distributedchat.wireformates.TestData;

//This class handles all messages that received at the chat server
public class ChatServerHandler extends MessageHandler{
	private MainChatServer chatServer;
	
	public ChatServerHandler(MainChatServer chatServer){
		this.chatServer = chatServer;
	}

	// It creates and sends response message as result of register request
	private boolean sendResponse(Socket link, byte status){
		RegisterResponse resMSG = new RegisterResponse();
		resMSG.setStatusCode(status);
		try {
			sendData(link.getOutputStream(), resMSG);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	// It handles register request by asking the chat server to add it as a member in one of the groups.
	// If returned group id is -1 means that joining this member could not done. In both cases response will be sent
	// and if the joining was successful, it sends group members to the client
	public synchronized void handleRegisterRequest(Socket link,RegisterRequest message) throws IOException {	
		RemoteChatClient remoteChatClient = RemoteChatClient.getInstance(message.getClientInfo());
		int groupID = chatServer.addMember(remoteChatClient);
		if (groupID == -1){
			System.out.println("Join request from "+message.getClientInfo().getClientID()+" has been not accepted because member of the same id is already found");
			sendResponse(link, Message.FAILURE);
			return;
		}
		System.out.println("The member "+message.getClientInfo().getClientID()+" has joined the Chat Group #:"+groupID);
		sendResponse(link, Message.SUCCEESS);
		remoteChatClient.setNewGroupSize(chatServer.getGroupSize(remoteChatClient.getNodeID()));
		remoteChatClient.setGroupMembers(chatServer.getGroupMembers(remoteChatClient.getNodeID()));
		
	}

	//It handles text message received from chat client. It asks the chat server to broadcast that message
	public void handleData(Socket link, Data data) {
		chatServer.broadCastData(data);
	}

	public void handleTestData(Socket link, TestData testData) {
		chatServer.broadCastData(testData);
	}
	
	public void handleGroupActivity(Socket link, GroupActivity groupActivity) throws IOException {
		chatServer.updateGroupActivity(groupActivity.getGroupID(), groupActivity.getNumOfSentMessages());
	}
}
