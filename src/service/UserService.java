package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import model.UserGroup;
import utils.Database;
import utils.MD5;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

public class UserService {

    public static User user;

    public boolean do_login(String login, String password) throws SQLException {

        boolean result = false;

        ResultSet rs = MainApp.db.query("SELECT id, login, password, fullname, group_id, group_name FROM users LEFT JOIN users_groups USING (id) WHERE login='%s' and password='%s'", login, password);

        if (rs.first())
        {
            System.out.println(rs.getInt(1));
            System.out.println(rs.getString(2));
            System.out.println(rs.getString(3));
            System.out.println(rs.getString(4));
            System.out.println(rs.getInt(5));
            System.out.println(rs.getString(6));
            user = new User(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4), Integer.toString(rs.getInt(5)), rs.getString(6));
            result = true;
        }

        return result;
    }

    public User get_authorization_user() throws SQLException {

        if(user != null) {
            return user;
        }
        else {
            return null;
        }
    }

    public ObservableList<User> get_list() throws SQLException {

        ObservableList<User> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT u.id, u.login, u.password, u.fullname, u.group_id, ug.group_name FROM users u LEFT JOIN users_groups ug ON u.group_id = ug.id");

        while (rs.next()) {
            tData.add(new User(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4), Integer.toString(rs.getInt(5)), rs.getString(6)));
            System.out.println(rs.getString(6));
        }

        return tData;
    }

    public ObservableList<UserGroup> get_status_list() throws SQLException {

        ObservableList<UserGroup> tData = FXCollections.observableArrayList();

        ResultSet rs = MainApp.db.query("SELECT id, group_name FROM users_groups");

        while (rs.next()) {
            tData.add(new UserGroup(Integer.toString(rs.getInt(1)), rs.getString(2)));
        }

        return tData;
    }

    public void edit(User user) throws SQLException {

        /**/
        String updateTableSQL = "UPDATE users SET login = ?, password = ?, fullname = ?, group_id = ? WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getFullname());
        preparedStatement.setInt(4, Integer.parseInt(user.getGroup_id()));
        preparedStatement.setInt(5, Integer.parseInt(user.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();

        //System.out.println(MainApp.db.execute("UPDATE users SET login = '%s', password = '%s', fullname = '%s', group_id = '%d' WHERE id = '%d'", user.getLogin(), user.getPassword(), user.getFullname(), Integer.parseInt(user.getGroup_id()), Integer.parseInt(user.getId())));

    }


    public void add(User user) throws SQLException {

        MD5 md5_obj = new MD5();
        String md5_password = md5_obj.crypt(user.getPassword());
        user.setPassword(md5_password);

        String addToTableSQL = "INSERT INTO users (login, password, fullname, group_id) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getFullname());
        preparedStatement.setInt(4, Integer.parseInt(user.getGroup_id()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();

        //System.out.println(MainApp.db.execute("INSERT INTO users(login, password, fullname, group_id) VALUES('%s', '%s', '%s', '%d')", user.getLogin(), user.getPassword(), user.getFullname(), Integer.parseInt(user.getGroup_id())));

    }

    public void remove(User user) throws SQLException {

        String removeFromTableSQL = "DELETE FROM users WHERE id = ?";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(removeFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(user.getId()));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();
    }

    public boolean can_remove(User data) throws SQLException {

        String countFromTableSQL = "SELECT COUNT(*) AS cnt FROM orders WHERE link_user_id = ?;";
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
