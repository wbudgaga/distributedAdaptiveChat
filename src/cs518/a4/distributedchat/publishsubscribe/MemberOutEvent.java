package cs518.a4.distributedchat.publishsubscribe;

import cs518.a4.distributedchat.wireformates.ClientInfo;

public class MemberOutEvent extends MemberEvent{
	public MemberOutEvent(Object source, ClientInfo memberInfo) {
		super(source,memberInfo);
	}
}
