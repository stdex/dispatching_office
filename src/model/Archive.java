package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by rostunov on 03.10.15.
 */
public class Archive {


    public final StringProperty id, title;

    public Archive() {
        this(null, null);
    }

    public Archive(String id, String title) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    @Override
    public boolean equals(Object object) {
        String otherGroupCode = "";
        if (object instanceof Region) {
            otherGroupCode = ((Region)object).getId();
        } else if(object instanceof String){
            otherGroupCode = (String)object;
        } else {
            return false;
        }

        if ((this.getId() == null && otherGroupCode != null) || (this.getId() != null && !this.getId().equals(otherGroupCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
