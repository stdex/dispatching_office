package controller;


import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import model.*;
import org.controlsfx.control.StatusBar;
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
    private Label dLabel0;
    @FXML
    private Label dLabel1;
    @FXML
    private Label dLabel2;
    @FXML
    private Label dLabel_region;
    @FXML
    private Label dLabel3;
    @FXML
    private Label dLabel4;
    @FXML
    private Label dLabel5;
    @FXML
    private Label dLabel6;
    @FXML
    private Label dLabel_starttime;
    @FXML
    private Label dLabel_endtime;
    @FXML
    private Label dLabel_price;
    @FXML
    private Label dLabel7;
    @FXML
    private Label dLabel8;
    @FXML
    private Label dLabel9;
    @FXML
    private Label dLabel10;


    @FXML
    private Label total;
    @FXML
    private Label totalInWork;

    @FXML
    private TextArea textArea1;


	@FXML
    private Label totalSum, items, testTimer;


    public static ObservableList<Order> tData = FXCollections.observableArrayList();
    public static ObservableList<Order> ttData = FXCollections.observableArrayList();
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
    private ComboBox search_archive;

    @FXML
    private DatePicker search_workdate;

    @FXML
    private DatePicker search_workdate_end;

    @FXML
    private TextField search_orderid;

    @FXML
    private Button dBtn1;


    public static ObservableList<OrderStatus> statuses = FXCollections.observableArrayList();
    public static ObservableList<OrderCategory> categories = FXCollections.observableArrayList();
    public static ObservableList<Archive> archive_statuses = FXCollections.observableArrayList();
	
	public static User loggedInUser;

    public static StatusBar statusBar = new StatusBar();
	
    @FXML
    private void initialize() throws SQLException, IOException {

        /*
        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(statusBar);
        */

        total.setText(String.valueOf(tService.get_count_all()));
        totalInWork.setText(String.valueOf(tService.get_count_inwork()));

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        //format.parse(cellData.getValue().work_datetimeProperty()).toString()

        tColumn1.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        tColumn2.setCellValueFactory(cellData -> cellData.getValue().append_datetimeProperty());
        tColumn3.setCellValueFactory(cellData -> cellData.getValue().timer_strProperty());
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


        statuses.clear();
        statuses.add(new OrderStatus());
        statuses.addAll(OrderStatusOverviewController.tService.get_list());
        search_status.setItems(statuses);

        Archive c_arch = new Archive("0", "Не в архиве");
        archive_statuses.clear();
        archive_statuses.add(new Archive());
        archive_statuses.add(c_arch);
        archive_statuses.add(new Archive("1", "В архиве"));
        search_archive.getSelectionModel().select(c_arch);
        search_archive.setItems(archive_statuses);

        search_workdate.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.equals("")) {
                    search_workdate.setValue(null);
                }

            }
        });

        search_workdate_end.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.equals("")) {
                    search_workdate_end.setValue(null);
                }

            }
        });

        handleSearch();
        update_suggests_controls();

        //recheck_table();

        Timeline tbl_update = new Timeline(
                new KeyFrame(Duration.seconds(60*5),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {

                                try {
                                    //recheck_table();
                                    handleSearch();
                                    update_suggests_controls();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(60*5))
        );
        tbl_update.setCycleCount(Animation.INDEFINITE);
        tbl_update.play();


        Timeline timeline_update = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {

                                try {
                                    refresh_timers();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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

    private void refresh_timers() throws ParseException, IOException, SQLException {

        ObservableList<Order> n_tData = FXCollections.observableArrayList();

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date cur_date = new Date();

        for(Order item : tData){
            String dt = item.getWork_datetime();
            Date db_date = dateFormat.parse(dt);

            long diff = cur_date.getTime() - db_date.getTime();

            //long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000);
            //long diffHours = diff / (60 * 60 * 1000);

            //String out_timer_datetime = diffHours+":"+diffMinutes+":"+diffSeconds;
            String out_timer_datetime = String.valueOf(diffMinutes);
            if(item.getArchive().equals("0") && (item.getStatus_id().equals("1") || item.getStatus_id().equals("2")) ) {
                item.setTimer_str(out_timer_datetime);
            }


            n_tData.add(item);
        }

        Order selected_order = null;
        if(dTable.getSelectionModel().selectedItemProperty().getValue() != null) {
            selected_order = dTable.getSelectionModel().selectedItemProperty().get();
        }

        tData.clear();
        tData = n_tData;
        dTable.setItems(tData);

        dTable.getSelectionModel().select(selected_order);

        refresh_row_color();

    }

    private void refresh_row_color() {

        dTable.setRowFactory(new Callback<TableView<Order>, TableRow<Order>>() {

            @Override
            public TableRow<Order> call(TableView<Order> paramP) {

                return new TableRow<Order>() {
                    @Override
                    protected void updateItem(Order paramT, boolean paramBoolean) {

                        super.updateItem(paramT, paramBoolean);

                        if (!isEmpty()) {

                            String status_id = paramT.getStatus_id();
                            String timer_value = paramT.getTimer_str();

                            if (!timer_value.equals("")) {
                                if (status_id.equals("1") && (Integer.valueOf(timer_value) > 15)) {

                                    String color = "#ffb366";

                                    String style = "-fx-control-inner-background: " + color + ";"
                                            + "-fx-control-inner-background-alt: " + color + ";";

                                    setStyle(style);

                                } else if (status_id.equals("2") && (Integer.valueOf(timer_value) > 15)) {

                                    String color = "#ff0000";

                                    String style = "-fx-control-inner-background: " + color + ";"
                                            + "-fx-control-inner-background-alt: " + color + ";";

                                    setStyle(style);
                                }
                            }


                            //super.updateItem(paramT, paramBoolean);
                        } else {
                            this.setStyle("cell: indexed-cell table-row-cell");
                        }

                    }
                };
            }
        });

    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }


    private void showDetails(Object obj) throws SQLException {
        // TODO: cast object invoke
        details = (Order) obj;
        //System.out.println(details);
        if (details != null) {
            dLabel0.setText(details.getId());
            dLabel1.setText(details.getFullname());
            dLabel2.setText(details.getPhone());
            dLabel_region.setText(details.getRegionName());
            dLabel3.setText(details.getAddress());
            dLabel4.setText(details.getAdvert_source());
            textArea1.setText(details.getReason());
            dLabel5.setText(details.getWork_cat());
            dLabel6.setText(details.getLink_worker());
            dLabel7.setText(details.getLink_worker_phone());
            dLabel_starttime.setText(details.getStart_time());
            dLabel_endtime.setText(details.getEnd_time());
            //dLabel7.setText(details.getWork_datetime());
            dLabel8.setText(details.getAppend_datetime());
            dLabel_price.setText(details.getPrice());
            dLabel9.setText(details.getStatus());
            dLabel10.setText(details.getAnnotation());

        } else {
            dLabel0.setText("");
            dLabel1.setText("");
            dLabel2.setText("");
            dLabel_region.setText("");
            dLabel3.setText("");
            dLabel4.setText("");
            dLabel5.setText("");
            dLabel6.setText("");
            dLabel7.setText("");
            dLabel_starttime.setText("");
            dLabel_endtime.setText("");
            dLabel8.setText("");
            dLabel_price.setText("");
            dLabel9.setText("");
            dLabel10.setText("");
            textArea1.setText("");
        }

        textArea1.setEditable(false);
    }

    
    // Reference to mainApp == receives the logged in customer from the loginView
    public void setMainApp(MainApp mainApp, User customer) throws SQLException, ParseException, IOException {
        this.mainApp = mainApp;

        loggedInUser = customer;

        //handleSearch();

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
        String append_datetime_start = "";
        String append_datetime_end = "";
        String archive = "";
        String orderid = "";

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

        for(Archive item : archive_statuses){
            if(search_archive.getSelectionModel().selectedItemProperty().getValue() != null) {
                if (item.getTitle() == search_archive.getSelectionModel().selectedItemProperty().getValue().toString()) {
                    if(item.getId() != null) {
                        archive = item.getId();
                    }
                }
            }
        }

        fullname = search_name.getText();
        phone = search_phone.getText();
        address = search_address.getText();
        link_worker = search_worker.getText();
        orderid = search_orderid.getText();

        LocalDate localDate = search_workdate.getValue();
        LocalDate localDate_2 = search_workdate_end.getValue();

        if(localDate != null) {
            if(localDate_2 != null) {
                Timestamp ts_start = Timestamp.valueOf(localDate.atStartOfDay());
                Timestamp ts_end = Timestamp.valueOf(localDate_2.plusDays(1).atStartOfDay());
                append_datetime_start = String.valueOf(ts_start.getTime());
                append_datetime_end = String.valueOf(ts_end.getTime());
            }
            else {
                Timestamp ts_start = Timestamp.valueOf(localDate.atStartOfDay());
                Timestamp ts_end = Timestamp.valueOf(localDate.plusDays(1).atStartOfDay());
                append_datetime_start = String.valueOf(ts_start.getTime());
                append_datetime_end = String.valueOf(ts_end.getTime());
            }
        }
        else {
            append_datetime_start = "";
            append_datetime_end = "";
        }

        Order selected_order = null;
        if(dTable.getSelectionModel().selectedItemProperty().getValue() != null) {
            selected_order = dTable.getSelectionModel().selectedItemProperty().get();
        }

        tData = tService.search_orders(fullname, phone, address, link_worker, work_cat_id, status_id, append_datetime_start, append_datetime_end, archive, orderid);
        dTable.setItems(tData);

        for (Order ord : tData) {

            if(selected_order != null && ord != null) {
                if(ord.getId().equals(selected_order.getId())) {
                    dTable.getSelectionModel().select(ord);
                    //dTable.getSelectionModel().focus(dTable.getSelectionModel().getSelectedIndex());
                }
            }

        }

        showDetails(null);

    }


    @FXML
    private void handleDelete() throws SQLException, ParseException, IOException {

        int selectedIndex = dTable.getSelectionModel().getSelectedIndex();
        selected = dTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tService.remove(selected);

            handleSearch();

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
    private void handleNew() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ParseException, IOException {
        temp = new Order();
        boolean okClicked = mainApp.showSPageEditDialog(title, fxml, mode, temp);
        if (okClicked) {

            handleSearch();

            total.setText(String.valueOf(tService.get_count_all()));
            totalInWork.setText(String.valueOf(tService.get_count_inwork()));
        }

    }

    @FXML
    private void handleEdit() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ParseException, IOException {

        selected = dTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean okClicked = mainApp.showSPageEditDialog(title, fxml, mode, selected);
            if (okClicked) {

                handleSearch();

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


    public void update_suggests_controls() throws SQLException, IOException {

        total.setText(String.valueOf(tService.get_count_all()));
        totalInWork.setText(String.valueOf(tService.get_count_inwork()));

        ttData.clear();
        ttData = tService.get_list();

        categories.clear();
        categories.add(new OrderCategory());
        categories.addAll(OrderCategoryOverviewController.tService.get_list());
        search_category.setItems(categories);

        List customer_suggest = new ArrayList<>();
        List customer_phone_suggest = new ArrayList<>();
        List worker_suggest = new ArrayList<>();
        List customer_addr_suggest = new ArrayList<>();

        for (Order order : ttData) {
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

        refresh_row_color();
    }

    /*
    public void recheck_table() throws SQLException, IOException {

        Order selected_order = null;
        if(dTable.getSelectionModel().selectedItemProperty().getValue() != null) {
            selected_order = dTable.getSelectionModel().selectedItemProperty().get();
        }

        tData.clear();
        tData = tService.get_list();
        dTable.setItems(tData);

        dTable.getSelectionModel().select(selected_order);

        categories.clear();
        categories.add(new OrderCategory());
        categories.addAll(OrderCategoryOverviewController.tService.get_list());
        search_category.setItems(categories);

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


        refresh_row_color();

    }
    */


}