package sol;

import java.util.List;
import java.util.Random;

import src.AttributeSelection;
import src.IDataset;
import src.Row;

/**
 * A class representing a training dataset for the decision tree
 */
public class Dataset implements IDataset  {
    /**
     * Constructor for a Dataset object
     * @param attributeList - a list of attributes
     * @param dataObjects -  a list of rows
     * @param selectionType - an enum for which way to select attributes
     */
    private AttributeSelection selectionType;
    private List<String> attributeList;
    private List<Row> dataObjects;

    /**
     * The constructor for the Dataset class which instantiates selectionType, attributeList, and dataObjects
     * @param attributeList - A list of strings representing each attribute
     * @param dataObjects - A list of rows of the dataset
     * @param selectionType - Enum AttributeSelection
     */
    public Dataset(List<String> attributeList, List<Row> dataObjects, AttributeSelection selectionType) {
        this.selectionType = selectionType;
        this.attributeList = attributeList;
        this.dataObjects = dataObjects;
    }


    /**
     * A method that returns which attribute the tree should split on
     * @return - Returns the String of the attribute
     */
    public String getAttributeToSplitOn() {
        if (this.selectionType == AttributeSelection.ASCENDING_ALPHABETICAL) {
            return this.attributeList.stream().sorted().toList().get(0);
        }
        if (this.selectionType == AttributeSelection.DESCENDING_ALPHABETICAL) {
            return this.attributeList.stream().sorted().toList().get(this.attributeList.size() - 1);
        }
        if (this.selectionType == AttributeSelection.RANDOM) {
            Random random = new Random();
            return this.attributeList.get(random.nextInt(this.attributeList.size()));
        }

        throw new RuntimeException("Non-Exhaustive Selection Type");
    }

    /**
     * A getter for attributeList
     * @return attributeList
     */
    @Override
    public List<String> getAttributeList() {
        return this.attributeList;
    }

    /**
     * A getter for dataObjects
     * @return dataObjects
     */
    @Override
    public List<Row> getDataObjects() {
        return this.dataObjects;
    }

    /**
     * A getter for selectionType
     * @return selectionType
     */
    @Override
    public AttributeSelection getSelectionType() {
        return this.selectionType;
    }

    /**
     * A getter for the numbers of rows
     * @return size of the dataObjects list
     */
    @Override
    public int size() {
        return this.dataObjects.size();
    }
}