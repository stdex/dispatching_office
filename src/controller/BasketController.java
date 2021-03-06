package controller;

import java.text.SimpleDateFormat;

import model.User;
import model.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class BasketController {
	
	@FXML private Label totalPrice, totalOrder, shippingFee, basketError;
	@FXML private Alert alert;
	@FXML private Button cancelOrder, editOrder, sendOrder, deleteOrder;
	@FXML private TableView<Product> order;
	@FXML private TableColumn<Product, String> orderProduct;
	@FXML private TableColumn<Product, String> orderPrice;
	@FXML private TableColumn<Product, String> orderCategory;
	@FXML private TableColumn<Product, String> orderQuantity;
	
	private MainApp mainApp;
	private Stage orderStage;
	private MainViewController controller;
	
	// default shipping fee
	private double shipping = 2.55;
	
	private User loggedInCustomer;

	// sets the values of the columns of the tableView
	@FXML private void initialize() {
		orderProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		orderPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
		orderCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
		orderQuantity.setCellValueFactory(new PropertyValueFactory<Product, String>("number"));
	}
	
	public void setEditStage(Stage orderStage){
		 this.orderStage = orderStage;
	}

	// Reference to the mainApp
    public void setMainApp(MainApp mainApp, User customer){
        this.mainApp = mainApp;
        // Add observable list data to the table
        ObservableList<Product> p = mainApp.getBasketList();
        order.setPlaceholder(new Label("The Basket is empty!"));
        loggedInCustomer = customer;
        order.setItems(p);
    }
    
    // calculates the shipping fee and totalPrice
    @FXML public void getPrice() {
    	if (mainApp.getBasketList().size() != 0) {
    		if (mainApp.getTotalPrice() >= 20.0) {
    			shipping = 0;
    		}
    		totalPrice.setText("£"+Double.toString(mainApp.getTotalPrice()));
    		shippingFee.setText("£"+shipping);
    		double finalPrice = mainApp.getTotalPrice() + shipping;
    		// truncates double to two digits after the comma
    		finalPrice = Math.round(finalPrice * 100);
    		finalPrice = finalPrice / 100;
			totalOrder.setText("£"+finalPrice);
    	} else {
    		totalPrice.setText("£0.00");
    	}
    }
     
    // returns to mainView when cancel is clicked
	 @FXML private void handleCancel(){
		 orderStage.close();
		 //mainApp.updateUIMainView();
	 }
	 
	 
	 @FXML private void handleDelete(){
	        int selectedIndex = order.getSelectionModel().getSelectedIndex();
	        if (selectedIndex >= 0){
	            order.getItems().remove(selectedIndex);
	            this.getPrice();
	        }
	 }
	 
	 // opens alert and saves the date when "send order" is clicked
	 @FXML private void handleSend(){
		 java.util.Date now= new java.util.Date();
		 SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd. MMMM yyyy -- HH:mm");
		 String date = DATE_FORMAT.format(now);
		 if (mainApp.getBasketList().size() != 0){
			 //mainApp.saveOrder(loggedInCustomer, mainApp.getTotalPrice(), date);
			 
			 // empties the basket list
			 mainApp.getBasketList().clear();
			 createAlert();
			 
		 } else {
			 basketError.setText("the basket is empty!");
		 }
	 }
	 
	 // creates the confirmation alert
	 private void createAlert() {
		 Alert alert = new Alert(AlertType.INFORMATION);
		 alert.setTitle("Confirmation");
		 alert.setHeaderText(null);
		 alert.setContentText("Your order has been sent!");
		 alert.showAndWait();
	 }
}