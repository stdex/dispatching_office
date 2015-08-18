package controller.workers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.SubSpecialization;
import model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import service.SubSpecializationService;
import service.UserService;
import utils.DateUtil;
import controller.MainApp;

import java.sql.SQLException;
import java.text.ParseException;

public class SubSpecializationOverviewController {
    @FXML
    private TableView<SubSpecialization> dTable;

    @FXML
    private TableColumn<SubSpecialization, String> tColumn1;
    @FXML
    private TableColumn<SubSpecialization, String> tColumn2;

    @FXML
    private Label dLabel1;
    @FXML
    private Label dLabel2;


    // Reference to the main application.
    private MainApp mainApp;
    private Stage orderStage;

    public static ObservableList<SubSpecialization> tData = FXCollections.observableArrayList();
    public static SubSpecializationService tService =  new SubSpecializationService();
    public static SubSpecialization temp = new SubSpecialization();
    public static SubSpecialization selected;
    public static SubSpecialization details;
    public static String title = "Редактирование";
    public static String mode = "SubSpecializationEdit";
    public static String fxml = "/view/workers/SubSpecializationEditDialog.fxml";

    public static String err_title = "Не выбран вид работ";
    public static String err_header = "Не выбран вид работ";
    public static String err_text = "Пожалуйста, выберите вид работы из таблицы.";

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public SubSpecializationOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        tColumn1.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        showDetails(null);
        tColumn2.setCellValueFactory(cellData -> cellData.getValue().main_nameProperty());

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
        details = (SubSpecialization) obj;
        if (details != null) {
            dLabel1.setText(details.getTitle());
            dLabel2.setText(details.getMain_name());
        } else {
            dLabel1.setText("");
            dLabel2.setText("");
        }
    }


    @FXML
    private void handleDelete() throws SQLException {

        int selectedIndex = dTable.getSelectionModel().getSelectedIndex();
        selected = dTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tService.remove(selected);
            tData.clear();
            tData = tService.get_list();
            dTable.setItems(tData);
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
