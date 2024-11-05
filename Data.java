import java.util.List;

public class Data {
    private List<Entry> row;

    public Data() {

    }

    public Data(List<Entry> row) {
        this.row = row;
    }

    public List<Entry> getRow() {
        return row;
    }

    public void setRow(List<Entry> row) {
        this.row = row;
    }

}
