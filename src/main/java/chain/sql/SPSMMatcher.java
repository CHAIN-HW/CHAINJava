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

public class SPSMMatcher {


    private Set<String> targets;

    SPSMMatcher(Set<String> targets) {
        this.targets = targets;
    }

    /**
     *
     * @param source
     * @return
     * @throws SMatchException
     * @throws SPSMMatchingException
     */
    public String match(String source) throws SMatchException, SPSMMatchingException {

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
            throw new SPSMMatchingException(msg);
        }
        return processResults(result);
    }

    /**
     *
      * @param result
     * @return
     * @throws SPSMMatchingException
     */
    private String processResults(IMapping<INode> result) throws SPSMMatchingException {
        List<String> matches = new ArrayList<>();

        for (IMappingElement<INode> e : result) {
            System.out.println(e.getSource().nodeData().getName() + "\t" + e.getRelation() + "\t" + e.getTarget().nodeData().getName());
            if(e.getRelation() == '=')
                return e.getTarget().nodeData().getName();

            matches.add(e.getTarget().nodeData().getName());
        }

        if(matches.size() > 1)
            throw new SPSMMatchingException("Multiple matches found");

        return matches.get(0);

    }

}
