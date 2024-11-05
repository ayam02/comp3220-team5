import java.util.ArrayList;
import java.util.List;

public class LineGraph extends Graph {
    private List<Float> xCoordinates;
    private List<Float> yCoordinates;

    public LineGraph(List<Float> xCoordinates, List<Float> yCoordinates) {
        this.xCoordinates = xCoordinates;
        this.yCoordinates = yCoordinates;
    }

    public LineGraph() {
        xCoordinates = new ArrayList<>();
        yCoordinates = new ArrayList<>();
    }

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
