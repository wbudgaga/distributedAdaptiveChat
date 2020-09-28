package cs518.a4.distributedchat.wireformates;

import java.net.Socket;

import cs518.a4.distributedchat.handler.MessageHandler;

//it is a supper class of RegisterResponse and DegisterResponse 
public class Response extends Message{
	private byte 	statusCode;
	private String 	additionalInfo;
	
	public Response(int MessageID) {
		super(MessageID);
	}

	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		setStatusCode(unpackByteField(byteStream));
		setAdditionalInfo(unpackStringField(byteStream));
	}

	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}

	@Override
	public void handle(Socket link, MessageHandler handler) {
	}


	private byte[] packAdditionalInfo(){
		return ByteStream.packString(getAdditionalInfo());
	}


	@Override
	public byte[] packMessageBody(){
		byte[] StatusCodeBytes = {getStatusCode()};
		return ByteStream.join (StatusCodeBytes, packAdditionalInfo());
	}

	public byte getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(byte statusCode) {
		this.statusCode = statusCode;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Override
	public String getMessageType() {
		return null;
	}

}
