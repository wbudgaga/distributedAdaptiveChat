package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.handler.MessageHandler;

//this class represents a RegisterRequest message. It is used to pack and unpack the RegisterRequest message by using the methods in superclass
//the method handle() calls the method handle() in receiver to handle the message
public class RegisterRequest extends Request{
	
	public RegisterRequest() {
		super(REGISTER_REQUEST);
	}

	public String getMessageType() {
		return "REGISTER_REQUEST";
	}
	
	public  void  handle(Socket link, MessageHandler handler) throws IOException{
		handler.handleRegisterRequest(link,this);
	}
	
}
