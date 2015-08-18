package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Order;
import model.OrderStatus;
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
public class OrderStatusService {

    public ObservableList<OrderStatus> get_list() throws SQLException {

        ObservableList<OrderStatus> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title, color FROM order_status");

        while (rs.next()) {
            tData.add(new OrderStatus(Integer.toString(rs.getInt(1)), rs.getString(2), (rs.getString(3)!=null)?rs.getString(3):null ));
        }

        return tData;
    }

    public void edit(OrderStatus data) throws SQLException {

        String updateTableSQL = "UPDATE order_status SET title = ?, color = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, data.getTitle());
        preparedStatement.setString(2, data.getColor());
        preparedStatement.setInt(3, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public void add(OrderStatus data) throws SQLException {

        String addToTableSQL = "INSERT INTO order_status (title, color) VALUES (?, ?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setString(1, data.getTitle());
        preparedStatement.setString(2, data.getColor());
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public void remove(OrderStatus data) throws SQLException {

        String removeFromTableSQL = "DELETE FROM order_status WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(removeFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public boolean can_remove(OrderStatus data) throws SQLException {

        String countFromTableSQL = "SELECT COUNT(*) AS cnt FROM orders WHERE status = ?;";
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
