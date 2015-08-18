package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Order;
import model.OrderAdvert;
import model.User;
import model.UserGroup;
import utils.Database;
import utils.MD5;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rostunov on 03.08.15.
 */
public class OrderAdvertService {

    public ObservableList<OrderAdvert> get_list() throws SQLException {

        ObservableList<OrderAdvert> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title FROM order_advert");

        while (rs.next()) {
            tData.add(new OrderAdvert(Integer.toString(rs.getInt(1)), rs.getString(2)));
        }

        return tData;
    }

    public void edit(OrderAdvert data) throws SQLException {

        String updateTableSQL = "UPDATE order_advert SET title = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, data.getTitle());
        preparedStatement.setInt(2, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public void add(OrderAdvert data) throws SQLException {

        String addToTableSQL = "INSERT INTO order_advert (title) VALUES (?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setString(1, data.getTitle());
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public void remove(OrderAdvert data) throws SQLException {

        String removeFromTableSQL = "DELETE FROM order_advert WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(removeFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public boolean can_remove(OrderAdvert data) throws SQLException {

        String countFromTableSQL = "SELECT COUNT(*) AS cnt FROM orders WHERE advert_source = ?;";
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
