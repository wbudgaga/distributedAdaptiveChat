package cs518.a4.distributedchat.publishsubscribe;

import cs518.a4.distributedchat.core.ChatGroup;

public class GroupEvent extends Event{
	private ChatGroup chatGroup;
	
	public GroupEvent(Object source, ChatGroup chatGroup) {
		super(source);
		setChatGroup(chatGroup);
	}

	public ChatGroup getChatGroup() {
		return chatGroup;
	}

	public void setChatGroup(ChatGroup chatGroup) {
		this.chatGroup = chatGroup;
	}

	
}
