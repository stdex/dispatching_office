package controller.workers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import controller.RootLayoutController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.CheckTreeView;
import org.controlsfx.control.textfield.TextFields;
import service.WorkerService;
import service.UserService;
import utils.DateUtil;
import controller.MainApp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class WorkerOverviewController {
    @FXML
    private TableView<Worker> dTable;
    @FXML
    private TreeView<SpecTreeModel> dTreeView1;
    @FXML
    private CheckTreeView<SpecTreeModel> dCheckTreeView1;

    @FXML
    private ListView<Region> listView1;


    @FXML
    private TableColumn<Worker, String> tColumn1;
    @FXML
    private TableColumn<Worker, String> tColumn2;
    @FXML
    private TableColumn<Worker, String> tColumn3;

    @FXML
    private Label dLabel1;
    @FXML
    private Label dLabel2;
    /*
    @FXML
    private Label dLabel3;

    @FXML
    private Label dLabel4;
    */
    //@FXML
    //private Label dLabel5;
    @FXML
    private Label dLabel6;
    @FXML
    private Label dLabel7;
    @FXML
    private Label dLabel8;
    @FXML
    private Label dLabel9;
    @FXML
    private Label dLabel10;
    @FXML
    private Label dLabel11;
    @FXML
    private Label dLabel_blacklist;
    @FXML
    private Label dLabel_annotation;

    @FXML
    private ImageView dImagePhoto;

    /*
    @FXML
    private CheckListView search_subspec;
    @FXML
    private ComboBox search_spec;
    */
    @FXML
    private TextField search_name;
    @FXML
    private TextField search_phone;
    @FXML
    private ComboBox search_region;

    @FXML
    private Button search_btn;

    @FXML
    private Button dBtnAdd;
    @FXML
    private Button dBtnEdit;
    @FXML
    private Button dBtnDelete;


    // Reference to the main application.
    private MainApp mainApp;
    private Stage orderStage;

    public static ObservableList<Worker> tData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tsData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tkData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tcData = FXCollections.observableArrayList();
    public static WorkerService tService =  new WorkerService();
    public static Worker temp = new Worker();
    public static Worker selected;
    public static Worker details;
    public static String title = "Редактирование";
    public static String mode = "WorkerEdit";
    public static String fxml = "/view/workers/WorkerEditDialog.fxml";

    public static String err_title = "Не выбран мастер";
    public static String err_header = "Не выбран мастер";
    public static String err_text = "Пожалуйста, выберите мастер из таблицы.";

    public static String err_exist_title = "Удаление невозможно";
    public static String err_exist_header = "Удаление невозможно";
    public static String err_exist_text = "Данный объект связан с другими объектами.";

    public static ObservableList<Specialization> specializations = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> sub_specializations = FXCollections.observableArrayList();

    public static ObservableList<Specialization> search_specializations = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> search_sub_specializations = FXCollections.observableArrayList();

    public static ObservableList<Region> regions = FXCollections.observableArrayList();

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public WorkerOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() throws SQLException {
        tColumn1.setCellValueFactory(cellData -> cellData.getValue().fullnameProperty());
        tColumn2.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        //tColumn3.setCellValueFactory(cellData -> cellData.getValue().specialization_nameProperty());
        showDetails(null);
        dTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        showDetails(newValue);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
        if(!RootLayoutController.current_user.getGroup_id().equals("1")) {
            dBtnAdd.setDisable(true);
            dBtnEdit.setDisable(true);
            dBtnDelete.setDisable(true);

        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) throws SQLException, IOException {
        this.mainApp = mainApp;
        tData = tService.get_list();
        //System.out.println(tData);
        dTable.setItems(tData);
        recheck_table();

    }

    public void setEditStage(Stage orderStage){
        this.orderStage = orderStage;
    }


    public void recheck_table() throws SQLException {

        /*
        specializations.clear();
        specializations.add(new Specialization());
        specializations.addAll(tService.get_specialization_list());
        search_spec.setItems(specializations);
        */

        regions.clear();
        regions.add(new Region());
        regions.addAll(tService.get_region_list());
        search_region.setItems(regions);


        search_specializations.clear();
        search_sub_specializations.clear();

        search_specializations = WorkerOverviewController.tService.get_specialization_list();

        HashMap<String, String> specialization_map = new HashMap<>();

        for (Specialization item : search_specializations) {
            specialization_map.put(item.getId(),item.getTitle());
        }

        search_sub_specializations = WorkerOverviewController.tService.get_sub_specialization_list();

        Multimap<String, SubSpecialization> multiSubSpec = ArrayListMultimap.create();

        for (SubSpecialization item : search_sub_specializations) {
            multiSubSpec.put(item.getMain_id(), item);
        }

        CheckBoxTreeItem<SpecTreeModel> dummyRoot = new CheckBoxTreeItem<>();
        dCheckTreeView1.setRoot(dummyRoot);
        dCheckTreeView1.setShowRoot(false);
        dCheckTreeView1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Set<String> spec_keys = multiSubSpec.keySet();
        for (String key : spec_keys) {
            CheckBoxTreeItem<SpecTreeModel> root = new CheckBoxTreeItem<SpecTreeModel>(new SpecTreeModel(specialization_map.get(key), "1", key, ""));

            for (SubSpecialization item : multiSubSpec.get(key)) {
                CheckBoxTreeItem<SpecTreeModel> tree_item = new CheckBoxTreeItem<SpecTreeModel>(new SpecTreeModel(item.getTitle(), "0", "", item.getId()));
                root.getChildren().add(tree_item);
                //root.setExpanded(true);
            }

            dummyRoot.getChildren().add(root);
        }

        /*
        search_spec.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Specialization>() {
            @Override
            public void changed(ObservableValue<? extends Specialization> arg0, Specialization arg1, Specialization arg2) {
                if (arg2 != null) {

                    String sub_spec = null;
                    try {
                        tcData = WorkerOverviewController.tService.get_overview_ss_list(sub_spec);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String current_choose_id = ((Specialization) search_spec.getSelectionModel().getSelectedItem()).getId();

                    //tsData.clear();
                    try {
                        tkData = WorkerOverviewController.tService.get_csubspec_list(current_choose_id);
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

        List workers_suggest = new ArrayList<>();
        List phone_suggest = new ArrayList<>();

        for (Worker worker : tData) {
            workers_suggest.add(worker.getFullname());
            phone_suggest.add(worker.getPhone());
        }

        TextFields.bindAutoCompletion(search_name, workers_suggest);
        TextFields.bindAutoCompletion(search_phone, phone_suggest);

        showDetails(null);

    }

    private void showDetails(Object obj) throws SQLException {
        // TODO: cast object invoke
        details = (Worker) obj;
        System.out.println(details);
        if (details != null) {
            dLabel1.setText(details.getFullname());
            dLabel2.setText(details.getPhone());
            //dLabel3.setText(details.getSpecialization_name());

            List<String> region_ids_list = new ArrayList<>();
            if(details.getRegion_id() != null) {
                String region_ids = details.getRegion_id();
                region_ids_list = Arrays.asList(region_ids.split(","));
            }

            System.out.println(details.getRegion_id());

            ObservableList<Region> item_regions = FXCollections.observableArrayList();

            //regions = WorkerOverviewController.tService.get_region_list();
            for (Region item : regions) {
                if (region_ids_list.contains(item.getId())) {
                    item_regions.add(item);
                }
            }

            listView1.setItems(item_regions);

            //dLabel4.setText(details.getRegion_name());
            //dLabel5.setText(details.getSub_specialization());
            dLabel6.setText(details.getCount_done());
            dLabel7.setText(details.getCount_inwork());
            dLabel8.setText(details.getCount_wasunavailable());
            dLabel9.setText(details.getCount_waschanged());
            dLabel10.setText(details.getCount_notperformed());
            dLabel11.setText(details.getSumm_price());

            String blacklist_tpl = "";
            if((details.getBlacklist().equals("1"))) {
                blacklist_tpl = "Да";
            }
            else {
                blacklist_tpl = "Нет";
            }
            dLabel_blacklist.setText(blacklist_tpl);

            dLabel_annotation.setText(details.getAnnotation());

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


            /*
            tsData.clear();
            tsData = tService.get_overview_ss_list(details.getSub_specialization());
            System.out.println(tsData);
            listView1.setItems(tsData);
            */

            specializations.clear();
            sub_specializations.clear();

            specializations = WorkerOverviewController.tService.get_specialization_list();

            HashMap<String, String> specialization_map = new HashMap<>();

            for (Specialization item : specializations) {
                specialization_map.put(item.getId(),item.getTitle());
            }

            sub_specializations = WorkerOverviewController.tService.get_overview_ss_list(details.getSub_specialization());

            Multimap<String, SubSpecialization> multiSubSpec = ArrayListMultimap.create();

            for (SubSpecialization item : sub_specializations) {
                multiSubSpec.put(item.getMain_id(), item);
            }

            /*
            String spec_ids = details.getSpecialization_id();
            List<String> spec_ids_list = Arrays.asList(spec_ids.split(","));

            String sub_spec_ids = details.getSub_specialization();
            List<String> sub_spec_ids_list = Arrays.asList(sub_spec_ids.split(","));
            */

            TreeItem<SpecTreeModel> dummyRoot = new TreeItem<>();
            dTreeView1.setRoot(dummyRoot);
            dTreeView1.setShowRoot(false);

            Set<String> spec_keys = multiSubSpec.keySet();
            for (String key : spec_keys) {
                TreeItem<SpecTreeModel> root = new TreeItem<SpecTreeModel>(new SpecTreeModel(specialization_map.get(key), "1", key, ""));

                for (SubSpecialization item : multiSubSpec.get(key)) {
                    TreeItem<SpecTreeModel> tree_item = new TreeItem<SpecTreeModel>(new SpecTreeModel(item.getTitle(), "0", "", item.getId()));
                    root.getChildren().add(tree_item);
                    //root.setExpanded(true);
                }

                dummyRoot.getChildren().add(root);
            }


        } else {
            dLabel1.setText("");
            dLabel2.setText("");
            //dLabel3.setText("");
            //dLabel4.setText("");
            //dLabel5.setText("");
            dLabel6.setText("");
            dLabel7.setText("");
            dLabel8.setText("");
            dLabel9.setText("");
            dLabel10.setText("");
            dLabel11.setText("");
            dLabel_blacklist.setText("");
            dLabel_annotation.setText("");

            BufferedImage bi = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, 150, 150);
            /**/
            dImagePhoto.setImage(SwingFXUtils.toFXImage(bi, null));

            //TreeItem<SpecTreeModel> dummyRoot = new TreeItem<>();
            //dTreeView1.setRoot(dummyRoot);
            //dTreeView1.setShowRoot(false);
            //dTreeView1.getSelectionModel().getSelectedItems().clear();
            //listView1.getItems().clear();

        }
    }


    @FXML
    private void handleDelete() throws SQLException, IOException {

        int selectedIndex = dTable.getSelectionModel().getSelectedIndex();
        selected = dTable.getSelectionModel().getSelectedItem();
        boolean can_remove = tService.can_remove(selected);
        System.out.println(can_remove);

        if (selected != null) {
            if(can_remove) {
                tService.remove(selected);
                tData.clear();
                tData = tService.get_list();
                dTable.setItems(tData);
                recheck_table();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle(err_exist_title);
                alert.setHeaderText(err_exist_header);
                alert.setContentText(err_exist_text);
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(err_title);
            alert.setHeaderText(err_header);
            alert.setContentText(err_text);
            alert.showAndWait();
        }

    }

    @FXML
    private void handleNew() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ParseException, IOException {

        temp = new Worker();
        boolean okClicked = mainApp.showSPageEditDialog(title, fxml, mode, temp);
        if (okClicked) {
            tData.clear();
            tData = tService.get_list();
            dTable.setItems(tData);
            recheck_table();
        }

    }

    @FXML
    private void handleEdit() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ParseException, IOException {

        selected = dTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean okClicked = mainApp.showSPageEditDialog(title, fxml, mode, selected);
            if (okClicked) {
                tData.clear();
                tData = tService.get_list();
                dTable.setItems(tData);
                recheck_table();
                showDetails(selected);
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(err_title);
            alert.setHeaderText(err_header);
            alert.setContentText(err_text);
            alert.showAndWait();
        }

    }

    @FXML
    private void handleSearch() throws SQLException, IOException {

        String spec_id = "";
        String fullname = "";
        //String sub_spec_ids = "";
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

        for (TreeItem<SpecTreeModel> item : dCheckTreeView1.getCheckModel().getCheckedItems()) {
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
        /*
        tkData = search_subspec.getCheckModel().getCheckedItems();
        String selected_subspec_items = "";
        List<String> selected_s_items = new ArrayList<String>();
        for(SubSpecialization item : tkData){
            selected_s_items.add(item.getId());
        }
        sub_spec_ids = String.join(",", selected_s_items);
        */

        fullname = search_name.getText();

        phone = search_phone.getText();

        tData = WorkerOverviewController.tService.search_workers(fullname, spec_id, selected_subspec_items, region_id, phone);
        dTable.setItems(tData);
        showDetails(null);

    }


}
