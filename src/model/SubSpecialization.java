package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * Created by rostunov on 03.08.15.
 */
public class SubSpecialization {
    public final StringProperty id, title, main_id, main_name;

    public SubSpecialization() {
        this(null, null, null, null);
    }

    public SubSpecialization(String id, String title, String main_id, String main_name) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.main_id = new SimpleStringProperty(main_id);
        this.main_name = new SimpleStringProperty(main_name);
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

    public String getMain_id() {
        return main_id.get();
    }

    public StringProperty main_idProperty() {
        return main_id;
    }

    public void setMain_id(String main_id) {
        this.main_id.set(main_id);
    }

    public String getMain_name() {
        return main_name.get();
    }

    public StringProperty main_nameProperty() {
        return main_name;
    }

    public void setMain_name(String main_name) {
        this.main_name.set(main_name);
    }

    @Override
    public boolean equals(Object object) {
        String otherGroupCode = "";
        if (object instanceof SubSpecialization) {
            otherGroupCode = ((SubSpecialization)object).getId();
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
