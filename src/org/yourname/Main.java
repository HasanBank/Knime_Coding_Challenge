package org.yourname;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Main class.
 * 
 * @author KNIME GmbH
 */
public class Main{
	
	public static List<String> inputTypeList;

	public static void main(String[] args) throws IOException {
				
		List<String> operationList;
		int threadNumbers;
		List<Method> operationCalls = new ArrayList<>();
		String outputArg = null;
		
	    Options options = new Options();

	    Option input = new Option("i","input", true, "input file path");
	    input.setRequired(true);
	    options.addOption(input);

	    Option inputType = new Option("it","inputtype", true, "input type");
	    inputType.setRequired(true);
	    options.addOption(inputType);
	        
	    Option operations = new Option("op","operations", true, "operations");
	    operations.setRequired(true);
	    options.addOption(operations);
	        
	    Option threads = new Option("t","threads", true, "threads");
	    threads.setRequired(true);
	    options.addOption(threads);
	        
	    Option output = new Option("o","output", true, "output");
	    output.setRequired(false);
	    options.addOption(output);
	        

	    CommandLineParser parser = new DefaultParser();
	    HelpFormatter formatter = new HelpFormatter();
	    CommandLine cmd = null;
	    
	     try {
	            cmd = parser.parse(options, args);
	     } catch (ParseException e) {
	            System.out.println(e.getMessage());
	            formatter.printHelp("utility-name", options);

	            System.exit(1);
	     }

	     String inputFilePath = removePointyBrackets(cmd.getOptionValue("input"));
	     inputTypeList = assignParametersToList(removePointyBrackets(cmd.getOptionValue("inputtype")));
	     operationList = assignParametersToList(removePointyBrackets(cmd.getOptionValue("operations")));
	     threadNumbers = Integer.valueOf(removePointyBrackets(cmd.getOptionValue("threads")));
	     if(cmd.getOptionValue("output") != null )
	     {
	    	 outputArg = removePointyBrackets(cmd.getOptionValue("output"));
	     }
	     
	     System.out.println("### Welcome ###");
	     System.out.println("Input File Name: " + inputFilePath);
	     System.out.println("Input Type(s): " + inputTypeList.toString());
	     System.out.println("Operation Type(s): " + operationList.toString());
	     System.out.println("Number of Threads: " + threadNumbers);
	     if(outputArg != null )
	    	 System.out.println("Output File Name: " + outputArg);


	     
	     //Operations
	     if(operationList.contains("capitalize") && !inputTypeList.contains("string")    )
		 {
				System.err.print("capitalize operation works with string input type!");
				return;	
		 }	     
	     
	     
	     if(operationList.contains("reverse") && !inputTypeList.contains("string") && !inputTypeList.contains("int") )
	     {
	    	 System.err.print("reverse operation works with string or int input type!");
	    	 return;
	     }
	     
	     if(operationList.contains("neg") && !inputTypeList.contains("int") && !inputTypeList.contains("double") )
	     {
	    	 System.err.print("neg operation works with int or double input type!");
	    	 return;
	     }
	     
		ExecutorService producerPool = Executors.newFixedThreadPool(1);
		producerPool.submit(new Multithread(false,inputFilePath));
				
		for(int i=0; i< operationList.size() ; i++)
		{
			Method method;
			try {
				method = Operations.class.getDeclaredMethod(operationList.get(i), String.class);
				operationCalls.add(method);
			} catch (NoSuchMethodException | SecurityException e) {	
				e.printStackTrace();
			}	
		}
		
						
		ExecutorService consumerPool = Executors.newFixedThreadPool(threadNumbers);
		for(int i =0; i < threadNumbers; i++ )
		{				
			consumerPool.submit(new Multithread(true,operationCalls));
		}
		
		producerPool.shutdown();
		consumerPool.shutdown();
		
		while(!producerPool.isTerminated() && !consumerPool.isTerminated())
		{}

		// DO NOT CHANGE THE FOLLOWING LINES OF CODE
		System.out.println(String.format("Processed %d lines (%d of which were unique)", //
				Statistics.getInstance().getNoOfLinesRead(), //
				Statistics.getInstance().getNoOfUniqueLines()));
				

		printResult(outputArg);

	}

	
	/**
	 * Removes the pointy brackets.
	 * 
	 * Pointy brackets are used in arguments so this function works to remove them to get data in arguments.
	 *
	 * @param String arg: the line of the file
	 * @return string : line without pointy brackets
	 */
	public static String removePointyBrackets(String arg)
	{
		return arg.substring(1, arg.length() - 1);
	}
	
	
	
	/**
	 * Assign parameters to list.
	 * 
	 * To iterate or check some values into arguments, lists are created.
	 *
	 * @param String arg: the value in arguments
	 * @return List<String> : argument values in list 
	 */
	public static List<String> assignParametersToList(String arg)
	{
		List<String> listArgs;
		
		if( arg.contains(","))
		{
			listArgs = Arrays.asList(arg.split(","));
		}
		else
		{
			listArgs = Arrays.asList(arg);
		}
		
		return listArgs;
		
	}
	

	/**
	 * Prints the result.
	 *
	 * @param String outputArg: outputpath to write result
	 */
	public static void printResult(String outputArg)
	{
		
		System.out.println("#Results#");
		for( String line :Multithread.linesDone )
		{
				System.out.println(line);
		}
		
		if(outputArg != null)
		{
			try {
				Path fileOut = Paths.get(outputArg);
	            Files.write(fileOut, Multithread.linesDone);
	            System.out.println("Results also have been written to the " + outputArg);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
		}
	}
		
	
}
