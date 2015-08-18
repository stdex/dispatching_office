package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import model.User;

import java.sql.SQLException;

public class RootLayoutController {
	
	private MainApp mainApp;
	private Stage mainStage;

	@FXML
	private Menu users;
	@FXML
	private Menu workers;
	@FXML
	private MenuItem menuOrderStatus;
    @FXML
    private MenuItem menuOrderCategory;
    @FXML
    private MenuItem menuOrderAdvert;
    @FXML
    private MenuItem menuRegions;
    @FXML
    private MenuItem menuSpecializations;
    @FXML
    private MenuItem menuSubSpecializations;

    public static User current_user;


	public RootLayoutController() {
	}

	@FXML
	private void initialize() {

	}
	
    public void setMainStage(Stage mainStage) {
    	this.mainStage = mainStage;
    }
    
    public void setMainApp(MainApp mainApp, User user) {

        current_user = user;

		if(!user.getGroup_id().equals("1")) {
			users.setVisible(false);
            menuRegions.setVisible(false);
            menuSpecializations.setVisible(false);
            menuSubSpecializations.setVisible(false);
			menuOrderStatus.setVisible(false);
            menuOrderCategory.setVisible(false);
            menuOrderAdvert.setVisible(false);
		}

		this.mainApp = mainApp;
    }
    
    public void handleLogOut() {
    	mainApp.getBasketList().clear();
    	mainStage.close();
    	mainApp.showLoginView();
    }

	public void handleQuit() {
		System.exit(0);
	}

	public void handleUsers() throws SQLException {
		mainApp.showUserView();
	}

	public void handleOrderStatus() throws SQLException {

		String title = "Статусы заказов";
		String fxml = "/view/orders/OrderStatusOverview.fxml";
		String mode = "orderStatus";
		mainApp.showSPageView(title, fxml, mode);
	}

    public void handleRegions() throws SQLException {

        String title = "Районы";
        String fxml = "/view/workers/RegionOverview.fxml";
        String mode = "Region";
        mainApp.showSPageView(title, fxml, mode);
    }


    public void handleSpecializations() throws SQLException {

        String title = "Специализации";
        String fxml = "/view/workers/SpecializationOverview.fxml";
        String mode = "Specialization";
        mainApp.showSPageView(title, fxml, mode);
    }

	public void handleOrderCategory() throws SQLException {

		String title = "Категории";
		String fxml = "/view/orders/OrderCategoryOverview.fxml";
		String mode = "orderCategory";
		mainApp.showSPageView(title, fxml, mode);
	}

    public void handleOrderAdvert() throws SQLException {

        String title = "Источники заказов";
        String fxml = "/view/orders/OrderAdvertOverview.fxml";
        String mode = "orderAdvert";
        mainApp.showSPageView(title, fxml, mode);
    }

    public void handleSubSpecializations() throws SQLException {

        String title = "Виды работ";
        String fxml = "/view/workers/SubSpecializationOverview.fxml";
        String mode = "SubSpecialization";
        mainApp.showSPageView(title, fxml, mode);
    }

    public void handleWorkers() throws SQLException {

        String title = "Мастера";
        String fxml = "/view/workers/WorkerOverview.fxml";
        String mode = "Workers";
        mainApp.showSPageView(title, fxml, mode);
    }



}
