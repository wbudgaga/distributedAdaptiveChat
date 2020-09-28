package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.handler.MessageHandler;

public class GroupSize extends GroupActivity{
	public GroupSize() {
		super(GROUP_SIZE);
	}
	

	@Override
	public void handle(Socket link, MessageHandler handler) throws IOException{
		handler.handleGroupSize(link, this);
	}

	public int getGroupSize() {
		return getNumOfSentMessages();
	}

	public void setGroupSize(int size) {
		setNumOfSentMessages(size);
	}
}
