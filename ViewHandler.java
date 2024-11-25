import java.util.ArrayList;
import java.util.List;

public class ViewHandler {
    private List<DataController> controllersList;

    public ViewHandler() {
        this.controllersList = new ArrayList<>();
    }

    public void addController(DataController controller) {
        this.controllersList.add(controller);
    }

    public List<DataController> getControllersList() {
        return controllersList;
    }

    public void setControllersList(List<DataController> controllersList) {
        this.controllersList = controllersList;
    }

    public DataController getController(String button) {
        for (DataController dataController : controllersList) {
            if (button.equals(dataController.getName())) {
                return dataController;
            }
        }
        String[] files = getFileNames(button);

        DataController dataController = new DataController(files[0], files[1]);
        controllersList.add(dataController);
        return dataController;
    }

    public String[] getFileNames(String button) {
        String dataSetPath = new String();
        String jsonPath = new String();
        switch (button) {
            case "Funding Insights":
                dataSetPath = "./data/csv/Funding.csv";
                jsonPath = "./configFiles/FundingConfig.json";
                break;
            case "Federal Budget Insights":
                dataSetPath = "./data/csv/Budget.csv";
                jsonPath = "./configFiles/BudgetConfig.csv";
                break;
            case "Housing Starts Insights":
                dataSetPath = "./data/csv/housingStarts.csv";
                jsonPath = "./configFiles/HousingConfig.json";
                break;
            case "Housing Prices Insights":
                dataSetPath = "./data/csv/toronto/csv/toronto_housing_data.csv";
                jsonPath = "./configFiles/HousePricesConfig.json";
                break;
        }
        String[] filesArray = new String[2];
        filesArray[0] = dataSetPath;
        filesArray[1] = jsonPath;

        return filesArray;
    }
}