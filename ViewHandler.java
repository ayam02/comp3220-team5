public class ViewHandler {
private List<DataController> dataController = new ArrayList<>();

public void initializeDataController(List<Data> dataSet){
  for (Data data: dataSet){
    dataController.add(new DataController(data));
    }
  }
}
