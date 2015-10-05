package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Order {

    public final StringProperty id;
    public final StringProperty fullname;
    public final StringProperty phone;
    public final StringProperty address;
    public final StringProperty advert_source_id;
    public final StringProperty advert_source;
    public final StringProperty reason;
    public final StringProperty work_cat_id;
    public final StringProperty work_cat;
    public final StringProperty link_worker_id;
    public final StringProperty link_worker;
    public final StringProperty work_datetime;
    public final StringProperty append_datetime;
    public final StringProperty status_id;
    public final StringProperty status;
    public final StringProperty status_color;
    public final StringProperty link_user_id;
    public final StringProperty link_user;
    public final StringProperty archive;
    public final StringProperty region;
    public final StringProperty region_name;
    public final StringProperty start_time;
    public final StringProperty end_time;
    public final StringProperty price;
    public final StringProperty link_worker_phone;
    public final StringProperty annotation;
    public final StringProperty timer_str;

    public Order() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }


    public Order(String id, String fullname, String phone, String address,  String advert_source_id, String advert_source, String reason, String work_cat_id, String work_cat, String link_worker_id, String link_worker, String work_datetime, String append_datetime, String status_id, String status, String status_color, String link_user_id, String link_user, String archive, String region, String region_name, String start_time, String end_time, String price, String link_worker_phone, String annotation, String timer_str) {

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        this.id = new SimpleStringProperty(id);
        this.fullname = new SimpleStringProperty(fullname);
        this.phone = new SimpleStringProperty(phone);
        this.address = new SimpleStringProperty(address);
        this.advert_source_id = new SimpleStringProperty(advert_source_id);
        this.advert_source = new SimpleStringProperty(advert_source);
        this.reason = new SimpleStringProperty(reason);
        this.work_cat_id = new SimpleStringProperty(work_cat_id);
        this.work_cat = new SimpleStringProperty(work_cat);
        this.link_worker_id = new SimpleStringProperty(link_worker_id);
        this.link_worker = new SimpleStringProperty(link_worker);
        this.work_datetime = new SimpleStringProperty(work_datetime);
        this.append_datetime = new SimpleStringProperty(append_datetime);
        this.status_id = new SimpleStringProperty(status_id);
        this.status = new SimpleStringProperty(status);
        this.status_color = new SimpleStringProperty(status_color);
        this.link_user_id = new SimpleStringProperty(link_user_id);
        this.link_user = new SimpleStringProperty(link_user);
        this.archive = new SimpleStringProperty(archive);
        this.region = new SimpleStringProperty(region);
        this.region_name = new SimpleStringProperty(region_name);
        this.start_time = new SimpleStringProperty(start_time);
        this.end_time = new SimpleStringProperty(end_time);
        this.price = new SimpleStringProperty(price);
        this.link_worker_phone = new SimpleStringProperty(link_worker_phone);
        this.annotation = new SimpleStringProperty(annotation);
        this.timer_str = new SimpleStringProperty(timer_str);

    }

    public String getRegion_name() {
        return region_name.get();
    }

    public StringProperty region_nameProperty() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name.set(region_name);
    }

    public String getTimer_str() {
        return timer_str.get();
    }

    public StringProperty timer_strProperty() {
        return timer_str;
    }

    public void setTimer_str(String timer_str) {
        this.timer_str.set(timer_str);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", fullname=" + fullname +
                ", phone=" + phone +
                ", address=" + address +
                ", advert_source_id=" + advert_source_id +
                ", advert_source=" + advert_source +
                ", reason=" + reason +
                ", work_cat_id=" + work_cat_id +
                ", work_cat=" + work_cat +
                ", link_worker_id=" + link_worker_id +
                ", link_worker=" + link_worker +
                ", work_datetime=" + work_datetime +
                ", append_datetime=" + append_datetime +
                ", status_id=" + status_id +
                ", status=" + status +
                ", status_color=" + status_color +
                ", link_user_id=" + link_user_id +
                ", link_user=" + link_user +
                ", archive=" + archive +
                ", region=" + region +
                ", region=" + region_name +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", price=" + price +
                ", link_worker_phone=" + link_worker_phone +
                ", annotation=" + annotation +
                ", timer_str=" + timer_str +
                '}';
    }

    public String getStatus_color() {
        return status_color.get();
    }

    public StringProperty status_colorProperty() {
        return status_color;
    }

    public void setStatus_color(String status_color) {
        this.status_color.set(status_color);
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

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getAdvert_source_id() {
        return advert_source_id.get();
    }

    public StringProperty advert_source_idProperty() {
        return advert_source_id;
    }

    public void setAdvert_source_id(String advert_source_id) {
        this.advert_source_id.set(advert_source_id);
    }

    public String getAdvert_source() {
        return advert_source.get();
    }

    public StringProperty advert_sourceProperty() {
        return advert_source;
    }

    public void setAdvert_source(String advert_source) {
        this.advert_source.set(advert_source);
    }

    public String getReason() {
        return reason.get();
    }

    public StringProperty reasonProperty() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason.set(reason);
    }

    public String getWork_cat_id() {
        return work_cat_id.get();
    }

    public StringProperty work_cat_idProperty() {
        return work_cat_id;
    }

    public void setWork_cat_id(String work_cat_id) {
        this.work_cat_id.set(work_cat_id);
    }

    public String getWork_cat() {
        return work_cat.get();
    }

    public StringProperty work_catProperty() {
        return work_cat;
    }

    public void setWork_cat(String work_cat) {
        this.work_cat.set(work_cat);
    }

    public String getLink_worker_id() {
        return link_worker_id.get();
    }

    public StringProperty link_worker_idProperty() {
        return link_worker_id;
    }

    public void setLink_worker_id(String link_worker_id) {
        this.link_worker_id.set(link_worker_id);
    }

    public String getLink_worker() {
        return link_worker.get();
    }

    public StringProperty link_workerProperty() {
        return link_worker;
    }

    public void setLink_worker(String link_worker) {
        this.link_worker.set(link_worker);
    }

    public String getWork_datetime() {
        return work_datetime.get();
    }

    public StringProperty work_datetimeProperty() {
        return work_datetime;
    }

    public void setWork_datetime(String work_datetime) {
        this.work_datetime.set(work_datetime);
    }

    public String getAppend_datetime() {
        return append_datetime.get();
    }

    public StringProperty append_datetimeProperty() {
        return append_datetime;
    }

    public void setAppend_datetime(String append_datetime) {
        this.append_datetime.set(append_datetime);
    }

    public String getStatus_id() {
        return status_id.get();
    }

    public StringProperty status_idProperty() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id.set(status_id);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getLink_user_id() {
        return link_user_id.get();
    }

    public StringProperty link_user_idProperty() {
        return link_user_id;
    }

    public void setLink_user_id(String link_user_id) {
        this.link_user_id.set(link_user_id);
    }

    public String getLink_user() {
        return link_user.get();
    }

    public StringProperty link_userProperty() {
        return link_user;
    }

    public void setLink_user(String link_user) {
        this.link_user.set(link_user);
    }

    public String getArchive() {
        return archive.get();
    }

    public StringProperty archiveProperty() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive.set(archive);
    }

    public String getRegion() {
        return region.get();
    }

    public StringProperty regionProperty() {
        return region;
    }

    public void setRegion(String region) {
        this.region.set(region);
    }

    public String getRegionName() {
        return region_name.get();
    }

    public StringProperty regionNameProperty() {
        return region_name;
    }

    public void setRegionName(String region_name) {
        this.region_name.set(region_name);
    }


    public String getStart_time() {
        return start_time.get();
    }

    public StringProperty start_timeProperty() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time.set(start_time);
    }

    public String getEnd_time() {
        return end_time.get();
    }

    public StringProperty end_timeProperty() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time.set(end_time);
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }


    public String getLink_worker_phone() {
        return link_worker_phone.get();
    }

    public StringProperty link_worker_phoneProperty() {
        return link_worker_phone;
    }

    public void setLink_worker_phone(String link_worker_phone) {
        this.link_worker_phone.set(link_worker_phone);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return !(id != null ? !id.equals(order.id) : order.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}