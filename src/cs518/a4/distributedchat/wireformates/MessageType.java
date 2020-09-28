package cs518.a4.distributedchat.wireformates;


// contains the messages types and their equivalent classes' names
// it is very important to keep the order the same in both lists 
public interface MessageType {
	public static final byte SUCCEESS 			= 1;
	public static final byte FAILURE 			= 2;
	
	public static final int REGISTER_REQUEST	= 0;
	public static final int REGISTER_RESPONSE	= 1;
	public static final int CLIENT_INFO			= 2;
	public static final int GROUP_MEMBERS		= 3;
	public static final int DATA				= 4;
	public static final int NEW_MEMBER			= 5;
	public static final int DEL_MEMBER			= 6;
	public static final int TEST_DATA			= 7;
	public static final int FORWARD				= 8;
	public static final int NEW_CHATSERVER		= 9;
	public static final int GROUP_ACTIVITY		= 10;	
	public static final int GROUP_SIZE			= 11;
	public static final int GROUP_MERGING		= 12;	
		
	public static enum ClassName{
		RegisterRequest,
		RegisterResponse,
		ClientInfo,
		GroupMembers,
		Data,
		NewMember,
		DelMember,
		TestData,
		Forward,
		NewChatServer,
		GroupActivity,
		GroupSize,
		GroupMerging;


		 public static String get(int i){
			 return values()[i].toString();
		 }
	}
}
