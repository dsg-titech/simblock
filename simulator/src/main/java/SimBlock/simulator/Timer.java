/**
 * Copyright 2019 Distributed Systems Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package SimBlock.simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import SimBlock.task.Task;


public class Timer {
	
	private static PriorityQueue<ScheduledTask> taskQueue = new PriorityQueue<ScheduledTask>();
	private static Map<Task,ScheduledTask> taskMap = new HashMap<Task,ScheduledTask>();
	private static long currentTime = 0L;
	
	private static class ScheduledTask implements Comparable<ScheduledTask> {
		private final Task task;
		private final long scheduledTime;
		
		private ScheduledTask(Task task, long scheduledTime){
			this.task = task;
			this.scheduledTime = scheduledTime;
		}
		
		private Task getTask(){ return this.task; }
		private long getScheduledTime(){ return this.scheduledTime; }
		
		public int compareTo(ScheduledTask o) {
			if(this.equals(o)) return 0;
			int order = Long.signum(this.scheduledTime - o.scheduledTime);
			if(order != 0) return order;
			order = System.identityHashCode(this) - System.identityHashCode(o);
			return order;
		}
	}
	
	public static void runTask(){
		if(taskQueue.size() > 0){
			ScheduledTask currentScheduledTask = taskQueue.poll();
			Task currentTask = currentScheduledTask.getTask();
			currentTime = currentScheduledTask.getScheduledTime();
			taskMap.remove(currentTask, currentScheduledTask);
			currentTask.run();
		}
	}

	public static void removeTask(Task task){
		if(taskMap.containsKey(task)){
			ScheduledTask stask = taskMap.get(task);
			taskQueue.remove(stask);
			taskMap.remove(task, stask);
		}
	}
	
	public static Task getTask(){
		if(taskQueue.size() > 0){
			ScheduledTask currentTask = taskQueue.peek();
			return currentTask.getTask();
		}else{
			return null;
		}
	}
	
	public static void putTask(Task task){
		ScheduledTask stask = new ScheduledTask(task, currentTime + task.getInterval());
		taskMap.put(task,stask);
		taskQueue.add(stask);
	}
	
	public static void putTaskAbsoluteTime(Task task,long time){
		ScheduledTask stask = new ScheduledTask(task, time);
		taskMap.put(task,stask);
		taskQueue.add(stask);
	}
	
	public static long getCurrentTime(){return currentTime;}
}
