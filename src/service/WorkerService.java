package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import controller.MainApp;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import model.Region;
import model.Specialization;
import model.SubSpecialization;
import model.Worker;
import utils.Database;
import utils.MD5;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class WorkerService {


    public ObservableList<Worker> get_list() throws SQLException, IOException {

        ObservableList<Worker> tData = FXCollections.observableArrayList();

        String selectFromTableSQL = "SELECT w.id, w.fullname, w.phone, w.region as region_id, w.specialization as specialization_id, s.title as specialization_name, w.sub_specialization as sub_specialization, w.count_done, (SELECT COUNT(*) FROM orders WHERE link_worker = w.id AND (status = 3 OR status = 4 OR status = 5 OR status = 8 OR status = 9) AND archive = 0) as count_inwork, w.count_wasunavailable, w.count_waschanged, w.count_notperformed, w.blacklist, w.annotation, w.photo, w.summ_price FROM workers w LEFT JOIN workers_specialization s ON w.specialization = s.id;";
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

            tData.add(new Worker(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), Integer.toString(rs.getInt(13)), rs.getString(14), photo, has_photo, Integer.toString(rs.getInt(16)) ));

        }

        return tData;
    }

    public ObservableList<Specialization> get_specialization_list() throws SQLException {

        ObservableList<Specialization> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title FROM workers_specialization");

        while (rs.next()) {
            tData.add(new Specialization(Integer.toString(rs.getInt(1)), rs.getString(2)));
        }

        return tData;
    }

    public ObservableList<SubSpecialization> get_sub_specialization_list() throws SQLException {

        ObservableList<SubSpecialization> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT ss.id, ss.title, ss.main_specialization, s.id as main_id, s.title as main_name FROM workers_sub_specialization ss LEFT JOIN workers_specialization s ON ss.main_specialization = s.id");

        while (rs.next()) {
            tData.add(new SubSpecialization(Integer.toString(rs.getInt(1)), rs.getString(2), Integer.toString(rs.getInt(3)), rs.getString(5)  ));
        }

        return tData;
    }

    public ObservableList<Region> get_region_list() throws SQLException {

        ObservableList<Region> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title FROM workers_regions");

        while (rs.next()) {
            tData.add(new Region(Integer.toString(rs.getInt(1)), rs.getString(2)));
        }

        return tData;
    }

    public ObservableList<SubSpecialization> get_csubspec_list(String current_choose_id) throws SQLException {

        ObservableList<SubSpecialization> tData = FXCollections.observableArrayList();

        String selectFromTableSQL = "SELECT ss.id, ss.title, ss.main_specialization, s.id as main_id, s.title as main_name FROM workers_sub_specialization ss LEFT JOIN workers_specialization s ON ss.main_specialization = s.id WHERE ss.main_specialization = ?;";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        preparedStatement.setString(1, current_choose_id);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new SubSpecialization(Integer.toString(rs.getInt(1)), rs.getString(2), Integer.toString(rs.getInt(3)), rs.getString(5)  ));
        }

        return tData;
    }

    public ObservableList<SubSpecialization> get_wsubspec_list(String worker_subspec) throws SQLException {

        ObservableList<SubSpecialization> tData = FXCollections.observableArrayList();

        String selectFromTableSQL = "SELECT ss.id, ss.title, ss.main_specialization, s.id as main_id, s.title as main_name, s.id as main_id, s.title as main_name FROM workers_sub_specialization ss LEFT JOIN workers_specialization s ON ss.main_specialization = s.id WHERE ss.main_specialization IN (?);";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        preparedStatement.setString(1, worker_subspec);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new SubSpecialization(Integer.toString(rs.getInt(1)), rs.getString(2), Integer.toString(rs.getInt(3)), rs.getString(5)  ));
        }

        return tData;
    }


    public ObservableList<SubSpecialization> get_overview_ss_list(String worker_subspec) throws SQLException {

        ObservableList<SubSpecialization> tData = FXCollections.observableArrayList();

        //String selectFromTableSQL = "SELECT ss.id, ss.title, ss.main_specialization, s.id as main_id, s.title as main_name, s.id as main_id, s.title as main_name FROM workers_sub_specialization ss LEFT JOIN workers_specialization s ON ss.main_specialization = s.id WHERE ss.id IN (?);";
        /*
        int i = 1;
        for(; i <=ids.length; i++){
            ps.setInt(i, ids[i-1]);
        }

        //set null for remaining ones
        for(; i<=PARAM_SIZE;i++){
            ps.setNull(i, java.sql.Types.INTEGER);
        }
         */
        String selectFromTableSQL = "SELECT ss.id, ss.title, ss.main_specialization, s.id as main_id, s.title as main_name, s.id as main_id, s.title as main_name FROM workers_sub_specialization ss LEFT JOIN workers_specialization s ON ss.main_specialization = s.id WHERE ss.id IN ("+worker_subspec+");";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        //preparedStatement.setString(1, worker_subspec);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new SubSpecialization(Integer.toString(rs.getInt(1)), rs.getString(2), Integer.toString(rs.getInt(3)), rs.getString(5)  ));
        }

        return tData;
    }

    public void edit(Worker data) throws SQLException, IOException {

        InputStream fis;
        int slen;
        if(data.getHas_photo().equals("1")) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(data.getPhoto(), null), "png", os);
            fis = new ByteArrayInputStream(os.toByteArray());
            slen = os.toByteArray().length;
        }
        else {
            fis = null;
            slen = 0;
        }

        String updateTableSQL = "UPDATE workers SET fullname = ?, phone = ?, region = ?, specialization = ?, sub_specialization = ?, count_done = ?, count_inwork = ?, count_wasunavailable = ?, count_waschanged = ?, count_notperformed = ?, blacklist = ?, annotation = ?, photo = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, data.getFullname());
        preparedStatement.setString(2, data.getPhone());
        preparedStatement.setString(3, data.getRegion_id());
        preparedStatement.setString(4, data.getSpecialization_id());
        preparedStatement.setString(5, data.getSub_specialization());

        if(data.getCount_done() != null) {
            preparedStatement.setInt(6, Integer.parseInt(data.getCount_done()));
        } else {
            preparedStatement.setInt(6, 0);
        }

        if(data.getCount_inwork() != null) {
            preparedStatement.setInt(7, Integer.parseInt(data.getCount_inwork()));
        } else {
            preparedStatement.setInt(7, 0);
        }

        if(data.getCount_wasunavailable() != null) {
            preparedStatement.setInt(8, Integer.parseInt(data.getCount_wasunavailable()));
        } else {
            preparedStatement.setInt(8, 0);
        }

        if(data.getCount_waschanged() != null) {
            preparedStatement.setInt(9, Integer.parseInt(data.getCount_waschanged()));
        } else {
            preparedStatement.setInt(9, 0);
        }

        if(data.getCount_notperformed() != null) {
            preparedStatement.setInt(10, Integer.parseInt(data.getCount_notperformed()));
        } else {
            preparedStatement.setInt(10, 0);
        }

        preparedStatement.setInt(11, Integer.parseInt(data.getBlacklist()));
        preparedStatement.setString(12, data.getAnnotation());
        preparedStatement.setBinaryStream(13, fis, slen);
        preparedStatement.setInt(14, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();

    }


    public void add(Worker data) throws SQLException, IOException {

        System.out.println(data);
        InputStream fis;
        int slen;
        if(data.getHas_photo().equals("1")) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(data.getPhoto(), null), "png", os);
            fis = new ByteArrayInputStream(os.toByteArray());
            slen = os.toByteArray().length;
        }
        else {
            fis = null;
            slen = 0;
        }

        String addToTableSQL = "INSERT INTO workers (fullname, phone, region, specialization, sub_specialization, count_done, count_inwork, count_wasunavailable, count_waschanged, count_notperformed, blacklist, annotation, photo, summ_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setString(1, data.getFullname());
        preparedStatement.setString(2, data.getPhone());
        preparedStatement.setString(3, data.getRegion_id());
        preparedStatement.setString(4, data.getSpecialization_id());
        preparedStatement.setString(5, data.getSub_specialization());

        if(data.getCount_done() != null) {
            preparedStatement.setInt(6, Integer.parseInt(data.getCount_done()));
        } else {
            preparedStatement.setInt(6, 0);
        }

        if(data.getCount_inwork() != null) {
            preparedStatement.setInt(7, Integer.parseInt(data.getCount_inwork()));
        } else {
            preparedStatement.setInt(7, 0);
        }

        if(data.getCount_wasunavailable() != null) {
            preparedStatement.setInt(8, Integer.parseInt(data.getCount_wasunavailable()));
        } else {
            preparedStatement.setInt(8, 0);
        }

        if(data.getCount_waschanged() != null) {
            preparedStatement.setInt(9, Integer.parseInt(data.getCount_waschanged()));
        } else {
            preparedStatement.setInt(9, 0);
        }

        if(data.getCount_notperformed() != null) {
            preparedStatement.setInt(10, Integer.parseInt(data.getCount_notperformed()));
        } else {
            preparedStatement.setInt(10, 0);
        }

        if(data.getBlacklist() != null) {
            preparedStatement.setInt(11, Integer.parseInt(data.getBlacklist()));
        }
        else {
            preparedStatement.setInt(11, 0);
        }

        preparedStatement.setString(12, data.getAnnotation());
        preparedStatement.setBinaryStream(13, fis, slen);

        if(data.getSumm_price() != null) {
            preparedStatement.setInt(14, Integer.parseInt(data.getSumm_price()));
        } else {
            preparedStatement.setInt(14, 0);
        }

        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public void remove(Worker data) throws SQLException {

        String removeFromTableSQL = "DELETE FROM workers WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(removeFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public ObservableList<Worker> search_workers(String fullname, String spec_id, String sub_spec_ids, String region_id, String phone) throws SQLException, IOException {

        ObservableList<Worker> tData = FXCollections.observableArrayList();

        /*
            public void myQuery(List<String> items, int other) {
              ...
              String q4in = generateQsForIn(items.size());
              String sql = "select * from stuff where foo in ( " + q4in + " ) and bar = ?";
              PreparedStatement ps = connection.prepareStatement(sql);
              int i = 1;
              for (String item : items) {
                ps.setString(i++, item);
              }
              ps.setInt(i++, other);
              ResultSet rs = ps.executeQuery();
              ...
            }

            private String generateQsForIn(int numQs) {
                String items = "";
                for (int i = 0; i < numQs; i++) {
                    if (i != 0) items += ", ";
                    items += "?";
                }
                return items;
            }
        */

        String where = "";
        if (!fullname.equals("")) {
            System.out.println(fullname);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.fullname LIKE '%" + fullname + "%'";
        }
        /*
        if (!spec_id.equals("")) {
            System.out.println(spec_id);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.specialization = " + spec_id;
        }
        */
        if (!sub_spec_ids.equals("")) {
            System.out.println(sub_spec_ids);
            List<String> list_id_subspec = new ArrayList<String>(Arrays.asList(sub_spec_ids.split(",")));


            for (String list_id : list_id_subspec) {
                where += (where.equals("")) ? " WHERE " : " AND ";
                where += "w.sub_specialization regexp '[[:<:]]("+list_id+")[[:>:]]'";
            }

            //where += "w.sub_specialization IN ("+sub_spec_ids+")";
        }
        if (!region_id.equals("")) {
            System.out.println(region_id);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.region regexp '[[:<:]]("+region_id+")[[:>:]]'";
        }
        if (!phone.equals("")) {
            System.out.println(phone);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.phone LIKE '%" + phone + "%'";
        }

        String selectFromTableSQL = "SELECT w.id, w.fullname, w.phone, w.region as region_id, s.id as specialization_id, s.title as specialization_name, w.sub_specialization as sub_specialization, w.count_done, (SELECT COUNT(*) FROM orders WHERE link_worker = w.id AND (status = 3 OR status = 4 OR status = 5 OR status = 8 OR status = 9) AND archive = 0) as count_inwork, w.count_wasunavailable, w.count_waschanged, w.count_notperformed, w.blacklist, w.annotation, w.photo, w.summ_price FROM workers w LEFT JOIN workers_specialization s ON w.specialization = s.id " + where;
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

            tData.add(new Worker(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4), Integer.toString(rs.getInt(5)), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), Integer.toString(rs.getInt(13)), rs.getString(14), photo, has_photo, Integer.toString(rs.getInt(16))));
        }

        return tData;
    }

    public ObservableList<Worker> get_workers(String fullname, String spec_id, String sub_spec_ids) throws SQLException, IOException {

        ObservableList<Worker> tData = FXCollections.observableArrayList();

        /*
            public void myQuery(List<String> items, int other) {
              ...
              String q4in = generateQsForIn(items.size());
              String sql = "select * from stuff where foo in ( " + q4in + " ) and bar = ?";
              PreparedStatement ps = connection.prepareStatement(sql);
              int i = 1;
              for (String item : items) {
                ps.setString(i++, item);
              }
              ps.setInt(i++, other);
              ResultSet rs = ps.executeQuery();
              ...
            }

            private String generateQsForIn(int numQs) {
                String items = "";
                for (int i = 0; i < numQs; i++) {
                    if (i != 0) items += ", ";
                    items += "?";
                }
                return items;
            }
        */

        String where = "";
        if (!fullname.equals("")) {
            System.out.println(fullname);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.fullname LIKE '%" + fullname + "%'";
        }
        if (!spec_id.equals("")) {
            System.out.println(spec_id);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.specialization = " + spec_id;
        }
        if (!sub_spec_ids.equals("")) {
            System.out.println(sub_spec_ids);
            List<String> list_id_subspec = new ArrayList<String>(Arrays.asList(sub_spec_ids.split(",")));
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.sub_specialization regexp '[[:<:]]("+sub_spec_ids+")[[:>:]]'";
            //where += "w.sub_specialization IN ("+sub_spec_ids+")";
        }
        //(catid regexp '[[:<:]](666)[[:>:]]')

        String selectFromTableSQL = "SELECT w.id, w.fullname, w.phone, w.region as region_id, s.id as specialization_id, s.title as specialization_name, w.sub_specialization as sub_specialization, w.count_done, (SELECT COUNT(*) FROM orders WHERE link_worker = w.id AND (status = 3 OR status = 4 OR status = 5 OR status = 5 OR status = 8 OR status = 9) AND archive = 0) as count_inwork, w.count_wasunavailable, w.count_waschanged, w.count_notperformed, w.blacklist, w.annotation, w.photo, w.summ_price FROM workers w LEFT JOIN workers_specialization s ON w.specialization = s.id " + where;
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


    public ObservableList<Worker> get_worker_by_id(String id) throws SQLException, IOException {

        ObservableList<Worker> tData = FXCollections.observableArrayList();

        String where = "";
        if (!id.equals("")) {
            System.out.println(id);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.id = " + id;
        }

        String selectFromTableSQL = "SELECT w.id, w.fullname, w.phone, w.region as region_id, s.id as specialization_id, s.title as specialization_name, w.sub_specialization as sub_specialization, w.count_done, (SELECT COUNT(*) FROM orders WHERE link_worker = w.id AND (status = 3 OR status = 4 OR status = 5 OR status = 8 OR status = 9) AND archive = 0) as count_inwork, w.count_wasunavailable, w.count_waschanged, w.count_notperformed, w.blacklist, w.annotation, w.photo, w.summ_price FROM workers w LEFT JOIN workers_specialization s ON w.specialization = s.id " + where;
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

    public boolean can_remove(Worker data) throws SQLException {

        String countFromTableSQL = "SELECT COUNT(*) AS cnt FROM orders WHERE link_worker = ?;";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(countFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet result = preparedStatement.executeQuery();

        if (result.next()) {
            int count = result.getInt("cnt");
            if(count > 0) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }

    }

    public void set_value_worker(Worker data, String mode, String value) throws SQLException, ParseException {

        System.out.println(data);
        System.out.println(mode);

        String updateTableSQL = "";
        PreparedStatement preparedStatement = null;

        switch(mode) {
            case "set_inc_inwork":
                updateTableSQL = "UPDATE workers SET count_inwork = count_inwork + 1 WHERE id = ?";
                preparedStatement = Database.conn.prepareStatement(updateTableSQL);
                preparedStatement.setInt(1, Integer.parseInt(data.getId()));
                break;
            case "set_dec_inwork":
                updateTableSQL = "UPDATE workers SET count_inwork = count_inwork - 1 WHERE id = ?";
                preparedStatement = Database.conn.prepareStatement(updateTableSQL);
                preparedStatement.setInt(1, Integer.parseInt(data.getId()));
                break;
            case "set_done":
                updateTableSQL = "UPDATE workers SET count_done = count_done + 1 WHERE id = ?";
                preparedStatement = Database.conn.prepareStatement(updateTableSQL);
                preparedStatement.setInt(1, Integer.parseInt(data.getId()));
                break;
            case "set_notperformed":
                updateTableSQL = "UPDATE workers SET count_notperformed = count_notperformed + 1 WHERE id = ?";
                preparedStatement = Database.conn.prepareStatement(updateTableSQL);
                preparedStatement.setInt(1, Integer.parseInt(data.getId()));
                break;
            case "set_wasunavailable":
                updateTableSQL = "UPDATE workers SET count_wasunavailable = count_wasunavailable + 1 WHERE id = ?";
                preparedStatement = Database.conn.prepareStatement(updateTableSQL);
                preparedStatement.setInt(1, Integer.parseInt(data.getId()));
                break;
            case "set_waschanged":
                updateTableSQL = "UPDATE workers SET count_waschanged = count_waschanged + 1 WHERE id = ?";
                preparedStatement = Database.conn.prepareStatement(updateTableSQL);
                preparedStatement.setInt(1, Integer.parseInt(data.getId()));
                break;
            case "set_blacklist":
                updateTableSQL = "UPDATE workers SET blacklist = 1 WHERE id = ?";
                preparedStatement = Database.conn.prepareStatement(updateTableSQL);
                preparedStatement.setInt(1, Integer.parseInt(data.getId()));
                break;
            case "set_inc_price":
                updateTableSQL = "UPDATE workers SET summ_price = summ_price + ? WHERE id = ?";
                preparedStatement = Database.conn.prepareStatement(updateTableSQL);
                preparedStatement.setInt(1, Integer.parseInt(value));
                preparedStatement.setInt(2, Integer.parseInt(data.getId()));
                break;
            default:
                break;
        }

        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();

    }

}
