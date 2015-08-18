package model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by rostunov on 03.08.15.
 */
public class UserGroup {

    public final String id, group_name;

    public UserGroup() {
        this(null, null);
    }

    public UserGroup(String id, String group_name) {
        this.id = id;
        this.group_name = group_name;
    }

    public String getId() {
        return id;
    }

    public String getGroup_name() {
        return group_name;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        String otherGroupCode = "";
        if (object instanceof UserGroup) {
            otherGroupCode = ((UserGroup)object).id;
        } else if(object instanceof String){
            otherGroupCode = (String)object;
        } else {
            return false;
        }

        if ((this.id == null && otherGroupCode != null) || (this.id != null && !this.id.equals(otherGroupCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return group_name;
    }


}
