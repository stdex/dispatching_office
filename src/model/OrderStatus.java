package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * Created by rostunov on 03.08.15.
 */
public class OrderStatus {
    public final StringProperty id, title, color;

    public OrderStatus() {
        this(null, null, null);
    }

    public OrderStatus(String id, String title, String color) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.color = new SimpleStringProperty(color);
    }

    public String getColor() {
        return color.get();
    }

    public StringProperty colorProperty() {
        return color;
    }

    public void setColor(String color) {
        this.color.set(color);
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
        if (object instanceof OrderStatus) {
            otherGroupCode = ((OrderStatus)object).getId();
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
