package cs518.a4.distributedchat.publishsubscribe;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class Publisher implements ApplicationEventPublisherAware{
	ApplicationEventPublisher publisher;

	public void publish(Event event) {
		publisher.publishEvent(event);
	}
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
		
	}
}
