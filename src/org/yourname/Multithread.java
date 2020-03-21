/*
 * 
 */
package org.yourname;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class Multithread implements Runnable {
	
	public final static BlockingQueue<String> linesReadQueue = new ArrayBlockingQueue<String>(30);
	public final static BlockingQueue<String> linesDone = new ArrayBlockingQueue<String>(30);
	public final static ReentrantLock queueLock = new ReentrantLock();
	
	private Operations operationsInstance;
	private boolean isConsumer;
	private String inputPath;
	private static boolean producerIsDone = false;
	private List<Method> operationCalls;
	
	public Multithread(boolean consumer, List<Method> operationCalls) {
		this.isConsumer = consumer;
		this.operationCalls = operationCalls;
		operationsInstance = new Operations();
	}
	
	public Multithread(boolean consumer, String inputPath)
	{
		this.isConsumer = consumer;
		this.inputPath = inputPath;
		operationsInstance = new Operations();

	}


	/**
	 * Read file.
	 * 
	 * 1 Producer thread runs this function and put lines to queue.
	 */
	private void readFile()
	{
		Path file = Paths.get(inputPath);
		try
		{
			Stream<String> lines = Files.lines(file,StandardCharsets.UTF_8);
			
			for(String line: (Iterable<String>) lines::iterator)
			{
				Statistics.getInstance().updateStatisticsWithLine(line);
				linesReadQueue.put(line);
			}	
			lines.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		producerIsDone = true;
	}
	
	/**
	 * Consume.
	 * In order to keep consumed lines order, lock and unlock has been used.
	 * Threads use this method to consume lines which has been produced by producer thread and make related operations.
	 * 
	 */
	private  void consume() {		
		queueLock.lock();		
			while( !producerIsDone ||  ( producerIsDone && !linesReadQueue.isEmpty()  ) )
			{
				try{
					String lineToProcess;
					lineToProcess = linesReadQueue.take();
		
					int i = 0;
					String outLine= lineToProcess;
					while (i < operationCalls.size() )
					{
							String lineFromOperation = (String) operationCalls.get(i).invoke(operationsInstance,outLine);
							if(lineFromOperation != outLine )
							{
								outLine = lineFromOperation;
							}
						i++;
					}
					linesDone.put(outLine);
				}
				catch( InterruptedException | InvocationTargetException | IllegalAccessException e)
				{
					System.err.println("An exception occurs during operations!");
					
				}	
			}
		queueLock.unlock();
	}
	

	@Override
	public void run()
	{
			if(isConsumer)
			{
				consume();
			}
			else
			{
				readFile();
			}
			
	}

}
