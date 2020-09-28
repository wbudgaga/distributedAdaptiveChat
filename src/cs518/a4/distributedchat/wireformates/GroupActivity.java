package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.handler.MessageHandler;

public class GroupActivity extends Message{
	private int groupID;
	private int	numOfSentMessages; 	
	
	public GroupActivity(int id) {
		super(id);
	}
	public GroupActivity() {
		super(GROUP_ACTIVITY);
		numOfSentMessages = 1; 
	}
	
	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		setGroupID(unpackIntField(byteStream));
		setNumOfSentMessages(unpackIntField(byteStream));
	}

	@Override
	protected byte[] packMessageBody(){
		return ByteStream.join(ByteStream.intToByteArray(getGroupID()),ByteStream.intToByteArray(getNumOfSentMessages()));
	}
	
	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}

	@Override
	public void handle(Socket link, MessageHandler handler) throws IOException{
		handler.handleGroupActivity(link, this);
	}

	@Override
	public String getMessageType() {
		return null;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public int getNumOfSentMessages() {
		return numOfSentMessages;
	}

	public void setNumOfSentMessages(int numOfSentMessages) {
		this.numOfSentMessages = numOfSentMessages;
	}
}
