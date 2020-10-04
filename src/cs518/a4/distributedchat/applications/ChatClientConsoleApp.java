package cs518.a4.distributedchat.applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import javax.swing.UnsupportedLookAndFeelException;

import cs518.a4.distributedchat.core.ChatClient;
import cs518.a4.distributedchat.gui.GUIFacade;
import cs518.a4.distributedchat.gui.MainGUI;
import cs518.a4.distributedchat.threadpool.ThreadPoolManager;

public class ChatClientConsoleApp {
	private ChatClient chatClient;
	private GUIFacade model;

	
	public void startup(String chatClientID, int port,String serverHost, int serverPort) throws IOException, UnsupportedLookAndFeelException, IllegalAccessException, InstantiationException {
		chatClient 		= new ChatClientImp(chatClientID,port,serverHost, serverPort);
		model 			= new GUIFacade(new GUIChatClientImp(chatClient));
		ThreadPoolManager tpm 	= new ThreadPoolManager(1);
		tpm.start();
		tpm.addTask(new CommandReader(model));
		if (!chatClient.run(Setting.CLIENT_THREADPOOL_SIZE))
			System.exit(0);
	}
	
	

	
	public static void main(String args[]) throws IOException, UnsupportedLookAndFeelException, IllegalAccessException, InstantiationException {
		
		if (args.length < 4) {
			System.err.println("Discovery Node:  Usage:");
			System.err.println("         java cs518.a1.distributedchat.core.ChatClientApp PORT-NUM ID SERVER-HOST SERVER-PORT");
		    return;
		}
		int 	port		= Integer.parseInt(args[0]);
		String 	serverHost 	= args[2];
		int 	serverPort	= Integer.parseInt(args[3]);
		ChatClientConsoleApp chatClientApp = new ChatClientConsoleApp();
		chatClientApp.startup(args[1], port, serverHost, serverPort);
	}
}
