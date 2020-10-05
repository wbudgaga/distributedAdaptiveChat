package cs518.a4.distributedchat.applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cs518.a4.distributedchat.applications.Setting;
import cs518.a4.distributedchat.gui.GUIFacade;
import cs518.a4.distributedchat.publishsubscribe.Context;
import cs518.a4.distributedchat.threadpool.Task;

// This is a task class whose the execute() method is called periodically based on configurable variable, MEMBERSCHECKER_TIMEINTERVAL
// Its job is to call removeFailedMembers() method in chatGroupsManager to remove all dead chat clients
public class CommandReader extends Task {
	private GUIFacade model;
	private BufferedReader 	bufferedReader;
	
	public  CommandReader(GUIFacade model){
		this.model 			= model;
		bufferedReader 			= new BufferedReader(new InputStreamReader(System.in));
	}
	
	protected String readCommand(){
		try {
			return bufferedReader.readLine();
		} catch (IOException e) {}
		return "quit";
	}
	private void listMembers(){
		System.out.println("=====================================================");
		System.out.println("Groipd#"+model.getGroupID()+"\t "+"Group Size:"+model.getGroupSize()+"\t ChatClientID:"+model.getMemberID());
		System.out.println("-----------------------------------------------------");
		for(String id:model.getMembersIDs())
			System.out.println(id);
		System.out.println("=====================================================");
	}
	
	protected void groupSending() {
		System.out.println("*** Sending stage ***");
		String command;
		while (true){
			command = readCommand();		
			if (command == null)							continue;
			if(command.compareTo("quit")			==0)	break;			
			model.sendGroupData(command);
		}
		System.out.println("Exiting the sending stage!");
	}
	
	protected void broadcast() {
		System.out.println("*** Broadcasting stage ***");
		String command;
		while (true){
			command = readCommand();		
			if (command == null)							continue;
			if(command.compareTo("quit")			==0)	break;			
			model.broadcastData(command);
		}
		System.out.println("Exiting the broadcasting stage!");
	}

	
	protected void commandLineReanderMainLoop() {
		String command;
		while (true){
			command = readCommand();
			if (command == null)							continue;
			if(command.compareTo("list")		==0)		{listMembers();			continue;}
			if(command.compareTo("send")		==0)		{groupSending();		continue;}
			if(command.compareTo("broadcast")	==0)		{broadcast();		continue;}
			if(command.compareTo("quit")		==0)		break;
				
			System.out.println("invalid command!");
		}
	}

	
	public void execute() throws IOException, InterruptedException {
		commandLineReanderMainLoop();
	}
}
