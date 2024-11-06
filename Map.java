import java.util.HashMap;
import java.util.Map;

public class Map {

    // Holds data for each city with associated housing metrics
    private Map<String, CityData> cityDataMap;

    // Constructor
    public Map() {
        cityDataMap = new HashMap<>();
    }

    // Method to add data for a city
    public void addCityData(String cityName, CityData data) {
        cityDataMap.put(cityName, data);
    }

    // Method to display federal funding for housing in a specific city
    public void displayFederalFunding(String cityName) {
        CityData data = cityDataMap.get(cityName);
        if (data != null) {
            System.out.println("Federal funding for " + cityName + ": " + data.getFederalFunding());
        } else {
            System.out.println("No data available for " + cityName);
        }
    }

    // Method to display property price trends in a specific city
    public void displayPropertyPrices(String cityName) {
        CityData data = cityDataMap.get(cityName);
        if (data != null) {
            System.out.println("Property prices for " + cityName + ": " + data.getPropertyPrices());
        } else {
            System.out.println("No data available for " + cityName);
        }
    }

    // Method to display forecasted housing development in a specific city
    public void displayHousingForecast(String cityName) {
        CityData data = cityDataMap.get(cityName);
        if (data != null) {
            System.out.println("Housing forecast for " + cityName + ": " + data.getHousingForecast());
        } else {
            System.out.println("No data available for " + cityName);
        }
    }

    // Method to render the map with available data (placeholder for actual map rendering)
    public void renderMap() {
        System.out.println("Rendering map with data for each city...");
        for (String cityName : cityDataMap.keySet()) {
            System.out.println("City: " + cityName + ", Data: " + cityDataMap.get(cityName));
        }
    }

    // Inner class to represent data for each city
    public static class CityData {
        private double federalFunding;
        private double propertyPrices;
        private int housingForecast;

        // Constructor
        public CityData(double federalFunding, double propertyPrices, int housingForecast) {
            this.federalFunding = federalFunding;
            this.propertyPrices = propertyPrices;
            this.housingForecast = housingForecast;
        }

        // Getters
        public double getFederalFunding() {
            return federalFunding;
        }

        public double getPropertyPrices() {
            return propertyPrices;
        }

        public int getHousingForecast() {
            return housingForecast;
        }

        // Override toString for easy display
        @Override
        public String toString() {
            return "Federal Funding: " + federalFunding + ", Property Prices: " + propertyPrices +
                    ", Housing Forecast: " + housingForecast;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        Map map = new Map();

        // Sample data for cities
        CityData torontoData = new CityData(5000000, 800000, 5000);
        CityData vancouverData = new CityData(3000000, 900000, 3000);

        // Adding city data to the map
        map.addCityData("Toronto", torontoData);
        map.addCityData("Vancouver", vancouverData);

        // Displaying data
        map.displayFederalFunding("Toronto");
        map.displayPropertyPrices("Vancouver");
        map.displayHousingForecast("Toronto");

        // Render map with all data
        map.renderMap();
    }
}
