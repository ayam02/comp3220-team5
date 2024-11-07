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
}