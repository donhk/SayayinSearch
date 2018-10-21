package ninja.donhk.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FileRecord {

    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty path = new SimpleStringProperty("");

    public FileRecord() {
        this("", "");
    }

    public FileRecord(String name, String path) {
        setName(name);
        setPath(path);
    }

    public void setName(String val) {
        name.set(val);
    }

    public void setPath(String val) {
        path.set(val);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty pathProperty() {
        return path;
    }
}
