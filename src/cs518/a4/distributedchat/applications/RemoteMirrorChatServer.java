package cs518.a4.distributedchat.applications;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import cs518.a4.distributedchat.core.Remote;
import cs518.a4.distributedchat.publishsubscribe.Context;
import cs518.a4.distributedchat.publishsubscribe.MemberInEvent;
import cs518.a4.distributedchat.publishsubscribe.Publisher;
import cs518.a4.distributedchat.publishsubscribe.SynchronizeEvent;
import cs518.a4.distributedchat.wireformates.ClientInfo;
import cs518.a4.distributedchat.wireformates.DelMember;
import cs518.a4.distributedchat.wireformates.GroupSize;
import cs518.a4.distributedchat.wireformates.Message;
import cs518.a4.distributedchat.wireformates.NewMember;

public class RemoteMirrorChatServer extends Remote{
	private Queue<Message> 	msgQueue 		= new LinkedList<Message>();
	
	public RemoteMirrorChatServer() {
		super(-1);
	}
	
	public RemoteMirrorChatServer(int listeningPort, int a) {
		super(listeningPort);
	}

	public void execute() {
		try {
			System.out.println("The mirror chat server can connect the port "+getPort());
			receiveConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addMember(ClientInfo member) throws IOException{
		NewMember memberMSG 			= new NewMember();
		memberMSG.setClientInfo(member);
		enQueue(memberMSG);
		emptyQueue();
	}
	
	public void removeMember(ClientInfo member) throws IOException{
		DelMember delMemberMSG 			= new DelMember();
		delMemberMSG.setClientInfo(member);
		enQueue(delMemberMSG);
		emptyQueue();
	}
	
	public void changeGroupSize(int groupID, int size) throws IOException{
		GroupSize msg 				= new GroupSize();
		msg.setGroupID(groupID);
		msg.setGroupSize(size);
		enQueue(msg);
		emptyQueue();
	}

	
	private synchronized void enQueue(Message msg){
		msgQueue.offer(msg);
	}
	
	private synchronized void emptyQueue() throws IOException{
		while (msgQueue.size()>0 && isConnected()){
			sendLinkMessage(msgQueue.poll());
		}
	}

}
