package cs518.a4.distributedchat.publishsubscribe;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Context {
	private final ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
	private static final Context instance = new Context();
	
	private Context(){}
	
	public static Context getInstance() {
		return instance;
	}

	public Object getBean(String beanName){
		return context.getBean(beanName);
	}
	
	public String getValue(String id){
		return context.getMessage(id, null, " Ad is currently not available",null);
		
	}
}
