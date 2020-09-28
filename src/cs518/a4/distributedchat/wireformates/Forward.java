package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.handler.MessageHandler;

public class Forward extends Data{
	public Forward() {
		super(FORWARD);
	}

	@Override
	public void handle(Socket link, MessageHandler handler) throws IOException{
		handler.handleForwardData(link, this);
	}

	public Data getData(){
		Data data = new Data();
		data.setSender(getSender());
		data.setText(getText());
		return data;
	}
}
