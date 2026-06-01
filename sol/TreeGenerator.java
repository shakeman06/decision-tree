package sol;

import src.AttributeSelection;
import src.ITreeGenerator;
import src.ITreeNode;
import src.Row;

import javax.xml.crypto.Data;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that implements the ITreeGenerator interface used to generate a decision tree
 */
public class TreeGenerator implements ITreeGenerator<Dataset> {
    private ITreeNode root;

    /**
     * This is an entry method That sorts the attributes and calls the recursive method
     * @param trainingData - the dataset to train on
     * @param targetAttribute - the attribute to predict
     */
    @Override
    public void generateTree(Dataset trainingData, String targetAttribute) {
        List<String> attributeList = new ArrayList<String>(trainingData.getAttributeList());
        attributeList.remove(targetAttribute);
        this.root = this.buildBranches(trainingData, targetAttribute, attributeList);
    }

    /**
     * This is the main recursive method of treeGenerator.
     * It contains a base case that returns a leaf if all rows have the same target value.
     * It also contains a second base case with the most common value if no attributes remain.
     * There is also a recursive case: That splits rows by a chosen attribute and recurses on
     * each split group.
     *
     * @param trainingData - The dataset of the data that should be tested
     * @param targetAttribute - the target attribute that is trying to be predicted
     * @param remainingAttributes - A list of the remaining attributes past the target one
     * @return - An ITreeNode of the root of the newly built subtree
     */
    private ITreeNode buildBranches(Dataset trainingData, String targetAttribute,
                                    List<String> remainingAttributes) {
        List<Row> dataObjects = trainingData.getDataObjects();

        // Base case: All rows agree on the target value, so return that value
        // as a decision leaf
        if(checkIfAllEqual(dataObjects, targetAttribute))
        {
            return new DecisionLeaf(dataObjects.get(0).getAttributeValue(targetAttribute));
        }

        // Base case: No remaining attributes to split on, return the most common
        // value as a leaf
        if(remainingAttributes.isEmpty()){
            return new DecisionLeaf(this.mostCommonValue(dataObjects, targetAttribute));
        }

        // Recursive case: first choose an attribute to split on
        Dataset remainingDataset = new Dataset(remainingAttributes, dataObjects, trainingData.getSelectionType());
        String splitAttribute = remainingDataset.getAttributeToSplitOn();
        String defaultVal = this.mostCommonValue(dataObjects, targetAttribute);

        // Group the rows by their value for the split attribute
        HashMap<String, List<Row>> groups = groupRows(trainingData.getDataObjects(),
                splitAttribute);

        AttributeNode node = new AttributeNode(splitAttribute, defaultVal);

        // Recursively build a subtree for each sub-group
        for(String value : groups.keySet()) {
            List<Row> subGroup = groups.get(value);
            List<String> remaining = new ArrayList<>(remainingAttributes);
            remaining.remove(splitAttribute);
            Dataset subDataset = new Dataset(remaining, subGroup, trainingData.getSelectionType());
            ITreeNode child = this.buildBranches(subDataset, targetAttribute, remaining);
            node.addChildren(value, child);
        }
        return node;
    }

    private HashMap<String, List<Row>> groupRows(List<Row> rows, String attribute) {
        HashMap<String, List<Row>> groups = new HashMap<>();
        for(Row row : rows){
            // Get this row's value for the split attribute
            String value = row.getAttributeValue(attribute);
            // Create an empty list for it if its hasn't been seen before
            if(!groups.containsKey(value)){
                groups.put(value, new ArrayList<>());
            }
            // Add the row to the correct group
            groups.get(value).add(row);
        }
        return groups;
    }

    /**
     *  A helper method to check if all the rows within a passed list contain the targetAttribute
     * @param rows - A list of rows from a given dataset
     * @param targetAttribute - the attribute that the method is attempting to find within all rows
     * @return - returns true if all rows contain the attribute
     */
    private boolean checkIfAllEqual(List<Row> rows, String targetAttribute) {
        // Get the first rows value for comparison
        String first = rows.get(0).getAttributeValue(targetAttribute);
        for(Row r : rows) {
            // If any row's value differs from the first, not all rows are equal
            if(!r.getAttributeValue(targetAttribute).equals(first)){
                return false;
            }
        }

        return true;
    }

    /**
     * A helper method that aims to find the most common value within a list
     * @param dataObjects - A list of all rows within the dataset
     * @param targetAttribute - the target attribute from which the hashmap value is derived from
     * @return - the most common value within the passed List
     */
    private String mostCommonValue(List<Row> dataObjects, String targetAttribute) {
        // HasMap to store count for each target's value
        HashMap<String, Integer> numCount = new HashMap<String, Integer>();
        for(Row r : dataObjects){
            String value = r.getAttributeValue(targetAttribute);
            // Increment count if seen before otherwise initialize at 1
            if(numCount.containsKey(value)) {
                numCount.replace(value, numCount.get(value)+1);
            } else {
                numCount.put(value,1);
            }
        }

        // Find and return the key with the most occurrences
        String mostCommon = null;
        int highestCount = 0;
        for(Map.Entry<String, Integer> e : numCount.entrySet()) {
            if(e.getValue() > highestCount){
                highestCount = e.getValue();
                mostCommon = e.getKey();
            }
        }

        return mostCommon;
    }


    /**
     * This method takes in a row and returns the predicted recursive outcome
     * @param datum the datum to lookup a decision for
     * @return - the predicted outcome as a String
     */
    @Override
    public String getDecision(Row datum) {
        return this.root.getDecision(datum);
    }
}
