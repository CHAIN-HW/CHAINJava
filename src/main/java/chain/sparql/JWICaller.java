package chain.sparql;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.morph.WordnetStemmer;

/* Author Diana Bental
 * Date November 2017
 * Modified
 */

// Calls the JWI Java WordNet Interface
// On a single word
// And returns an arraylist of all the associated words for every word in the term

public class JWICaller {

	public static void main(String[] args) {
		
		ArrayList<String> words = new ArrayList<String>() ;	
		Set <String> associatedWords = new HashSet <String>() ;
		try {IDictionary dict = openDictionary () ; 
		
		words = StringParser.splitCamelCase("waterBodyMeasures") ;
		associatedWords.clear() ;
		for(String word: words) {
			associatedWords = JWICaller.getAssociatedWords(dict, word, associatedWords) ;
		}
		System.out.println(associatedWords) ;
		System.out.println(associatedWords.size()) ; //559
		
		words = StringParser.splitCamelCase("waterBodyPressures") ;	
		associatedWords.clear()  ;		
		for(String word: words) {
			associatedWords = JWICaller.getAssociatedWords(dict, word, associatedWords) ;
		}
		System.out.println(associatedWords) ;
		System.out.println(associatedWords.size()) ;  //428
		
		words = StringParser.splitCamelCase("conferenceDocument") ;	
		associatedWords.clear()  ;
		for(String word: words) {
			associatedWords = JWICaller.getAssociatedWords(dict, word, associatedWords) ;
		}
		System.out.println(associatedWords) ;
		System.out.println(associatedWords.size()) ; //122
		
		words = StringParser.splitCamelCase("conferenceReview") ;	
		associatedWords.clear()  ;
		for(String word: words) {
			associatedWords = JWICaller.getAssociatedWords(dict, word, associatedWords) ;
		}
		System.out.println(associatedWords) ;
		System.out.println(associatedWords.size()) ; //129
		
		
		dict.close();
	}catch(Exception e){
		e.printStackTrace();
	}
		
	}
	
	public static IDictionary openDictionary () throws IOException {
		// construct the URL to the Wordnet dictionary directory
		// String wnhome = System . getenv ("WORDNET_HOME");
		String wnhome = "./Wordnet/wn3.1.dict";
		String path = wnhome + File . separator + "dict";
		URL url = new URL("file", null , path );
		// System.out.println(url);
		
		// construct the dictionary object and open it
		IDictionary dict = new Dictionary (url);
		dict . open ();
		
		return(dict) ;
	}
	
public static Set <String> getAssociatedWords ( IDictionary dict, String wordStr,  Set<String> associatedWords) {
		
		// ArrayList <String> allWords = new ArrayList<String>();
			
		// First stem the word
		WordnetStemmer wordNetStems = new WordnetStemmer(dict) ;
		List<String> stems = new ArrayList<String>();
		stems = wordNetStems.findStems(wordStr, null) ;
		// System.out.println("Stems " + stems) ;
			
		
		// Then get associated words
		for(String stem : stems) {
			associatedWords = getRelatedWords(dict, stem, associatedWords) ;
		}
		
		return associatedWords;
		
	}
	
	// Get all lexically related words, synonyms and synset-related words
	// Superset of relations used in original CHAIn
	// CHAIn originally only used:
	// 	sim - similarity - similar_to
	// 	hypernym and hyponym
	// 	ins - subclass and superclass
	// 	meronym - part-whole; inverse is holonym
		public static Set<String> getRelatedWords ( IDictionary dict, String wordStr, Set<String> associatedWords ) {

			
			for(POS p : POS.values()) {  // Each part of speech in turn	
				IIndexWord idxWord = dict . getIndexWord (wordStr, p );
				if(idxWord != null) {
					for (IWordID wordID : idxWord . getWordIDs ()) {  // each meaning of the word in turn
						IWord word = dict . getWord ( wordID );
						
						// Iterate over all the (lexically related) connection types, ignore antonym
						for(Pointer pointer : Pointer.values()) {
							if(!pointer.equals(Pointer.ANTONYM) ) {
								for (IWordID lexicalWordId : word.getRelatedWords(pointer)) {
									String lemma = dict.getWord(lexicalWordId).getLemma() ;
									associatedWords.add(lemma) ;
								}
							}
						}
						
						ISynset synset = word . getSynset ();

						// Get all the other words in the synset
						for( IWord w : synset . getWords ()) {
							// System.out.println(w.getLemma());
							String lemma = w.getLemma() ;
								associatedWords.add(lemma) ;
						}
						// Iterate over the synset relations - hypernyms, hyponyms, meronyms etc
						for (Pointer pointer : Pointer.values()) {
							// Get all the synsets associated by this relation
							List < ISynsetID > associatedSet =	
									synset . getRelatedSynsets ( pointer );
							// Add all the words in each associated synset for this relation
							ArrayList<String> associatedWordSet = buildWordList(dict, associatedSet) ;
							for (String associatedWord : associatedWordSet) {
									associatedWords.add(associatedWord) ;
							}
							
						}
						
					}
				}
			}
			return associatedWords ;
		}
	
		// Given a dictionary and a list of synset IDs,
		// return a combined list of all the words in all the synsets
		public static ArrayList<String> buildWordList(IDictionary dict, List < ISynsetID > synsetIDs) {
			// build the list of all the words in all the synsets
			List <IWord > words ;
			ArrayList<String> allWords = new ArrayList <String>(); 
			for( ISynsetID sid : synsetIDs ){
				words = dict . getSynset (sid). getWords ();
				for( Iterator <IWord > i = words . iterator (); i. hasNext () ;){
					String lemma = i. next (). getLemma () ;
					allWords.add(lemma);	//a lemma is a string for a single word or phrase	
				}
			}
			return(allWords) ;
		}
		
}
