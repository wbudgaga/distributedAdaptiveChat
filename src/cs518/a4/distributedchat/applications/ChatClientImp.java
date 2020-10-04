package cs518.a4.distributedchat.applications;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cs518.a4.distributedchat.core.ChatClient;
import cs518.a4.distributedchat.core.ChatGroup;
import cs518.a4.distributedchat.core.Node;
import cs518.a4.distributedchat.gui.MainGUI;
import cs518.a4.distributedchat.handler.ChatClientHandler;
import cs518.a4.distributedchat.publishsubscribe.Context;
import cs518.a4.distributedchat.publishsubscribe.Observer;
import cs518.a4.distributedchat.wireformates.ClientInfo;
import cs518.a4.distributedchat.wireformates.Message;

public class ChatClientImp extends Node implements ChatClient{
	protected ChatGroup chatGroup;
	protected RemoteChatServer remoteChatServer;
	private String serverHost;
	private int serverPort;
	private MainGUI mainGUI;
	
	public ChatClientImp(String nodeID, int port,String serverHost, int serverPort) throws UnknownHostException {
		super(nodeID, port);
		chatGroup 			= (ChatGroup) Context.getInstance().getBean("chatGroup");// new ChatGroup(groupID,this);
		this.serverHost 		= serverHost;
		this.serverPort 		= serverPort;
	}	
		
	public ArrayList<RemoteChatClient> getMembers(){
		return chatGroup.getMembers();
	}
		
	public void newMemberGroupNotification() throws IOException{
		chatGroup.sendNewMemberNotification(RemoteChatClient.getInstance(getClientInfo()));
	}

	public synchronized void newMemberNotification(RemoteChatClient member) {
		chatGroup.addMember(member);
	}
	
	public void leavingMemberNotification(ClientInfo departedMember){
		chatGroup.removeMember(departedMember.getClientID());
	}

	public void dataReceivedNotifying(String senderID, String data){
		String text 			= senderID + " ==> " + data;
		System.out.println(text);
		//mainGUI.updateTextArea(text);
	}
	
	public synchronized void refreshChatGroup(){
		if (mainGUI!=null)
			mainGUI.updatePage();
	}
	
	public void sendData(String memberID, String data) throws IOException{
		 chatGroup.getMember(memberID).sendData(getClientInfo(),data);
	}
	
	public void sendGroupMSG(Message data) throws IOException{
		 chatGroup.sendMessage(data);
	}

	public void sendGroupData(String data) throws IOException{
		 chatGroup.sendData(getClientInfo(), data); 
	}
	
	public void broadcastData(String data){
		try {
			sendGroupData(data);
			remoteChatServer.sendData(getClientInfo(), data);
		} catch (IOException e) {
		}
		
	}

	public boolean joinChatting(){
		setRemoteChatServer(new RemoteChatServer(serverHost,serverPort));
		if (!remoteChatServer.join(this))
			return false;
		return true;
	}
	
	public boolean leaveChatting(){
		if (!remoteChatServer.join(this))
			return false;
		return true;
	}

	public boolean run(int threadpoolSize) throws IOException {
		setHandler(new ChatClientHandler(this));
		if (!startListening(threadpoolSize))
			return false;
		if (!joinChatting()){
			stopListening();
			return false;
		}
		Observer.register(this);
		return true;
	}
	
	public ClientInfo getClientInfo() {
		RemoteChatClient senderClient =  new RemoteChatClient(getNodeID(),getHost(), getPortNum(),getGroupID());
		return senderClient.getClientInfo();
	}
	
	public void setGroupID(int groupID) {
		chatGroup.setGroupID(groupID);
	}

	public int getGroupID() {
		return chatGroup.getGroupID();
	}

	public void cleanGroup() {
		chatGroup.removeAll();
	}

	public String getServerHost() {
		return serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}
	
	@Override
	public boolean setGroupMembers(ArrayList<RemoteChatClient> members) {
		return true;
	}

	@Override
	public boolean isAlive() {
		return true;
	}

	public void setMainGUI(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
	}

	public RemoteChatServer getRemoteChatServer() {
		return remoteChatServer;
	}

	public void setRemoteChatServer(RemoteChatServer remoteChatServer) {
		this.remoteChatServer = remoteChatServer;
	}

	@Override
	public void updateGroupActivity(ChatGroup chatGroup) {
		try {
			remoteChatServer.updateGroupActivity(chatGroup);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setGroupSize(int size) {
		chatGroup.setMaxSize(size);
		
	}

	@Override
	public int getGroupSize() {
		return chatGroup.getMaxSize();
	}
}
