package chain.core;

import java.io.*;
import java.util.*;

/* Author Diana Bental
 * Date November 2017
 * Modified
 */

public class StringParser {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList <String> parsed = splitCamelCase("camelValue") ;
		parsed = splitCamelCase("TitleValue") ;
		parsed = splitCamelCase("TitleVALUE") ;
		parsed = splitCamelCase("CAPITALS") ;
		parsed = splitCamelCase("lowercase") ;
		parsed = splitCamelCase("EclipseRCPExt") ;
		parsed = splitCamelCase("EclipseRCPExt01") ;
		parsed = splitCamelCase("Eclipse01RCPExt") ;
		
		parsed = splitCamelCaseLC("TitleValue") ;
		parsed = splitCamelCaseLC("TitleVALUE") ;
		parsed = splitCamelCaseLC("CAPITALS") ;
		parsed = splitCamelCaseLC("lowercase") ;
		parsed = splitCamelCaseLC("EclipseRCPExt") ;
		parsed = splitCamelCaseLC("EclipseRCPExt01") ;
		parsed = splitCamelCaseLC("Eclipse01RCPExt") ;
		
		parsed = splitSeparators("Split Title_Value") ;
		parsed = splitSeparators("Title VALUE") ;
		parsed = splitSeparators("CAPITALS") ;
		parsed = splitSeparators("lower__case") ;
		parsed = splitSeparators("Eclipse_RCP  Ext") ;
		parsed = splitSeparators("__Eclipse__RCPExt01__") ;
		parsed = splitSeparators("_Eclipse_RCP_Ext_01__") ;
		parsed = splitSeparators("Eclipse _01 RCP  Ext") ;

	}
	
	// Parse a single camel case string into an arraylist of separate words.
	// Doesn't need pure camel-case - can also split TitleCase and words in ALCAPS (which are treated as single words)
	
	public static ArrayList <String> splitCamelCase(String camelCase)
	{
		ArrayList <String> parsed = new ArrayList <String> () ;
		// System.out.println("Original:" + camelCase) ;
	    for (String w : camelCase.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
	        parsed.add(w) ;
	    	// System.out.println(w);
	    }
	    return parsed ;
	}  
	
	// Parse a single camel case string into an arraylist of separate words.
	// Doesn't need pure camel-case - can also split TitleCase and words in ALCAPS (which are treated as single words)
	// Returns results in all lower case (for WordNet)
	
	public static ArrayList <String> splitCamelCaseLC(String camelCase)
	{
		ArrayList <String> parsed = new ArrayList <String> () ;
		// System.out.println("Original:" + camelCase) ;
	    for (String w : camelCase.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
	        parsed.add(w.toLowerCase()) ;
	    	// System.out.println(w);
	    }
	    return parsed ;
	}  
	
	// Split words separated by charavters which are not letters or numbers
	// Multiple separators are stripped out
	public static ArrayList <String> splitSeparators(String separatedWords)
	{
		ArrayList <String> parsed = new ArrayList <String> () ;
		// System.out.println("Original:" + separatedWords) ;
		// Deal with any non-word character and also underscore (which is a Java word character)
		String[] words = separatedWords.split("[\\W_]+") ;
		for (String w:words) {
			if(!w.equals("")) {
				parsed.add(w) ;
		    	// System.out.println(w);
			}
			
		}
	    return parsed ;
	}  


}
