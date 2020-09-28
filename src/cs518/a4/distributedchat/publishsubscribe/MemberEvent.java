package cs518.a4.distributedchat.publishsubscribe;

import cs518.a4.distributedchat.wireformates.ClientInfo;

public class MemberEvent extends Event{
	private ClientInfo memberInfo;
	
	public MemberEvent(Object source, ClientInfo memberInfo) {
		super(source);
		setMemberInfo(memberInfo);
	}

	public ClientInfo getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(ClientInfo memberInfo) {
		this.memberInfo = memberInfo;
	}

	
}
