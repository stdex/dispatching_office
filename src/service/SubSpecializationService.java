package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import utils.Database;
import utils.MD5;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rostunov on 03.08.15.
 */
public class SubSpecializationService {

    public static SubSpecialization data;

    public ObservableList<SubSpecialization> get_list() throws SQLException {

        ObservableList<SubSpecialization> tData = FXCollections.observableArrayList();

        String selectFromTableSQL = "SELECT ss.id, ss.title, ss.main_specialization, s.id as main_id, s.title as main_name, s.id as main_id, s.title as main_name FROM workers_sub_specialization ss LEFT JOIN workers_specialization s ON ss.main_specialization = s.id;";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            tData.add(new SubSpecialization(Integer.toString(rs.getInt(1)), rs.getString(2), Integer.toString(rs.getInt(3)), rs.getString(5)  ));
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

    public void edit(SubSpecialization data) throws SQLException {

        String updateTableSQL = "UPDATE workers_sub_specialization SET title = ?, main_specialization = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, data.getTitle());
        preparedStatement.setInt(2, Integer.parseInt(data.getMain_id()));
        preparedStatement.setInt(3, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public void add(SubSpecialization data) throws SQLException {

        String addToTableSQL = "INSERT INTO workers_sub_specialization (title, main_specialization) VALUES (?, ?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setString(1, data.getTitle());
        preparedStatement.setInt(2, Integer.parseInt(data.getMain_id()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public void remove(SubSpecialization data) throws SQLException {

        String removeFromTableSQL = "DELETE FROM workers_sub_specialization WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(removeFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }
}
