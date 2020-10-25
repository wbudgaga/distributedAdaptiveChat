package cs518.a4.distributedchat.communication;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

import cs518.a4.distributedchat.threadpool.Task;
import cs518.a4.distributedchat.wireformates.ByteStream;

// it is a thread class that is responsible for reading the incoming data from the connection.
// the message header will be removed for each received message
public class ReceivingTask extends Task{
	private Socket 	link;
	private InputStream inStream;
	private ConnectionManager connectionManager;
	
	public  ReceivingTask(ConnectionManager connection,Socket link) throws IOException {
		this.link				= link;
		this.connectionManager			= connection;
	//	this.link.setSoTimeout(10);
		this.inStream 				= link.getInputStream();
	}

	private static byte[] readMessageBody(InputStream inStream, int bodyLength) throws SocketException, IOException {
		int totalBytesRcvd 			= 0;  // Total bytes received so far
		byte[] byteBuffer			= new byte[bodyLength];
		int bytesRcvd;           // Bytes received in last read
		while (totalBytesRcvd < bodyLength) {
		      if ((bytesRcvd = inStream.read(byteBuffer, totalBytesRcvd, bodyLength - totalBytesRcvd)) == -1)
		    	  throw new SocketException("Connection close prematurely");
		      
		      totalBytesRcvd 			+= bytesRcvd;
		}
	    return byteBuffer;
	}
	
	public static byte[] receiveMessageFrom(InputStream inStream) throws SocketException, IOException{
		byte[] messageLength			= new byte[4];	
		if (inStream.read(messageLength, 0,4) < 4)
			return null;
		int bodyLength 				= ByteStream.byteArrayToInt(messageLength); // reading the message length and removing the message header
		return readMessageBody(inStream,bodyLength);
	}
	
	 private void receivingMessage() throws SocketException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchAlgorithmException{
		byte[] byteBuffer 			= receiveMessageFrom(this.inStream);
		if (byteBuffer == null)
			return;
		connectionManager.handleMassage(link, byteBuffer);
	}
	 
	@Override
	public void execute() {
		try {
			receivingMessage();
		//	this.link.close();
		} catch (IOException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
		}
		
	}	
}
