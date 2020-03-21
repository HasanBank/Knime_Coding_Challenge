package org.yourname;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class TestClass {
	
	@Test
	public void testWithOutput()
	{

			String[] parameters = new String[10];
			parameters[0] = "--input";
			parameters[1] = "<demo.txt>";
			
			parameters[2] = "--inputtype";
			parameters[3] = "<int>";
			
			parameters[4] = "--operations";
			parameters[5] = "<neg,reverse>";
			
			parameters[6] = "--threads";
			parameters[7] = "<1>";
			
			parameters[8] = "--output";
			parameters[9] = "<Output.txt>";
	
			try {
				Main.main(parameters);

				File fileReal = new File("Output.txt");
				File fileExpected = new File("testExpectedResult.txt");
				boolean isTwoEqual = FileUtils.contentEquals(fileReal, fileExpected);

				if(isTwoEqual)
				{
					System.out.println("Test works as expected!");
				}
				else
				{
					System.err.println("Test fails");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}
}
