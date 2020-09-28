package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.handler.MessageHandler;

public class Request extends Message{
	private ClientInfo client;
		
	public Request(int MessageID) {
		super(MessageID);
	}
			
	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		client = new ClientInfo();
		client.initiate(byteStream);
	}
		
	protected byte[] packMessageBody(){
		return client.packMessageBody();
	}
		
	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}

	@Override
	public String getMessageType() {
		return null;
	}

	@Override
	public void handle(Socket link,MessageHandler handler) throws IOException {
	}

	public ClientInfo getClientInfo() {
		return client;
	}

	public void setClientInfo(ClientInfo client) {
		this.client = client;
	}
}
