package sol;

import java.util.List;

import src.AttributeSelection;
import src.ITreeNode;
import java.util.HashMap;
import src.Row;

/**
 * A class representing an inner node in the decision tree.
 */
public class AttributeNode implements ITreeNode {
    private HashMap<String, ITreeNode> children;
    private String defaultDecision;
    private String attribute;

    /**
     * The constructor for Attribute Node
     * @param attribute - The string representing the attribute
     * @param defaultDecision - String representing the default decision
     */
    public AttributeNode(String attribute, String defaultDecision) {
        this.defaultDecision = defaultDecision;
        this.attribute = attribute;
        this.children = new HashMap<>();
    }

    /**
     * A setter method to add key and values to the children hashmap
     * @param key - The children hashmap key
     * @param value - The children hashmap value
     */
    public void addChildren(String key, ITreeNode value) {
        this.children.put(key, value);
    }

    /**
     * This method returns either a decision from the hashmap or the default decision.
     * @param forDatum the datum to lookup a decision for
     * @return - returns the String decision
     */
    @Override
    public String getDecision(Row forDatum) {
        String value = forDatum.getAttributeValue(this.attribute);
        if (this.children.containsKey(value)) {
            return this.children.get(value).getDecision(forDatum);
        } else {
            return this.defaultDecision;
        }
    }
}
