package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by rostunov on 20.09.15.
 */
public class OrderTimeline {

    public final StringProperty id;
    public final StringProperty id_order;
    public final StringProperty id_status;
    public final StringProperty status;
    public final StringProperty status_color;
    public final StringProperty link_worker_id;
    public final StringProperty link_worker;
    public final StringProperty link_user_id;
    public final StringProperty link_user;
    public final StringProperty dt;
    public final StringProperty reason;


    public OrderTimeline() {
        this(null, null, null, null, null, null, null, null, null, null, null);
    }

    public OrderTimeline(String id, String id_order, String id_status, String status,  String status_color, String link_worker_id, String link_worker, String link_user_id, String link_user, String dt, String reason) {

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        this.id = new SimpleStringProperty(id);
        this.id_order = new SimpleStringProperty(id_order);
        this.id_status = new SimpleStringProperty(id_status);
        this.status = new SimpleStringProperty(status);
        this.status_color = new SimpleStringProperty(status_color);
        this.link_worker_id = new SimpleStringProperty(link_worker_id);
        this.link_worker = new SimpleStringProperty(link_worker);
        this.link_user_id = new SimpleStringProperty(link_user_id);
        this.link_user = new SimpleStringProperty(link_user);
        this.dt = new SimpleStringProperty(dt);
        this.reason = new SimpleStringProperty(reason);

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

    public String getId_order() {
        return id_order.get();
    }

    public StringProperty id_orderProperty() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order.set(id_order);
    }

    public String getId_status() {
        return id_status.get();
    }

    public StringProperty id_statusProperty() {
        return id_status;
    }

    public void setId_status(String id_status) {
        this.id_status.set(id_status);
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

    public String getStatus_color() {
        return status_color.get();
    }

    public StringProperty status_colorProperty() {
        return status_color;
    }

    public void setStatus_color(String status_color) {
        this.status_color.set(status_color);
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

    public String getDt() {
        return dt.get();
    }

    public StringProperty dtProperty() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt.set(dt);
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


}
