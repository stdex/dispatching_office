package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import service.UserService;
import utils.MD5;

public class LoginController {

    @FXML private Button loginButton;
    @FXML private TextField loginNode;
    @FXML private PasswordField passwordNode;
    @FXML private Label information;
    //@FXML private ComboBox<Customer> customerComboBox;
    
    private MainApp MainApp;
    private Stage primaryStage;
    
    @FXML
    public void initialize() {
    }
    
    @FXML public void loginAction (ActionEvent event) throws Exception {
    	
    	// Stores the entered text from the passwordField
        String login = loginNode.getText();
    	String password = passwordNode.getText();

        if(login.equals("") || password.equals("")) {
            information.setText("Вы не ввели логин или пароль!");
        }
        else {
            MD5 md5_obj = new MD5();
            String md5_password = md5_obj.crypt(password);
            System.out.println(md5_password);
            UserService u_service =  new UserService();
            //System.out.println( Boolean.toString(u_service.do_login(login, md5_password)) );
            if(u_service.do_login(login, md5_password)) {
                User user = u_service.get_authorization_user();
                MainApp.callMainView(user);
                primaryStage.close();
                primaryStage.hide();
            }
            else {
                information.setText("Неправильные логин или пароль!");
            }
            //System.out.println( Boolean.toString(MainApp.u_service.do_login(login, md5_password)) );

        }

    }
    // Called when the Cancel button is pressed. It closes the stage and the program
    @FXML public void handleCancel() {
    	primaryStage.close();
    }
 // Called when the 'Create New Account' label is pressed. 'Add Customer' view gets opened on the same stage.
    @FXML public void createNewAccount() {
    	MainApp.addCustomer(null);
    }
    // Creates a reference to the primaryStage
    public void setPrimaryStage(Stage primaryStage) {
    	this.primaryStage = primaryStage;
    }
    // Creates a reference to the MainApp, receives the customerList and populates the comboBox with the customers
    public void setApp(MainApp mainApp) {
    	this.MainApp = mainApp;
    	//ObservableList<Customer> p = MainApp.getCustomerList();
    	//customerComboBox.setItems(p);
    }
}
