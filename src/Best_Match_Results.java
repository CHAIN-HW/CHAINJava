import java.util.*;

/*
 * Responsible for taking results after calling SPSM
 * and then filtering to ensure each result is over the 
 * determined threshold, cut the number of results to the
 * desired n and sort so that the match with the highest
 * similarity is at the top of the list
 * 
 * This class is tested in Match_Results_Test_Cases.java & SPSM_Filter_Results_Test_Cases.java
 */
public class Best_Match_Results {
	
	//main method used only for testing purposes
	//allows for SPSM to be called with arguments from console
	public static void main(String[] args){
		Best_Match_Results instance = new Best_Match_Results();
		
		ArrayList<Match_Struc> results = new ArrayList<Match_Struc>();
		results.add(new Match_Struc(1.0,"author(name,document)"));
		results.add(new Match_Struc(0.1,"author(name,document)"));
		results.add(new Match_Struc(0.0,"author(name,document)"));
		results.add(new Match_Struc(0.2,"author(name,document)"));
		results.add(new Match_Struc(0.7,"author(name,document)"));
		results.add(new Match_Struc(0.3,"author(name,document)"));
		results.add(new Match_Struc(0.8,"author(name,document)"));
		results.add(new Match_Struc(0.6,"author(name,document)"));
		results.add(new Match_Struc(0.9,"author(name,document)"));
		results.add(new Match_Struc(0.4,"author(name,document)"));
		results.add(new Match_Struc(0.5,"author(name,document)"));
		
		results = instance.getThresholdAndFilter(results,0.3,0);
		
		System.out.println("\nResults after filtering:");
		for(int i = 0 ; i < results.size() ; i++){
			Match_Struc current = results.get(i);
			System.out.println(current.getDatasetSchema() + " similarity: " + current.getSimValue());
		}
	}
	
	//start off by taking in the results, threshold value and the number of results wanted
	//if these values haven't been passed in through params, then ask user to enter in console
	public ArrayList<Match_Struc> getThresholdAndFilter(ArrayList<Match_Struc> results, Double threshVal, int limNum){
		System.out.println("Filtering results from SPSM");
		
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
		ArrayList<Match_Struc> filteredList = new ArrayList<Match_Struc>();
		for(int i = 0 ; i < results.size() ; i++){
			Match_Struc currMatch = results.get(i);
			double currSim = currMatch.getSimValue();
			
			if(currSim >= thresholdVal){
				filteredList.add(currMatch);
			}
		}

		return sortResultingMatches(filteredList,limitNo);
	}
	
	//this method will sort the resulting matches after they have been filtered
	//to ensure that only those over or equal to the threshold value are left
	public ArrayList<Match_Struc> sortResultingMatches(ArrayList<Match_Struc> filteredRes,int limitNo){
		Collections.sort(filteredRes, new Comparator<Match_Struc>(){
			@Override
			public int compare(Match_Struc m1, Match_Struc m2){
				return Double.compare(m2.getSimValue(), m1.getSimValue());
			}
		});
		
		//after sorting, cut the size of the results to be the limit
		//that the user has entered at the start
		if(limitNo != 0 && filteredRes.size() >= limitNo){
			filteredRes = new ArrayList<Match_Struc>(filteredRes.subList(0, limitNo));
		}
		
		return filteredRes;
	}
	
	//used for testing to display and check what is being returned
	public void displayResults(ArrayList<Match_Struc> res){
		if(res.size() == 0){
			System.out.println("\nNo matches over this threshold value!");
		}else{
			System.out.println("\nSuggested match similarity values:\n");
			
			for(int i = 0 ; i < res.size() ; i++){
				double currSim = res.get(i).getSimValue();
				System.out.println(currSim);
			}
		}
	}
}
