package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Worker {

    public final StringProperty id;
    public final StringProperty fullname;
    public final StringProperty phone;
    public final StringProperty region_id;
    public final StringProperty specialization_id;
    public final StringProperty specialization_name;
    public final StringProperty sub_specialization;
    public final StringProperty count_done;
    public final StringProperty count_inwork;
    public final StringProperty count_wasunavailable;
    public final StringProperty count_waschanged;
    public final StringProperty count_notperformed;
    public final StringProperty blacklist;
    public final StringProperty annotation;
    public Image photo;
    public final StringProperty has_photo;
    public final StringProperty summ_price;

    public Worker() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public Worker(String id, String fullname, String phone, String region_id, String specialization_id, String specialization_name, String sub_specialization, String count_done, String count_inwork, String count_wasunavailable, String count_waschanged, String count_notperformed, String blacklist, String annotation, Image photo, String has_photo, String summ_price) {

        this.id = new SimpleStringProperty(id);
        this.fullname = new SimpleStringProperty(fullname);
        this.phone = new SimpleStringProperty(phone);
        this.region_id = new SimpleStringProperty(region_id);
        this.specialization_id = new SimpleStringProperty(specialization_id);
        this.specialization_name = new SimpleStringProperty(specialization_name);
        this.sub_specialization = new SimpleStringProperty(sub_specialization);
        this.count_done = new SimpleStringProperty(count_done);
        this.count_inwork = new SimpleStringProperty(count_inwork);
        this.count_wasunavailable = new SimpleStringProperty(count_wasunavailable);
        this.count_waschanged = new SimpleStringProperty(count_waschanged);
        this.count_notperformed = new SimpleStringProperty(count_notperformed);
        this.blacklist = new SimpleStringProperty(blacklist);
        this.annotation = new SimpleStringProperty(annotation);
        this.photo = photo;
        this.has_photo = new SimpleStringProperty(has_photo);
        this.summ_price = new SimpleStringProperty(summ_price);
    }

    public String getSumm_price() {
        return summ_price.get();
    }

    public StringProperty summ_priceProperty() {
        return summ_price;
    }

    public void setSumm_price(String summ_price) {
        this.summ_price.set(summ_price);
    }

    public String getHas_photo() {
        return has_photo.get();
    }

    public StringProperty has_photoProperty() {
        return has_photo;
    }

    public void setHas_photo(String has_photo) {
        this.has_photo.set(has_photo);
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

    public String getFullname() {
        return fullname.get();
    }

    public StringProperty fullnameProperty() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname.set(fullname);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getRegion_id() {
        return region_id.get();
    }

    public StringProperty region_idProperty() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id.set(region_id);
    }


    public String getSpecialization_id() {
        return specialization_id.get();
    }

    public StringProperty specialization_idProperty() {
        return specialization_id;
    }

    public void setSpecialization_id(String specialization_id) {
        this.specialization_id.set(specialization_id);
    }

    public String getSpecialization_name() {
        return specialization_name.get();
    }

    public StringProperty specialization_nameProperty() {
        return specialization_name;
    }

    public void setSpecialization_name(String specialization_name) {
        this.specialization_name.set(specialization_name);
    }

    public String getSub_specialization() {
        return sub_specialization.get();
    }

    public StringProperty sub_specializationProperty() {
        return sub_specialization;
    }

    public void setSub_specialization(String sub_specialization) {
        this.sub_specialization.set(sub_specialization);
    }

    public String getCount_done() {
        return count_done.get();
    }

    public StringProperty count_doneProperty() {
        return count_done;
    }

    public void setCount_done(String count_done) {
        this.count_done.set(count_done);
    }

    public String getCount_inwork() {
        return count_inwork.get();
    }

    public StringProperty count_inworkProperty() {
        return count_inwork;
    }

    public void setCount_inwork(String count_inwork) {
        this.count_inwork.set(count_inwork);
    }

    public String getCount_wasunavailable() {
        return count_wasunavailable.get();
    }

    public StringProperty count_wasunavailableProperty() {
        return count_wasunavailable;
    }

    public void setCount_wasunavailable(String count_wasunavailable) {
        this.count_wasunavailable.set(count_wasunavailable);
    }

    public String getCount_waschanged() {
        return count_waschanged.get();
    }

    public StringProperty count_waschangedProperty() {
        return count_waschanged;
    }

    public void setCount_waschanged(String count_waschanged) {
        this.count_waschanged.set(count_waschanged);
    }

    public String getCount_notperformed() {
        return count_notperformed.get();
    }

    public StringProperty count_notperformedProperty() {
        return count_notperformed;
    }

    public void setCount_notperformed(String count_notperformed) {
        this.count_notperformed.set(count_notperformed);
    }


    public String getBlacklist() {
        return blacklist.get();
    }

    public StringProperty blacklistProperty() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist.set(blacklist);
    }

    public String getAnnotation() {
        return annotation.get();
    }

    public StringProperty annotationProperty() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation.set(annotation);
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
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
        if (object instanceof Worker) {
            otherGroupCode = ((Worker)object).getId();
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
        return this.getFullname();
    }


}
