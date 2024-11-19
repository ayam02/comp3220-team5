import java.util.ArrayList;
import java.util.List;

/**
 * Controls and manages housing funding data operations for the Canadian Housing
 * Dashboard.
 */
public class DataController extends DataManager {
  private String name; // represents the name of the data set

  /**
   * Constructs a new DataController with the specified CSV data source.
   * 
   * @param csvPath        The file path to the CSV containing housing funding
   *                       data
   * @param configFilePath The file path to the configuration file (if needed)
   */
  public DataController(String csvPath, String configFilePath) {
    super(csvPath, configFilePath);
  }

  /**
   * Sorts the dataset based on a specific field (column index).
   * 
   * @param fieldIndex The index of the field (column) to sort by
   */
  public void sort(int fieldIndex) {
    // First, ensure the dataset is not empty
    if (getDataSet() == null || getDataSet().isEmpty()) {
      System.err.println("Dataset is empty or null. Cannot perform sort.");
      return;
    }

    // Sort the dataset using the provided field index
    getDataSet().sort((data1, data2) -> {
      String value1 = data1.getRow().get(fieldIndex).getValue().toString();
      String value2 = data2.getRow().get(fieldIndex).getValue().toString();
      return value1.compareTo(value2); // Sort in ascending order
    });

    System.out.println("Dataset sorted by field index " + fieldIndex);
  }

  /**
   * Filters the dataset based on a specific field (column index) and value.
   * Only rows where the specified field matches the given value will be kept.
   * 
   * @param fieldIndex The index of the field (column) to filter by
   * @param value      The value that the field should match
   */
  public void filter(int fieldIndex, String value) {
    // Ensure the dataset is not empty
    if (getDataSet() == null || getDataSet().isEmpty()) {
      System.err.println("Dataset is empty or null. Cannot perform filter.");
      return;
    }

    // Filter the dataset
    List<Data> filteredData = new ArrayList<>();
    for (Data data : getDataSet()) {
      String fieldValue = data.getRow().get(fieldIndex).getValue().toString();
      if (fieldValue.equals(value)) {
        filteredData.add(data);
      }
    }

    // Update the dataset with the filtered result
    setDataSet(filteredData);
    System.out.println("Dataset filtered by field index " + fieldIndex + " with value " + value);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
