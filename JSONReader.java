import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JSONReader {
    private Map<String, Object> jsonData;

    public JSONReader(String filePath) {
        try {
            String jsonContent = readFileAsString(filePath);
            jsonData = parseJson(jsonContent);
            System.out.println("Parsed JSON Data: " + jsonData);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static String readFileAsString(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(filePath)) {
            int i;
            while ((i = reader.read()) != -1) {
                contentBuilder.append((char) i);
            }
        }
        return contentBuilder.toString();
    }

    public Map<String, Object> getJsonData() {
        return jsonData;
    }

    public static Map<String, Object> parseJson(String json) {
        Map<String, Object> dataMap = new LinkedHashMap<>();

        json = json.trim();
        json = json.substring(1, json.length() - 1).trim();

        String[] pairs = json.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            String key = keyValue[0].trim().replace("\"", "");
            String value = keyValue[1].trim().replace("\"", "");
            dataMap.put(key, value);
        }

        return dataMap;
    }
}