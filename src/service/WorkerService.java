package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import controller.MainApp;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Region;
import model.Specialization;
import model.SubSpecialization;
import model.Worker;
import utils.Database;
import utils.MD5;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class WorkerService {


    public ObservableList<Worker> get_list() throws SQLException {

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

    public ObservableList<Specialization> get_specialization_list() throws SQLException {

        ObservableList<Specialization> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title FROM workers_specialization");

        while (rs.next()) {
            tData.add(new Specialization(Integer.toString(rs.getInt(1)), rs.getString(2)));
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

    public void edit(Worker data) throws SQLException {

        String updateTableSQL = "UPDATE workers SET fullname = ?, phone = ?, region = ?, specialization = ?, sub_specialization = ?, count_done = ?, count_inwork = ?, count_wasunavailable = ?, count_waschanged = ?, count_notperformed = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, data.getFullname());
        preparedStatement.setString(2, data.getPhone());
        preparedStatement.setInt(3, Integer.parseInt(data.getRegion_id()));
        preparedStatement.setInt(4, Integer.parseInt(data.getSpecialization_id()));
        preparedStatement.setString(5, data.getSub_specialization());
        preparedStatement.setString(6, data.getCount_done());
        preparedStatement.setString(7, data.getCount_inwork());
        preparedStatement.setString(8, data.getCount_wasunavailable());
        preparedStatement.setString(9, data.getCount_waschanged());
        preparedStatement.setString(10, data.getCount_notperformed());
        preparedStatement.setInt(11, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();

    }


    public void add(Worker data) throws SQLException {

        System.out.println(data);

        String addToTableSQL = "INSERT INTO workers (fullname, phone, region, specialization, sub_specialization, count_done, count_inwork, count_wasunavailable, count_waschanged, count_notperformed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setString(1, data.getFullname());
        preparedStatement.setString(2, data.getPhone());
        preparedStatement.setInt(3, Integer.parseInt(data.getRegion_id()));
        preparedStatement.setInt(4, Integer.parseInt(data.getSpecialization_id()));
        preparedStatement.setString(5, data.getSub_specialization());
        preparedStatement.setString(6, data.getCount_done());
        preparedStatement.setString(7, data.getCount_inwork());
        preparedStatement.setString(8, data.getCount_wasunavailable());
        preparedStatement.setString(9, data.getCount_waschanged());
        preparedStatement.setString(10, data.getCount_notperformed());
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


    public ObservableList<Worker> search_workers(String fullname, String spec_id, String sub_spec_ids, String region_id, String phone) throws SQLException {

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
        if (!region_id.equals("")) {
            System.out.println(region_id);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "r.id = " + region_id;
        }
        if (!phone.equals("")) {
            System.out.println(phone);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.phone LIKE '%" + phone + "%'";
        }

        String selectFromTableSQL = "SELECT w.id, w.fullname, w.phone, r.id as region_id, r.title as region_name, s.id as specialization_id, s.title as specialization_name, w.sub_specialization as sub_specialization, w.count_done, w.count_inwork, w.count_wasunavailable, w.count_waschanged, w.count_notperformed FROM workers w LEFT JOIN workers_specialization s ON w.specialization = s.id LEFT JOIN workers_regions r ON w.region = r.id" + where;
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new Worker(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), Integer.toString(rs.getInt(4)), rs.getString(5), Integer.toString(rs.getInt(6)), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13) ));
        }

        return tData;
    }

    public ObservableList<Worker> get_workers(String fullname, String spec_id, String sub_spec_ids) throws SQLException {

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

        String selectFromTableSQL = "SELECT w.id, w.fullname, w.phone, r.id as region_id, r.title as region_name, s.id as specialization_id, s.title as specialization_name, w.sub_specialization as sub_specialization, w.count_done, w.count_inwork, w.count_wasunavailable, w.count_waschanged, w.count_notperformed FROM workers w LEFT JOIN workers_specialization s ON w.specialization = s.id LEFT JOIN workers_regions r ON w.region = r.id" + where;
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new Worker(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), Integer.toString(rs.getInt(4)), rs.getString(5), Integer.toString(rs.getInt(6)), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13) ));
        }

        return tData;
    }


    public ObservableList<Worker> get_worker_by_id(String id) throws SQLException {

        ObservableList<Worker> tData = FXCollections.observableArrayList();

        String where = "";
        if (!id.equals("")) {
            System.out.println(id);
            where += (where.equals("")) ? " WHERE " : " AND ";
            where += "w.id = " + id;
        }

        String selectFromTableSQL = "SELECT w.id, w.fullname, w.phone, r.id as region_id, r.title as region_name, s.id as specialization_id, s.title as specialization_name, w.sub_specialization as sub_specialization, w.count_done, w.count_inwork, w.count_wasunavailable, w.count_waschanged, w.count_notperformed FROM workers w LEFT JOIN workers_specialization s ON w.specialization = s.id LEFT JOIN workers_regions r ON w.region = r.id" + where;
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new Worker(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), Integer.toString(rs.getInt(4)), rs.getString(5), Integer.toString(rs.getInt(6)), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13) ));
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

}
