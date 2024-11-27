import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Controls and manages housing funding data operations for the Canadian Housing Dashboard.
 * This class extends DataManager to provide specific functionality for processing
 * and analyzing housing funding data from CSV sources.
 */
public class DataController extends DataManager {
  /** The name identifier for this dataset */
  private String name;

  /**
   * Constructs a new DataController with specified CSV and configuration sources.
   * 
   * @param csvPath The file path to the CSV containing housing funding data
   * @param configFilePath The file path to the configuration file
   */
  public DataController(String csvPath, String configFilePath) {
    super(csvPath, configFilePath);
  }

  /**
   * Constructs a new DataController with default configuration.
   * 
   * @param csvPath The file path to the CSV containing housing funding data
   */
  public DataController(String csvPath) {
    super(csvPath, "./configFiles/FundingConfig.json");
  }

  /**
   * Sorts the dataset based on a specified field index.
   * Performs an ascending string comparison on the field values.
   * 
   * @param fieldIndex The index of the field (column) to sort by
   */
  public void sort(int fieldIndex) {
    if (getDataSet() == null || getDataSet().isEmpty()) {
      System.err.println("Dataset is empty or null. Cannot perform sort.");
      return;
    }

    getDataSet().sort((data1, data2) -> {
      String value1 = data1.getRow().get(fieldIndex).getValue().toString();
      String value2 = data2.getRow().get(fieldIndex).getValue().toString();
      return value1.compareTo(value2);
    });
  }

  /**
   * Filters the dataset to only include rows where the specified field matches the given value.
   * 
   * @param fieldIndex The index of the field (column) to filter by
   * @param value The value that the field should match exactly
   */
  public void filter(int fieldIndex, String value) {
    if (getDataSet() == null || getDataSet().isEmpty()) {
      System.err.println("Dataset is empty or null. Cannot perform filter.");
      return;
    }

    List<Data> filteredData = new ArrayList<>();
    for (Data data : getDataSet()) {
      String fieldValue = data.getRow().get(fieldIndex).getValue().toString();
      if (fieldValue.equals(value)) {
        filteredData.add(data);
      }
    }

    setDataSet(filteredData);
  }

  /**
   * Retrieves the name of this dataset.
   * 
   * @return The name identifier of the dataset
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name identifier for this dataset.
   * 
   * @param name The new name to set for the dataset
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Retrieves a list of all city names from the funding CSV file.
   * Skips the header row and extracts the first column of each data row.
   * 
   * @return List of city names, empty list if file cannot be read
   */
  public List<String> getCityNames() {
    List<String> cityNames = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("./data/csv/Funding.csv"))) {
        String line;
        br.readLine(); // Skip header
        while ((line = br.readLine()) != null) {
            String[] columns = line.split(",");
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
   * Handles various numeric formats including currency symbols, commas,
   * and decimal values through rounding.
   * 
   * @return List of processed funding values as integers, with 0 for invalid entries
   */
  public List<Integer> getFundingValues() {
    List<Integer> fundingValues = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("./data/csv/Funding.csv"))) {
        String line;
        br.readLine(); // Skip header
        while ((line = br.readLine()) != null) {
            String[] columns = line.split(",");
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
   * Retrieves future housing plan numbers from the dataset.
   * Converts string values to integers and handles empty or invalid entries.
   * 
   * @return List of housing plan numbers as integers, with 0 for invalid or empty entries
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

  /**
   * Calculates the total funding by province from the dataset.
   * Groups funding amounts by province and returns the top 4 provinces with others combined.
   * 
   * @return Map of province abbreviations to their total funding amounts
   */
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

  /**
   * Calculates the total housing plans by province from the dataset.
   * Groups housing plan numbers by province and returns the top 4 provinces with others combined.
   * 
   * @return Map of province abbreviations to their total housing plan numbers
   */
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

  /**
   * Extracts the province name from a location string.
   * Handles special cases and standardizes province names.
   * 
   * @param location The full location string (e.g., "City, Province")
   * @return Standardized province name, or "Other" if province cannot be determined
   */
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

  /**
   * Extracts the city name from a full location string.
   * 
   * @param fullLocation The full location string (e.g., "City, Province")
   * @return The extracted city name
   */
  private String extractCityName(String fullLocation) {
    if (fullLocation.contains(",")) {
        return fullLocation.split(",")[0].replace("\"", "").trim();
    }
    return fullLocation.replace("\"", "").trim();
  }

  /**
   * Converts full province names to their standard abbreviations.
   * 
   * @param province The full province name
   * @return The standard two-letter abbreviation for the province
   */
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

  /**
   * Returns the top 4 provinces by value from a provincial data map.
   * Combines all other provinces into an "Others" category.
   * 
   * @param provincialData Map of province names to their values
   * @return Map of the top 4 province abbreviations (plus "Others" if applicable) to their values
   */
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
