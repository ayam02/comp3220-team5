import java.util.ArrayList;
import java.util.List;

/**
 * Controls and manages housing funding data operations for the Canadian Housing
 * Dashboard.
 */
public class DataController extends DataManager {

  /**
   * Constructs a new DataController with the specified CSV data source.
   * 
   * @param csvPath The file path to the CSV containing housing funding data
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

<<<<<<< HEAD
    // Sort the dataset using the provided field index
    getDataSet().sort((data1, data2) -> {
      String value1 = data1.getRow().get(fieldIndex).getValue().toString();
      String value2 = data2.getRow().get(fieldIndex).getValue().toString();
      return value1.compareTo(value2); // Sort in ascending order
    });

    System.out.println("Dataset sorted by field index " + fieldIndex);
=======
  /**
   * Retrieves a list of all city names from the dataset.
   * Cleans location data by removing trailing commas if present.
   * 
   * @return A list of city names
   */
  public List<String> getCityNames() {
    List<String> cities = new ArrayList<>();
    for (Data data : dataManager.getDataSet()) {
        String location = data.getRow().get(0).getValue().toString();
        // Remove trailing comma if it exists
        if (location.endsWith(",")) {
            location = location.substring(0, location.length() - 1);
        }
        cities.add(location);
    }
    return cities;
  }

  /**
   * Extracts and processes funding values from the dataset.
   * Handles various numeric formats including:
   * <ul>
   *   <li>Removing currency symbols and commas</li>
   *   <li>Converting string values to integers</li>
   *   <li>Handling decimal values through rounding</li>
   * </ul>
   * 
   * @return A list of processed funding values as integers
   */
  public List<Integer> getFundingValues() {
    List<Integer> funding = new ArrayList<>();
    for (Data data : dataManager.getDataSet()) {
        String fundingStr = data.getRow().get(2).getValue().toString();
        try {
            // Remove everything except numbers and decimal points
            fundingStr = fundingStr.replaceAll("[^0-9.]", "").trim();
            
            if (!fundingStr.isEmpty()) {
                double value = Double.parseDouble(fundingStr);
                funding.add((int)Math.round(value));
            } else {
                funding.add(0);
            }
        } catch (NumberFormatException e) {
            funding.add(0);
            System.err.println("Error parsing funding value: " + fundingStr);
        }
    }
    return funding;

>>>>>>> 18dd0c396e4fe1256926ffdd7910060990affa6f
  }

  /**
   * Filters the dataset based on a specific field (column index) and value.
   * Only rows where the specified field matches the given value will be kept.
   * 
   * @param fieldIndex The index of the field (column) to filter by
   * @param value The value that the field should match
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
<<<<<<< HEAD
=======

  /**
   * Converts a full province name or location string to its standard two-letter abbreviation.
   * 
   * @param location The location string containing the province name
   * @return The two-letter province abbreviation, or "Other" if no match is found
   */
  private String extractProvince(String location) {
    if (location.contains("Ontario")) return "ON";
    if (location.contains("British Columbia")) return "BC";
    if (location.contains("Alberta")) return "AB";
    if (location.contains("Quebec")) return "QC";
    if (location.contains("Nova Scotia")) return "NS";
    if (location.contains("New Brunswick")) return "NB";
    if (location.contains("Manitoba")) return "MB";
    if (location.contains("Saskatchewan")) return "SK";
    if (location.contains("Newfoundland")) return "NL";
    if (location.contains("Prince Edward Island")) return "PE";
    if (location.contains("Yukon")) return "YT";
    if (location.contains("Northwest Territories")) return "NT";
    if (location.contains("Nunavut")) return "NU";
    return "Other";
  }

  public List<Integer> getFutureHousingPlans() {
      List<Integer> futureHousingPlans = new ArrayList<>();
      for (Data data : dataManager.getDataSet()) {
          try {
              Integer homes = (Integer) data.getRow().get(3).getValue();
              futureHousingPlans.add(homes != null ? homes : 0);
          } catch (Exception e) {
              futureHousingPlans.add(0);
              System.err.println("Error getting housing plans: " + e.getMessage());
          }
      }
      return futureHousingPlans;
  }

>>>>>>> 18dd0c396e4fe1256926ffdd7910060990affa6f
}
