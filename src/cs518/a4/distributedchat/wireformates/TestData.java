package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import cs518.a4.distributedchat.handler.MessageHandler;

public class TestData extends Data{
	public TestData() {
		super(TEST_DATA);
	}

	@Override
	public void handle(Socket link, MessageHandler handler) throws NoSuchAlgorithmException, IOException{
		handler.handleTestData(link, this);
	}

}
