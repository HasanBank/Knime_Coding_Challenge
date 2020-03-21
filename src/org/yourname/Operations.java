package org.yourname;

/**
 * The Class Operations.
 */
public class Operations {

	/**
	 * Capitalize.
	 *
	 * @param String arg : line of the text 
	 * @return String : modified or unmodified line
	 */
	public static String capitalize(String arg)
	{
		if(validtaeString(arg))
			return arg.toUpperCase();
		else
			return arg;
	}

	/**
	 * Reverse.
	 *
	 * Reverse function works in string and int input types.
	 * String input should have only letters or space to run the function.
	 *
	 * @param String arg: line of the text
	 * @return String : modified or unmodified line
	 */
	public static String reverse(String arg)
	{
		try{
			if ( Main.inputTypeList.contains("int") && Integer.valueOf(arg) != null  )
			{
				return reverseInteger(Integer.valueOf(arg));
			}
			else
			{
				if(validtaeString(arg))
					return reverseStringToString(arg);
				else
					return arg;
			}
			
		}
		catch(NullPointerException | NumberFormatException e)
		{
			if(validtaeString(arg))
				return reverseStringToString(arg);
			else
				return arg;
		}

	}
	
	private static String reverseStringToString(String arg)
	{
		String reverse = "";    
	    for(int i = arg.length() - 1; i >= 0; i--)
	    {
	            reverse = reverse + arg.charAt(i);
	    }    
	    return reverse;
	}
	
	
	/**
	 * Neg.
	 *
	 * Works in only double or integer input type.
	 * 
	 * If input type does not have one of them, program returns error message in the beginning 
	 * and this function will not be called.
	 * 
	 * If the input has letters, same line is returned.
	 * 
	 *
	 * @param String arg: line of the text
	 * @return String: modified or unmodified line
	 */
	public static String neg(String arg)
	{
		if(validtaeString(arg) != true)
		{	
			boolean intContains = false;
			try{
				if( Main.inputTypeList.contains("int") )
				{
					intContains = true;
					Integer valInteger = Integer.valueOf(arg);
					return String.valueOf(- valInteger); 
				}
				else
				{
					Double valDouble = Double.valueOf(arg);
					return String.valueOf(- valDouble);
				}
				
			}
			catch(NullPointerException | NumberFormatException e)
			{
					if(intContains == true)
					{
						if( Main.inputTypeList.contains("double") )
						{
							Double valDouble = Double.valueOf(arg);
							return String.valueOf(- valDouble);
						}
						else
						{
							return arg;
						}
					}
					else
					{
						return arg;
					}
				
			}
			
		}
		else
		{
			return arg;
		}

	}


	
	/**
	 * Validate string.
	 *
	 * True if the string has only letters and space
	 *
	 *
	 * @param String str
	 * @return boolean
	 */
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
	
	private static String reverseInteger(int number)
	{
		boolean isNegative = number < 0 ? true : false;
		if(isNegative)
		{
			number = number * -1;
		}
		int reverse = 0;
		int lastDigit = 0;
		
		while(number >= 1) {
			lastDigit = number % 10;
			reverse = reverse * 10 + lastDigit;
			number = number / 10;
		}
		
		return isNegative == true ? String.valueOf(reverse * -1) : String.valueOf(reverse);
				
	}
	
	
		

}
