import java.util.List;

public class BarGraph extends Graph {
    private List<String> xCoordinates;
    private List<Float> yCoordinates;

    public void setxCoordinates(List<String> xCoordinates) {
        this.xCoordinates = xCoordinates;
    }

    public List<String> getxCoordinates() {
        return xCoordinates;
    }

    public void setyCoordinates(List<Float> yCoordinates) {
        this.yCoordinates = yCoordinates;
    }

    public List<Float> getyCoordinates() {
        return yCoordinates;
    }

    @Override
    public void createGraph() {
    }
}
