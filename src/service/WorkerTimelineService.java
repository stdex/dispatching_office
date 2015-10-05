package service;

import com.mysql.jdbc.JDBC4PreparedStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.OrderTimeline;
import model.WorkerTimeline;
import utils.Database;

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
public class WorkerTimelineService {

    public ObservableList<WorkerTimeline> get_list_by_worker(String id_worker) throws SQLException, IOException {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        ObservableList<WorkerTimeline> tData = FXCollections.observableArrayList();

        String selectFromTableSQL = "SELECT wt.id, wt.id_order, wt.id_reaction, r.title as reaction, wt.id_worker as link_worker_id, w.fullname as link_worker, wt.id_user as link_user_id, u.fullname as link_user, wt.dt as dt, wt.reason as reason FROM workers_timeline wt LEFT JOIN workers w ON wt.id_worker = w.id LEFT JOIN users u ON wt.id_user = u.id LEFT JOIN workers_reaction r ON wt.id_reaction = r.id WHERE wt.id_worker = ? ORDER BY wt.id ASC;";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(selectFromTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(id_worker));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {

            tData.add(new WorkerTimeline(Integer.toString(rs.getInt(1)), Integer.toString(rs.getInt(2)), Integer.toString(rs.getInt(3)), rs.getString(4), Integer.toString(rs.getInt(5)), rs.getString(6), Integer.toString(rs.getInt(7)), rs.getString(8), (rs.getTimestamp(9)!=null)?df.format(new Date(rs.getTimestamp(9).getTime())).toString():null, rs.getString(10) ));

        }

        return tData;
    }



    public void add_timeline(WorkerTimeline data) throws SQLException, ParseException {

        System.out.println(data);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        String addToTableSQL = "INSERT INTO workers_timeline (id_user, id_worker, id_order, id_reaction, reason, dt) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = Database.conn.prepareStatement(addToTableSQL);
        preparedStatement.setInt(1, Integer.parseInt(data.getLink_user_id()));
        preparedStatement.setInt(2, Integer.parseInt(data.getLink_worker_id()));
        preparedStatement.setInt(3, Integer.parseInt(data.getId_order()));
        preparedStatement.setInt(4, Integer.parseInt(data.getId_reaction()));
        preparedStatement.setString(5, data.getReason());
        preparedStatement.setTimestamp(6, convertStringToTimestamp(dateFormat.format(date)));
        System.out.println(((JDBC4PreparedStatement) preparedStatement).asSql());
        preparedStatement.executeUpdate();

    }

}
