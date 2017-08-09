import java.util.ArrayList;

/*
 * 
 * Responsible for taking in ArrayList of Match_Struc objects
 * and going through each one and creating a repaired schema
 * based on the relation between the source and target schemas
 * 
 * This class is tested in Repair_Schema_Test_Cases.java
 * 
 */
public class Repair_Schema {
	
	//main method used for testing purposes only
	//this method can be run with the source and target schemas outlined
	//below and the repaired schema will be produced
	public static void main(String[] args){
		Call_SPSM spsmCall;
		Repair_Schema queryCreator;
		ArrayList<Match_Struc> finalRes;
		String target, source;
		
		spsmCall = new Call_SPSM();
		queryCreator = new Repair_Schema();
	
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		
		finalRes = new ArrayList<Match_Struc>();
		
		finalRes = spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes != null && finalRes.size() != 0){
			finalRes = queryCreator.prepare(finalRes);	
			
			//then check what we have stored as our repaired schemas
			//testing purposes only!!
			System.out.println("\nSource: "+source);
			System.out.println("Target: "+target);
			
			System.out.println("\nFinal Repaired Schema(s): ");
			for(int i = 0 ; i < finalRes.size() ; i++){
				System.out.println(finalRes.get(i).getRepairedSchema());
			}
		}
	}
	
	//take in the ArrayList of objects and start processing the 
	//repaired schema for each one in the list
	public ArrayList<Match_Struc> prepare(ArrayList<Match_Struc> matchRes){
		System.out.println("Repairing Target Schemas");
		
		//for each of the targets, create a tree and create repaired schema
		//and store that into the match structure
		for(int i = 0 ; i < matchRes.size() ; i++){
			
			//current match
			Match_Struc currMatch = matchRes.get(i);
			currMatch = repairSchema(currMatch);
		}
		
		return matchRes;
	}
	
	//creates a tree structure of the schema and adds that as
	//a field to the Match_Struc object based on the relation
	//between source and target schemas
	public Match_Struc repairSchema(Match_Struc matchStructure){
		
		ArrayList<String[]> matchDetails = matchStructure.getMatches();
		
		//start off by going through the list of match details
		//creating/adding to a tree for that target schema
		Node targetRoot=null;
		
		for(int i = 0 ; i < matchDetails.size() ; i++){
				
			String[] currMatchDetails = matchDetails.get(i);
			String targetConcept = currMatchDetails[2];
			
			//System.out.println("Modifying tree for " + targetConcept);
			targetRoot = modifyRepairedTree(targetRoot, targetConcept);
		}
		
		//after we have been through all details, we add to structure
		matchStructure.setRepairedSchemaTree(targetRoot);
		matchStructure.setRepairedSchema(targetRoot.printTree());
		
		//return the updated match structure
		return matchStructure;
	}
	
	//using relation concept data, modify the tree to make sure
	//that there are only links to parts of the schema
	//that we think there is a match and return this
	public Node modifyRepairedTree(Node root, String concept){
		
		//split the concept string
		//so that we have the different parents/children
		String[] conceptParts = concept.split(",");
		
		if(root == null){
			//if the root is null, then our tree is empty
			//add the root as the first element in the conceptParts
			root = new Node(conceptParts[0]);
		}
		
		//keep a note of who the parent is
		//since we will be working our way down the list
		Node parent = root;
		 
		//make sure we have more than one element in the list
		if(conceptParts.length > 1){
			
			//then we can skip the first element in the array
			//as it will always be the name of the predicate
			//i.e. our root!	
			for(int i = 1 ; i < conceptParts.length ; i++){
				
				Node currNode = new Node(conceptParts[i]);
				
				//for each of these, we want to check if this child exists
				//by looking at its parents list of children
				
				ArrayList<String> childrenVals = parent.getChildrenValues();
				
				if(!(childrenVals.contains(currNode.getValue()))){
					//doesn't yet exist, add to list of children
					//and set node to be new parent
					parent.addChild(currNode);
					parent = currNode;
				}else{
					//this is already in our structure as a child
					//so we set it to be the parent
					int newParentIndex = childrenVals.indexOf(currNode.getValue());
					Node tmpCurr = parent.getChildren().get(newParentIndex);
					
					parent = tmpCurr;
				}
			}
		}
		
		//return the modified tree
		return root;
	}	
}
