import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls and manages housing funding data operations for the Canadian Housing Dashboard.
 * This controller handles data processing, transformation, and provides methods to extract
 * visualization-ready datasets for various chart types and displays.
 */
public class DataController {
  private DataManager dataManager;

  /**
   * Constructs a new DataController with the specified CSV data source.
   * 
   * @param csvPath The file path to the CSV containing housing funding data
   */
  public DataController(String csvPath) {
    this.dataManager = new DataManager(csvPath);
    setupController();
  }

  /**
   * Initializes the controller by loading configuration and populating data structures.
   */
  private void setupController() {
    dataManager.readJSON("configFiles/FundingConfig.json");
    dataManager.populateData();
  }

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
        String fundingStr = (String) data.getRow().get(2).getValue();
        try {
            // Remove commas, dollar signs, and other non-numeric characters except decimal points
            fundingStr = fundingStr.replaceAll("[^0-9.]", "").trim();
            
            if (!fundingStr.isEmpty()) {
                // Parse as double first to handle decimal values, then convert to int
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
  }

  /**
   * Provides access to the underlying data manager.
   * 
   * @return The DataManager instance
   */
  public DataManager getDataManager() {
    return dataManager;
  }

  /**
   * Calculates the total funding distribution by province.
   * Returns a map containing the top 4 provinces by funding amount,
   * with remaining provinces combined into an "Other" category.
   * 
   * @return A map of province abbreviations to their total funding values
   */
  public Map<String, Integer> getProvincialFunding() {
    Map<String, Integer> allFunding = new HashMap<>();
    
    for (Data data : dataManager.getDataSet()) {
        String location = data.getRow().get(0).getValue().toString();
        String fundingStr = data.getRow().get(2).getValue().toString()
            .replaceAll("[^0-9.]", "");
        
        try {
            int funding = (int)Double.parseDouble(fundingStr);
            
            // Extract province and convert to abbreviation
            String province = extractProvince(location);
            
            // Add funding to province total
            allFunding.merge(province, funding, Integer::sum);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing funding value: " + fundingStr);
        }
    }
    
    // Create final map with top 4 + Others
    Map<String, Integer> topFunding = new HashMap<>();
    
    // Sort by value and get top 4
    allFunding.entrySet().stream()
        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
        .limit(4)
        .forEach(e -> topFunding.put(e.getKey(), e.getValue()));
    
    // Sum remaining provinces into "Other"
    int otherTotal = allFunding.entrySet().stream()
        .filter(e -> !topFunding.containsKey(e.getKey()))
        .mapToInt(Map.Entry::getValue)
        .sum();
    
    topFunding.put("Other", otherTotal);
    
    return topFunding;
  }

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
}
 