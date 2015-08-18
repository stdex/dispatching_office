package controller.orders;

import controller.MainViewController;
import controller.RootLayoutController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDateTimeTextField;
import model.*;
import org.controlsfx.control.CheckListView;
import service.WorkerService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderEditDialogController {

    @FXML
    private TextField dField1;
    @FXML
    private TextField dField2;
    @FXML
    private TextField dField3;

    @FXML
    private TextArea dArea1;

    @FXML
    private ComboBox dCombo1;
    @FXML
    private ComboBox dCombo2;
    //@FXML
    //private ComboBox dCombo3;
    @FXML
    private ComboBox dCombo4;

    @FXML
    private LocalDateTimeTextField dLocalDate1;

    @FXML
    private Button dBtn1;


    @FXML
    private CheckListView search_subspec;
    @FXML
    private ComboBox search_spec;
    @FXML
    private ComboBox search_region;
    /*
    @FXML
    private TextField search_name;
    @FXML
    private TextField search_phone;

    */

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
    private Button search_btn;

    @FXML
    private TableView<Worker> dTable;

    @FXML
    private TableColumn<Worker, String> tColumn1;



    private Stage dialogStage;
    private boolean okClicked = false;
    private Order obj;

    public static ObservableList<Worker> workers;
    public static ObservableList<OrderStatus> statuses;
    public static ObservableList<OrderCategory> categorires;
    public static ObservableList<OrderAdvert> adverts;

    public static ObservableList<Worker> tData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tsData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tkData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tcData = FXCollections.observableArrayList();

    public static String err_title = "Незаполненные поля";
    public static String err_header = "Пожалуйста, заполните поля!";
    public static String err_text = "Вы не ввели ФИО!\n";
    public static String err_text_phone = "Вы не ввели телефон!\n";
    public static String err_text_adress = "Вы не ввели адрес!\n";
    public static String err_text_advert = "Вы не ввели источник!\n";
    public static String err_text_category = "Вы не ввели каегорию!\n";
    public static String err_text_worker = "Вы не ввели мастера!\n";
    public static String err_text_status = "Вы не ввели статус!\n";
    public static String err_text_time = "Вы не ввели время!\n";

    public static ObservableList<Specialization> specializations = FXCollections.observableArrayList();
    public static ObservableList<Region> regions = FXCollections.observableArrayList();;
    public static WorkerService tService =  new WorkerService();

    public static Worker temp = new Worker();
    public static Worker selected;
    public static Worker details;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        tColumn1.setCellValueFactory(cellData -> cellData.getValue().fullnameProperty());
        dTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    obj.setLink_worker_id(newValue.getId());
                    obj.setLink_worker(newValue.getFullname());
                    try {
                        showDetails(newValue);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
        //showDetails(null);


    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     * 
     * @param obj
     */
    public void setData(Object obj) throws SQLException, ParseException {
        // TODO: refactoring with invoke
        this.obj = (Order) obj;
        dField1.setText(this.obj.getFullname());
        dField2.setText(this.obj.getPhone());
        dField3.setText(this.obj.getAddress());

        dArea1.setText(this.obj.getReason());

        adverts = MainViewController.tService.get_advert_list();
        dCombo1.setItems(adverts);
        dCombo1.getSelectionModel().select(this.obj.getAdvert_source_id());

        categorires = MainViewController.tService.get_category_list();
        dCombo2.setItems(categorires);
        dCombo2.getSelectionModel().select(this.obj.getWork_cat_id());

        /*
        workers = MainViewController.tService.get_workers_list();
        dCombo3.setItems(workers);
        dCombo3.getSelectionModel().select(this.obj.getLink_worker_id());
        */

        statuses = MainViewController.tService.get_status_list();
        dCombo4.setItems(statuses);
        dCombo4.getSelectionModel().select(this.obj.getStatus_id());

        if(this.obj.getWork_datetime() != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            Date ts = dateFormat.parse(this.obj.getWork_datetime());
            Instant instant = Instant.ofEpochMilli(ts.getTime());
            LocalDateTime res_ts = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            dLocalDate1.setLocalDateTime(res_ts);
        }



        //tData = tService.get_list();
        //dTable.setItems(tData);


        specializations.clear();
        specializations.add(new Specialization());
        specializations.addAll(tService.get_specialization_list());
        search_spec.setItems(specializations);

        regions.clear();
        regions.add(new Region());
        regions.addAll(tService.get_region_list());
        search_region.setItems(regions);

        if(this.obj.getId() == null) {
            dBtn1.setDisable(true);
            showDetails(null);
        }
        else {
            ObservableList<Worker> wData = FXCollections.observableArrayList();
            wData =  tService.get_worker_by_id(this.obj.getLink_worker_id());
            dTable.setItems(wData);
            Worker worker = wData.get(0);
            dTable.getSelectionModel().select(worker);
            search_spec.getSelectionModel().select(worker.getSpecialization_id());
            search_region.getSelectionModel().select(worker.getRegion_id());

            String sub_spec = worker.getSub_specialization();

            if( search_spec.getValue() != null) {

                try {
                    tcData = tService.get_overview_ss_list(sub_spec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                String current_choose_id = ((Specialization) search_spec.getSelectionModel().getSelectedItem()).getId();


                //tsData.clear();
                try {
                    tsData = tService.get_csubspec_list(current_choose_id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                search_subspec.setItems(tsData);
                for (SubSpecialization item : tsData) {
                    if (tcData.contains(item)) {
                        search_subspec.getCheckModel().check(item);
                    }
                }


            }

        }

        /*
        regions = tService.get_region_list();
        search_region.setItems(regions);
        */

        search_spec.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Specialization>() {
            @Override
            public void changed(ObservableValue<? extends Specialization> arg0, Specialization arg1, Specialization arg2) {
                if (arg2 != null) {

                    dTable.setItems(null);

                    String sub_spec = null;
                    search_subspec.getCheckModel().clearChecks();
                    try {
                        tcData = tService.get_overview_ss_list(sub_spec);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String current_choose_id = ((Specialization) search_spec.getSelectionModel().getSelectedItem()).getId();

                    //tsData.clear();
                    try {
                        tkData = tService.get_csubspec_list(current_choose_id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    search_subspec.setItems(tkData);
                    for (SubSpecialization item : tkData) {
                        if (tcData.contains(item)) {
                            search_subspec.getCheckModel().check(item);
                        }
                    }

                }
            }
        });

        search_region.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Region>() {
            @Override
            public void changed(ObservableValue<? extends Region> arg0, Region arg1, Region arg2) {
                if (arg2 != null) {

                    dTable.setItems(null);

                }
            }
        });


    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() throws ParseException {
        if (isInputValid()) {

            obj.setFullname(dField1.getText());
            obj.setPhone(dField2.getText());
            obj.setAddress(dField3.getText());
            obj.setReason(dArea1.getText());

            obj.setLink_user(RootLayoutController.current_user.getFullname());
            obj.setLink_user_id(RootLayoutController.current_user.getId());
            obj.setArchive("0");

            if(dCombo1.getSelectionModel().selectedItemProperty().getValue() != null) {
                for(OrderAdvert item : adverts){
                    if (item.getTitle() == dCombo1.getSelectionModel().selectedItemProperty().getValue().toString()) {
                        obj.setAdvert_source_id(item.getId());
                        obj.setAdvert_source(item.getTitle());
                    }
                }
            }

            if(dCombo2.getSelectionModel().selectedItemProperty().getValue() != null) {
                for(OrderCategory item : categorires){
                    if (item.getTitle() == dCombo2.getSelectionModel().selectedItemProperty().getValue().toString()) {
                        obj.setWork_cat_id(item.getId());
                        obj.setWork_cat(item.getTitle());
                    }
                }
            }

            /*
            if(dCombo3.getSelectionModel().selectedItemProperty().getValue() != null) {
                for(Worker item : workers){
                    if (item.getFullname() == dCombo3.getSelectionModel().selectedItemProperty().getValue().toString()) {
                        obj.setLink_worker_id(item.getId());
                        obj.setLink_worker(item.getFullname());
                    }
                }
            }
            else {

                obj.setLink_worker_id("");
                obj.setLink_worker("");

            }
            */
            if(dTable.getSelectionModel().selectedItemProperty().getValue() == null) {
                obj.setLink_worker_id("");
                obj.setLink_worker("");
            }


            if(dCombo4.getSelectionModel().selectedItemProperty().getValue() != null) {
                for(OrderStatus item : statuses){
                    if (item.getTitle() == dCombo4.getSelectionModel().selectedItemProperty().getValue().toString()) {
                        obj.setStatus_id(item.getId());
                        obj.setStatus(item.getTitle());
                    }
                }
            }

            LocalDateTime ldt = dLocalDate1.getLocalDateTime();
            if(ldt != null) {
                Timestamp ts = Timestamp.valueOf(ldt);
                obj.setWork_datetime(String.valueOf(ts.getTime()));
            }

            /*
            System.out.println(ldt);
            ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
            String timestamp = Long.toString(zdt.toInstant().toEpochMilli());
            System.out.println(timestamp);
            */

            //obj.setWork_datetime(Long.toString(System.currentTimeMillis()));
            //obj.setAppend_datetime(Long.toString(System.currentTimeMillis()));


            try {
                if(obj.getId() != null) {
                    MainViewController.tService.edit(obj);
                }
                else {
                    MainViewController.tService.add(obj);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleArchive() throws SQLException, ParseException {
        obj.setArchive("1");
        MainViewController.tService.to_archive(obj);

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleSearch() throws SQLException, ParseException {
        String spec_id = "";
        String fullname = "";
        String sub_spec_ids = "";
        String region_id = "";
        String phone = "";

        for(Specialization item : specializations){
            if(search_spec.getSelectionModel().selectedItemProperty().getValue() != null) {
                if (item.getTitle() == search_spec.getSelectionModel().selectedItemProperty().getValue().toString()) {
                    spec_id = item.getId();
                    if(spec_id == null) {
                        spec_id = "";
                    }
                }
            }
        }

        for(Region item : regions){
            if(search_region.getSelectionModel().selectedItemProperty().getValue() != null) {
                if (item.getTitle() == search_region.getSelectionModel().selectedItemProperty().getValue().toString()) {
                    region_id = item.getId();
                    if(region_id == null) {
                        region_id = "";
                    }
                }
            }

        }

        //tcData.clear();
        tkData = search_subspec.getCheckModel().getCheckedItems();
        String selected_subspec_items = "";
        List<String> selected_s_items = new ArrayList<String>();
        for(SubSpecialization item : tkData){
            selected_s_items.add(item.getId());
        }
        sub_spec_ids = String.join(",", selected_s_items);

        //fullname = search_name.getText();

        //phone = search_phone.getText();

        tData = tService.search_workers(fullname, spec_id, sub_spec_ids, region_id, phone);
        dTable.setItems(tData);
        showDetails(null);
    }

    private void showDetails(Object obj) throws SQLException {
        // TODO: cast object invoke
        details = (Worker) obj;
        System.out.println(details);
        if (details != null) {
            dLabel1.setText(details.getFullname());
            dLabel2.setText(details.getPhone());
            dLabel3.setText(details.getCount_done());
            dLabel4.setText(details.getCount_inwork());
            dLabel5.setText(details.getCount_wasunavailable());
            dLabel6.setText(details.getCount_waschanged());
            dLabel7.setText(details.getCount_notperformed());
        } else {
            dLabel1.setText("");
            dLabel2.setText("");
            dLabel3.setText("");
            dLabel4.setText("");
            dLabel5.setText("");
            dLabel6.setText("");
            dLabel7.setText("");
        }
    }

    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (dField1.getText() == null || dField1.getText().length() == 0) {
            errorMessage += err_text;
        }

        if (dField2.getText() == null || dField2.getText().length() == 0) {
            errorMessage += err_text_phone;
        }

        if (dField3.getText() == null || dField3.getText().length() == 0) {
            errorMessage += err_text_adress;
        }

        if (dCombo1.getSelectionModel().selectedItemProperty().getValue()  == null) {
            errorMessage += err_text_advert;
        }

        if (dCombo2.getSelectionModel().selectedItemProperty().getValue()  == null) {
            errorMessage += err_text_category;
        }

        /*
        if (dCombo3.getSelectionModel().selectedItemProperty().getValue()  == null) {
            errorMessage += err_text_worker;
        }
        */

        if (dCombo4.getSelectionModel().selectedItemProperty().getValue()  == null) {
            errorMessage += err_text_status;
        }

        if(dLocalDate1.getLocalDateTime() == null) {
            errorMessage += err_text_time;
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle(err_title);
            alert.setHeaderText(err_header);
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            
            return false;
        }
    }
}
