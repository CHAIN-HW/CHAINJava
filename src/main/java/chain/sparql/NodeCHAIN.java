package chain.sparql;
import java.util.*;

/* Author Tanya Howden
 * Date September 2017
 * Modified
 */

/*
 * 
 * Structure that is being used to store the
 * repaired schema tree
 * 
 * This structure will have a node that has a value
 * and also a list of children nodes
 * 
 */
public class NodeCHAIN {
	//FIELDS
	private String value;
	private List<NodeCHAIN> children = null;
	
	//CONSTRUCTOR
	public NodeCHAIN(String val){
		value = val;
		children = new ArrayList<>();
	}
	
	//PUBLIC METHODS: GETTER & SETTER
	
	//adds a child node
	public void addChild(NodeCHAIN child){
		children.add(child);
	}
	
	//returns the value of the node
	public String getValue(){
		return value;
	}
	
	//returns the list of children nodes
	public List<NodeCHAIN> getChildren(){
		return children;
	}
	
	//returns a list containing all of the childrens values
	public ArrayList<String> getChildrenValues(){
		ArrayList<String> childrenValues = new ArrayList<String>();
		
		for(int i = 0 ; i < children.size() ; i++){
			childrenValues.add(children.get(i).getValue());
		}
		
		return childrenValues;
	}
	
	//returns whether or not the node has children
	public boolean hasChildren(){
		return children.size() > 0;
	}
	
	//returns the number of children
	public int getNumChildren(){
		return children.size();
	}
	
	//print out the tree as a repaired schema
	public String printTree(){
		String stringTree="";
		
		if(children.size()>0){
			stringTree = stringTree+value+"(";
			
			stringTree = stringTree + printChildren(children);
			
			stringTree = stringTree+")";
		}else{
			stringTree=stringTree+value;
		}
		
		
		//System.out.print(")");
		
		return stringTree;
	}
	
	//prints out the children for the repaired schema
	public String printChildren(List<NodeCHAIN> childrenList){
		String childrenString="";
		
		for(int i = 0 ; i < childrenList.size() ; i++){
			
			NodeCHAIN currNode = childrenList.get(i);
			
			childrenString = childrenString + currNode.getValue();
			//System.out.print(currNode.getValue());
			
			if(currNode.hasChildren()){
				childrenString = childrenString + "(";
				//System.out.print("(");
				childrenString = childrenString + printChildren(currNode.getChildren());
				
				childrenString = childrenString + ")";
				//System.out.print(")");
			}
			
			if(i != childrenList.size()-1){
				childrenString = childrenString + ",";
			}
		}
		
		return childrenString;
	}
	
	//add the parameters correctly into the current tree
	public void addToTree(String[] paramParts){
		
		for(int i = 0 ; i < paramParts.length ; i++){
			String currParam = paramParts[i];
			
			//first check it isn't equal to root
			if(value.equals(currParam)){
				continue;
			}
			
			//then check if it is in the list of children
			NodeCHAIN tmpNode = new NodeCHAIN(currParam);
			if(children.contains(tmpNode)){
				continue;
			}
			
			//otherwise the parameter before the current one
			//is the parent so we can add it on
			int indexOfParent = children.indexOf(paramParts[i-1]);
			NodeCHAIN tmpParent = children.get(indexOfParent);
			tmpParent.addChild(new NodeCHAIN(currParam));
		}
		
	}
	
	public String toString() {
		String nodeString = "" ;
		nodeString += getValue() ;
		
		List<NodeCHAIN> children = getChildren() ;
		
		if(children == null || children.isEmpty()) {
			return nodeString ;						
		}else {
			nodeString += "(" ;
			int size = children.size(); 
			for (int i = 0; i < size -1; i++ ) {
				NodeCHAIN child = children.get(i) ;
				String childString = child.toString() ;
				nodeString += childString ;
				nodeString += "," ;				
			}
			NodeCHAIN child = children.get(size-1) ;
			String childString = child.toString() ;
			nodeString += childString ;		
			nodeString += ")" ;			
		}		
		return nodeString ;
	}
	
}
