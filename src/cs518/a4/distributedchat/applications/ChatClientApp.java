package cs518.a4.distributedchat.applications;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.UnsupportedLookAndFeelException;

import cs518.a4.distributedchat.core.ChatClient;
import cs518.a4.distributedchat.gui.GUIFacade;
import cs518.a4.distributedchat.gui.MainGUI;

public class ChatClientApp {
	private ChatClient chatClient;
	private MainGUI mainGUI;
	private GUIFacade model;
	
	public void startup(String chatClientID, int port,String serverHost, int serverPort) throws IOException, UnsupportedLookAndFeelException, IllegalAccessException, InstantiationException {
		chatClient 			= new ChatClientImp(chatClientID,port,serverHost, serverPort);
		model 				= new GUIFacade(new GUIChatClientImp(chatClient));
		createMainGUI(model);
		
		if (!chatClient.run(Setting.CLIENT_THREADPOOL_SIZE))
			System.exit(0);
	}
	
	private void createMainGUI(GUIFacade facade) throws UnsupportedLookAndFeelException, IllegalAccessException, InstantiationException{		
	      try {
	            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
	                if ("Nimbus".equals(info.getName())) {
	                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
	                    break;
	                }
	            }
	        } catch (ClassNotFoundException ex) {
	            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	        } 
	      
   		java.awt.EventQueue.invokeLater(new Runnable() {

        		public void run() {
				mainGUI 	= new MainGUI(model);
				mainGUI.setVisible(true);
				chatClient.setMainGUI(mainGUI);
        		}
    		});

	}
	
	public static void main(String args[]) throws IOException, UnsupportedLookAndFeelException, IllegalAccessException, InstantiationException {
		
		if (args.length < 4) {
			System.err.println("Discovery Node:  Usage:");
			System.err.println("         java cs518.a1.distributedchat.core.ChatClientApp PORT-NUM ID SERVER-HOST SERVER-PORT");
		    return;
		}
		int 	port			= Integer.parseInt(args[0]);
		String 	serverHost 		= args[2];
		int 	serverPort		= Integer.parseInt(args[3]);
		ChatClientApp chatClientApp 	= new ChatClientApp();
		chatClientApp.startup(args[1], port, serverHost, serverPort);
	}
}
