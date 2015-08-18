package controller.orders;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.OrderStatus;
import model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import service.OrderStatusService;
import service.UserService;
import utils.DateUtil;
import controller.MainApp;

import java.sql.SQLException;
import java.text.ParseException;

public class OrderStatusOverviewController {
    @FXML
    private TableView<OrderStatus> dTable;

    @FXML
    private TableColumn<OrderStatus, String> tColumn1;

    @FXML
    private Label dLabel1;
    @FXML
    private Label dLabel2;


    // Reference to the main application.
    private MainApp mainApp;
    private Stage orderStage;

    public static ObservableList<OrderStatus> tData = FXCollections.observableArrayList();
    public static OrderStatusService tService =  new OrderStatusService();
    public static OrderStatus temp = new OrderStatus();
    public static OrderStatus selected;
    public static OrderStatus details;
    public static String title = "Редактирование";
    public static String mode = "orderStatusEdit";
    public static String fxml = "/view/orders/OrderStatusEditDialog.fxml";

    public static String err_title = "Не выбран cтатус";
    public static String err_header = "Не выбран cтатус";
    public static String err_text = "Пожалуйста, выберите cтатус из таблицы.";

    public static String err_exist_title = "Удаление невозможно";
    public static String err_exist_header = "Удаление невозможно";
    public static String err_exist_text = "Данный объект связан объектами из другой таблицы.";


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public OrderStatusOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        tColumn1.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        showDetails(null);
        dTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) throws SQLException {
        this.mainApp = mainApp;
        tData = tService.get_list();
        dTable.setItems(tData);
    }

    public void setEditStage(Stage orderStage){
        this.orderStage = orderStage;
    }


    private void showDetails(Object obj) {
        // TODO: cast object invoke
        details = (OrderStatus) obj;
        if (details != null) {
            dLabel1.setText(details.getTitle());
            dLabel2.setText(details.getColor());
            if(details.getColor() != null) {
                dLabel2.setTextFill(Color.web(details.getColor()));
            }

        } else {
            dLabel1.setText("");
            dLabel2.setText("");
        }
    }


    @FXML
    private void handleDelete() throws SQLException {

        int selectedIndex = dTable.getSelectionModel().getSelectedIndex();
        selected = dTable.getSelectionModel().getSelectedItem();
        boolean can_remove = tService.can_remove(selected);
        System.out.println(can_remove);

        if (selected != null) {
            if(can_remove) {
                tService.remove(selected);
                tData.clear();
                tData = tService.get_list();
                dTable.setItems(tData);
            }
            else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle(err_exist_title);
                alert.setHeaderText(err_exist_header);
                alert.setContentText(err_exist_text);
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(err_title);
            alert.setHeaderText(err_header);
            alert.setContentText(err_text);
            alert.showAndWait();
        }

    }

    @FXML
    private void handleNew() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ParseException {

        boolean okClicked = mainApp.showSPageEditDialog(title, fxml, mode, temp);
        if (okClicked) {
            tData.clear();
            tData = tService.get_list();
            dTable.setItems(tData);
        }

    }

    @FXML
    private void handleEdit() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ParseException {

        selected = dTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean okClicked = mainApp.showSPageEditDialog(title, fxml, mode, selected);
            if (okClicked) {
                tData.clear();
                tData = tService.get_list();
                dTable.setItems(tData);
                showDetails(selected);
            }

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(err_title);
            alert.setHeaderText(err_header);
            alert.setContentText(err_text);
            alert.showAndWait();
        }

    }


}