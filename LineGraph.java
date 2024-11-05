import java.util.List;

public class LineGraph extends Graph {
    private List<Float> xCoordinates;
    private List<Float> yCoordinates;

    public void setxCoordinates(List<Float> xCoordinates) {
        this.xCoordinates = xCoordinates;
    }

    public List<Float> getxCoordinates() {
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
