package controller;

import model.User;
import model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class OrderController {
	
	@FXML private ListView<String> orderlistView;
	@FXML private ObservableList<String> p = FXCollections.observableArrayList();
	
	private Stage primaryStage;
	private MainApp mainApp;
	private User loggedInCustomer;

	// Reference to the mainApp
	public void setMainApp(MainApp mainApp, User customer) {
		this.mainApp = mainApp;
		this.loggedInCustomer = customer;
		this.viewOrders();
		
	}
	// display the orders in the listView
	private void viewOrders() {
		// checks if any order in the observableList
        /*
		 if (mainApp.getOrderList().size() > 0) {
			 // finds orders that match to the logged in customer and display them
			 for ( int i=0;i<mainApp.getOrderList().size();i++) {
				 if (mainApp.getOrderList().get(i).getCustomer().equals(loggedInCustomer)) {
					 System.out.println("customers match");
					 Order order = mainApp.getOrderList().get(i);
					 p.add(order.getDate()+"  -----  Total Price: £"+order.getTotalSum());
				 }
			 }
			 orderlistView.setItems(p);
		 }
		 */
	 }
	
	// returns to customerView when ok is clicked
	@FXML public void handleOk() {
		mainApp.showCustomer(loggedInCustomer);
	}
	
	// returns to customerView when cancel is clicked
	@FXML public void handleCancel() {
		mainApp.showCustomer(loggedInCustomer);
	}

}
