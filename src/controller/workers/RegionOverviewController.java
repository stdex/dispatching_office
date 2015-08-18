package controller.workers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.Region;
import model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import service.RegionService;
import service.UserService;
import utils.DateUtil;
import controller.MainApp;

import java.sql.SQLException;
import java.text.ParseException;

public class RegionOverviewController {
    @FXML
    private TableView<Region> dTable;

    @FXML
    private TableColumn<Region, String> tColumn1;

    @FXML
    private Label dLabel1;


    // Reference to the main application.
    private MainApp mainApp;
    private Stage orderStage;

    public static ObservableList<Region> tData = FXCollections.observableArrayList();
    public static RegionService tService =  new RegionService();
    public static Region temp = new Region();
    public static Region selected;
    public static Region details;
    public static String title = "Редактирование";
    public static String mode = "RegionEdit";
    public static String fxml = "/view/workers/RegionEditDialog.fxml";

    public static String err_title = "Не выбран район";
    public static String err_header = "Не выбран район";
    public static String err_text = "Пожалуйста, выберите район из таблицы.";

    public static String err_exist_title = "Удаление невозможно";
    public static String err_exist_header = "Удаление невозможно";
    public static String err_exist_text = "Данный объект связан объектами из другой таблицы.";

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public RegionOverviewController() {
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
        details = (Region) obj;
        if (details != null) {
            dLabel1.setText(details.getTitle());
        } else {
            dLabel1.setText("");
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
