package cs518.a4.distributedchat.wireformates;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


import cs518.a4.distributedchat.handler.MessageHandler;

public class GroupMembers extends Message{
	private int 				  groupID;
	private ArrayList<ClientInfo> membersList = new ArrayList<ClientInfo>();
	
	public GroupMembers() {
		super(GROUP_MEMBERS);
	}

	public GroupMembers(int id) {
		super(id);
	}

	public void addMember(ClientInfo clientInfo){
		membersList.add(clientInfo);
	}
	
	public ClientInfo getMember(int index){
		return membersList.get(index);
	}

	public int getNumberOfMembers(){
		return membersList.size();
	}
	
	private int unpackNumberOfMembers(byte[] byteStream){
		return unpackIntField(byteStream);
	}

	private void unpackClientInfo(byte[] byteStream){
		ClientInfo member = new ClientInfo();
		member.initiate(readObjectBytes(byteStream));
		addMember(member);
	}
	
	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		setGroupID(unpackIntField(byteStream));
		int numberOfMembers = unpackNumberOfMembers(byteStream);
		for (int i=0; i < numberOfMembers; ++i){
			unpackClientInfo(byteStream);
		}
	}

	private byte[] packNumberOfMembers(){
		return ByteStream.intToByteArray(getNumberOfMembers());
	}

	@Override
	protected byte[] packMessageBody(){
		byte[] bytes = ByteStream.join(ByteStream.intToByteArray(getGroupID()),packNumberOfMembers());
		for (int i=0; i<getNumberOfMembers(); ++i){
			ClientInfo member = membersList.get(i);
			bytes = ByteStream.join(bytes, ByteStream.addPacketHeader(member.packMessage()));
		}
		return bytes;
	}
	
	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}

	@Override
	public void handle(Socket link, MessageHandler handler) throws IOException{
		handler.handleGroupMembers(link, this);
	}

	@Override
	public String getMessageType() {
		return null;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public ArrayList<ClientInfo> getMembersList() {
		return membersList;
	}

}
