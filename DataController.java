public class DataController {
  private DataManager dataManager;

  public DataController(Data data){
    this.dataManager = new DataManager(data);
    setupController();
  }
  private void setupController(){
  }
}
