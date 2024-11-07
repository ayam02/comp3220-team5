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
    
    /** Stores raw CSV records where the outer list represents the file and the inner list represents rows */
    private List<List<String>> records = new ArrayList<>();
    
    /** Stores configuration data from JSON */
    private Map<String, Object> jsonData;

    /**
     * Constructs a DataManager and immediately loads data from the specified file.
     *
     * @param filePath The path to the CSV data file
     */
    public DataManager(String filePath) {
        this.readData(filePath);
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
                String[] values = line.split("\t");
                records.add(Arrays.asList(values));
            }
            reader.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Loads and parses configuration data from a JSON file.
     *
     * @param filePath The path to the JSON configuration file
     */
    public void readJSON(String filePath) {
        JSONReader jsonreader = new JSONReader(filePath);
        this.jsonData = jsonreader.getJsonData();
    }

    /**
     * Processes the raw CSV records into structured Data objects.
     * This method:
     * <ul>
     *   <li>Skips the header row</li>
     *   <li>Cleans and validates location data</li>
     *   <li>Processes funding amounts</li>
     *   <li>Handles home count data, converting to integers</li>
     *   <li>Creates structured Data objects for each row</li>
     * </ul>
     * Invalid or malformed data is handled gracefully with appropriate error logging.
     */
    public void populateData() {
        int rowCount = records.size();
        
        for (int i = 1; i < rowCount; i++) { // Start from 1 to skip header
            try {
                Data row = new Data();
                List<String> record = records.get(i);

                // Get the full jurisdiction (city and province together)
                String fullLocation = record.get(0).replaceAll("\"", "").trim();
                
                // Get funding value
                String funding = "";
                if (record.size() > 1) {
                    funding = record.get(1).replaceAll("\"", "").trim();
                }

                // Get homes value
                Integer homes = 0;
                if (record.size() > 2) {
                    String homesStr = record.get(2).replaceAll("\"", "").replace("--", "0").trim();
                    if (!homesStr.isEmpty()) {
                        try {
                            homes = Integer.valueOf(homesStr);
                        } catch (NumberFormatException e) {
                            homes = 0;
                        }
                    }
                }

                row.addToRow(new Entry("String", fullLocation, "City"));
                row.addToRow(new Entry("String", "", "Province")); // Province is part of city
                row.addToRow(new Entry("String", funding, "Federal Funding"));
                row.addToRow(new Entry("Integer", homes, "New Homes Over 10 Years"));

                dataSet.add(row);
                
            } catch (Exception e) {
                System.err.println("Error processing row " + i + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}