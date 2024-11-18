
/**
 * Controls and manages housing funding data operations for the Canadian Housing
 * Dashboard.
 */
public class DataController extends DataManager {

  /**
   * Constructs a new DataController with the specified CSV data source.
   * 
   * @param csvPath The file path to the CSV containing housing funding data
   */
  public DataController(String csvPath, String configFilePath) {
    super(csvPath, configFilePath);
  }

  public void sort() {

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

  }

  public void filter() {

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

}
