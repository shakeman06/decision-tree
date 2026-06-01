package sol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.DecisionTreeTester;
import src.Row;

import javax.xml.crypto.Data;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class containing the tests for methods in the TreeGenerator and Dataset classes
 */
public class DecisionTreeTest {

    private List<String> fvAttributes;
    private Dataset fvDataSet;
    private List<Row> fvRows;
    private TreeGenerator tree;

    /**
     * This sets up all the instance variables used in testing.
     */
    @Before
    public void setUp() {
        this.fvRows = DecisionTreeCSVParser.parse("data/fruits-and-vegetables.csv");
        this.fvAttributes = new ArrayList<>(this.fvRows.get(0).getAttributes());
        this.fvDataSet = new Dataset(this.fvAttributes, this.fvRows, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.tree = new TreeGenerator();
        this.tree.generateTree(this.fvDataSet, "foodType");
    }

    // Tests the IDataset method getDataObjects
    @Test
    public void testGetDataObjects(){
        assertEquals(this.fvRows, this.fvDataSet.getDataObjects());
    }

    // Tests the IDataset method getAttributeList
    @Test
    public void testGetAttributeList() {
        assertEquals(this.fvAttributes, this.fvDataSet.getAttributeList());
    }

    // Tests the IDataset method size
    @Test
    public void testSize() {
        assertEquals(this.fvRows.size(), this.fvDataSet.size());
    }

    // Tests the IDataset method getSelectionType
    @Test
    public void testGetSelectionType() {
        assertEquals(AttributeSelection.ASCENDING_ALPHABETICAL, this.fvDataSet.getSelectionType());
    }

    // Tests generateTree by comparing a value to getDecision's output
    @Test
    public void testGenerateTree() {
        Row row = this.fvRows.get(0);
        Assert.assertEquals(row.getAttributeValue("foodType"),this.tree.getDecision(row));
    }

    // Tests getDecision by comparing the expected string to the method's output
    @Test
    public void testGetDecision() {
        Row tangerine = new Row("test row (tangerine)");
        tangerine.setAttributeValue("color", "orange");
        tangerine.setAttributeValue("highProtein", "false");
        tangerine.setAttributeValue("calories", "high");

        assertEquals("fruit", this.tree.getDecision(tangerine));
    }

    // Tests the accuracy of the villain's training dataset
    @Test
    public void testVillains() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        DecisionTreeTester tester = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);

        double accuracy = tester.getAverageDecisionTreeAccuracy(
                "data/villains/training.csv",
                "data/villains/training.csv",
                "isVillain", 100);
        assertTrue(accuracy >= 0.95);

        accuracy = tester.getAverageDecisionTreeAccuracy(
                "data/villains/training.csv",
                "data/villains/testing.csv",
                "isVillain", 100);
        assertTrue(accuracy >= 0.70);
    }

    // Tests the accuracy of the mushroom's training dataset
    @Test
    public void testMushrooms() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        DecisionTreeTester tester = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);

        double accuracy = tester.getAverageDecisionTreeAccuracy(
                "data/mushrooms/training.csv",
                "data/mushrooms/training.csv",
                "isPoisonous", 100);
        assertTrue(accuracy >= 0.95);

        accuracy = tester.getAverageDecisionTreeAccuracy(
                "data/mushrooms/training.csv",
                "data/mushrooms/testing.csv",
                "isPoisonous", 100);
        assertTrue(accuracy >= 0.70);
    }

    // Tests the accuracy of the songs training dataset
    @Test
    public void testSongs() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        DecisionTreeTester tester = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);

        double accuracy = tester.getAverageDecisionTreeAccuracy(
                "data/songs/training.csv",
                "data/songs/training.csv",
                "isPopular", 100);
        System.out.println(accuracy);

        accuracy = tester.getAverageDecisionTreeAccuracy(
                "data/songs/training.csv",
                "data/songs/testing.csv",
                "isPopular", 100);
        assertTrue(accuracy >= 0.70);
    }

    // Tests the accuracy of Dataset 1
    @Test
    public void testPlayingOutsideAccuracy() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        DecisionTreeTester tester = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);

        double accuracy = tester.getAverageDecisionTreeAccuracy(
                "data/Dataset1.csv",
                "data/Dataset1.csv",
                "Outcome", 100);
        System.out.println(accuracy);

        accuracy = tester.getAverageDecisionTreeAccuracy(
                "data/Dataset1.csv",
                "data/Dataset1.csv",
                "Outcome", 100);
        assertTrue(accuracy >= 0.70);
    }

    // Tests the accuracy of Dataset 2
    @Test
    public void testDiningHallAccuracy() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        DecisionTreeTester tester = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);

        double accuracy = tester.getAverageDecisionTreeAccuracy(
                "data/Dataset2.csv",
                "data/Dataset2.csv",
                "Dining Hall", 100);
        System.out.println(accuracy);

        accuracy = tester.getAverageDecisionTreeAccuracy(
                "data/Dataset2.csv",
                "data/Dataset2.csv",
                "Dining Hall", 100);
        assertTrue(accuracy >= 0.70);
    }

    // Tests dataset 1 by comparing an expected outcome to the result of getDecision
    @Test
    public void testPlayingOutside() {
        List<Row> rows = DecisionTreeCSVParser.parse("data/Dataset1.csv");
        Dataset dataset = new Dataset(new ArrayList<>(rows.get(0).getAttributes()), rows, AttributeSelection.ASCENDING_ALPHABETICAL);
        TreeGenerator treegenerator = new TreeGenerator();
        treegenerator.generateTree(dataset, "Outcome");

        Row row = new Row("test");
        row.setAttributeValue("Weather", "Sunny");
        row.setAttributeValue("Friends", "2");
        row.setAttributeValue("Game", "Soccer");
        row.setAttributeValue("Homework", "Complete");
        assertEquals("Accept", treegenerator.getDecision(row));
    }

    // Tests dataset 2 by comparing an expected outcome to the result of getDecision
    @Test
    public void testDiningHall() {
        List<Row> rows = DecisionTreeCSVParser.parse("data/Dataset2.csv");
        Dataset dataset = new Dataset(new ArrayList<>(rows.get(0).getAttributes()), rows, AttributeSelection.ASCENDING_ALPHABETICAL);
        TreeGenerator treegenerator = new TreeGenerator();
        treegenerator.generateTree(dataset, "Dining Hall");

        Row row = new Row("test");
        row.setAttributeValue("Appetite", "Hungry");
        row.setAttributeValue("Location", "North");
        row.setAttributeValue("Time", "Morning");
        row.setAttributeValue("Extra Swipes", "Yes");
        assertEquals("Main", treegenerator.getDecision(row));
    }
}
