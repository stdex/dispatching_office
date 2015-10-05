package controller.orders;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import controller.MainViewController;
import controller.RootLayoutController;
import controller.workers.WorkerOverviewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDateTimeTextField;
import model.*;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.CheckTreeView;
import service.OrderTimelineService;
import service.WorkerService;
import service.WorkerTimelineService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class OrderEditDialogController {

    @FXML
    private TextField dField1;
    @FXML
    private TextField dField2;
    @FXML
    private TextField dField3;
    @FXML
    private TextField dField4;
    @FXML
    private TextField dField5;
    @FXML
    private TextField dField6;
    //@FXML
    //private TextField dField7;
    //@FXML
    //private TextField dField8;


    @FXML
    private TextArea dArea1;
    @FXML
    private TextArea dArea2;

    @FXML
    private ComboBox dCombo0;
    @FXML
    private ComboBox dCombo1;
    @FXML
    private ComboBox dCombo2;
    //@FXML
    //private ComboBox dCombo3;
    @FXML
    private ComboBox dCombo4;

    @FXML
    private ImageView dImagePhoto;

    /*
    @FXML
    private LocalDateTimeTextField dLocalDate1;
    */

    //@FXML
    //private Button dBtn1;

    @FXML
    private CheckTreeView<SpecTreeModel> dCheckTreeView;

    /*
    @FXML
    private CheckListView search_subspec;
    @FXML
    private ComboBox search_spec;
    */
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
    private Label dLabel8;
    @FXML
    private Label dLabel9;


    @FXML
    private Button search_btn;

    @FXML
    private TableView<Worker> dTable;

    @FXML
    private TableColumn<Worker, String> tColumn1;

    @FXML
    private Tab tabProfile;
    @FXML
    private Tab tabStatistic;
    @FXML
    private TabPane tabsOrder;

    @FXML
    private Button dBtnNotPerform;
    @FXML
    private Button dBtnWasUnav;

    @FXML
    private TableView<OrderTimeline> dTimelineOrders;
    @FXML
    private TableColumn<OrderTimeline, String> tTimelineColumn1;
    @FXML
    private TableColumn<OrderTimeline, String> tTimelineColumn2;
    @FXML
    private TableColumn<OrderTimeline, String> tTimelineColumn3;
    @FXML
    private TableColumn<OrderTimeline, String> tTimelineColumn4;
    @FXML
    private TableColumn<OrderTimeline, String> tTimelineColumn5;

    private Stage dialogStage;
    private boolean okClicked = false;
    private Order obj;
    private Worker worker;

    public static ObservableList<Worker> workers;
    public static ObservableList<OrderStatus> statuses;
    public static ObservableList<OrderCategory> categorires;

    public static ObservableList<Worker> tData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tsData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tkData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tcData = FXCollections.observableArrayList();

    public static String err_title = "Незаполненные поля";
    public static String err_header = "Пожалуйста, заполните поля!";
    public static String err_text = "Вы не ввели ФИО!\n";
    public static String err_text_phone = "Вы не ввели телефон!\n";
    public static String err_text_region = "Вы не выбрали район!\n";
    public static String err_text_adress = "Вы не ввели адрес!\n";
    public static String err_text_advert = "Вы не ввели источник!\n";
    public static String err_text_category = "Вы не ввели категорию!\n";
    public static String err_text_worker = "Вы не ввели мастера!\n";
    public static String err_text_status = "Вы не ввели статус!\n";
    public static String err_text_time = "Вы не ввели время!\n";
    public static String err_text_worker_not_select = "Вы не выбрали мастера!\n";
    public static String err_text_worker_equals = "Вы не выбрали нового мастера!\n";

    public static ObservableList<Specialization> specializations = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> sub_specializations = FXCollections.observableArrayList();
    public static ObservableList<Region> regions = FXCollections.observableArrayList();
    public static ObservableList<OrderAdvert> adverts = FXCollections.observableArrayList();
    public static WorkerService tService =  new WorkerService();

    public static OrderTimelineService timelineService =  new OrderTimelineService();
    public static WorkerTimelineService workerTimelineService =  new WorkerTimelineService();
    public static ObservableList<OrderTimeline> timelineData = FXCollections.observableArrayList();

    public static Worker temp = new Worker();
    public static Worker selected = null;
    public static Worker details = null;
    public static Worker old_worker = new Worker();
    public static Worker current_worker = new Worker();

    public static String dialog_reason = "";


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() throws IOException, SQLException {

        tColumn1.setCellValueFactory(cellData -> cellData.getValue().fullnameProperty());
        dTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    //System.out.println(oldValue);
                    //System.out.println(newValue);
                    if (newValue != null) {
                        current_worker = newValue;
                        obj.setLink_worker_id(newValue.getId());
                        obj.setLink_worker(newValue.getFullname());
                        if (obj.getId() != null) {
                            dBtnNotPerform.setDisable(false);
                            dBtnWasUnav.setDisable(false);
                        }
                    } else {
                        dBtnNotPerform.setDisable(true);
                        dBtnWasUnav.setDisable(true);
                    }

                    try {
                        showDetails(newValue);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

        tTimelineColumn1.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        tTimelineColumn2.setCellValueFactory(cellData -> cellData.getValue().reasonProperty());
        tTimelineColumn3.setCellValueFactory(cellData -> cellData.getValue().dtProperty());
        tTimelineColumn4.setCellValueFactory(cellData -> cellData.getValue().link_workerProperty());
        tTimelineColumn5.setCellValueFactory(cellData -> cellData.getValue().link_userProperty());
        //showDetails(null);

        dCombo4.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderStatus>() {
            @Override
            public void changed(ObservableValue ov, OrderStatus t, OrderStatus t1) {
                System.out.println(ov);
                System.out.println(t);
                System.out.println(t1);
                if( !(t1.getId().equals("1") || t1.getId().equals("2") || t1.getId().equals("4")) ) {
                    dTable.setDisable(true);
                    dCheckTreeView.setDisable(true);
                    search_region.setDisable(true);
                    search_btn.setDisable(true);
                }
                else {
                    dTable.setDisable(false);
                    dCheckTreeView.setDisable(false);
                    search_region.setDisable(false);
                    search_btn.setDisable(false);
                }
                //dTable
            }
        });



    }

    private void recheck_timeline_table() throws IOException, SQLException {

        if(this.obj.getId() != null) {
            timelineData.clear();
            timelineData = timelineService.get_list_by_order(this.obj.getId());
            dTimelineOrders.setItems(timelineData);
        }
        else {
            dTimelineOrders.setItems(null);
        }

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
    public void setData(Object obj) throws SQLException, ParseException, IOException {
        // TODO: refactoring with invoke
        this.obj = (Order) obj;
        dField1.setText(this.obj.getFullname());
        dField2.setText(this.obj.getPhone());
        dField3.setText(this.obj.getAddress());
        dField4.setText(this.obj.getStart_time());
        dField5.setText(this.obj.getEnd_time());
        dField6.setText(this.obj.getPrice());
        //dField7.setText(this.obj.getAnnotation());
        //dField8.setText(this.obj.getPrice());

        dArea1.setText(this.obj.getReason());
        dArea2.setText(this.obj.getAnnotation());

        adverts.clear();
        adverts.add(new OrderAdvert());
        adverts.addAll(MainViewController.tService.get_advert_list());
        dCombo1.setItems(adverts);

        if(this.obj.getId() != null) {
            if (!this.obj.getAdvert_source_id().equals("0")) {
                dCombo1.getSelectionModel().select(this.obj.getAdvert_source_id());
            }
        }

        /*
        adverts = MainViewController.tService.get_advert_list();
        dCombo1.setItems(adverts);
        dCombo1.getSelectionModel().select(this.obj.getAdvert_source_id());
        */

        categorires = MainViewController.tService.get_category_list();
        dCombo2.setItems(categorires);
        dCombo2.getSelectionModel().select(this.obj.getWork_cat_id());

        /*
        workers = MainViewController.tService.get_workers_list();
        dCombo3.setItems(workers);
        dCombo3.getSelectionModel().select(this.obj.getLink_worker_id());
        */

        statuses = MainViewController.tService.get_status_list();
        if(this.obj.getId() != null) {
            dCombo4.setItems(statuses);
        }
        else {
            ObservableList<OrderStatus> first_status= FXCollections.observableArrayList();
            first_status.add(statuses.get(0));
            first_status.add(statuses.get(1));
            dCombo4.setItems(first_status);
        }

        dCombo4.getSelectionModel().select(this.obj.getStatus_id());

        /*
        if(this.obj.getWork_datetime() != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            Date ts = dateFormat.parse(this.obj.getWork_datetime());
            Instant instant = Instant.ofEpochMilli(ts.getTime());
            LocalDateTime res_ts = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            dLocalDate1.setLocalDateTime(res_ts);
        }
        */

        //tData = tService.get_list();
        //dTable.setItems(tData);

        /*
        specializations.clear();
        specializations.add(new Specialization());
        specializations.addAll(tService.get_specialization_list());
        search_spec.setItems(specializations);
        */
        CheckBoxTreeItem<SpecTreeModel> dummyRoot = new CheckBoxTreeItem<>();
        dCheckTreeView.setRoot(dummyRoot);
        dCheckTreeView.setShowRoot(false);
        dCheckTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        specializations = WorkerOverviewController.tService.get_specialization_list();

        HashMap<String, String> specialization_map = new HashMap<>();

        for (Specialization item : specializations) {
            specialization_map.put(item.getId(),item.getTitle());
        }

        sub_specializations = WorkerOverviewController.tService.get_sub_specialization_list();

        Multimap<String, SubSpecialization> multiSubSpec = ArrayListMultimap.create();

        for (SubSpecialization item : sub_specializations) {
            multiSubSpec.put(item.getMain_id(), item);
        }

        List<String> spec_ids_list = new ArrayList<>();
        List<String> sub_spec_ids_list = new ArrayList<>();

        regions.clear();
        regions.add(new Region());
        regions.addAll(tService.get_region_list());
        search_region.setItems(regions);

        dCombo0.setItems(regions);
        dCombo0.getSelectionModel().select(this.obj.getRegion());

        if(this.obj.getId() == null) {
            tabStatistic.setDisable(true);
            dBtnNotPerform.setDisable(true);
            dBtnWasUnav.setDisable(true);
            //dBtn1.setDisable(true);
            showDetails(null);
        }
        else {

            recheck_timeline_table();

            ObservableList<Worker> wData = FXCollections.observableArrayList();
            System.out.println(this.obj.getLink_worker_id());
            Worker worker;
            if(!this.obj.getLink_worker_id().equals("0")){
                wData =  tService.get_worker_by_id(this.obj.getLink_worker_id());
                dTable.setItems(wData);
                worker = wData.get(0);
                old_worker = worker;
                dTable.getSelectionModel().select(worker);

                if(worker.getSpecialization_id() != null) {
                    String spec_ids = worker.getSpecialization_id();
                    spec_ids_list = Arrays.asList(spec_ids.split(","));
                }

                if(worker.getSub_specialization() != null) {
                    String sub_spec_ids = worker.getSub_specialization();
                    sub_spec_ids_list = Arrays.asList(sub_spec_ids.split(","));
                }


            }
            else{
                worker = new Worker();
                showDetails(null);
            }


            //search_spec.getSelectionModel().select(worker.getSpecialization_id());
            //search_region.getSelectionModel().select(worker.getRegion_id());

            /*
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
            */

        }


        Set<String> spec_keys = multiSubSpec.keySet();
        for (String key : spec_keys) {
            CheckBoxTreeItem<SpecTreeModel> root = new CheckBoxTreeItem<SpecTreeModel>(new SpecTreeModel(specialization_map.get(key), "1", key, ""));

            for (SubSpecialization item : multiSubSpec.get(key)) {
                CheckBoxTreeItem<SpecTreeModel> tree_item = new CheckBoxTreeItem<SpecTreeModel>(new SpecTreeModel(item.getTitle(), "0", "", item.getId()));
                for(String str: sub_spec_ids_list) {
                    if(str.trim().equals(item.getId())) {
                        tree_item.setSelected(true);
                        dCheckTreeView.getCheckModel().check(tree_item);
                        dCheckTreeView.getSelectionModel().select(tree_item);
                    }
                }
                root.getChildren().add(tree_item);
                root.setExpanded(true);
            }

            for(String str: spec_ids_list) {
                if(str.trim().equals(key)) {
                    root.setSelected(true);
                    dCheckTreeView.getCheckModel().check(root);
                    dCheckTreeView.getSelectionModel().select(root);
                }
            }

            dummyRoot.getChildren().add(root);
        }


        /*
        regions = tService.get_region_list();
        search_region.setItems(regions);
        */

        /*
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
        */

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
    private void handleOk() throws ParseException, IOException {
        if (isInputValid()) {

            obj.setFullname(dField1.getText());
            obj.setPhone(dField2.getText());
            obj.setAddress(dField3.getText());
            obj.setReason(dArea1.getText());
            obj.setStart_time(dField4.getText());
            obj.setEnd_time(dField5.getText());
            obj.setPrice(dField6.getText());
            obj.setAnnotation(dArea2.getText());
            //obj.setPrice(dField8.getText());

            obj.setLink_user(RootLayoutController.current_user.getFullname());
            obj.setLink_user_id(RootLayoutController.current_user.getId());
            obj.setArchive("0");

            if(dCombo1.getSelectionModel().selectedItemProperty().getValue() != null) {

                for(OrderAdvert item : adverts){
                    if (item.getTitle() == dCombo1.getSelectionModel().selectedItemProperty().getValue().toString()) {
                        obj.setAdvert_source_id(item.getId());
                        obj.setAdvert_source(item.getTitle());
                        break;
                    }
                    else{
                        obj.setAdvert_source_id("0");
                        obj.setAdvert_source("");
                    }
                }

                if(dCombo1.getSelectionModel().selectedItemProperty().getValue().equals(new OrderAdvert())) {
                    obj.setAdvert_source_id("0");
                    obj.setAdvert_source("");
                }

            }
            else {
                obj.setAdvert_source_id("0");
                obj.setAdvert_source("");
            }



            //System.out.println(obj.getAdvert_source_id().getClass());
            //System.out.println(dCombo1.getSelectionModel().selectedItemProperty().getValue().getClass());



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
            String old_worker_id = null;
            if(obj.getId() != null) {
                old_worker_id = obj.getLink_worker_id();
            }
            if(dTable.getSelectionModel().selectedItemProperty().getValue() == null) {
                obj.setLink_worker_id("");
                obj.setLink_worker("");
            }

            String old_status_id = null;
            if(obj.getId() != null) {
                old_status_id = obj.getStatus_id();
            }

            if(dCombo4.getSelectionModel().selectedItemProperty().getValue() != null) {
                for(OrderStatus item : statuses){
                    if (item.getTitle() == dCombo4.getSelectionModel().selectedItemProperty().getValue().toString()) {
                        obj.setStatus_id(item.getId());
                        obj.setStatus(item.getTitle());
                    }
                }
            }

            if(dCombo0.getSelectionModel().selectedItemProperty().getValue() != null) {
                for(Region item : regions){
                    if (item.getTitle() == dCombo0.getSelectionModel().selectedItemProperty().getValue().toString()) {
                        obj.setRegion(item.getId());
                    }
                }
            }

            //LocalDateTime ldt = dLocalDate1.getLocalDateTime();
            LocalDateTime ldt = null;
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

                    if(!old_status_id.equals(obj.getStatus_id())) {
                        status_handler(obj.getId());
                        MainViewController.tService.set_work_datetime(obj);
                    }
                }
                else {
                    String order_id = "";
                    order_id = MainViewController.tService.add(obj);
                    status_handler(order_id);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            okClicked = true;
            dialogStage.close();
        }
    }


    private void status_handler(String order_id) throws IOException, SQLException, ParseException {

        OrderTimeline ot = null;
        WorkerTimeline wt = null;

        System.out.println(order_id);

        switch (obj.getStatus_id()) {
            case "1":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", "");
                timelineService.add_timeline(ot);
                break;
            case "2":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", "");
                timelineService.add_timeline(ot);
                wt = new WorkerTimeline(null, order_id, "3", "", details.getId(), details.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", "");
                workerTimelineService.add_timeline(wt);
                break;
            case "3":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", "");
                timelineService.add_timeline(ot);
                //tService.set_value_worker(details, "set_inc_inwork", "");
                break;
            case "4":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", "");
                timelineService.add_timeline(ot);
                wt = new WorkerTimeline(null, order_id, "4", "", old_worker.getId(), old_worker.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", "");
                workerTimelineService.add_timeline(wt);
                tService.set_value_worker(old_worker, "set_waschanged", "");
                //tService.set_value_worker(old_worker, "set_dec_inwork", "");
                wt = new WorkerTimeline(null, order_id, "3", "", details.getId(), details.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", "");
                workerTimelineService.add_timeline(wt);
                //tService.set_value_worker(details, "set_inc_inwork", "");
                break;
            case "5":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", "");
                timelineService.add_timeline(ot);
                break;
            case "6":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", dialog_reason);
                timelineService.add_timeline(ot);
                wt = new WorkerTimeline(null, order_id, "5", "", details.getId(), details.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", dialog_reason);
                workerTimelineService.add_timeline(wt);
                //tService.set_value_worker(details, "set_dec_inwork", "");
                MainViewController.tService.set_archive(obj);
                break;
            case "7":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", dialog_reason);
                timelineService.add_timeline(ot);
                wt = new WorkerTimeline(null, order_id, "5", "", details.getId(), details.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", dialog_reason);
                workerTimelineService.add_timeline(wt);
                //tService.set_value_worker(details, "set_dec_inwork", "");
                MainViewController.tService.set_archive(obj);
                break;
            case "8":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", "");
                timelineService.add_timeline(ot);
                break;
            case "9":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", "");
                timelineService.add_timeline(ot);
                break;
            case "10":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", "");
                timelineService.add_timeline(ot);
                wt = new WorkerTimeline(null, this.obj.getId(), "6", "", details.getId(), details.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", "");
                workerTimelineService.add_timeline(wt);
                tService.set_value_worker(details, "set_done", "");
                //tService.set_value_worker(details, "set_dec_inwork", "");
                tService.set_value_worker(details, "set_inc_price", (this.obj.getPrice()!=null)?this.obj.getPrice():"0");
                MainViewController.tService.set_archive(obj);
                break;
            case "11":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", "");
                timelineService.add_timeline(ot);
                wt = new WorkerTimeline(null, order_id, "6", "", details.getId(), details.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", "");
                workerTimelineService.add_timeline(wt);
                wt = new WorkerTimeline(null, order_id, "7", "", details.getId(), details.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", "");
                workerTimelineService.add_timeline(wt);
                tService.set_value_worker(details, "set_done", "");
                //tService.set_value_worker(details, "set_dec_inwork", "");
                tService.set_value_worker(details, "set_blacklist", "");
                MainViewController.tService.set_archive(obj);
                break;
            case "12":
                ot = new OrderTimeline(null, order_id, obj.getStatus_id(), "", "", obj.getLink_worker_id(), "", obj.getLink_user_id(), "", "", "");
                timelineService.add_timeline(ot);
                wt = new WorkerTimeline(null, order_id, "6", "", details.getId(), details.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", "");
                workerTimelineService.add_timeline(wt);
                tService.set_value_worker(details, "set_done", "");
                //tService.set_value_worker(details, "set_dec_inwork", "");
                MainViewController.tService.set_archive(obj);
                break;
            default:
                break;
        }
        recheck_timeline_table();

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
    private void handleNotPerformed() throws SQLException, ParseException, IOException {
        tService.set_value_worker(details, "set_notperformed", "");
        WorkerTimeline wt = new WorkerTimeline(null, this.obj.getId(), "1", "", details.getId(), details.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", "");
        workerTimelineService.add_timeline(wt);
        dBtnNotPerform.setDisable(true);
        dLabel7.setText(String.valueOf(Integer.valueOf(dLabel7.getText()) + 1));
    }

    @FXML
    private void handleWasUnavailable() throws SQLException, ParseException, IOException {
        tService.set_value_worker(details, "set_wasunavailable", "");
        WorkerTimeline wt = new WorkerTimeline(null, this.obj.getId(), "2", "", details.getId(), details.getFullname(), MainViewController.loggedInUser.getId(), MainViewController.loggedInUser.getFullname(), "", "");
        workerTimelineService.add_timeline(wt);
        dBtnWasUnav.setDisable(true);
        dLabel5.setText(String.valueOf(Integer.valueOf(dLabel5.getText()) + 1));
    }

    @FXML
    private void handleSearch() throws SQLException, ParseException, IOException {
        String spec_id = "";
        String fullname = "";
        String sub_spec_ids = "";
        String region_id = "";
        String phone = "";

        /*
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
        */


        List<Integer> spec_list_int = new ArrayList<Integer>();
        List<Integer> sub_spec_list_int = new ArrayList<Integer>();

        for (TreeItem<SpecTreeModel> item : dCheckTreeView.getCheckModel().getCheckedItems()) {
            SpecTreeModel it = item.getValue();
            if(it != null) {
                if(it.getCat().equals("1")) {
                    spec_list_int.add(Integer.parseInt(it.getCat_id()));
                }
                else if(it.getCat().equals("0")) {
                    sub_spec_list_int.add(Integer.parseInt(it.getSpec_id()));
                }
            }
        }

        Collections.sort(spec_list_int);
        Collections.sort(sub_spec_list_int);

        List<String> spec_list = spec_list_int.stream().map(Object::toString).collect(Collectors.toList());
        List<String> sub_spec_list = sub_spec_list_int.stream().map(Object::toString).collect(Collectors.toList());

        String selected_spec_items = "";
        String selected_subspec_items = "";
        selected_spec_items = String.join(",", spec_list);
        selected_subspec_items = String.join(",", sub_spec_list);
        System.out.println(selected_spec_items);
        System.out.println(selected_subspec_items);
        spec_id = selected_spec_items;
        sub_spec_ids = selected_subspec_items;


        if(search_region.getSelectionModel().selectedItemProperty().getValue() != null) {

            for(Region item : regions){
                if (item.getTitle() == search_region.getSelectionModel().selectedItemProperty().getValue().toString()) {
                    if(item.getId() != null) {
                        region_id = item.getId();
                        break;
                    }
                }
                else{
                    region_id = "";
                }
            }

            if(search_region.getSelectionModel().selectedItemProperty().getValue().equals(new OrderAdvert())) {
                region_id = "";
            }

        }
        else {
            region_id = "";
        }

        /*
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
        */

        //tcData.clear();
        /*
        tkData = search_subspec.getCheckModel().getCheckedItems();
        String selected_subspec_items = "";
        List<String> selected_s_items = new ArrayList<String>();
        for(SubSpecialization item : tkData){
            selected_s_items.add(item.getId());
        }
        sub_spec_ids = String.join(",", selected_s_items);
        */

        //fullname = search_name.getText();

        //phone = search_phone.getText();

        tData = tService.search_workers(fullname, spec_id, sub_spec_ids, region_id, phone);
        //dTable.getItems().clear();
        dTable.getSelectionModel().clearSelection();

        if(tData != null) {
            dTable.setItems(tData);
        }
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
            dLabel8.setText(details.getSumm_price());

            String blacklist_tpl = "";
            if((details.getBlacklist().equals("1"))) {
                blacklist_tpl = "Да";
            }
            else {
                blacklist_tpl = "Нет";
            }
            dLabel9.setText(blacklist_tpl);


            if(details.getHas_photo().equals("1")) {
                dImagePhoto.setImage(details.getPhoto());
            }
            else {
                BufferedImage bi = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, 150, 150);
                dImagePhoto.setImage(SwingFXUtils.toFXImage(bi, null));
            }

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

            BufferedImage bi = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, 150, 150);
            dImagePhoto.setImage(SwingFXUtils.toFXImage(bi, null));
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

        if (dCombo0.getSelectionModel().selectedItemProperty().getValue()  == null) {
            errorMessage += err_text_region;
        }

        String c_status_id = "";

        if(dCombo4.getSelectionModel().selectedItemProperty().getValue() != null) {
            for(OrderStatus item : statuses){
                if (item.getTitle() == dCombo4.getSelectionModel().selectedItemProperty().getValue().toString()) {
                    c_status_id = item.getId();
                }
            }
        }

        if( (details == null) && (c_status_id.equals("2")) ) {
            errorMessage += err_text_worker_not_select;
        }

        if( (current_worker == null) && (c_status_id.equals("4")) ) {
            errorMessage += err_text_worker_not_select;

        }

        if( (current_worker.equals(old_worker)) && (c_status_id.equals("4")) ) {
            errorMessage += err_text_worker_equals;

        }

        if( (c_status_id.equals("6")) || (c_status_id.equals("7")) ) {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Отказ");
            dialog.setHeaderText("Причина отказа");
            dialog.setContentText("Введите причину отказа:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                dialog_reason = result.get();
            }

        }


        /*
        if (dField3.getText() == null || dField3.getText().length() == 0) {
            errorMessage += err_text_adress;
        }

        if (dCombo1.getSelectionModel().selectedItemProperty().getValue()  == null) {
            errorMessage += err_text_advert;
        }
        */

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

        /*
        if(dLocalDate1.getLocalDateTime() == null) {
            errorMessage += err_text_time;
        }
        */

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
