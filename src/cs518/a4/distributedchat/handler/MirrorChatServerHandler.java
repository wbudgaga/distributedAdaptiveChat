package cs518.a4.distributedchat.handler;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.applications.MirrorChatServer;
import cs518.a4.distributedchat.applications.RemoteChatClient;
import cs518.a4.distributedchat.wireformates.DelMember;
import cs518.a4.distributedchat.wireformates.GroupSize;
import cs518.a4.distributedchat.wireformates.NewMember;

//This class handles all messages that received at the mirror chat server
public class MirrorChatServerHandler extends MessageHandler{
	private MirrorChatServer chatServer;
	
	public MirrorChatServerHandler(MirrorChatServer chatServer){
		this.chatServer = chatServer;
	}
	
	public void handleNewMember(Socket link, NewMember newMember) throws IOException {
		chatServer.addMember(RemoteChatClient.getInstance(newMember.getClientInfo()));
		test();
	}
	
	public void handleDelMember(Socket link, DelMember delMember) throws IOException {
		chatServer.removeMember(RemoteChatClient.getInstance(delMember.getClientInfo()));
		test();
	}
	
	public void handleGroupSize(Socket link, GroupSize groupSize) throws IOException {
		chatServer.changeGroupSize(groupSize.getGroupID(), groupSize.getGroupSize());
	}

	public void test(){
		chatServer.test();
	}
}
