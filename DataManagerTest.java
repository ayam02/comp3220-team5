import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DataManagerTest {
    private DataManager dataManager;
    private List<Data> mockDataSet;

    @Before
    public void setUp() {
        // Mock data for testing
        mockDataSet = new ArrayList<>();
        Data mockRow = new Data();
        mockRow.addToRow(new Entry("String", "TestCity", "City"));
        mockRow.addToRow(new Entry("Integer", 1000, "Population"));
        mockDataSet.add(mockRow);

        // Initialize DataManager with mock dataset
        dataManager = new DataManager(mockDataSet);
        System.out.println("DataManager initialized with mock data.");
    }

    /**
     * Test the constructor with a pre-existing dataset.
     */
    @Test
    public void testConstructorWithDataSet() {
        System.out.println("Testing constructor with dataset...");
        List<Data> dataSet = dataManager.getDataSet();
        assertNotNull("Dataset should not be null", dataSet);
        System.out.println("Dataset: " + dataSet);

        assertEquals("Dataset should have 1 row", 1, dataSet.size());
        System.out.println("Constructor test passed: Dataset size = " + dataSet.size());
    }

    /**
     * Test the setDataSet() method.
     */
    @Test
    public void testSetDataSet() {
        System.out.println("Testing setDataSet...");
        List<Data> newDataSet = new ArrayList<>();
        Data newRow = new Data();
        newRow.addToRow(new Entry("String", "NewCity", "City"));
        newDataSet.add(newRow);

        dataManager.setDataSet(newDataSet);
        assertEquals("Dataset should have 1 row after reset", 1, dataManager.getDataSet().size());
        assertEquals("First row's City should be NewCity", "NewCity", newDataSet.get(0).getRow().get(0).getValue());

        System.out.println("New dataset: " + dataManager.getDataSet());
        System.out.println("setDataSet test passed.");
    }

    /**
     * Test adding a new row to the dataset.
     */
    @Test
    public void testAddNewRow() {
        System.out.println("Testing adding a new row...");
        Data newRow = new Data();
        newRow.addToRow(new Entry("String", "AnotherCity", "City"));
        newRow.addToRow(new Entry("Integer", 2000, "Population"));

        List<Data> currentDataSet = dataManager.getDataSet();
        currentDataSet.add(newRow);
        dataManager.setDataSet(currentDataSet);

        assertEquals("Dataset should now have 2 rows", 2, dataManager.getDataSet().size());
        assertEquals("Second row's City should be AnotherCity", "AnotherCity", dataManager.getDataSet().get(1).getRow().get(0).getValue());

        System.out.println("Updated dataset: " + dataManager.getDataSet());
        System.out.println("Add new row test passed.");
    }

    /**
     * Test retrieving the first row from the dataset.
     */
    @Test
    public void testGetFirstRow() {
        System.out.println("Testing retrieving the first row...");
        Data firstRow = dataManager.getDataSet().get(0);

        assertNotNull("First row should not be null", firstRow);
        assertEquals("First row's City should be TestCity", "TestCity", firstRow.getRow().get(0).getValue());

        System.out.println("First row: " + firstRow.getRow());
        System.out.println("Get first row test passed.");
    }

    /**
     * Test clearing the dataset.
     */
    @Test
    public void testClearDataSet() {
        System.out.println("Testing clearing the dataset...");
        dataManager.setDataSet(new ArrayList<>()); // Set an empty dataset

        assertEquals("Dataset should have 0 rows after clearing", 0, dataManager.getDataSet().size());
        System.out.println("Cleared dataset: " + dataManager.getDataSet());
        System.out.println("Clear dataset test passed.");
    }
}
