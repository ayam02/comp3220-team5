import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public class DataManager {
    private List<Data> dataSet;
    private static List<List<String>> records; // the external list represents the file and the
                                               // internal list

    public DataManager(String filePath) {
        records = new ArrayList<>();
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
        String line; // represents the row which contains multiple values

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
            reader.close();

        } catch (Exception e) {
            System.err.println(e);
        }

        System.err.println(records.get(3).get(1));
    }

}