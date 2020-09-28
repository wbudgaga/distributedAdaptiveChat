package cs518.a4.distributedchat.wireformates;

import java.net.Socket;

import cs518.a4.distributedchat.handler.MessageHandler;

public class ClientInfo extends Message{
	private int 	groupID=-1;
	private String 	ClientID=" ";
	private String 	host=" ";
	private int 	portNum= -1;

	
	public ClientInfo(int messageID) {
		super(messageID);
	}
	public ClientInfo() {
		super(CLIENT_INFO);
	}
	
	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		setGroupID(unpackIntField(byteStream));
		setClientID(unpackStringField(byteStream));
		setHost(unpackStringField(byteStream));
		setPortNum(unpackIntField(byteStream));
	}

	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}


	@Override
	protected byte[] packMessageBody() {
		byte[] bytes= ByteStream.join ( ByteStream.intToByteArray(getGroupID()),ByteStream.packString(getClientID()));
		bytes		= ByteStream.join ( bytes, ByteStream.packString(getHost()));
		return ByteStream.join(bytes, ByteStream.intToByteArray(getPortNum()));
	}

	@Override
	public String getMessageType() {
		return null;
	}
	
	@Override
	public  void  handle(Socket link, MessageHandler handler){
	}

	public String getClientID() {
		return ClientID;
	}
	public void setClientID(String clientID) {
		ClientID = clientID;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPortNum() {
		return portNum;
	}
	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
}
