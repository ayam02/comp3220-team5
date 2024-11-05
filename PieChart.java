import java.util.ArrayList;
import java.util.List;

public class PieChart extends Graph {
    private List<Float> numbers;
    private List<String> labels;

    public PieChart() {
        List<Float> numbers = new ArrayList<Float>();
        List<String> labels = new ArrayList<String>();
    }

    public PieChart(List<Float> numbers, List<String> labels) {
        this.labels = labels;
        this.numbers = numbers;
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<Float> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Float> numbers) {
        this.numbers = numbers;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public void createGraph() {
    }

}
