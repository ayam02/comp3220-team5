import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

public class DataManager {
    private List<Data> dataSet = new ArrayList<Data>();;
    private List<List<String>> records = new ArrayList<>(); // the external list represents the file and the
                                                            // internal list is a row
    private Map<String, Object> jsonData;

    // public static void main(String args[]) {
    // readData("data/csv/Funding.csv");
    // readJSON("configFiles/FundingConfig.json");
    // populateData();
    // }

    public DataManager(String filePath) {
        this.readData(filePath);
    }

    public DataManager(List<Data> dataSet) {
        this.dataSet = dataSet;
    }

    public List<Data> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<Data> dataSet) {
        this.dataSet = dataSet;
    }

    public void readData(String filePath) {
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\t");  // Split only on tabs
                records.add(Arrays.asList(values));
            }
            reader.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void readJSON(String filePath) {
        JSONReader jsonreader = new JSONReader(filePath);
        this.jsonData = jsonreader.getJsonData();
    }

    public void populateData() {
        int rowCount = records.size();
        
        for (int i = 1; i < rowCount; i++) { // Start from 1 to skip header
            try {
                Data row = new Data();
                List<String> record = records.get(i);

                // Get the full jurisdiction (city and province together)
                String fullLocation = record.get(0).replaceAll("\"", "").trim();  // Remove quotes
                
                // Get funding value
                String funding = "";
                if (record.size() > 1) {
                    funding = record.get(1).replaceAll("\"", "").trim();  // Remove quotes
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
                row.addToRow(new Entry("String", "", "Province")); // Keep this empty since province is now part of city
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