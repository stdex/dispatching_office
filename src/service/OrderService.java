package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import utils.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static utils.DateUtil.convertStringToTimestamp;

/**
 * Created by rostunov on 10.08.15.
 */
public class OrderService {

    public ObservableList<Order> get_list() throws SQLException {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        //df.format(new Date(rs.getTimestamp(12).getTime()))
        ObservableList<Order> tData = FXCollections.observableArrayList();

        String selectFromTableSQL = "SELECT o.id, o.fullname, o.phone, o.address, oa.id as adv_id, oa.title as adv_title, o.reason, oc.id as cat_id, oc.title as cat_title, w.id as worker_id, w.fullname as worker_name, o.work_datetime, o.append_datetime, os.id as status_id, os.title as status_title, os.color as status_color, u.id as user_id, u.fullname as user_name, o.archive FROM orders o LEFT JOIN order_advert oa ON o.advert_source = oa.id LEFT JOIN order_category oc ON o.work_cat = oc.id LEFT JOIN workers w ON o.link_worker = w.id LEFT JOIN order_status os ON o.status = os.id LEFT JOIN users u ON o.link_user_id = u.id WHERE o.archive = 0;";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new Order(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4), Integer.toString(rs.getInt(5)), rs.getString(6), rs.getString(7), Integer.toString(rs.getInt(8)), rs.getString(9), Integer.toString(rs.getInt(10)), rs.getString(11), (rs.getTimestamp(12)!=null)?df.format(new Date(rs.getTimestamp(12).getTime())).toString():null, (rs.getTimestamp(13)!=null)?df.format(new Date(rs.getTimestamp(13).getTime())).toString():null, Integer.toString(rs.getInt(14)), rs.getString(15), rs.getString(16), Integer.toString(rs.getInt(17)), rs.getString(18), Integer.toString(rs.getInt(19)) ));
        }

        return tData;
    }

    public int get_count_all() throws SQLException {

        String countFromTableSQL = "SELECT COUNT(*) AS cnt FROM orders;";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(countFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet result = preparedStatement.executeQuery();

        int count = 0;

        if (result.next()) {
            count = result.getInt("cnt");
        }
        else {
            count = 0;
        }

        return count;
    }

    public int get_count_inwork() throws SQLException {

        String countFromTableSQL = "SELECT COUNT(*) AS cnt FROM orders WHERE archive = 0;";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(countFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet result = preparedStatement.executeQuery();

        int count = 0;

        if (result.next()) {
            count = result.getInt("cnt");
        }
        else {
            count = 0;
        }

        return count;
    }

    public ObservableList<OrderAdvert> get_advert_list() throws SQLException {

        ObservableList<OrderAdvert> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title FROM order_advert");

        while (rs.next()) {
            tData.add(new OrderAdvert(Integer.toString(rs.getInt(1)), rs.getString(2)));
        }

        return tData;
    }

    public ObservableList<OrderCategory> get_category_list() throws SQLException {

        ObservableList<OrderCategory> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title FROM order_category");

        while (rs.next()) {
            tData.add(new OrderCategory(Integer.toString(rs.getInt(1)), rs.getString(2)));
        }

        return tData;
    }

    public ObservableList<OrderStatus> get_status_list() throws SQLException {

        ObservableList<OrderStatus> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title, color FROM order_status");

        while (rs.next()) {
            tData.add(new OrderStatus(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3)));
        }

        return tData;
    }

    public ObservableList<Worker> get_workers_list() throws SQLException {

        ObservableList<Worker> tData = FXCollections.observableArrayList();

        String selectFromTableSQL = "SELECT w.id, w.fullname, w.phone, r.id as region_id, r.title as region_name, s.id as specialization_id, s.title as specialization_name, w.sub_specialization as sub_specialization, w.count_done, w.count_inwork, w.count_wasunavailable, w.count_waschanged, w.count_notperformed FROM workers w LEFT JOIN workers_specialization s ON w.specialization = s.id LEFT JOIN workers_regions r ON w.region = r.id;";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new Worker(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), Integer.toString(rs.getInt(4)), rs.getString(5), Integer.toString(rs.getInt(6)), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13) ));
        }

        return tData;
    }


    public void edit(Order data) throws SQLException, ParseException {

        System.out.println(data);

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = new Date();

        String updateTableSQL = "UPDATE orders SET fullname = ?, phone = ?, address = ?, advert_source = ?, reason = ?, work_cat = ?, link_worker = ?, work_datetime = ?, append_datetime = ?, status = ?, link_user_id = ?, archive = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, data.getFullname());
        preparedStatement.setString(2, data.getPhone());
        preparedStatement.setString(3, data.getAddress());
        preparedStatement.setInt(4, Integer.parseInt(data.getAdvert_source_id()));
        preparedStatement.setString(5, data.getReason());
        preparedStatement.setInt(6, Integer.parseInt(data.getWork_cat_id()));
        preparedStatement.setInt(7, Integer.parseInt(data.getLink_worker_id()));
        if(data.getWork_datetime() == null) {
            preparedStatement.setTimestamp(8, convertStringToTimestamp(dateFormat.format(date)));
        }
        else {
            preparedStatement.setTimestamp(8, new Timestamp(Long.valueOf((data.getWork_datetime()))) );
        }

        if(data.getAppend_datetime() == null) {
            preparedStatement.setTimestamp(9, convertStringToTimestamp(dateFormat.format(date)));
        }
        else {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date ts = df.parse(data.getAppend_datetime());
            preparedStatement.setTimestamp(9, new Timestamp(ts.getTime()));
        }

        //preparedStatement.setTimestamp(8, convertStringToTimestamp(data.getWork_datetime()));
        //preparedStatement.setTimestamp(9, convertStringToTimestamp(data.getAppend_datetime()));

        preparedStatement.setInt(10, Integer.parseInt(data.getStatus_id()));
        preparedStatement.setInt(11, Integer.parseInt(data.getLink_user_id()));
        preparedStatement.setInt(12, Integer.parseInt(data.getArchive()));
        preparedStatement.setInt(13, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());

        preparedStatement.executeUpdate();

    }


    public void add(Order data) throws SQLException {

        System.out.println(data);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        String addToTableSQL = "INSERT INTO orders (fullname, phone, address, advert_source, reason, work_cat, link_worker, work_datetime, append_datetime, status, link_user_id, archive) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setString(1, data.getFullname());
        preparedStatement.setString(2, data.getPhone());
        preparedStatement.setString(3, data.getAddress());
        preparedStatement.setInt(4, Integer.parseInt(data.getAdvert_source_id()));
        preparedStatement.setString(5, data.getReason());
        preparedStatement.setInt(6, Integer.parseInt(data.getWork_cat_id()));
        System.out.println(data.getLink_worker_id());
        if(data.getLink_worker_id() != "") {
            preparedStatement.setInt(7, Integer.parseInt(data.getLink_worker_id()));
        }
        else {
            preparedStatement.setNull(7, java.sql.Types.INTEGER);
        }

        if(data.getWork_datetime() == null) {
            preparedStatement.setTimestamp(8, convertStringToTimestamp(dateFormat.format(date)));
        }
        else {
            preparedStatement.setTimestamp(8, new Timestamp(Long.valueOf((data.getWork_datetime()))) );
        }
        preparedStatement.setTimestamp(9, convertStringToTimestamp(dateFormat.format(date)));
        preparedStatement.setInt(10, Integer.parseInt(data.getStatus_id()));
        preparedStatement.setInt(11, Integer.parseInt(data.getLink_user_id()));
        preparedStatement.setInt(12, Integer.parseInt(data.getArchive()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public void to_archive(Order data) throws SQLException, ParseException {

        System.out.println(data);

        String updateTableSQL = "UPDATE orders SET archive = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getArchive()));
        preparedStatement.setInt(2, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());

        preparedStatement.executeUpdate();

    }

    public void remove(Order data) throws SQLException {

        String removeFromTableSQL = "DELETE FROM orders WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(removeFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public ObservableList<Order> search_orders(String fullname, String phone, String address, String link_worker, String work_cat_id, String status_id, String work_datetime_start, String work_datetime_end) throws SQLException {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        ObservableList<Order> tData = FXCollections.observableArrayList();

        String where = "";
        if (!fullname.equals("")) {
            System.out.println(fullname);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "o.fullname LIKE '%" + fullname + "%'";
        }

        if (!phone.equals("")) {
            System.out.println(phone);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "o.phone LIKE '%" + phone + "%'";
        }

        if (!address.equals("")) {
            System.out.println(address);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "o.address LIKE '%" + address + "%'";
        }

        if (!link_worker.equals("")) {
            System.out.println(link_worker);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.fullname LIKE '%" + link_worker + "%'";
        }

        if (!work_cat_id.equals("")) {
            System.out.println(work_cat_id);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "o.work_cat = " + work_cat_id;
        }

        if (!status_id.equals("")) {
            System.out.println(status_id);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "o.status = " + status_id;
        }

        if (!work_datetime_start.equals("")) {
            System.out.println(work_datetime_start);
            System.out.println(work_datetime_end);
            where += (where.equals("")) ? " WHERE " : " AND ";

            where += "o.work_datetime >= FROM_UNIXTIME(" + work_datetime_start.substring(0, work_datetime_start.length() - 3) + ") AND o.work_datetime <= FROM_UNIXTIME(" + work_datetime_end.substring(0, work_datetime_end.length() - 3) + ")";
        }


        where += (where.equals("")) ? " WHERE " : " AND ";
        where += "o.archive = 0";

        String selectFromTableSQL = "SELECT o.id, o.fullname, o.phone, o.address, oa.id as adv_id, oa.title as adv_title, o.reason, oc.id as cat_id, oc.title as cat_title, w.id as worker_id, w.fullname as worker_name, o.work_datetime, o.append_datetime, os.id as status_id, os.title as status_title, os.color as status_color, u.id as user_id, u.fullname as user_name, o.archive FROM orders o LEFT JOIN order_advert oa ON o.advert_source = oa.id LEFT JOIN order_category oc ON o.work_cat = oc.id LEFT JOIN workers w ON o.link_worker = w.id LEFT JOIN order_status os ON o.status = os.id LEFT JOIN users u ON o.link_user_id = u.id "+ where;

        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new Order(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4), Integer.toString(rs.getInt(5)), rs.getString(6), rs.getString(7), Integer.toString(rs.getInt(8)), rs.getString(9), Integer.toString(rs.getInt(10)), rs.getString(11), (rs.getTimestamp(12)!=null)?df.format(new Date(rs.getTimestamp(12).getTime())).toString():null, (rs.getTimestamp(13)!=null)?df.format(new Date(rs.getTimestamp(13).getTime())).toString():null, Integer.toString(rs.getInt(14)), rs.getString(15), rs.getString(16), Integer.toString(rs.getInt(17)), rs.getString(18), Integer.toString(rs.getInt(19)) ));
        }


        return tData;
    }

}
