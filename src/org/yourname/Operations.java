package org.yourname;

public class Operations {

	public static String capitalize(String arg)
	{
		if(validtaeString(arg))
			return arg.toUpperCase();
		else
			return arg;
	}
	
	public static String reverse(String arg)
	{
		try{
			if(validtaeString(arg) == true || Integer.valueOf(arg) != null )
			{
				String reverse = "";    
			    for(int i = arg.length() - 1; i >= 0; i--)
			    {
			            reverse = reverse + arg.charAt(i);
			    }    
			    return reverse;
			}
			else
			{
				return arg;
			}
			
		}
		catch(NullPointerException | NumberFormatException e)
		{
			return arg;
		}

	}
	
	
	public static String neg(String arg)
	{
		try{
		Integer valInteger = Integer.valueOf(arg);
		return String.valueOf(-valInteger);
		}
		catch(NullPointerException | NumberFormatException e)
		{
			try{
				Double valDouble = Double.valueOf(arg);
				return String.valueOf(- valDouble);
			}
			catch(NullPointerException | NumberFormatException e2)
			{
				return arg;
			}

		}
		
		
	}


	
	private static boolean validtaeString(String str) {
	      str = str.toLowerCase();
	      char[] charArray = str.toCharArray();
	      for (int i = 0; i < charArray.length; i++) {
	         char ch = charArray[i];
	         if (!( (ch >= 'a' && ch <= 'z') || ch ==' ' )) {
	            return false;
	         }
	      }
	      return true;
	   }
	
	
	
	
		

}
