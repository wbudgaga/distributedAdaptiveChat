package cs518.a4.distributedchat.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import cs518.a4.distributedchat.communication.ConnectionManager;
import cs518.a4.distributedchat.wireformates.Data;
import cs518.a4.distributedchat.wireformates.DelMember;
import cs518.a4.distributedchat.wireformates.Forward;
import cs518.a4.distributedchat.wireformates.GroupActivity;
import cs518.a4.distributedchat.wireformates.GroupMembers;
import cs518.a4.distributedchat.wireformates.GroupMerging;
import cs518.a4.distributedchat.wireformates.GroupSize;
import cs518.a4.distributedchat.wireformates.Message;
import cs518.a4.distributedchat.wireformates.NewChatServer;
import cs518.a4.distributedchat.wireformates.NewMember;
import cs518.a4.distributedchat.wireformates.RegisterRequest;
import cs518.a4.distributedchat.wireformates.RegisterResponse;
import cs518.a4.distributedchat.wireformates.TestData;

// abstract classes extended by ChatClientHandle and ChatServerHanlder. 
// Some of its methods implemented by ChatClientHandler and the rest by ChatServerHanlder
public abstract class MessageHandler {
	public void handleRegisterRequest(Socket link, RegisterRequest registerRequest) throws IOException {} 		//Implemented by ChatServerHanlder
	public void handleRegisterResponse(Socket link, RegisterResponse registerResponse) {} 	//Implemented by ChatServerHanlder
	public void handleGroupMembers(Socket link, GroupMembers groupMembers) throws IOException {} 				//Implemented by ChatClientHandler
	public void handleData(Socket link, Data data) {}									 	//Implemented by both 
	public void handleForwardData(Socket link, Forward data)  throws IOException{}		 	//Implemented by ChatClientHandler
	public void handleTestData (Socket link, TestData testData) throws NoSuchAlgorithmException, IOException {}						 	//Implemented by both
	public void handleNewMember(Socket link, NewMember newMember) throws IOException {}					 	//Implemented by ChatClientHandler
	public void handleDelMember(Socket link, DelMember newMember) throws IOException {}						//Implemented by ChatClientHandler
	public void handleGroupActivity(Socket link, GroupActivity groupActivity) throws IOException {}			//Implemented by ChatServerHanlder
	public void handleGroupSize(Socket link, GroupSize groupSize) throws IOException {}
	public void handleGroupMerging(Socket link, GroupMerging groupMerging) throws IOException {}
	
	public void handleNewChatServer(Socket link, NewChatServer newChatServer) throws IOException {}
	protected void sendData(OutputStream outStream, Message msg) throws IOException{
		ConnectionManager.sendData(outStream, msg);
	}
}
