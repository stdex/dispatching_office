package controller;

import model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomerController {
	
	@FXML private Label firstName, emailAddress, address;

	private User loggedInCustomer;
	private MainApp mainApp;
	private Stage primaryStage;
	
	public CustomerController() { }
	
	@FXML public void initialize() { }
	
	// reference to the mainApp
    public void setMainApp(MainApp mainApp, User customer) {
        this.mainApp = mainApp;
        loggedInCustomer = customer;
    }
    
    // reference to the primaryStage
	public void setPrimaryStage(Stage primaryStage){
		 this.primaryStage = primaryStage;
	}
	
	// populates the textFields with the customer details
	public void showCustomerDetails(User customer) {
		/*
		firstName.setText(customer.getFirstName()+" "+customer.getLastName());
		emailAddress.setText(customer.getEmailAddress());
		address.setText(customer.getStreet()+" "+customer.getCity());
		*/
	}
	
	 @FXML private void handleOk() {
		 primaryStage.close();
	 }
	
	 @FXML private void handleCancel(){
		 primaryStage.close();
	 }
	 
	 @FXML private void handleEditDetails() {
		 mainApp.addCustomer(loggedInCustomer);
	 }
	 
	 @FXML private void handleEditPassword() {
		 mainApp.showPassword(loggedInCustomer);
	 }
	 
	 @FXML private void handleViewOrders() {
		 //mainApp.showOrder(loggedInCustomer);
	 }
}
