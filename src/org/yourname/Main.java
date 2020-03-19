package org.yourname;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class Main {

	public static void main(String[] args) throws IOException {
		// add your code here
		
		List<String> inputTypeList;
		List<String> operationList;
		List<String> resultText = new ArrayList<>();
		
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
	     String threadNumbers = removePointyBrackets(cmd.getOptionValue("threads"));
	     String outputArg = removePointyBrackets(cmd.getOptionValue("output"));

	     System.out.println(inputFilePath);
	     System.out.println(inputTypeList.toString());
	     System.out.println(operationList.toString());
	     System.out.println(threadNumbers);
	     System.out.println(outputArg);

	     
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
	     
	     resultText = readLineByLineText(inputFilePath,operationList);
	     System.out.println(resultText);
	     
			
		
		
		
		
		
		
		// DO NOT CHANGE THE FOLLOWING LINES OF CODE
		System.out.println(String.format("Processed %d lines (%d of which were unique)", //
				Statistics.getInstance().getNoOfLinesRead(), //
				Statistics.getInstance().getNoOfUniqueLines()));
				


		
	
	
	
	
	
	}

	
	public static String removePointyBrackets(String arg)
	{
		return arg.substring(1, arg.length() - 1);
	}
	
	public static List<String> readLineByLineText(String inputPath, List<String>operationList)
	{
		String outLine = null;
		List<String> outText = new ArrayList<>();
		List<Method> operationCalls = new ArrayList<>();
		Operations operationsInstance = new Operations(); 
		

		
			
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
		
		
		
		try  
		{  
			File file=new File(inputPath);    //creates a new file instance  
			FileReader fr=new FileReader(file);   //reads the file  
			BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream    
			String line;  
			while((line=br.readLine())!=null)  
			{  
				Statistics.getInstance().updateStatisticsWithLine(line);
				int i = 0;
				outLine= line;
				while (i < operationCalls.size() )
				{
					
					try {
						String lineFromOperation = (String) operationCalls.get(i).invoke(operationsInstance,outLine);
						if(lineFromOperation != line )
						{
							outLine = lineFromOperation;
						}
						
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
					i++;
				}
				outText.add(outLine);

			}  
			fr.close();    //closes the stream and release the resources  
			
		}  
		catch(IOException e)  
		{  
			e.printStackTrace();  
		}  
		
		return outText;
	}
	
	
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
	
	
	
	
	
}
