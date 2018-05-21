package security.zw.com.securitycheck.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutor {
	
	public interface Task {
		Object proccess() throws Exception;
		void success(Object obj);
		void fail(Throwable e);
	}
	
	public static class SimpleTask implements Task{

		@Override
		public Object proccess() throws Exception {
			return null;
		}

		@Override
		public void success(Object obj) {

		}

		@Override
		public void fail(Throwable e) {

		}
		
	}
	private static TaskExecutor mInstance = null;
	private ExecutorService executor;
	public synchronized static TaskExecutor getInstance(){
		if(mInstance == null){
			mInstance = new TaskExecutor();
		}
		return mInstance;
	}
	private TaskExecutor(){
		executor = Executors.newSingleThreadExecutor();
	}
	/**
	 * Add task to wait for executing.<br />
	 * <b>Note: </b> Check return value to see whether task added successfully or not.
	 * @param task
	 */
	public boolean addTask(Task task){
		if(task == null){
			throw new NullPointerException("task must not be null");
		}
		boolean ret = false;
		if(!executor.isShutdown() 
				&& !executor.isTerminated()){
			executor.submit(new Worker(task));
			ret = true;
		}
		return ret;
	}
	/**
	 * Attempts to stop all actively executing tasks, halts the processing of waiting tasks, and returns a list of the tasks that were awaiting execution.
	 * @return	list of tasks that never commenced execution
	 * 
	 */
	public synchronized List<Task> shutdownAll(){
		List<Runnable> waitingRunnables = executor.shutdownNow();
		List<Task> waitingTask = null;
		if(null != waitingRunnables 
				&& !waitingRunnables.isEmpty()){
			final int size = waitingRunnables.size();
			waitingTask = new ArrayList<Task>();
			for(int i=0;i<size;i++){
				Runnable runnable = waitingRunnables.get(i);
				if(null != runnable && runnable instanceof Worker){
					waitingTask.add(((Worker)runnable).getTask());
				}
			}
		}
		return waitingTask;
	}
	private final class Worker implements Runnable {
		Task task = null;
		Worker(Task task){
			this.task = task;
		}
		Task getTask(){
			return task;
		}
		@Override
		public void run() {
			try{
				Object result = task.proccess();
				task.success(result);
			}catch (Exception e) {
				task.fail(e);
			}
		}
	}
}

