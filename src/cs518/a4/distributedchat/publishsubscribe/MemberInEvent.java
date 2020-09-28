package cs518.a4.distributedchat.publishsubscribe;

import cs518.a4.distributedchat.wireformates.ClientInfo;

public class MemberInEvent extends MemberEvent{
	public MemberInEvent(Object source, ClientInfo memberInfo) {
		super(source,memberInfo);
	}
}
