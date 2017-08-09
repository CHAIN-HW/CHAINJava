import java.util.*;

/*
 * 
 * Structure that is being used to store the
 * repaired schema tree
 * 
 * This structure will have a node that has a value
 * and also a list of children nodes
 * 
 */
public class Node {
	//FIELDS
	private String value;
	private List<Node> children = null;
	
	//CONSTRUCTOR
	public Node(String val){
		value = val;
		children = new ArrayList<>();
	}
	
	//PUBLIC METHODS: GETTER & SETTER
	
	//adds a child node
	public void addChild(Node child){
		children.add(child);
	}
	
	//returns the value of the node
	public String getValue(){
		return value;
	}
	
	//returns the list of children nodes
	public List<Node> getChildren(){
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
	public String printChildren(List<Node> childrenList){
		String childrenString="";
		
		for(int i = 0 ; i < childrenList.size() ; i++){
			
			Node currNode = childrenList.get(i);
			
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
			Node tmpNode = new Node(currParam);
			if(children.contains(tmpNode)){
				continue;
			}
			
			//otherwise the parameter before the current one
			//is the parent so we can add it on
			int indexOfParent = children.indexOf(paramParts[i-1]);
			Node tmpParent = children.get(indexOfParent);
			tmpParent.addChild(new Node(currParam));
		}
		
	}
	
}
