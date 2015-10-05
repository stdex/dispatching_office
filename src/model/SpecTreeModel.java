package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by rostunov on 17.09.15.
 */
public class SpecTreeModel {

    public final StringProperty title, cat, cat_id, spec_id;

    public SpecTreeModel() {
        this(null, null, null, null);
    }

    public SpecTreeModel(String title, String cat, String cat_id, String spec_id) {
        this.title = new SimpleStringProperty(title);
        this.cat = new SimpleStringProperty(cat);
        this.cat_id = new SimpleStringProperty(cat_id);
        this.spec_id = new SimpleStringProperty(spec_id);
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

    public String getCat() {
        return cat.get();
    }

    public StringProperty catProperty() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat.set(cat);
    }

    public String getCat_id() {
        return cat_id.get();
    }

    public StringProperty cat_idProperty() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id.set(cat_id);
    }

    public String getSpec_id() {
        return spec_id.get();
    }

    public StringProperty spec_idProperty() {
        return spec_id;
    }

    public void setSpec_id(String spec_id) {
        this.spec_id.set(spec_id);
    }

    @Override
    public boolean equals(Object object) {
        String otherGroupCode = "";
        if (object instanceof SpecTreeModel) {
            otherGroupCode = ((SpecTreeModel)object).getTitle();
        } else if(object instanceof String){
            otherGroupCode = (String)object;
        } else {
            return false;
        }

        if ((this.getTitle() == null && otherGroupCode != null) || (this.getTitle() != null && !this.getTitle().equals(otherGroupCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }

}
