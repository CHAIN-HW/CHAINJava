package chain.core;

import java.util.*;

import edu.mit.jwi.IDictionary;

/* Author Diana Bental
 * Date November 2017
 * Modified
 */

public class NarrowDown {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static String narrowDown(String queryHead, String targetSchemas) {
		String narrowedTargets = "";
		
		// Split the query head into a list of words
		// e.g. waterBodyMeasures to [water, Body, Measures]
		// System.out.println("QueryHead:" + queryHead) ;
		ArrayList<String> queryWords = StringParser.splitCamelCase(queryHead) ;
		// System.out.println("Query words:" + queryWords) ;
		
		// Use WordNet to build  a set of words (or terms) associated with any of the words in the query head
		Set<String> associatedWords = new HashSet<String>() ;
		try {IDictionary dict = JWICaller.openDictionary () ;
			// One word at a time from the query head
			for (String qWord : queryWords) {
				// System.out.println("qWord:"+qWord) ;
				associatedWords = JWICaller.getAssociatedWords(dict, qWord, associatedWords) ;
			}
			dict.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// System.out.println("Associated words " + associatedWords) ;
		
		// For each target schema in turn - do any of the words in the head match any of the associaed words?
		// If so, then add the whole target schema to the list of narrowed schemas.
		ArrayList<String> narrowedList = new ArrayList <String> () ;
		String[] targetList = targetSchemas.split(";");
		for(String target:targetList) {
			String targetHead = target.split("\\(")[0];
			// System.out.println("Target head " + targetHead) ;
			Set <String> targetWords = new HashSet <String> (StringParser.splitCamelCaseLC(targetHead)) ;
			// System.out.println("Target words " + targetWords) ;
			boolean matches = false ;
			targetWords.retainAll(associatedWords) ;
			// System.out.println("Overlapping words " + targetWords) ;
			if(!targetWords.isEmpty()) {
				narrowedList.add(target) ;			
			}
		}
		
		// Return to the same format as the original, i.e. a semi-colon separated string of schemas
		narrowedTargets = String.join(";", narrowedList) ;
		// System.out.println("Narrowed Targets " + narrowedTargets) ;
		
		return narrowedTargets;
		
	}

}
