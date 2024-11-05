public abstract class Graph {
    private String x;

    private String y;
    private String description;

    public Graph() {
    }

    public Graph(String x, String y, String description) {
        this.x = x;
        this.y = y;
        this.description = description;
    }

    public abstract void createGraph();

    public void setX(String x) {
        this.x = x;
    }

    public String getX() {
        return x;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getY() {
        return y;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
