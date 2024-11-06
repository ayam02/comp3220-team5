import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<Entry> row;

    public Data() {
        row = new ArrayList<Entry>();
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

    public void addToRow(Entry entry) {
        row.add(entry);
    }

}
