// package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Studio {
    private SimpleIntegerProperty id;
    private SimpleStringProperty namaStudio;

    public Studio(int id, String namaStudio) {
        this.id = new SimpleIntegerProperty(id);
        this.namaStudio = new SimpleStringProperty(namaStudio);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getNamaStudio() {
        return namaStudio.get();
    }

    public SimpleStringProperty namaStudioProperty() {
        return namaStudio;
    }
}
