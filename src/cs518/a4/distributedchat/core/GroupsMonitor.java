package cs518.a4.distributedchat.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cs518.a4.distributedchat.applications.Setting;
import cs518.a4.distributedchat.publishsubscribe.Context;
import cs518.a4.distributedchat.threadpool.Task;
import cs518.a4.distributedchat.wireformates.ClientInfo;
import cs518.a4.distributedchat.wireformates.Forward;

// This is a task class whose method execute is called periodically based on configurable  MEMBERSCHECKER_TIMEINTERVAL
// Its job is to call removeFailedMembers() method in chatGroupsManager to remove all dead chat clients
public class GroupsMonitor extends Task {
	private long 						startTime;
	private ChatGroupsManager 			chatGroupsManager;
	private HashMap<Integer,Integer> 	numOfMessages 	= new HashMap<Integer,Integer>();// <groupID, #msgs>
	private HashMap<Integer,Integer> 	smallGroups 	= new HashMap<Integer,Integer>();// <groupID, #itrs>
	private Forward						adMessage;
	private int							smallGroup1		= -1;
	private int							smallGroup2		= -1;
	
	public  GroupsMonitor(){
		startTime = System.currentTimeMillis();
		adMessage = new Forward();
		ClientInfo sender = new ClientInfo();
		sender.setClientID("Advertising Center");
		adMessage.setSender(sender);
	}
	
	public void setChatGroupsManager(ChatGroupsManager chatGroupsManager){
		this.chatGroupsManager = chatGroupsManager;
	}
	
	public ChatGroupsManager getChatGroupsManager(){
		return this.chatGroupsManager;
	}

	private int getListValue(HashMap<Integer,Integer> list,int key){
		Integer num = list.get(key);
		if (num == null)
			num = 0;
		return num;
	}
	
	private void incrListValue(HashMap<Integer,Integer> list, int key, int incValue){
		synchronized(list){
			list.put(key, getListValue(list,key) + incValue);
		}
	}
	
	public boolean incrNumOfMessages(int groupID, int incValue){
		incrListValue(numOfMessages,groupID,incValue);
	    double rate = calcExchangeRate(groupID);
	    ChatGroup group = chatGroupsManager.getChatGroup(groupID);
		return isActiveGroup(group, rate);
	}
	
	public double calcExchangeRate(int groupID){
		long 	numOfSeconds= (System.currentTimeMillis() - startTime)/1000;
		return getListValue(numOfMessages,groupID)/(double)numOfSeconds;
	}
	
	public void execute() throws IOException, InterruptedException {
		 while (true) {
			Thread.sleep(Setting.ADVERTISING_TIMEINTERVAL);
			doMonitoring();
		 } 
	}
	
	private boolean isActiveGroup(ChatGroup group, double groupsRate){
		if(group==null) return false;
		return groupsRate > group.getNumberOfMembers() * Setting.MERT;
	}
	
	private boolean isSmallGroup(ChatGroup group){
		return group.getNumberOfMembers() <  group.getMaxSize() * Setting.MRT;
	}

	private void setSmallGroup(int groupID){
		if (smallGroup1 == -1)
			smallGroup1 = groupID;
		else
			smallGroup2 = groupID;
	}
	
	private void trackGroupSize(ChatGroup group){
		if (isSmallGroup(group)){
			incrListValue(smallGroups,group.getGroupID(),1);
			if(getListValue(smallGroups, group.getGroupID()) >= Setting.MERGING_WAIT_COUNT)
				setSmallGroup(group.getGroupID());
		}else
			smallGroups.remove(group.getGroupID());
	}
	
	private void doActiveGroupAction(ChatGroup group) throws IOException{
	    if (isActiveGroup(group, calcExchangeRate(group.getGroupID()))){
	    	int adID = (int) (System.currentTimeMillis()  % 10);
	    	adMessage.setText(Context.getInstance().getValue("ad"+adID));
			group.sendMessageToOneMember(adMessage);
	    }
	}
	
	private void doMerging2Groups() throws IOException{
		if(smallGroup1 != -1 && smallGroup2 != -1){
			chatGroupsManager.merge2Groups(smallGroup1,smallGroup2);
		}
	}

	public void doMonitoring() throws IOException{
		smallGroup1 = smallGroup2 = -1;
		synchronized(numOfMessages){
			for (ChatGroup group: chatGroupsManager.getGroups().values()) {
				doActiveGroupAction(group);
				trackGroupSize(group);
			}
		}
		doMerging2Groups();
	}
}
