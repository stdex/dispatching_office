package controller.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import service.UserService;
import utils.DateUtil;
import controller.MainApp;

import java.sql.SQLException;

public class UserOverviewController {
    @FXML
    private TableView<User> dTable;
    @FXML
    private TableColumn<User, String> tColumn1;
    @FXML
    private TableColumn<User, String> tColumn2;

    @FXML
    private Label dLabel1;
    @FXML
    private Label dLabel2;
    @FXML
    private Label dLabel3;

    // Reference to the main application.
    private MainApp mainApp;
    private Stage orderStage;
    public static ObservableList<User> tData = FXCollections.observableArrayList();
    public static UserService u_service =  new UserService();

    public static String err_exist_title = "Удаление невозможно";
    public static String err_exist_header = "Удаление невозможно";
    public static String err_exist_text = "Данный объект связан объектами из другой таблицы.";


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public UserOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        tColumn1.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        tColumn2.setCellValueFactory(cellData -> cellData.getValue().fullnameProperty());

        showDetails(null);

        // Listen for selection changes and show the person details when changed.
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

        //u_service =  new UserService();
        tData = u_service.get_list();
        dTable.setItems(tData);
    }

    public void setEditStage(Stage orderStage){
        this.orderStage = orderStage;
    }
    
    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     * 
     * @param user the person or null
     */
    private void showDetails(User user) {
        System.out.println(user);
        if (user != null) {
            // Fill the labels with info from the person object.
            dLabel1.setText(user.getLogin());
            dLabel2.setText(user.getFullname());
            dLabel3.setText(user.getGroup_name());
        } else {
            // Person is null, remove all the text.
            dLabel1.setText("");
            dLabel2.setText("");
            dLabel3.setText("");
        }
    }


    @FXML
    private void handleDelete() throws SQLException {

        int selectedIndex = dTable.getSelectionModel().getSelectedIndex();
        User selected = dTable.getSelectionModel().getSelectedItem();
        boolean can_remove = u_service.can_remove(selected);
        System.out.println(can_remove);

        if (selected != null) {
            if(can_remove) {
                u_service.remove(selected);
                tData.clear();
                tData = u_service.get_list();
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

            //dTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Не выбран пользователь");
            alert.setHeaderText("Не выбран пользователь");
            alert.setContentText("Пожалуйста, выберите пользователя из таблицы.");
            
            alert.showAndWait();
        }

    }

    @FXML
    private void handleNew() throws SQLException {
        /**/
        User temp = new User();
        boolean okClicked = mainApp.showUserEditDialog(temp);
        if (okClicked) {
            tData.clear();
            tData = u_service.get_list();
            dTable.setItems(tData);
            //tData.add(temp);
        }

    }

    @FXML
    private void handleEdit() throws SQLException {

        User selected = dTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean okClicked = mainApp.showUserEditDialog(selected);
            if (okClicked) {
                tData.clear();
                tData = u_service.get_list();
                dTable.setItems(tData);
                showDetails(selected);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Не выбран пользователь");
            alert.setHeaderText("Не выбран пользователь");
            alert.setContentText("Пожалуйста, выберите пользователя из таблицы.");
            
            alert.showAndWait();
        }

    }


}