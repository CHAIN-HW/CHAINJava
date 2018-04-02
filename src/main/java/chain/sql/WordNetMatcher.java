package chain.sql;


import it.unitn.disi.smatch.IMatchManager;
import it.unitn.disi.smatch.MatchManager;
import it.unitn.disi.smatch.SMatchException;
import it.unitn.disi.smatch.data.mappings.IMapping;
import it.unitn.disi.smatch.data.mappings.IMappingElement;
import it.unitn.disi.smatch.data.trees.IContext;
import it.unitn.disi.smatch.data.trees.INode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SPSMMatcher
 * 
 * Use SPSM Match Manager to create a match for each of a given set of targets.
 *
 */
public class WordNetMatcher {


    private Set<String> targets;

    /**
     * Constructor for  the WordNetMatcher class
     * @param targets is a set of strings that SPSM should try and match to
     */
    WordNetMatcher(Set<String> targets) {
        this.targets = targets;
    }

    /**
     * Creates a tree structure for source and target and runs a match between them
     *
     * @param source is the broken term for which a match is being looked for
     * @return A string from the target set that closely matches the source
     * @throws SMatchException An exception captured in SMatch
     * @throws WordNetMatchingException if no match is made or if there is more than one possible match
     */
    public String match(String source) throws SMatchException, WordNetMatchingException {

        IMatchManager mm = MatchManager.getInstanceFromResource("/s-match.xml");
        IContext sourceNode = mm.createContext();
        sourceNode.createRoot(source);


        IContext targetNode = mm.createContext();
        INode root = targetNode.createRoot();

        for(String target: targets ) {
            root.createChild(target);
        }

        IMapping<INode> result =  mm.match(sourceNode,targetNode);

        if(result.size() < 1) {
            String msg = "No matches found for " + source + "\nPossibilities were: ";
            for(String target : targets) {
                msg = msg + "\n" + target;
            }
            throw new WordNetMatchingException(msg);
        }
        return processResults(result);
    }

    /**
     * Processes data stored in match nodes
     *
     * @param result is a list of nodes containing a source and a target that matches it
     * @return A matching target as a string
     * @throws WordNetMatchingException Thrown if no matches exist or if there is more than one
     */
    private String processResults(IMapping<INode> result) throws WordNetMatchingException {
        List<String> matches = new ArrayList<>();

        for (IMappingElement<INode> e : result) {
            System.out.println(e.getSource().nodeData().getName() + "\t" + e.getRelation() + "\t" + e.getTarget().nodeData().getName());
            if(e.getRelation() == '=')
                return e.getTarget().nodeData().getName();

            matches.add(e.getTarget().nodeData().getName());
        }

        if(matches.size() > 1)
            throw new WordNetMatchingException("Multiple matches found");

        return matches.get(0);

    }

}
