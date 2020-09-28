package cs518.a4.distributedchat.test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import cs518.a4.distributedchat.applications.ChatClientImp;
import cs518.a4.distributedchat.applications.Setting;
import cs518.a4.distributedchat.handler.ChatClientHandler;
import cs518.a4.distributedchat.util.Helper;
import cs518.a4.distributedchat.wireformates.ByteStream;
import cs518.a4.distributedchat.wireformates.TestData;

public class ChatClientTestApp extends ChatClientImp{

	private 	Queue<String> 		sentDataTrackerQueue 	= new LinkedList<String>();
	private		int 				messageCounter 			= 0;
	private 	ConcurrentHashMap<String, Integer> ReceivingTracker   = new ConcurrentHashMap<String, Integer>();

	public ChatClientTestApp(String nodeID, int port, String serverHost,int serverPort) throws UnknownHostException {
		super(nodeID, port, serverHost, serverPort);
	}

	private void broadcastTestData(String data) throws IOException{
		TestData testDate = new TestData();
		testDate.setText(data);
		testDate.setSender(getClientInfo());
		chatGroup.sendMessage(testDate);
		remoteChatServer.sendMessage(testDate);
	}
	
	private synchronized void report(){
		if (ReceivingTracker.size() == 0)
			return;
		for (Map.Entry<String, Integer> entry : ReceivingTracker.entrySet()) {
			if (sentDataTrackerQueue.peek().compareTo(entry.getKey())==0){
				System.out.println("[Msg-"+messageCounter+"] Received: "+ entry.getKey() +"==>"+ entry.getValue() + " times correctly");
				sentDataTrackerQueue.poll();
			}else
				System.out.println("[Msg-"+messageCounter+"] Received: "+ entry.getKey() +"==>"+ entry.getValue() + " times not correctly");
			ReceivingTracker.remove(entry.getKey());
	    }
	}
	
	public synchronized void dataReceivedNotifying(String senderID, String data){
		if (ReceivingTracker.containsKey(data))
			ReceivingTracker.put(data, ReceivingTracker.get(data)+1);
		else{
			ReceivingTracker.put(data,1);
		}
	}

	public synchronized void broadcastData(String data){
		try{
			report();
			String hashCodeOfSentData 	= Helper.fromBytes(ByteStream.StringToByteArray(data));
			broadcastTestData(data);
			messageCounter++;
			sentDataTrackerQueue.offer(hashCodeOfSentData);
			System.out.println("[Msg-"+messageCounter+"] Sent    : "+hashCodeOfSentData);
		}catch(NoSuchAlgorithmException  e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean run(int threadpoolSize) throws IOException {
		setHandler(new ChatClientHandler(this));
		if (!startListening(threadpoolSize))
			return false;
		if (!joinChatting()){
			stopListening();
			return false;
		}
		BroadCaster broadCaster = new BroadCaster(this, Setting.TEST_MESSAGE_RATE);
		getThreadPoolManager().addTask(broadCaster);
		return true;
	}

	public static void main(String args[]) throws IOException {
		
		if (args.length < 3) {
			System.err.println("Discovery Node:  Usage:");
			System.err.println("         java cs518.a1.distributedchat.test.ChatClientTestApp PORT-NUM SERVER-HOST SERVER-PORT");
		    return;
		}

		int 	port		= Integer.parseInt(args[0]);
		String 	serverHost 	= args[1];
		int 	serverPort	= Integer.parseInt(args[2]);
		ChatClientTestApp chatClientTester = new ChatClientTestApp("chatClientTester",port,serverHost,serverPort);
		chatClientTester.run(Setting.CLIENT_THREADPOOL_SIZE);
	}


}
