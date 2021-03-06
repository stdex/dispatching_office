package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Order;
import model.OrderCategory;
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
public class OrderCategoryService {

    public ObservableList<OrderCategory> get_list() throws SQLException {

        ObservableList<OrderCategory> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, title FROM order_category");

        while (rs.next()) {
            tData.add(new OrderCategory(Integer.toString(rs.getInt(1)), rs.getString(2)));
        }

        return tData;
    }

    public void edit(OrderCategory data) throws SQLException {

        String updateTableSQL = "UPDATE order_category SET title = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, data.getTitle());
        preparedStatement.setInt(2, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public void add(OrderCategory data) throws SQLException {

        String addToTableSQL = "INSERT INTO order_category (title) VALUES (?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setString(1, data.getTitle());
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public void remove(OrderCategory data) throws SQLException {

        String removeFromTableSQL = "DELETE FROM order_category WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(removeFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }


    public boolean can_remove(OrderCategory data) throws SQLException {

        String countFromTableSQL = "SELECT COUNT(*) AS cnt FROM orders WHERE work_cat = ?;";
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
