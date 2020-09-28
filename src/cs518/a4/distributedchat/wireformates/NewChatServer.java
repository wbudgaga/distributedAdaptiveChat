package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.handler.MessageHandler;

public class NewChatServer extends Request{

	public NewChatServer() {
		super(NEW_CHATSERVER);
	}

	@Override
	public  void  handle(Socket link, MessageHandler handler) throws IOException{
		handler.handleNewChatServer(link, this);
	}

}
