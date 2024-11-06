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
                String[] values = line.split(","); // data must be comma delimited
                this.records.add(Arrays.asList(values));
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
        int rowCount = records.size() - 1;
        // int rowCount = 4;
        Object[] keys = this.jsonData.keySet().toArray();

        for (int i = 2; i < rowCount; i++) { // skip the headers
            // create a data list and store each entry
            Data row = new Data();

            for (int j = 0; j < jsonData.keySet().size(); j++) {
                String type = jsonData.get(keys[j]).toString();
                // System.out.println(records.get(i).get(j));
                // System.out.println(type);
                // System.out.println(keys[j]);
                String record = records.get(i).get(j);
                record = record.trim(); // remove leading spaces
                record = record.replace("\"", "");
                // System.out.println(record);
                Object castedRecord = new Object();

                // cast the value and assign the type and assign the key
                switch (type) {
                    case "String":
                        castedRecord = record.toString();
                        break;
                    case "Integer":
                        castedRecord = Integer.valueOf(record);
                        break;
                    case "Float":
                        castedRecord = Integer.valueOf(record);
                        break;
                    default:
                        break;
                }
                // create an entry for each data point
                Entry entry = new Entry(type, castedRecord, keys[i].toString());
                row.addToRow(entry);

                entry.toString();
            }
            // add the row to the data set
            dataSet.add(row);
        }

    }

}