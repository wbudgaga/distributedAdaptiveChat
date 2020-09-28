package cs518.a4.distributedchat.applications;

import java.io.IOException;

import cs518.a4.distributedchat.core.ChatGroup;
import cs518.a4.distributedchat.core.Remote;
import cs518.a4.distributedchat.wireformates.GroupActivity;
import cs518.a4.distributedchat.wireformates.Message;
import cs518.a4.distributedchat.wireformates.RegisterRequest;
import cs518.a4.distributedchat.wireformates.RegisterResponse;


public class RemoteChatServer extends Remote{

	public RemoteChatServer(String serverHost, int serverPort) {
		super(serverHost,serverPort);
	}

	private Message sendReceiveMessage(Message msg){
		return connectionManager.sendReceiveMessage(msg,getHost(), getPort());
	}
	
	public boolean join(ChatClientImp client) {
		RegisterRequest registerRequestMSG = new RegisterRequest();
		registerRequestMSG.setClientInfo(client.getClientInfo());
		RegisterResponse  registerResponse = (RegisterResponse) sendReceiveMessage(registerRequestMSG);	
		if(registerResponse == null){
			System.out.println("Connection with Chat Server could not be established!");
			return false;
		}
		if (registerResponse.getStatusCode() == Message.FAILURE){
			System.out.println("Joining denied because member with the same ID is already found!");
			return false;
		}
		System.out.println("Registeration accepted");
		return true;
	}
	
	public void updateGroupActivity(ChatGroup ChatGroup) throws IOException {
		GroupActivity msg = new GroupActivity();
		msg.setGroupID(ChatGroup.getGroupID());
		sendMessage(msg);
	}

}
