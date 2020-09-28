package cs518.a4.distributedchat.publishsubscribe;

import org.springframework.context.ApplicationEvent;

public abstract class Event extends ApplicationEvent{
	public Event(Object source) {
		super(source);
	}
}
