package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedHashSet;

import controller.orders.*;

import controller.workers.*;

import controller.users.UserEditDialogController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.*;
import service.UserService;
import service.MaintranceService;
import utils.Config;
import utils.Database;
import controller.users.UserOverviewController;
import controller.workers.RegionOverviewController;
import controller.workers.WorkerOverviewController;
import controller.orders.OrderEditDialogController;


public class MainApp extends Application {

	private Stage primaryStage, mainStage;
	private BorderPane rootLayout;
	private AnchorPane mainView, viewBasket, customerPane, addCustomer, loginView, passwordPane, orderPane, userView;

	private LoginController loginViewController;
	private MainViewController mainViewController;
	private BasketController cartController;
	private CustomerController customerController;
	private AddCustomerController addCustomerController;
	private RootLayoutController rootLayoutController;
	private PasswordController passwordController;
	private OrderController orderController;
	private UserOverviewController userController;
	private OrderStatusOverviewController orderstatusController;
    private RegionOverviewController regionController;
    private WorkerOverviewController workerController;

	File customerFile = new File("resources/customers.xml");
	public static Database db;

	public static void main(String[] args) { launch(args); }

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setResizable(false);
		this.primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));
		//this.loadFile(customerFile);

		try {
			Config.loadConfig();

		} catch (Exception e) {
			// display an alert when the file load is corrupt
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Конфигурационный файл не загружен");
			alert.setContentText("Конфигурационный файл не загружен");
			alert.showAndWait();
		}

		// Database Connection
		String dbconstr = Config.getStringDefault("DatabaseConString", "");
		String dbname = Config.getStringDefault("DatabaseName", "");
		String dbuser = Config.getStringDefault("DatabaseUsername", "");
		String dbpass = Config.getStringDefault("DatabasePassword", "");

		try {
			db = new Database(dbconstr + dbname, dbuser, dbpass);
		} catch (Exception e) {
			// display an alert when the file load is corrupt
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Не удалось подключиться к MySQL серверу");
			alert.setContentText("Проверьте настройки подключения");
			alert.showAndWait();
		}


		MaintranceService maintranceService =  new MaintranceService();
		maintranceService.set_names();

		// display the login window
		showLoginView();
	}

	public Stage getPrimaryStage() { return primaryStage; }

	public MainApp() {}

	// loads stage and scene for the customer login
	public void showLoginView() {
        try {

        	// Load LoginView.fxml from 'view' package
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/LoginView.fxml"));
            loginView = (AnchorPane) loader.load();

            // Give the controller access to MainApp and primaryStage
            loginViewController = loader.getController();
            loginViewController.setApp(this);
            loginViewController.setPrimaryStage(primaryStage);

            // Set the scene
            Scene loginScene = new Scene(loginView);
            primaryStage.setTitle("Вход в систему");
            primaryStage.setScene(loginScene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	// handles change from loginView to mainView
    public void callMainView(User customer) throws Exception{
        this.showRootLayout(customer);
        this.primaryStage.close();
    }

    // adds a menubar to the main view
	public void showRootLayout(User user) throws SQLException, ParseException {
		try {
			// Load RootLayout.fxml from 'view' package
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/RootLayout.fxml"));
			rootLayout = loader.load();
			mainStage = new Stage();
			mainStage.setTitle("Диспетчерская служба");
			mainStage.getIcons().add(new Image("file:resources/images/icon.png"));
			mainStage.setMaximized(true);
			mainStage.setResizable(false);
			//mainStage.setFullScreen(false);
			//mainStage.setFullScreen(true);
			//mainStage.setMinWidth(1000.00);
			//mainStage.setMinHeight(600.00);

			// Set the scene
			Scene rootScene = new Scene(rootLayout);
			mainStage.setScene(rootScene);
			mainStage.show();

            // Give the controller access to MainApp and mainStage
            rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this, user);
            rootLayoutController.setMainStage(mainStage);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// calls the MainView to be loaded in the mainStage
		showMainView(user);
	}

	public void showUserView() throws SQLException {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/users/UserOverview.fxml"));
			AnchorPane cOverview = (AnchorPane) loader.load();

			/*
			rootLayout.setCenter(cOverview);
			UserOverviewController controller = loader.getController();
			controller.setMainApp(this);
			*/


			// Set the scene
			/**/
			UserOverviewController controller = loader.getController();

			Stage cStage = new Stage();
			cStage.setTitle("Пользователи");
			cStage.initOwner(mainStage);
			Scene orderScene = new Scene(cOverview);
			cStage.setScene(orderScene);

			controller.setEditStage(cStage);
			controller.setMainApp(this);
			cStage.showAndWait();



			/*
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/UserView.fxml"));
			userView = (AnchorPane) loader.load();

			// Set the scene
			Stage userStage = new Stage();
			userStage.setTitle("Управление пользователями");
			userStage.initOwner(mainStage);
			Scene orderScene = new Scene(userView);
			userStage.setScene(orderScene);

			Object controller = Class.forName(controllerName).newInstance();
			Class.forName(controllerName).cast(controller);



			// Give the controller access to the MainApp and basketStage and
			// sends information about the logged-in customer
			userController = loader.getController();
			userController.setEditStage(userStage);
			userController.setMainApp(this);

			// Display the CartView view and wait for user to close it
			userStage.showAndWait();
			*/
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void showOrderStatusView() throws SQLException {
		try {

			String title = "Статусы заказов";
			String fxml = "/view/order/OrderStatusOverview.fxml";

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(fxml));
			AnchorPane cOverview = (AnchorPane) loader.load();

			// TODO: Refactoring load conntroller to invoke
			OrderStatusOverviewController controller = loader.getController();

			controller = loader.getController();
			Stage cStage = new Stage();
			cStage.setTitle(title);
			cStage.initOwner(mainStage);
			Scene orderScene = new Scene(cOverview);
			cStage.setScene(orderScene);

			controller.setEditStage(cStage);
			controller.setMainApp(this);
			cStage.showAndWait();

		} catch (IOException e){
			e.printStackTrace();
		}
	}


	public void showSPageView(String title, String fxml, String mode) throws SQLException {
		try {


			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(fxml));
			AnchorPane cOverview = (AnchorPane) loader.load();
			Stage cStage = new Stage();

            // TODO: Refactoring load conntroller to invoke
            if(mode.equals("orderStatus")) {
                OrderStatusOverviewController controller = loader.getController();
                controller = loader.getController();
                controller.setEditStage(cStage);
                controller.setMainApp(this);
            }
            else if(mode.equals("Region")) {
              RegionOverviewController controller = loader.getController();
              controller = loader.getController();
              controller.setEditStage(cStage);
              controller.setMainApp(this);
            }
            else if(mode.equals("Specialization")) {
              SpecializationOverviewController controller = loader.getController();
              controller = loader.getController();
              controller.setEditStage(cStage);
              controller.setMainApp(this);
            }
            else if(mode.equals("orderCategory")) {
              OrderCategoryOverviewController controller = loader.getController();
              controller = loader.getController();
              controller.setEditStage(cStage);
              controller.setMainApp(this);
            }
            else if(mode.equals("orderAdvert")) {
              OrderAdvertOverviewController controller = loader.getController();
              controller = loader.getController();
              controller.setEditStage(cStage);
              controller.setMainApp(this);
            }
            else if(mode.equals("SubSpecialization")) {
              SubSpecializationOverviewController controller = loader.getController();
              controller = loader.getController();
              controller.setEditStage(cStage);
              controller.setMainApp(this);
            }
            else if(mode.equals("Workers")) {
                WorkerOverviewController controller = loader.getController();
                controller = loader.getController();
                controller.setEditStage(cStage);
                controller.setMainApp(this);
            }


			cStage.setTitle(title);
			cStage.initOwner(mainStage);
			Scene orderScene = new Scene(cOverview);
			cStage.setScene(orderScene);
			cStage.showAndWait();

		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public boolean showSPageEditDialog(String title, String fxml, String mode, Object obj) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, ParseException {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(fxml));
			AnchorPane page = (AnchorPane) loader.load();
			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle(title);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);


            if(mode.equals("orderStatusEdit")) {
                OrderStatusEditDialogController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setData(obj);
                dialogStage.setResizable(false);
                dialogStage.showAndWait();
                return controller.isOkClicked();
            }
            else if(mode.equals("RegionEdit")) {
              RegionEditDialogController controller = loader.getController();
              controller.setDialogStage(dialogStage);
                controller.setData(obj);
                dialogStage.setResizable(false);
              dialogStage.showAndWait();
              return controller.isOkClicked();
            }
            else if(mode.equals("SpecializationEdit")) {
              SpecializationEditDialogController controller = loader.getController();
              controller.setDialogStage(dialogStage);
                controller.setData(obj);
                dialogStage.setResizable(false);
              dialogStage.showAndWait();
              return controller.isOkClicked();
            }
            else if(mode.equals("orderCategoryEdit")) {
              OrderCategoryEditDialogController controller = loader.getController();
              controller.setDialogStage(dialogStage);
                controller.setData(obj);
                dialogStage.setResizable(false);
              dialogStage.showAndWait();
              return controller.isOkClicked();
            }
            else if(mode.equals("orderAdvertEdit")) {
              OrderAdvertEditDialogController controller = loader.getController();
              controller.setDialogStage(dialogStage);
                controller.setData(obj);
                dialogStage.setResizable(false);
              dialogStage.showAndWait();
              return controller.isOkClicked();
            }
            else if(mode.equals("SubSpecializationEdit")) {
              SubSpecializationEditDialogController controller = loader.getController();
              controller.setDialogStage(dialogStage);
              controller.setData(obj);
                dialogStage.setResizable(false);
              dialogStage.showAndWait();
              return controller.isOkClicked();
            }
            else if(mode.equals("WorkerEdit")) {
                WorkerEditDialogController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setData(obj);
                dialogStage.setResizable(false);
                dialogStage.showAndWait();
                return controller.isOkClicked();
            }
            else if(mode.equals("OrderEdit")) {
                OrderEditDialogController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setData(obj);
                dialogStage.setResizable(false);
                dialogStage.showAndWait();
                return controller.isOkClicked();
            }


			return false;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	public boolean showUserEditDialog(User user) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/users/UserEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Редактирование");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			UserEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setUser(user);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// shows the main window with product catalogue
	public void showMainView(User customer) throws SQLException, ParseException {
		try {
			// Load MainView.fxml from 'view' package
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/MainView.fxml"));
			mainView = (AnchorPane) loader.load();
			rootLayout.setCenter(mainView);

			// Give the controller access to the main app and
	        // sends information about the logged-in customer
	        mainViewController = loader.getController();
	        mainViewController.setMainApp(this, customer);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

// BASKET HANDLING

	// empty basket list
	private ObservableList<Product> basketList = FXCollections.observableArrayList();

	// adds a product to the basket list, including it's selected quantity
	public void addBasket(Product product, int quantity) {
		basketList.add(new Product(product.getName(), product.getPrice()*quantity, null,
				product.getCategory(), product.getImage(), quantity));
	}

	// gives access to the basketList
	public ObservableList<Product> getBasketList(){ return basketList; }

	// shows the actual basket view
	public void showViewBasket(User customer) {
		try{
	        // Load CartView.fxml from 'view' package
	        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/BasketView.fxml"));
	        viewBasket = (AnchorPane) loader.load();

	        // Set the scene
	        Stage basketStage = new Stage();
	        basketStage.setTitle("Your Basket");
	        basketStage.initOwner(mainStage);
	        Scene orderScene = new Scene(viewBasket);
	        basketStage.setScene(orderScene);

	        // Give the controller access to the MainApp and basketStage and
	        // sends information about the logged-in customer
	        cartController = loader.getController();
	        cartController.setEditStage(basketStage);
	        cartController.setMainApp(this, customer);
	        cartController.getPrice();

	        // Display the CartView view and wait for user to close it
	        basketStage.showAndWait();
	    } catch (IOException e){
	        e.printStackTrace();
	    }
	}

	// calculates the total price of the order and rounds the double to 2 decimals
    public double getTotalPrice() {
    	double price = 0;
    	for (int i=0; i<getBasketList().size();i++) {
    		price = price + getBasketList().get(i).getPrice();
    	}
    	price = Math.round(price * 100);
    	price = price / 100;
    	return price;
    }

    // updates the totalPrice label in Mainview / BasketView
    /*
	public void updateUIMainView() {
		mainViewController.setCustomerLabel();
		mainViewController.setLabelText();
	}
	*/



// ORDER HANDLING

	// empty basket list
    /*
	private ObservableList<Order> orderList = FXCollections.observableArrayList();

	// creates a order for the customer and saves it to the orderList
	public void saveOrder(User customer, double totalSum, String date) {
		String sum = Double.toString(totalSum);
		orderList.add(new Order(customer, sum, date));
	}

	public ObservableList<Order> getOrderList(){ return orderList; }

	public void showOrder(User customer) {
		try{
	        // Load CustomerView.fxml from 'view' package
	        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/OrderView.fxml"));
	        orderPane = (AnchorPane) loader.load();

	        // create and new scene and set it to the primaryStage
	        Scene orderScene = new Scene(orderPane);
	        primaryStage.setTitle("My Orders");
	        primaryStage.setScene(orderScene);

	        // Give the input Person to the controller
	        orderController = loader.getController();
	        orderController.setMainApp(this, customer);

	        // Display the CustomerProfile view and wait for user to close it
	        primaryStage.show();
	    } catch (IOException e){
	        e.printStackTrace();
	    }
	}
	*/

	// shows the password edit window
	public void showPassword(User customer) {
		try{
	        // Load Password.fxml from 'view' package
	        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/PasswordView.fxml"));
	        passwordPane = (AnchorPane) loader.load();

	        // create and new scene and set it to the primaryStage
	        Scene passwordScene = new Scene(passwordPane);
	        primaryStage.setTitle("Change Password");
	        primaryStage.setScene(passwordScene);

	        // Give the input Person to the controller
	        passwordController = loader.getController();
	        passwordController.setMainApp(this, customer);

	        // Display the Password view and wait for user to close it
	        primaryStage.show();
	    } catch (IOException e){
	        e.printStackTrace();
	    }
	}

	public void updateLoginUI() {
		this.loginViewController.setApp(this);
	}


// CUSTOMER HANDLING

	private ObservableList<User> customerList = FXCollections.observableArrayList();

	public ObservableList<User> getCustomerList(){ return customerList; }

	// shows the customer profile
	public void showCustomer(User customer) {
		try{
	        // Load CustomerView.fxml from 'view' package
	        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/CustomerView.fxml"));
	        customerPane = (AnchorPane) loader.load();

	        Scene customerScene = new Scene(customerPane);
	        //primaryStage.initOwner(mainStage);
	        //primaryStage.initModality(Modality.WINDOW_MODAL);
	        primaryStage.setTitle("My Profile");
	        primaryStage.setScene(customerScene);

	        // Give the input Person to the controller
	        customerController = loader.getController();
	        customerController.setPrimaryStage(primaryStage);
	        customerController.showCustomerDetails(customer);
	        customerController.setMainApp(this, customer);

	        // Display the CustomerProfile view and wait for user to close it
	        primaryStage.show();
	    } catch (IOException e){
	        e.printStackTrace();
	    }
	}

	public void addCustomer(User customer) {
		try{
	        // Load AddCustomer.fxml from 'view' package
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/AddCustomerView.fxml"));
	        addCustomer = (AnchorPane) loader.load();

	        // Create new scene and set to primaryStage
	        Scene addCustomerScene = new Scene(addCustomer);
	        primaryStage.setTitle("New Customer");
	        primaryStage.setScene(addCustomerScene);

	        // Give the input Person to the controller
	        addCustomerController = loader.getController();
	        addCustomerController.setMainApp(this, customer);

	        // Display the CustomerProfile view and wait for user to close it
	        primaryStage.show();
	    } catch (IOException e){
	        e.printStackTrace();
	    }
	}

	// called when 'delete profile' in addCustomerView is called
	// removes the customer entry from the customerList, saves the file, closes the mainStage and returns to loginView
	public void removeCustomer(User customer) {
		customerList.remove(customer);
		this.saveToFile(customerFile);
		mainStage.close();
		this.showLoginView();
	}


// FILE HANDLING

	// the file handling logic is inspired by the code used in the tutorial on the
	// following page: http://code.makery.ch/library/javafx-8-tutorial/part5/

	public void loadFile(File file) {
	    try {
	        JAXBContext context = JAXBContext.newInstance(CustomerListWrapper.class);
	        Unmarshaller unMarshall = context.createUnmarshaller();

	        // load xml from file
	        CustomerListWrapper wrapper = (CustomerListWrapper) unMarshall.unmarshal(file);

	        // clear the actual list and add all entries from the file
	        customerList.clear();
	        customerList.addAll(wrapper.getCustomer());

	    } catch (Exception e) {
	    	// display an alert when the file load is corrupt
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("File not loaded");
	        alert.setContentText("Could not load file:\n" + file.getPath());
	        alert.showAndWait();
	    }
	}

	public void saveToFile(File file) {
	    try {
	        // Wrapping up the product data
    		JAXBContext context = JAXBContext.newInstance(CustomerListWrapper.class);
    		Marshaller marshall = context.createMarshaller();
    		marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		CustomerListWrapper wrapper = new CustomerListWrapper();
    		wrapper.setCustomer(customerList);

    		// save the xml in the customer file
    		marshall.marshal(wrapper, file);

	    } catch (Exception e) {
	    	// display an alert when the file save is corrupt
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("File not saved");
	        alert.setContentText("Could not save file:\n" + file.getPath());
	        alert.showAndWait();
	    }
	}
}
