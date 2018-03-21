package chain.sparql;

import java.util.*;

/* Author Tanya Howden
 * Date September 2017
 * Modified Diana Bental November 2017
 */


/*
 * Responsible for taking results after calling SPSM
 * and then filtering to ensure each result is over the 
 * determined threshold, cut the number of results to the
 * desired n and sort so that the match with the highest
 * similarity is at the top of the list
 * 
 * This class is tested in Match_Results_Test_Cases.java & SPSM_Filter_Results_Test_Cases.java
 */
public class BestMatchResults {
	
	//main method used only for testing purposes
	//allows for SPSM to be called with arguments from console
	public static void main(String[] args){
		BestMatchResults instance = new BestMatchResults();
		
		ArrayList<MatchStruc> results = new ArrayList<MatchStruc>();
		results.add(new MatchStruc(1.0,"author(name,document)"));
		results.add(new MatchStruc(0.1,"author(name,document)"));
		results.add(new MatchStruc(0.0,"author(name,document)"));
		results.add(new MatchStruc(0.2,"author(name,document)"));
		results.add(new MatchStruc(0.7,"author(name,document)"));
		results.add(new MatchStruc(0.3,"author(name,document)"));
		results.add(new MatchStruc(0.8,"author(name,document)"));
		results.add(new MatchStruc(0.6,"author(name,document)"));
		results.add(new MatchStruc(0.9,"author(name,document)"));
		results.add(new MatchStruc(0.4,"author(name,document)"));
		results.add(new MatchStruc(0.5,"author(name,document)"));
		
		results = instance.getThresholdAndFilter(results,0.3,0);
		
		System.out.println("\nResults after filtering:");
		for(int i = 0 ; i < results.size() ; i++){
			MatchStruc current = results.get(i);
			System.out.println(current.getDatasetSchema() + " similarity: " + current.getSimValue());
		}
	}
	
	//start off by taking in the results, threshold value and the number of results wanted
	//if these values haven't been passed in through params, then ask user to enter in console
	public ArrayList<MatchStruc> getThresholdAndFilter(ArrayList<MatchStruc> results, Double threshVal, int limNum){
		System.out.println("\nFiltering results from SPSM");
		
		Double thresholdVal;
		int limitNo;
		
		//if params haven't been entered, ask user input
		if(threshVal == null && limNum == 0){
			Scanner reader = new Scanner(System.in);
			
			System.out.println("Please enter a threshold value between 0.0 and 1.0");
			thresholdVal = (double) reader.nextDouble();
			
			System.out.println("If you would like to limit the number of results then please enter the limit, if not enter '0'");
			limitNo = reader.nextInt();
			
			reader.close();
		}else{
			thresholdVal = threshVal;
			limitNo = limNum;
		}
		
		//strip out any results that are lower than the threshold value
		ArrayList<MatchStruc> filteredList = new ArrayList<MatchStruc>();
		for(int i = 0 ; i < results.size() ; i++){
			MatchStruc currMatch = results.get(i);
			double currSim = currMatch.getSimValue();
			
			if(currSim >= thresholdVal){
				filteredList.add(currMatch);
			}
		}
		
		displayResults(filteredList) ; //DB

		return sortResultingMatches(filteredList,limitNo);
	}
	
	//this method will sort the resulting matches after they have been filtered
	//to ensure that only those over or equal to the threshold value are left
	public ArrayList<MatchStruc> sortResultingMatches(ArrayList<MatchStruc> filteredRes, int limitNo){
		Collections.sort(filteredRes, new Comparator<MatchStruc>(){
			@Override
			public int compare(MatchStruc m1, MatchStruc m2){
				return Double.compare(m2.getSimValue(), m1.getSimValue());
			}
		});
		
		//after sorting, cut the size of the results to be the limit
		//that the user has entered at the start
		if(limitNo != 0 && filteredRes.size() >= limitNo){
			filteredRes = new ArrayList<MatchStruc>(filteredRes.subList(0, limitNo));
		}
		
		return filteredRes;
	}
	
	//used for testing to display and check what is being returned
	public void displayResults(ArrayList<MatchStruc> res){
		if(res.size() == 0){
			System.out.println("\nNo matches over this threshold value!");
		}else{
			System.out.println("\nSuggested match similarity values:");
			
			for(int i = 0 ; i < res.size() ; i++){
				double currSim = res.get(i).getSimValue();
				String currSchema = res.get(i).getDatasetSchema();
				System.out.println("Target: " + currSchema + " " + currSim);
			}
		}
	}
}
