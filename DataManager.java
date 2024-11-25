import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

/**
 * Manages the loading, storage, and processing of housing funding data.
 * This class handles both CSV data files and JSON configuration files,
 * providing a structured approach to data management and access.
 */
public class DataManager {

    /** Stores processed data entries */
    private List<Data> dataSet = new ArrayList<Data>();

    /**
     * Stores raw CSV records where the outer list represents the file and the inner
     * list represents rows
     */
    private List<List<String>> records = new ArrayList<>();

    /** Stores configuration data from JSON */
    private Map<String, Object> jsonData;

    public static void main(String args[]) {
        DataManager manager = new DataManager("./data/csv/Budget.csv", "./configFiles/BudgetConfig.json");
        for (int i = 0; i < manager.dataSet.size(); i++) { // loop through rows
            System.out.println("Row: " + (i + 1));
            List<Entry> row = manager.dataSet.get(i).getRow();
            for (int j = 0; j < row.size(); j++) { // loop through entries
                System.out.println("key = " + row.get(j).getField() + " type = " +
                        row.get(j).getType() + " value = "
                        + row.get(j).getValue());
            }
            System.out.println("");
        }
    }

    /**
     * Constructs a DataManager and immediately loads data from the specified file.
     *
     * @param filePath The path to the CSV data file
     */
    public DataManager(String filePath, String jsonPath) {
        this.readJSON(jsonPath);
        this.readData(filePath);
        this.populateData();

    }

    /**
     * Constructs a DataManager with a pre-existing dataset.
     *
     * @param dataSet The list of Data objects to manage
     */
    public DataManager(List<Data> dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Retrieves the current dataset.
     *
     * @return The list of processed Data objects
     */
    public List<Data> getDataSet() {
        return dataSet;
    }

    /**
     * Updates the current dataset.
     *
     * @param dataSet The new list of Data objects to manage
     */
    public void setDataSet(List<Data> dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Reads and parses tab-delimited data from a file.
     * Stores the parsed data in the records list for further processing.
     *
     * @param filePath The path to the tab-delimited data file
     */
    public void readData(String filePath) {
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                this.records.add(Arrays.asList(values));
            }
            reader.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        // for (int i = 0; i < records.size(); i++) { // loop through the rows
        // List<String> row = records.get(i);
        // for (int j = 0; j < row.size(); j++) { // loop through the entries in each
        // row

        // System.out.println(row.get(j));
        // }
        // System.out.println("");

        // }
        // System.out.println();
    }

    /**
     * Loads and parses configuration data from a JSON file.
     *
     * @param filePath The path to the JSON configuration file
     */
    public void readJSON(String filePath) {
        JSONReader jsonreader = new JSONReader(filePath);
        jsonData = jsonreader.getJsonData();
    }

    /**
     * Processes the raw CSV records into structured Data objects.
     * This method:
     * <ul>
     * <li>Skips the header row</li>
     * <li>Cleans and validates location data</li>
     * <li>Processes funding amounts</li>
     * <li>Handles home count data, converting to integers</li>
     * <li>Creates structured Data objects for each row</li>
     * </ul>
     * Invalid or malformed data is handled gracefully with appropriate error
     * logging.
     */
    public void populateData() {
        int rowCount = records.size();
        Object[] keys = jsonData.keySet().toArray();
        int cols = jsonData.keySet().size();

        for (int i = 1; i < rowCount; i++) { // Start from 1 to skip header
            try {
                Data row = new Data();
                List<String> record = records.get(i);
                
                for (int j = 0; j < cols; j++) {
                    String type = jsonData.get(keys[j]).toString();
                    String value = j < record.size() ? record.get(j).trim() : ""; // Check index bounds
                    
                    // Create entry based on type
                    Entry entry = new Entry();
                    entry.setField(keys[j].toString());
                    entry.setType(type);
                    
                    // Convert value based on type
                    switch(type.toUpperCase()) {
                        case "INTEGER":
                            // Check for non-numeric strings and handle them
                            if (value.equals("--") || value.isEmpty()) {
                                entry.setValue(0); // Default to 0
                            } else {
                                entry.setValue(Integer.parseInt(value.replaceAll("[^0-9-]", "")));
                            }
                            break;
                        case "FLOAT":
                            if (value.equals("--") || value.isEmpty()) {
                                entry.setValue(0.0f); // Default to 0.0
                            } else {
                                entry.setValue(Float.parseFloat(value.replaceAll("[^0-9.-]", "")));
                            }
                            break;
                        default: // STRING
                            entry.setValue(value);
                    }
                    
                    row.addToRow(entry);
                }
                dataSet.add(row);
            } catch (Exception e) {
                System.err.println("Error processing row " + i + ": " + e.getMessage());
            }
        }
    }
}