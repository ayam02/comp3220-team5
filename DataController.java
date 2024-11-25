import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

  public DataController(String csvPath) {
    super(csvPath, "./configFiles/FundingConfig.json"); // Default config
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

  public List<String> getCityNames() {
    List<String> cityNames = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("./data/csv/Funding.csv"))) {
        String line;
        br.readLine(); // Skip the header
        while ((line = br.readLine()) != null) {
            String[] columns = line.split(","); // Adjust delimiter if necessary
            if (columns.length > 0) {
                String cityName = columns[0].replaceAll("\"", "").trim();
                cityNames.add(cityName);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return cityNames;
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
    List<Integer> fundingValues = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("./data/csv/Funding.csv"))) {
        String line;
        br.readLine(); // Skip the header
        while ((line = br.readLine()) != null) {
            String[] columns = line.split(","); // Adjust delimiter if necessary
            if (columns.length > 1) {
                String fundingStr = columns[2].replaceAll("[^0-9.]", "").trim();
                try {
                    if (!fundingStr.isEmpty()) {
                        double value = Double.parseDouble(fundingStr);
                        fundingValues.add((int) Math.round(value));
                    } else {
                        fundingValues.add(0);
                    }
                } catch (NumberFormatException e) {
                    fundingValues.add(0);
                    System.err.println("Error parsing funding value: " + fundingStr);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return fundingValues;
  }

  /**
   * Retrieves housing plan numbers from the dataset.
   * Handles empty values marked as "--" and converts string values to integers.
   * 
   * @return A list of housing plan numbers as integers
   */
  public List<Integer> getFutureHousingPlans() {
    List<Integer> plans = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("./data/csv/Funding.csv"))) {
        String line;
        br.readLine(); // Skip the header
        while ((line = br.readLine()) != null) {
            String[] columns = line.split(","); // Adjust delimiter if necessary
            if (columns.length > 2) {  // Make sure we have the third column
                String rawValue = columns[3].replaceAll("[^0-9]", "").trim();
                try {
                    if (!rawValue.isEmpty()) {
                        plans.add(Integer.parseInt(rawValue));
                    } else {
                        plans.add(0); // Handle empty or "--" values
                    }
                } catch (NumberFormatException e) {
                    plans.add(0);
                    System.err.println("Error parsing housing value: " + rawValue);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return plans;
  }

  public Map<String, Integer> getProvincialFunding() {
    Map<String, Integer> provincialFunding = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader("./data/csv/Funding.csv"))) {
        String line;
        br.readLine(); // Skip the header
        while ((line = br.readLine()) != null) {
            String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (columns.length >= 2) {
                String location = columns[0].trim().replace("\"", "");
                String province = extractProvince(location);
                
                String fundingStr = columns[1].replaceAll("[^0-9.]", "").trim();
                int funding = 0;
                try {
                    if (!fundingStr.isEmpty()) {
                        funding = (int) Math.round(Double.parseDouble(fundingStr));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing funding for " + location + ": " + fundingStr);
                }
                
                if (!province.equals("Other")) {
                    provincialFunding.merge(province, funding, Integer::sum);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    // Return top 4 provinces with abbreviated names and others combined
    return getTop4Provinces(provincialFunding);
  }

  public Map<String, Integer> getProvincialHousingPlans() {
    Map<String, Integer> provincialPlans = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader("./data/csv/Funding.csv"))) {
        String line;
        br.readLine(); // Skip the header
        while ((line = br.readLine()) != null) {
            // Split on comma but not within quotes
            String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (columns.length >= 3) {
                String location = columns[0].trim().replace("\"", "");
                String province = extractProvince(location);
                
                // Parse housing plans from third column
                String plansStr = columns[2].replaceAll("[^0-9]", "").trim();
                int plans = 0;
                try {
                    if (!plansStr.isEmpty() && !plansStr.equals("--")) {
                        plans = Integer.parseInt(plansStr);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing housing plans for " + location + ": " + plansStr);
                }
                
                // Add to province total
                if (!province.equals("Other")) {  // Only add if we successfully extracted a province
                    provincialPlans.merge(province, plans, Integer::sum);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    // Return top 4 provinces with abbreviated names and others combined
    return getTop4Provinces(provincialPlans);
  }

  private String extractProvince(String location) {
    // Special case for Quebec
    if (location.contains("Province of Quebec")) {
        return "Quebec";
    }
    
    if (location.contains(",")) {
        String province = location.split(",")[1].trim();
        // Clean up the province name and standardize it
        province = province.replace("\"", "").trim();
        
        // Map common variations to standard names
        switch (province) {
            case "Ontario": return "Ontario";
            case "British Columbia": return "British Columbia";
            case "Alberta": return "Alberta";
            case "Quebec": return "Quebec";
            case "Nova Scotia": return "Nova Scotia";
            case "New Brunswick": return "New Brunswick";
            case "Manitoba": return "Manitoba";
            case "Saskatchewan": return "Saskatchewan";
            case "Newfoundland and Labrador": return "Newfoundland and Labrador";
            case "Prince Edward Island": return "Prince Edward Island";
            case "Yukon": return "Yukon";
            case "Northwest Territories": return "Northwest Territories";
            case "Nunavut": return "Nunavut";
            case "Nanavut": return "Nunavut";  // Handle typo in data
            default: 
                System.err.println("Unrecognized province: " + province + " from location: " + location);
                return province;
        }
    }
    return "Other";
  }

  private String extractCityName(String fullLocation) {
    if (fullLocation.contains(",")) {
        return fullLocation.split(",")[0].replace("\"", "").trim();
    }
    return fullLocation.replace("\"", "").trim();
  }

  private String abbreviateProvince(String province) {
    switch (province) {
        case "Ontario": return "ON";
        case "British Columbia": return "BC";
        case "Alberta": return "AB";
        case "Quebec": return "QC";
        case "Nova Scotia": return "NS";
        case "New Brunswick": return "NB";
        case "Manitoba": return "MB";
        case "Saskatchewan": return "SK";
        case "Newfoundland and Labrador": return "NL";
        case "Prince Edward Island": return "PEI";
        case "Yukon": return "YT";
        case "Northwest Territories": return "NT";
        case "Nunavut": return "NU";
        default: return province;
    }
  }

  private Map<String, Integer> getTop4Provinces(Map<String, Integer> provincialData) {
    Map<String, Integer> result = new HashMap<>();
    
    // Sort provinces by value in descending order
    List<Map.Entry<String, Integer>> sortedProvinces = new ArrayList<>(provincialData.entrySet());
    sortedProvinces.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
    
    // Add top 4 provinces
    int othersSum = 0;
    for (int i = 0; i < sortedProvinces.size(); i++) {
        if (i < 4) {
            String abbrev = abbreviateProvince(sortedProvinces.get(i).getKey());
            result.put(abbrev, sortedProvinces.get(i).getValue());
        } else {
            othersSum += sortedProvinces.get(i).getValue();
        }
    }
    
    // Add "Others" if there are more than 4 provinces
    if (sortedProvinces.size() > 4) {
        result.put("Others", othersSum);
    }
    
    return result;
  }
}
