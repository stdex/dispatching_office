package service;

import controller.MainApp;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rostunov on 03.08.15.
 */
public class MaintranceService {

    public void set_names() throws SQLException {

        MainApp.db.execute("SET NAMES utf8;");

    }

}
