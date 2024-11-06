public class Entry {
    private String type; // the type is set a string that can hold the value "STRING", "INTEGER", "FLOAT"
    private Object value; // can be of any type such as integer, float or string depending on the data set
    private String field; // the name of the column using the keys from the config file

    public Entry() {
    }

    public Entry(String type, Object value, String field) {
        this.field = field;
        this.value = value;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        return field + " = " + value + " is of type: " + type;
    }
}