package ninja.donhk.model;

import javafx.beans.property.SimpleStringProperty;

public class FileRecord {

    private final SimpleStringProperty nameBean = new SimpleStringProperty("");
    private final SimpleStringProperty pathBean = new SimpleStringProperty("");

    public FileRecord() {
        this("", "");
    }

    public FileRecord(String name, String path) {
        setName(name);
        setPath(path);
    }

    public void setName(String val) {
        nameBean.set(val);
    }

    public void setPath(String val) {
        pathBean.set(val);
    }

    public String getNameBean() {
        return nameBean.getValue();
    }

    public String getPathBean() {
        return pathBean.getValue();
    }
}
