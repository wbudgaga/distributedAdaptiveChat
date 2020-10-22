package cs518.a4.distributedchat.communication;

//Manages the connections. It hides all the connections details and is responsible on performing all connection operations
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import cs518.a4.distributedchat.handler.MessageHandler;
import cs518.a4.distributedchat.threadpool.ThreadPoolManager;
import cs518.a4.distributedchat.wireformates.ByteStream;
import cs518.a4.distributedchat.wireformates.Message;
import cs518.a4.distributedchat.wireformates.MessageFactory;

public class ConnectionManager {
	private ListeningTask 		listeningTask ;
	private	MessageHandler		messageHandler;
	private	ThreadPoolManager	threadPoolManager;
	private volatile boolean 	keepReceiving = false;
	
	public ConnectionManager(MessageHandler	messageHandler, ThreadPoolManager	threadPoolManager){
		this.messageHandler 	= messageHandler;
		this.threadPoolManager 	= threadPoolManager;
	}
	
	public void connect(String hostAddress, int port) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchAlgorithmException{
		Socket socket 	= new Socket(hostAddress, port);
		keepReceiving	= true;
		while (keepReceiving){
			byte[] byteBuffer = ReceivingTask.receiveMessageFrom(socket.getInputStream());
			if (byteBuffer==null)
				break;
			handleMassage(socket, byteBuffer);
		}
		System.out.println("Closing connection...");
		socket.close();
	}
	
	public void closeConnection(){
		keepReceiving = false;
	}

	public static Socket getConnection(int port) throws IOException{
		return ListeningTask.getIncomingConnection(port);
	}
	
	public void handleConnection(Socket connection) throws IOException{
		receiveData(connection);
	}
	
	public void receiveData(Socket connection) throws IOException{
		threadPoolManager.addTask(new ReceivingTask(this,connection));
	}
	
	public void handleMassage(Socket link, byte[] byteBuffer) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, NoSuchAlgorithmException{
		Message msg =  MessageFactory.getInstance().createMessage(byteBuffer);
		msg.handle(link, messageHandler);
	}
	
	public void startListening(int listeningPort) throws IOException{
		listeningTask = new ListeningTask(listeningPort,this);
		threadPoolManager.addTask(listeningTask);
	}

	public void stopListening(){
		if (listeningTask!= null)
			listeningTask.stop();
	}
	
	protected static void sendByteData(OutputStream outStream, byte[] dataToBeSent) throws IOException {
		outStream.write(ByteStream.addPacketHeader(dataToBeSent)); // message header will be added by sending each message
		outStream.flush();
	}

	public static void sendData(OutputStream outStream, Message msg) throws IOException{
		sendByteData(outStream,msg.packMessage());
	}
	
	public static boolean isAlive(String hostAddress, int port){
		try {
			Socket socket 	= new Socket(hostAddress, port);
			socket.close();
			return true;
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
		return false;
	}
	
	public void sendMessage(Message msg, String hostAddress, int port){
		try{
			Socket socket 	= new Socket(hostAddress, port);
			sendByteData(socket.getOutputStream(),msg.packMessage());
			socket.close();
		} catch (IOException e) {}
	}
	
	public Message sendReceiveMessage(Message msg, String hostAddress, int port) {
		try{
			Socket socket 	= new Socket(hostAddress, port);
			sendByteData(socket.getOutputStream(),msg.packMessage());
			byte[] byteBuffer = ReceivingTask.receiveMessageFrom(socket.getInputStream());
			socket.close();
			return MessageFactory.getInstance().createMessage(byteBuffer);
		} catch (IOException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {}
		return null;
	}

}
