package cs518.a4.distributedchat.publishsubscribe;

import cs518.a4.distributedchat.core.ChatGroup;

public class GroupSizeEvent extends GroupEvent{
	
	public GroupSizeEvent(Object source, ChatGroup chatGroup) {
		super(source,chatGroup);
	}
	
}
