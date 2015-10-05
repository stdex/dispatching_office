package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import com.mysql.jdbc.Statement;
import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import model.*;
import utils.Database;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

        String order_by = " ORDER BY o.id DESC";

        String selectFromTableSQL = "SELECT o.id, o.fullname, o.phone, o.address, oa.id as adv_id, oa.title as adv_title, o.reason, oc.id as cat_id, oc.title as cat_title, w.id as worker_id, w.fullname as worker_name, o.work_datetime, o.append_datetime, os.id as status_id, os.title as status_title, os.color as status_color, u.id as user_id, u.fullname as user_name, o.archive, o.region, wr.title as region_name, o.start_time, o.end_time, o.price, w.phone as worker_phone, o.annotation as annotation FROM orders o LEFT JOIN order_advert oa ON o.advert_source = oa.id LEFT JOIN order_category oc ON o.work_cat = oc.id LEFT JOIN workers w ON o.link_worker = w.id LEFT JOIN order_status os ON o.status = os.id LEFT JOIN users u ON o.link_user_id = u.id LEFT JOIN workers_regions wr ON o.region = wr.id WHERE o.archive = 0 " + order_by;
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new Order(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4), Integer.toString(rs.getInt(5)), rs.getString(6), rs.getString(7), Integer.toString(rs.getInt(8)), rs.getString(9), Integer.toString(rs.getInt(10)), rs.getString(11), (rs.getTimestamp(12)!=null)?df.format(new Date(rs.getTimestamp(12).getTime())).toString():null, (rs.getTimestamp(13)!=null)?df.format(new Date(rs.getTimestamp(13).getTime())).toString():null, Integer.toString(rs.getInt(14)), rs.getString(15), rs.getString(16), Integer.toString(rs.getInt(17)), rs.getString(18), Integer.toString(rs.getInt(19)), Integer.toString(rs.getInt(20)), rs.getString(21), rs.getString(22), rs.getString(23), rs.getString(24), rs.getString(25), rs.getString(26), "" ) );
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

    public ObservableList<Worker> get_workers_list() throws SQLException, IOException {

        ObservableList<Worker> tData = FXCollections.observableArrayList();

        String selectFromTableSQL = "SELECT w.id, w.fullname, w.phone, r.id as region_id, s.id as specialization_id, s.title as specialization_name, w.sub_specialization as sub_specialization, w.count_done, (SELECT COUNT(*) FROM orders WHERE link_worker = w.id AND (status = 3 OR status = 4 OR status = 5 OR status = 8 OR status = 9) AND archive = 0) as count_inwork, w.count_wasunavailable, w.count_waschanged, w.count_notperformed, w.blacklist, w.annotation, w.photo, w.summ_price FROM workers w LEFT JOIN workers_specialization s ON w.specialization = s.id LEFT JOIN workers_regions r ON w.region = r.id;";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {

            Image photo;
            String has_photo;

            if(rs.getBinaryStream((15)) == null) {
                BufferedImage bi = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, 150, 150);
                photo = SwingFXUtils.toFXImage(bi, null);
                has_photo = "0";
            }
            else {
                photo = SwingFXUtils.toFXImage(ImageIO.read(rs.getBinaryStream((15))), null);
                has_photo = "1";
            }

            tData.add(new Worker(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4), Integer.toString(rs.getInt(5)), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), Integer.toString(rs.getInt(13)), rs.getString(14), photo, has_photo, Integer.toString(rs.getInt(16)) ));
        }

        return tData;
    }


    public void edit(Order data) throws SQLException, ParseException {

        System.out.println(data);

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = new Date();

        String updateTableSQL = "UPDATE orders SET fullname = ?, phone = ?, address = ?, advert_source = ?, reason = ?, work_cat = ?, link_worker = ?, status = ?, link_user_id = ?, archive = ?, region = ?, start_time = ?, end_time = ?, price = ?, annotation = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, data.getFullname());
        preparedStatement.setString(2, data.getPhone());
        preparedStatement.setString(3, data.getAddress());
        if(!data.getAdvert_source_id().equals("0")) {
            preparedStatement.setInt(4, Integer.parseInt(data.getAdvert_source_id()));
        }
        else {
            preparedStatement.setNull(4, java.sql.Types.INTEGER);
        }
        preparedStatement.setString(5, data.getReason());
        preparedStatement.setInt(6, Integer.parseInt(data.getWork_cat_id()));
        System.out.println(data.getLink_worker_id());
        if(data.getLink_worker_id() != "") {
            preparedStatement.setInt(7, Integer.parseInt(data.getLink_worker_id()));
        }
        else {
            preparedStatement.setNull(7, java.sql.Types.INTEGER);
        }

        //preparedStatement.setInt(7, Integer.parseInt(data.getLink_worker_id()));
        /*
        if(data.getWork_datetime() == null) {
            preparedStatement.setTimestamp(8, convertStringToTimestamp(dateFormat.format(date)));
        }
        else {
            preparedStatement.setTimestamp(8, new Timestamp(Long.valueOf((data.getWork_datetime()))) );
        }

        if(data.getWork_datetime() == null) {
            preparedStatement.setTimestamp(8, convertStringToTimestamp(dateFormat.format(date)));
        }
        else {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date ts = df.parse(data.getAppend_datetime());
            preparedStatement.setTimestamp(8, new Timestamp(ts.getTime()));
        }


        if(data.getAppend_datetime() == null) {
            preparedStatement.setTimestamp(9, convertStringToTimestamp(dateFormat.format(date)));
        }
        else {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date ts = df.parse(data.getAppend_datetime());
            preparedStatement.setTimestamp(9, new Timestamp(ts.getTime()));
        }
        */
        //preparedStatement.setTimestamp(8, convertStringToTimestamp(data.getWork_datetime()));
        //preparedStatement.setTimestamp(9, convertStringToTimestamp(data.getAppend_datetime()));

        preparedStatement.setInt(8, Integer.parseInt(data.getStatus_id()));
        preparedStatement.setInt(9, Integer.parseInt(data.getLink_user_id()));
        preparedStatement.setInt(10, Integer.parseInt(data.getArchive()));
        if(data.getRegion() != null) {
            preparedStatement.setInt(11, Integer.parseInt(data.getRegion()));
        }
        else {
            preparedStatement.setNull(11, java.sql.Types.INTEGER);
        }
        preparedStatement.setString(12, data.getStart_time());
        preparedStatement.setString(13, data.getEnd_time());
        preparedStatement.setString(14, data.getPrice());
        preparedStatement.setString(15, data.getAnnotation());
        preparedStatement.setInt(16, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());

        preparedStatement.executeUpdate();

    }


    public String add(Order data) throws SQLException {

        System.out.println(data);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        String addToTableSQL = "INSERT INTO orders (fullname, phone, address, advert_source, reason, work_cat, link_worker, work_datetime, append_datetime, status, link_user_id, archive, region, start_time, end_time, price, annotation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, data.getFullname());
        preparedStatement.setString(2, data.getPhone());
        preparedStatement.setString(3, data.getAddress());
        if(!data.getAdvert_source_id().equals("0") && !data.getAdvert_source_id().equals("")) {
            preparedStatement.setInt(4, Integer.parseInt(data.getAdvert_source_id()));
        }
        else {
            preparedStatement.setNull(4, java.sql.Types.INTEGER);
        }
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
        if(data.getRegion() != null) {
            preparedStatement.setInt(13, Integer.parseInt(data.getRegion()));
        }
        else {
            preparedStatement.setNull(13, java.sql.Types.INTEGER);
        }
        preparedStatement.setString(14, data.getStart_time());
        preparedStatement.setString(15, data.getEnd_time());
        preparedStatement.setString(16, data.getPrice());
        preparedStatement.setString(17, data.getAnnotation());
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        int affectedRows = preparedStatement.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating order failed, no rows affected.");
        }

        String order_id = "";

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                order_id = String.valueOf(generatedKeys.getLong(1));
            }
            else {
                order_id = "";
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        }

        return order_id;

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

    public void set_archive(Order data) throws SQLException, ParseException {

        System.out.println(data);

        String updateTableSQL = "UPDATE orders SET archive = 1 WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());

        preparedStatement.executeUpdate();

    }

    public void set_work_datetime(Order data) throws SQLException, ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        String updateTableSQL = "UPDATE orders SET work_datetime = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setTimestamp(1, convertStringToTimestamp(dateFormat.format(date)));
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


    public ObservableList<Order> search_orders(String fullname, String phone, String address, String link_worker, String work_cat_id, String status_id, String append_datetime_start, String append_datetime_end, String archive, String orderid) throws SQLException {

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

        if (!archive.equals("")) {
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "o.archive = " + archive;
        }

        if (!orderid.equals("")) {
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "o.id = " + orderid;
        }
        /*
        else {
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "o.archive = 0";
        }
        */

        if (!append_datetime_start.equals("")) {
            System.out.println(append_datetime_start);
            System.out.println(append_datetime_end);
            where += (where.equals("")) ? " WHERE " : " AND ";

            where += "o.append_datetime >= FROM_UNIXTIME(" + append_datetime_start.substring(0, append_datetime_start.length() - 3) + ") AND o.work_datetime <= FROM_UNIXTIME(" + append_datetime_end.substring(0, append_datetime_end.length() - 3) + ")";
        }

        /*
        where += (where.equals("")) ? " WHERE " : " AND ";
        where += "o.archive = 0";
        */

        String order_by = " ORDER BY o.id DESC";

        String selectFromTableSQL = "SELECT o.id, o.fullname, o.phone, o.address, oa.id as adv_id, oa.title as adv_title, o.reason, oc.id as cat_id, oc.title as cat_title, w.id as worker_id, w.fullname as worker_name, o.work_datetime, o.append_datetime, os.id as status_id, os.title as status_title, os.color as status_color, u.id as user_id, u.fullname as user_name, o.archive, o.region, wr.title as region_name, o.start_time, o.end_time, o.price, w.phone as worker_phone, o.annotation as annotation FROM orders o LEFT JOIN order_advert oa ON o.advert_source = oa.id LEFT JOIN order_category oc ON o.work_cat = oc.id LEFT JOIN workers w ON o.link_worker = w.id LEFT JOIN order_status os ON o.status = os.id LEFT JOIN users u ON o.link_user_id = u.id LEFT JOIN workers_regions wr ON o.region = wr.id "+ where + order_by;

        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new Order(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4), Integer.toString(rs.getInt(5)), rs.getString(6), rs.getString(7), Integer.toString(rs.getInt(8)), rs.getString(9), Integer.toString(rs.getInt(10)), rs.getString(11), (rs.getTimestamp(12)!=null)?df.format(new Date(rs.getTimestamp(12).getTime())).toString():null, (rs.getTimestamp(13)!=null)?df.format(new Date(rs.getTimestamp(13).getTime())).toString():null, Integer.toString(rs.getInt(14)), rs.getString(15), rs.getString(16), Integer.toString(rs.getInt(17)), rs.getString(18), Integer.toString(rs.getInt(19)), Integer.toString(rs.getInt(20)), rs.getString(21), rs.getString(22), rs.getString(23), rs.getString(24), rs.getString(25), rs.getString(26), "" ) );
        }


        return tData;
    }

}
