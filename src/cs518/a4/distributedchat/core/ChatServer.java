package cs518.a4.distributedchat.core;

import java.io.IOException;
import java.util.ArrayList;

import cs518.a4.distributedchat.applications.RemoteChatClient;
import cs518.a4.distributedchat.wireformates.Data;

public interface ChatServer {
	public void run(int threadpoolSize) throws InstantiationException, IllegalAccessException, IOException, InterruptedException;
	public ChatServer nextRun() throws InstantiationException, IllegalAccessException, IOException, InterruptedException;
}
