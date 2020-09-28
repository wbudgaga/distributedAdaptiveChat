package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import cs518.a4.distributedchat.handler.MessageHandler;

public class Data extends Message{
	private ClientInfo sender;
	private String 		text;
	
	public Data(int MessageID) {
		super(MessageID);
	}

	public Data() {
		super(DATA);
	}

	private void unpackClientInfo(byte[] byteStream){
		sender = new ClientInfo();
		sender.initiate(readObjectBytes(byteStream));
	}
	
	@Override
	public void initiate(byte[] byteStream) {
		currentIndex = 4;
		unpackClientInfo(byteStream);
		setText(unpackStringField(byteStream));
	}

	@Override
	protected byte[] packMessageBody(){
		return ByteStream.join(ByteStream.addPacketHeader(sender.packMessage()),ByteStream.packString(getText()));
	}

	@Override
	public void handle(Socket link, MessageHandler handler) throws IOException, NoSuchAlgorithmException{
		handler.handleData(link, this);
	}

	@Override
	public String getMessageType() {
		return null;
	}
	public ClientInfo getSender() {
		return sender;
	}
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setSender(ClientInfo sender) {
		this.sender = sender;
	}
	
	public Forward getForward(){
		Forward fwd = new Forward();
		fwd.setSender(getSender());
		fwd.setText(getText());
		return fwd;
	}

}
