package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Product;
import model.ProductDescription;
import model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class UserController {
    @FXML private TableView<User> tableView;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> loginColumn;
    @FXML private TableColumn<Product, String> statusColumn;
    @FXML private TextField login;
    @FXML private TextField password;
    @FXML private TextField fullname;
    @FXML private ComboBox<String> status;

    private MainApp mainApp;
    private Stage orderStage;
    private MainViewController controller;
    private User loggedInCustomer;

    @FXML
    private void initialize() {
        // Initialize the person table
        /*
        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("fullname"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));

        tableView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Product>() {
                    public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
                        showProductDetails(newValue);
                    }
                });

        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Product> filteredList = new FilteredList<>(productData, p -> true);

        // Wrap the FilteredList in a SortedList.
        SortedList<Product> SortedList = new SortedList<>(filteredList);

        //  Bind the SortedList comparator to the TableView comparator.
        SortedList.comparatorProperty().bind(tableView.comparatorProperty());

        // Add sorted (and filtered) data to the table.
        tableView.setItems(SortedList);
        */

    }

    /*
    // Reference to mainApp == receives the logged in customer from the loginView
    public void setMainApp(MainApp mainApp, User customer) {
        this.mainApp = mainApp;
        this.addToList();
        loggedInCustomer = customer;
        // displayes the customer name top left
        setCustomerLabel();
        this.populateCombo();
        // sets the default product quantity to 1
        tableView.getSelectionModel().select(0);
    }
    */

    public UserController() { }

    // populates the product list
    public void addToList() {
        //userData.add(new User("Banana", "", "", "", "", "", "", "", ""));
    }

    /*
    // updates the total price and item number in the labels top right
    @FXML public void setLabelText() {
        items.setText(Integer.toString(mainApp.getBasketList().size()));
        if (mainApp.getBasketList().size() != 0) {
            totalSum.setText("£"+Double.toString(mainApp.getTotalPrice()));
        } else {
            totalSum.setText("£0.00");
        }
    }

    // opens the basket window
    @FXML private void handleViewOrder() { mainApp.showViewBasket(loggedInCustomer); }

    // opens the customerView
    @FXML private void handleViewCustomer() { mainApp.showCustomer(loggedInCustomer); }
    */
    private void populateCombo() {
        //status.getItems().addAll(1,2,3,4,5);
        //status.getSelectionModel().select(null);
    }
    /*

    @FXML public void setCustomerLabel() {
        customerName.setText(loggedInCustomer.getFirstName()+" "+loggedInCustomer.getLastName());
    }
    */


    // returns to mainView when cancel is clicked
    @FXML private void handleCancel(){
        orderStage.close();
        //mainApp.updateUIMainView();
    }

    // returns to mainView when cancel is clicked
    @FXML private void handleAddUser(){
        orderStage.close();
        //mainApp.updateUIMainView();
    }


    // contains the products displayed in mainView
    private ObservableList<User> userData = FXCollections.observableArrayList();

    public ObservableList<User> getUserData() { return userData; }

    public void setEditStage(Stage orderStage){
        this.orderStage = orderStage;
    }

    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
        // Add observable list data to the table
        //ObservableList<Product> p = mainApp.getBasketList();
        //order.setPlaceholder(new Label("The Basket is empty!"));
        //loggedInCustomer = customer;
        //order.setItems(p);
    }

}