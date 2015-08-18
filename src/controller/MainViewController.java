package controller;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import controller.orders.OrderCategoryOverviewController;
import controller.orders.OrderStatusOverviewController;
import controller.workers.WorkerOverviewController;
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
import javafx.util.Callback;
import javafx.util.Duration;
import model.*;
import org.controlsfx.control.textfield.TextFields;
import service.OrderService;
import utils.ColorFormatCell;
import utils.StyleChangingRowFactory;

public class MainViewController {

    @FXML
    private TableView<Order> dTable;
    @FXML
    private TableColumn<Order, String> tColumn1;
    @FXML
    private TableColumn<Order, String> tColumn2;
    @FXML
    private TableColumn<Order, String> tColumn3;
    //@FXML
    //private TableColumn<Order, String> tColumn4;
    @FXML
    private TableColumn<Order, String> tColumn5;
    @FXML
    private TableColumn<Order, String> tColumn6;

    @FXML
    private Label dLabel1;
    @FXML
    private Label dLabel2;
    @FXML
    private Label dLabel3;
    @FXML
    private Label dLabel4;
    @FXML
    private Label dLabel5;
    @FXML
    private Label dLabel6;
    @FXML
    private Label dLabel7;
    @FXML
    private Label dLabel8;
    @FXML
    private Label dLabel9;

    @FXML
    private Label total;
    @FXML
    private Label totalInWork;

    @FXML
    private TextArea textArea1;


	@FXML
    private Label totalSum, items, testTimer;


    public static ObservableList<Order> tData = FXCollections.observableArrayList();
    public static ObservableList<Worker> wData = FXCollections.observableArrayList();
    public static OrderService tService =  new OrderService();
    public static Order temp = new Order();
    public static Order selected;
    public static Order details;
    public static String title = "Редактирование";
    public static String mode = "OrderEdit";
    public static String fxml = "/view/orders/OrderEditDialog.fxml";

    public static String err_title = "Не выбран заказ";
    public static String err_header = "Не выбран заказ";
    public static String err_text = "Пожалуйста, выберите заказ из таблицы.";
	
	private MainApp mainApp;
    private Stage orderStage;
	private int quant, rating;
	private List<Integer> ratingArray;

    @FXML
    private TextField search_name;
    @FXML
    private TextField search_phone;
    @FXML
    private TextField search_address;
    @FXML
    private TextField search_worker;

    @FXML
    private ComboBox search_category;
    @FXML
    private ComboBox search_status;

    @FXML
    private DatePicker search_workdate;

    @FXML
    private Button dBtn1;


    public static ObservableList<OrderStatus> statuses = FXCollections.observableArrayList();;
    public static ObservableList<OrderCategory> categories = FXCollections.observableArrayList();;

	
	public static User loggedInCustomer;
	
    @FXML
    private void initialize() throws SQLException {

        total.setText(String.valueOf(tService.get_count_all()));
        totalInWork.setText(String.valueOf(tService.get_count_inwork()));

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        //format.parse(cellData.getValue().work_datetimeProperty()).toString()

        //tColumn1.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        tColumn2.setCellValueFactory(cellData -> cellData.getValue().work_datetimeProperty());
        tColumn3.setCellValueFactory(cellData -> cellData.getValue().work_catProperty());
        //tColumn4.setCellValueFactory(cellData -> cellData.getValue().reasonProperty());
        tColumn5.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        tColumn6.setCellValueFactory(cellData -> cellData.getValue().link_workerProperty());
        showDetails(null);
        //final StyleChangingRowFactory<Order> rowFactory = new StyleChangingRowFactory<>("highlightedRow");
        dTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        showDetails(newValue);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

        recheck_table();
        /*
        dTable.setRowFactory(new Callback<TableView<Order>, TableRow<Order>>() {
            @Override
            public TableRow<Order> call(TableView<Order> paramP) {

                return new TableRow<Order>() {
                    @Override
                    protected void updateItem(Order paramT, boolean paramBoolean) {

                        if(paramT != null) {
                            String color = paramT.getStatus_color();

                            String style = "-fx-control-inner-background: "+color+";"
                                    + "-fx-control-inner-background-alt: "+color+";";

                            setStyle(style);
                            super.updateItem(paramT, paramBoolean);
                        }


                    }
                };
            }
        });
        */


        /*
        dTable.setRowFactory(new Callback<TableView<Order>, TableView<Order>>() {
            @Override
            public TableView<Order> call(TableView<Order> list) {
                return new ColorFormatCell();
            }
        */

        Timeline timeline_update = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                //rowFactory.getStyledRowIndices().setAll(dTable.getSelectionModel().getSelectedIndices());
                                //System.out.println(123);
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline_update.setCycleCount(Animation.INDEFINITE);
        timeline_update.play();


            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    Calendar time = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                                    testTimer.setText(simpleDateFormat.format(time.getTime()));
                                }
                            }
                    ),
                    new KeyFrame(Duration.seconds(1))
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

        }


    private void showDetails(Object obj) throws SQLException {
        // TODO: cast object invoke
        details = (Order) obj;
        System.out.println(details);
        if (details != null) {
            dLabel1.setText(details.getFullname());
            dLabel2.setText(details.getPhone());
            dLabel3.setText(details.getAddress());
            dLabel4.setText(details.getAdvert_source());
            textArea1.setText(details.getReason());
            dLabel5.setText(details.getWork_cat());
            dLabel6.setText(details.getLink_worker());
            dLabel7.setText(details.getWork_datetime());
            dLabel8.setText(details.getAppend_datetime());
            dLabel9.setText(details.getStatus());
        } else {
            dLabel1.setText("");
            dLabel2.setText("");
            dLabel3.setText("");
            dLabel4.setText("");
            dLabel5.setText("");
            dLabel6.setText("");
            dLabel7.setText("");
            dLabel8.setText("");
            dLabel9.setText("");
            textArea1.setText("");
        }

        textArea1.setEditable(false);
    }

    
    // Reference to mainApp == receives the logged in customer from the loginView
    public void setMainApp(MainApp mainApp, User customer) throws SQLException, ParseException {
        this.mainApp = mainApp;

        tData = tService.get_list();
        dTable.setItems(tData);
        recheck_table();

    }

    public void setEditStage(Stage orderStage){
        this.orderStage = orderStage;
    }
    
	public MainViewController() { }

    @FXML
    private void handleSearch() throws SQLException {

        String fullname = "";
        String phone = "";
        String address = "";
        String link_worker = "";
        String work_cat_id = "";
        String status_id = "";
        String work_datetime_start = "";
        String work_datetime_end = "";

        for(OrderCategory item : categories){
            if(search_category.getSelectionModel().selectedItemProperty().getValue() != null) {
                if (item.getTitle() == search_category.getSelectionModel().selectedItemProperty().getValue().toString()) {
                    work_cat_id = item.getId();
                    if(work_cat_id == null) {
                        work_cat_id = "";
                    }
                }
            }
        }

        for(OrderStatus item : statuses){
            if(search_status.getSelectionModel().selectedItemProperty().getValue() != null) {
                if (item.getTitle() == search_status.getSelectionModel().selectedItemProperty().getValue().toString()) {
                    status_id = item.getId();
                    if(status_id == null) {
                        status_id = "";
                    }
                }
            }

        }

        fullname = search_name.getText();
        phone = search_phone.getText();
        address = search_address.getText();
        link_worker = search_worker.getText();

        LocalDate localDate = search_workdate.getValue();
        if(localDate != null) {
            Timestamp ts_start = Timestamp.valueOf(localDate.atStartOfDay());
            Timestamp ts_end = Timestamp.valueOf(localDate.plusDays(1).atStartOfDay());
            work_datetime_start = String.valueOf(ts_start.getTime());
            work_datetime_end = String.valueOf(ts_end.getTime());
        }
        else {
            work_datetime_start = "";
            work_datetime_end = "";
        }



        tData = tService.search_orders(fullname, phone, address, link_worker, work_cat_id, status_id, work_datetime_start, work_datetime_end);
        dTable.setItems(tData);
        showDetails(null);

    }


    @FXML
    private void handleDelete() throws SQLException, ParseException {

        int selectedIndex = dTable.getSelectionModel().getSelectedIndex();
        selected = dTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tService.remove(selected);

            recheck_table();

            total.setText(String.valueOf(tService.get_count_all()));
            totalInWork.setText(String.valueOf(tService.get_count_inwork()));

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(err_title);
            alert.setHeaderText(err_header);
            alert.setContentText(err_text);
            alert.showAndWait();
        }

    }

    @FXML
    private void handleNew() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ParseException {
        temp = new Order();
        boolean okClicked = mainApp.showSPageEditDialog(title, fxml, mode, temp);
        if (okClicked) {

            recheck_table();

            total.setText(String.valueOf(tService.get_count_all()));
            totalInWork.setText(String.valueOf(tService.get_count_inwork()));
        }

    }

    @FXML
    private void handleEdit() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ParseException {

        selected = dTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean okClicked = mainApp.showSPageEditDialog(title, fxml, mode, selected);
            if (okClicked) {

                recheck_table();

                total.setText(String.valueOf(tService.get_count_all()));
                totalInWork.setText(String.valueOf(tService.get_count_inwork()));
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(err_title);
            alert.setHeaderText(err_header);
            alert.setContentText(err_text);
            alert.showAndWait();
        }

    }

    public void recheck_table() throws SQLException {

        tData.clear();
        tData = tService.get_list();
        dTable.setItems(tData);

        categories.clear();
        categories.add(new OrderCategory());
        categories.addAll(OrderCategoryOverviewController.tService.get_list());
        search_category.setItems(categories);

        statuses.clear();
        statuses.add(new OrderStatus());
        statuses.addAll(OrderStatusOverviewController.tService.get_list());
        search_status.setItems(statuses);

        List customer_suggest = new ArrayList<>();
        List customer_phone_suggest = new ArrayList<>();
        List worker_suggest = new ArrayList<>();
        List customer_addr_suggest = new ArrayList<>();

        for (Order order : tData) {
            customer_suggest.add(order.getFullname());
            customer_phone_suggest.add(order.getPhone());
            customer_addr_suggest.add(order.getAddress());
        }

        wData = WorkerOverviewController.tService.get_list();

        for (Worker worker : wData) {
            worker_suggest.add(worker.getFullname());
        }

        TextFields.bindAutoCompletion(search_name, customer_suggest);
        TextFields.bindAutoCompletion(search_phone, customer_phone_suggest);
        TextFields.bindAutoCompletion(search_address, customer_addr_suggest);
        TextFields.bindAutoCompletion(search_worker, worker_suggest);


        dTable.setRowFactory(new Callback<TableView<Order>, TableRow<Order>>() {
            @Override
            public TableRow<Order> call(TableView<Order> paramP) {

                return new TableRow<Order>() {
                    @Override
                    protected void updateItem(Order paramT, boolean paramBoolean) {


                        super.updateItem(paramT, paramBoolean);
                        if (!isEmpty()) {
                            String color = paramT.getStatus_color();

                            String style = "-fx-control-inner-background: " + color + ";"
                                    + "-fx-control-inner-background-alt: " + color + ";";

                            setStyle(style);
                            //super.updateItem(paramT, paramBoolean);
                        }
                        else {
                            this.setStyle("cell: indexed-cell table-row-cell");
                        }

                    }
                };
            }
        });



    }


}