package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Specialization;
import utils.Database;
import utils.MD5;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rostunov on 03.08.15.
 */
public class SpecializationService {

    public static Specialization data;

    public ObservableList<Specialization> get_list() throws SQLException {

        ObservableList<Specialization> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title FROM workers_specialization");

        while (rs.next()) {
            tData.add(new Specialization(Integer.toString(rs.getInt(1)), rs.getString(2)));
        }

        return tData;
    }


    public void edit(Specialization data) throws SQLException {

        String updateTableSQL = "UPDATE workers_specialization SET title = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, data.getTitle());
        preparedStatement.setInt(2, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public void add(Specialization data) throws SQLException {

        String addToTableSQL = "INSERT INTO workers_specialization (title) VALUES (?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setString(1, data.getTitle());
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public void remove(Specialization data) throws SQLException {

        String removeFromTableSQL = "DELETE FROM workers_specialization WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(removeFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public boolean can_remove(Specialization data) throws SQLException {

        String countFromTableSQL = "SELECT COUNT(*) AS cnt FROM workers_sub_specialization WHERE main_specialization = ?;";
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
