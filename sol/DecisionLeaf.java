package sol;

import src.ITreeNode;
import src.Row;

/**
 * A class representing a leaf in the decision tree.
 */
public class DecisionLeaf implements ITreeNode {
    private String decision;

    /**
     * This is the constructor for DecisionLeaf
     * @param decision - The String representing the decision
     */
    public DecisionLeaf (String decision) {
        this.decision = decision;
    }

    /**
     * Getter for the decision string
     * @param forDatum the datum to lookup a decision for
     * @return - decision tree
     */
    @Override
    public String getDecision(Row forDatum) {
        return this.decision;
    }
}
