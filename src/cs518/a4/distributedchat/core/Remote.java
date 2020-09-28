package cs518.a4.distributedchat.core;

import java.io.IOException;
import java.net.Socket;

import cs518.a4.distributedchat.communication.ConnectionManager;
import cs518.a4.distributedchat.threadpool.Task;
import cs518.a4.distributedchat.wireformates.ClientInfo;
import cs518.a4.distributedchat.wireformates.Data;
import cs518.a4.distributedchat.wireformates.Message;

public class Remote extends Task{
	protected 	ConnectionManager 	connectionManager;
	private 	String 				host;
	private 	int 				port;
	private 	Socket				link;
	
	public Remote(String 	host, int 	port){
		setHost(host);
		setPort(port);
		connectionManager = new ConnectionManager(null,null);
	}
	
	public Remote(int port){
		setPort(port);
		link = null;
	}

	public void receiveConnection() throws IOException{
		link =  ConnectionManager.getConnection(port);
	}
	
	public boolean isConnected(){
		return link!=null;
	}
	
	public void sendMessage(Message msg) throws IOException{
		connectionManager.sendMessage(msg,getHost(), getPort());
	}
	
	public void sendLinkMessage(Message msg) throws IOException{
		ConnectionManager.sendData(link.getOutputStream(), msg);
	}

	public void sendData(ClientInfo sender, String data) throws IOException{
		Data dataMSG = new Data();
		dataMSG.setText(data);
		dataMSG.setSender(sender);
		sendMessage(dataMSG);
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void execute() throws IOException, InterruptedException {
	}
}
