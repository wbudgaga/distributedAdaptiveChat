package cs518.a4.distributedchat.threadpool;

import java.io.IOException;


// Thread that execute tasks 
public class Worker implements Runnable{
	private ThreadPoolManager 	theManager;
	private Thread	 			workerThread;
	private Task	 			job 			= null;
	
	public Worker(ThreadPoolManager manager, int workerID){
		theManager 		= manager;
		workerThread 	= new Thread(this,"workerThread-"+workerID);
		
	}
	
	protected String getName(){
		return workerThread.getName();
	}
	protected void stop(){
		workerThread.interrupt();
	}

	protected void start(){
		workerThread.start();
	}

	protected synchronized void assignTask(Task newTask){
		this.job = newTask;
		notifyAll();
	}

	private void processJob() throws IOException, InterruptedException{
		job.execute();
		job = null;
		theManager.workerFinished(this);
	}
	
	private boolean hasJob(){
		if (job == null)
			return false;
		return true;
	}
	
	@Override
	public void run() {
		while(true){
			synchronized(this){
				
				try {
					if (!hasJob())
						wait();
					if (hasJob()) 
						processJob();
				} catch (InterruptedException | IOException e) {	return;}
			}
		}
	
	}

}
