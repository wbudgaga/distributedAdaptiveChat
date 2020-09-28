package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.handler.MessageHandler;

public class GroupMerging extends GroupMembers{	
	public GroupMerging() {
		super(GROUP_MERGING);
	}

	@Override
	public void handle(Socket link, MessageHandler handler) throws IOException{
		handler.handleGroupMerging(link, this);
	}
}
