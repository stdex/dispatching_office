package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import model.OrderTimeline;
import model.Worker;
import utils.Database;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static utils.DateUtil.convertStringToTimestamp;

/**
 * Created by rostunov on 20.09.15.
 */
public class OrderTimelineService {

    public ObservableList<OrderTimeline> get_list_by_order(String id_order) throws SQLException, IOException {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        ObservableList<OrderTimeline> tData = FXCollections.observableArrayList();

        String selectFromTableSQL = "SELECT ot.id, ot.id_order, ot.id_status, os.title as status, os.color as color, ot.id_worker as link_worker_id, w.fullname as link_worker, ot.id_user as link_user_id, u.fullname as link_user, ot.dt as dt, ot.reason as reason FROM order_timeline ot LEFT JOIN order_status os ON ot.id_status = os.id LEFT JOIN workers w ON ot.id_worker = w.id LEFT JOIN users u ON ot.id_user = u.id WHERE ot.id_order = ? ORDER BY ot.id DESC;";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(id_order));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {

            tData.add(new OrderTimeline(Integer.toString(rs.getInt(1)), Integer.toString(rs.getInt(2)), Integer.toString(rs.getInt(3)), rs.getString(4), rs.getString(5), Integer.toString(rs.getInt(6)), rs.getString(7), Integer.toString(rs.getInt(8)), rs.getString(9), (rs.getTimestamp(10)!=null)?df.format(new Date(rs.getTimestamp(10).getTime())).toString():null, rs.getString(11) ));

        }

        return tData;
    }


    public void add_timeline(OrderTimeline data) throws SQLException, ParseException {

        System.out.println(data);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        String addToTableSQL = "INSERT INTO order_timeline (id_order, id_status, id_worker, id_user, dt, reason) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getId_order()));
        preparedStatement.setInt(2, Integer.parseInt(data.getId_status()));

        if(!data.getLink_worker_id().equals("0") && !data.getLink_worker_id().equals("")) {
            preparedStatement.setInt(3, Integer.parseInt(data.getLink_worker_id()));
        }
        else {
            preparedStatement.setNull(3, java.sql.Types.INTEGER);
        }

        preparedStatement.setInt(4, Integer.parseInt(data.getLink_user_id()));
        preparedStatement.setTimestamp(5, convertStringToTimestamp(dateFormat.format(date)));
        preparedStatement.setString(6, data.getReason());
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();

    }


}
