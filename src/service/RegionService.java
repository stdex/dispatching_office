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
public class RegionService {

    public static Region data;

    public ObservableList<Region> get_list() throws SQLException {

        ObservableList<Region> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title FROM workers_regions");

        while (rs.next()) {
            tData.add(new Region(Integer.toString(rs.getInt(1)), rs.getString(2)));
        }

        return tData;
    }

    public void edit(Region data) throws SQLException {

        String updateTableSQL = "UPDATE workers_regions SET title = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, data.getTitle());
        preparedStatement.setInt(2, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public void add(Region data) throws SQLException {

        String addToTableSQL = "INSERT INTO workers_regions (title) VALUES (?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setString(1, data.getTitle());
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public void remove(Region data) throws SQLException {

        String removeFromTableSQL = "DELETE FROM workers_regions WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(removeFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public boolean can_remove(Region data) throws SQLException {

        String countFromTableSQL = "SELECT COUNT(*) AS cnt FROM workers WHERE region = ?;";
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
