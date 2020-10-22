package cs518.a4.distributedchat.aspects;

import java.util.ArrayList;
import org.aspectj.lang.ProceedingJoinPoint;

import cs518.a4.distributedchat.applications.RemoteChatClient;
import cs518.a4.distributedchat.core.ChatGroup;
import cs518.a4.distributedchat.core.GroupsMonitor;
import cs518.a4.distributedchat.publishsubscribe.Context;
import cs518.a4.distributedchat.publishsubscribe.GroupEvent;
import cs518.a4.distributedchat.publishsubscribe.GroupSizeEvent;
import cs518.a4.distributedchat.publishsubscribe.MemberInEvent;
import cs518.a4.distributedchat.publishsubscribe.MemberOutEvent;
import cs518.a4.distributedchat.publishsubscribe.Publisher;
import cs518.a4.distributedchat.publishsubscribe.SynchronizeEvent;
import cs518.a4.distributedchat.wireformates.GroupSize;

public class PublishingAspect {
	
	public Publisher getPublisher(){
		return (Publisher) Context.getInstance().getBean("publisher");
	}
	
	public Object removeMember(ProceedingJoinPoint pjp) throws Throwable{
		ChatGroup group 		= (ChatGroup) pjp.getTarget();
		RemoteChatClient member 	= group.getMember((String) pjp.getArgs()[0]);
		getPublisher().publish(new MemberOutEvent(this, member.getClientInfo()));
		return pjp.proceed();		
	}
	
	public Object increaseGroupSize(ProceedingJoinPoint pjp) throws Throwable{
		boolean result 			= (boolean) pjp.proceed();
		if (result){
			GroupsMonitor gm 	= (GroupsMonitor) pjp.getTarget();
			ChatGroup cg 		= gm.getChatGroupsManager().getChatGroup((int) pjp.getArgs()[0]);
			if(cg.isFull()){
				cg.increaseGroupSize();
				getPublisher().publish(new GroupSizeEvent(this,cg));
			}
		}
		return  result;
	}
	
	public void publishGroupActivityEvent(ProceedingJoinPoint pjp) throws Throwable{
		pjp.proceed();
		GroupEvent groupEvent = new GroupEvent(this,(ChatGroup) pjp.getTarget() );
		getPublisher().publish(groupEvent);
	}


	public void publishSynchronizeEvent(ProceedingJoinPoint pjp) throws Throwable{
		pjp.proceed();
		SynchronizeEvent synchronizeEvent = new SynchronizeEvent(this);
		getPublisher().publish(synchronizeEvent);
	}

	private void publishMemberInEvent(RemoteChatClient member){
		MemberInEvent memberInEvent = new MemberInEvent(this, member.getClientInfo());
		getPublisher().publish(memberInEvent);
	}
	
	public Object publishAllMembersEvent(ProceedingJoinPoint pjp) throws Throwable{
		ArrayList<RemoteChatClient> list= (ArrayList<RemoteChatClient>) pjp.proceed();
		for(RemoteChatClient member:list)
			publishMemberInEvent(member);
		return list;
	}

	public Object publishMemberOutEvent(ProceedingJoinPoint pjp) throws Throwable{
		ArrayList<RemoteChatClient> list= (ArrayList<RemoteChatClient>) pjp.proceed();
		for(RemoteChatClient member:list)
			getPublisher().publish(new MemberOutEvent(this, member.getClientInfo()));
		return list;
	}

	public Object publishMemberInEvent(ProceedingJoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		RemoteChatClient member = (RemoteChatClient) args[0];
		boolean result = (boolean) pjp.proceed();
		if (result)
			publishMemberInEvent(member);
		return result;
	}
}
