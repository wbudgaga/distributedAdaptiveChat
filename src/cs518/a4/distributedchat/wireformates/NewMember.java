package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.handler.MessageHandler;

public class NewMember extends Request{
	public NewMember() {
		super(NEW_MEMBER);
	}
	
	@Override
	public  void  handle(Socket link, MessageHandler handler) throws IOException{
		handler.handleNewMember(link, this);
	}
}
