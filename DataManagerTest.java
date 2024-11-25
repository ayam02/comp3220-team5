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
    }

    /**
     * Test the constructor with a pre-existing dataset.
     */
    @Test
    public void testConstructorWithDataSet() {
        assertNotNull("Dataset should not be null", dataManager.getDataSet());
        assertEquals("Dataset should have 1 row", 1, dataManager.getDataSet().size());
    }

    /**
     * Test the constructor with file paths for CSV and JSON.
     * This requires mock or sample files.
     */
    @Test
    public void testConstructorWithFiles() {
        DataManager fileDataManager = new DataManager("./data/mockData.csv", "./configFiles/mockConfig.json");
        assertNotNull("Dataset should not be null after file processing", fileDataManager.getDataSet());
    }

    /**
     * Test the setDataSet() method.
     */
    @Test
    public void testSetDataSet() {
        List<Data> newDataSet = new ArrayList<>();
        Data newRow = new Data();
        newRow.addToRow(new Entry("String", "NewCity", "City"));
        newDataSet.add(newRow);

        dataManager.setDataSet(newDataSet);
        assertEquals("Dataset should have 1 row after reset", 1, dataManager.getDataSet().size());
        assertEquals("First row's City should be NewCity", "NewCity", newDataSet.get(0).getRow().get(0).getValue());
    }

    /**
     * Test the readData() method.
     * This requires a sample CSV file.
     */
    @Test
    public void testReadData() {
        // Call readData() to read a sample file
        dataManager.readData("./data/mockData.csv");
    
        // Populate data to validate the effect of reading the file
        dataManager.populateData();
    
        // Check if the dataset is populated
        List<Data> populatedDataSet = dataManager.getDataSet();
        assertNotNull("Dataset should not be null after populating", populatedDataSet);
        assertTrue("Dataset should contain rows from the file", populatedDataSet.size() > 0);
    }
    

    /**
     * Test the readJSON() method.
     * This requires a sample JSON file.
     */
    @Test
    public void testReadJSON() {
        // Call readJSON() to load a sample JSON configuration file
        dataManager.readJSON("./configFiles/mockConfig.json");
    
        // Populate data to validate the effect of reading JSON
        dataManager.populateData();
    
        // Check if the dataset is populated
        List<Data> populatedDataSet = dataManager.getDataSet();
        assertNotNull("Dataset should not be null after reading JSON and populating data", populatedDataSet);
        assertTrue("Dataset should contain rows", populatedDataSet.size() > 0);
    }
    
    /**
     * Test the populateData() method.
     * Requires mock data and JSON files to simulate a full pipeline.
     */
    @Test
    public void testPopulateData() {
        dataManager.readData("./data/mockData.csv");
        dataManager.readJSON("./configFiles/mockConfig.json");
        dataManager.populateData();

        List<Data> populatedDataSet = dataManager.getDataSet();
        assertNotNull("Dataset should not be null after populating", populatedDataSet);
        assertTrue("Dataset should contain rows", populatedDataSet.size() > 0);
    }

    /**
     * Test error handling in populateData() when given invalid data.
     */
    @Test
    public void testPopulateDataWithInvalidData() {
        dataManager.readData("./data/invalidMockData.csv"); // Contains invalid data for testing
        dataManager.readJSON("./configFiles/mockConfig.json");
        try {
            dataManager.populateData();
        } catch (Exception e) {
            fail("populateData() should handle errors gracefully and not throw exceptions");
        }
        assertNotNull("Dataset should not be null even with invalid data", dataManager.getDataSet());
    }
}
