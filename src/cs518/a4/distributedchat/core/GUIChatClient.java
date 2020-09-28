package cs518.a4.distributedchat.core;

import java.io.IOException;

// This interface hide details of the core implementations. 
// It is available to the gui model. Theerefore, the gui classes can only call these available methods
public interface GUIChatClient {
	public String[] getMembersIDs();
	public String 	getMemberID();
	public int 		getGroupID();
	public void 	sendGroupData(String data) ;
	public void 	broadcastData(String data) ;
	public int 		getGroupSize();
}
