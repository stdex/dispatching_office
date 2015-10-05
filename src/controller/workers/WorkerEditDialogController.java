package controller.workers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.CheckTreeView;
import service.WorkerTimelineService;
import utils.ImgUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class WorkerEditDialogController {

    @FXML
    private TextField dField1;
    @FXML
    private TextField dField2;
    @FXML
    private TextField dField3;
    /*
    @FXML
    private TextField dField3;
    @FXML
    private TextField dField4;
    @FXML
    private TextField dField5;
    @FXML
    private TextField dField6;
    @FXML
    private TextField dField7;
    */

    @FXML
    private ComboBox dCombo1;
    //@FXML
    //private ComboBox dCombo2;
    @FXML
    private ComboBox dCombo3;
    @FXML
    private CheckListView<Region> checkListView1;

    @FXML
    private CheckTreeView<SpecTreeModel> dCheckTreeView;

    @FXML
    private TextField spinner1;
    @FXML
    private TextField spinner2;
    @FXML
    private TextField spinner3;
    @FXML
    private TextField spinner4;
    @FXML
    private TextField spinner5;
    @FXML
    private TextField spinner6;

    @FXML
    private VBox vBoxImg;

    @FXML
    private Tab tabProfile;
    @FXML
    private Tab tabOrders;
    @FXML
    private TabPane tabsWorker;

    @FXML
    private TreeTableView<WorkerTimeline> dTimelineOrders;
    @FXML
    private TreeTableColumn<WorkerTimeline, String> tTimelineColumn1;
    @FXML
    private TreeTableColumn<WorkerTimeline, String> tTimelineColumn2;
    @FXML
    private TreeTableColumn<WorkerTimeline, String> tTimelineColumn3;
    @FXML
    private TreeTableColumn<WorkerTimeline, String> tTimelineColumn4;
    @FXML
    private TreeTableColumn<WorkerTimeline, String> tTimelineColumn5;

    private Stage dialogStage;
    private boolean okClicked = false;
    private Worker obj;

    public static ObservableList<Region> regions;
    public static ObservableList<Specialization> specializations;
    public static ObservableList<SubSpecialization> sub_specializations;
    public static ObservableList<BlackList> blacklist = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tsData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tcData = FXCollections.observableArrayList();
    public static ObservableList<Region> rData = FXCollections.observableArrayList();

    public static String err_title = "Незаполненные поля";
    public static String err_header = "Пожалуйста, заполните текстовые поля!";
    public static String err_text = "Вы не ввели ФИО!\n";
    public static String err_text_region = "Вы не выбрали район!\n";
    public static String err_text_speciazlization = "Вы не выбрали специализацию!\n";


    public static Image photo = null;
    private ImageView myImageView;

    public static WorkerTimelineService timelineService =  new WorkerTimelineService();
    public static ObservableList<WorkerTimeline> timelineData = FXCollections.observableArrayList();


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() throws IOException, SQLException {

        /*
        tTimelineColumn1.setCellValueFactory(cellData -> cellData.getValue().id_orderProperty());
        tTimelineColumn2.setCellValueFactory(cellData -> cellData.getValue().reactionProperty());
        tTimelineColumn3.setCellValueFactory(cellData -> cellData.getValue().reasonProperty());
        tTimelineColumn4.setCellValueFactory(cellData -> cellData.getValue().dtProperty());
        tTimelineColumn5.setCellValueFactory(cellData -> cellData.getValue().link_userProperty());
        */

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
    public void setData(Object obj) throws SQLException, IOException {
        // TODO: refactoring with invoke
        this.obj = (Worker) obj;
        dField1.setText(this.obj.getFullname());
        dField2.setText(this.obj.getPhone());
        dField3.setText(this.obj.getAnnotation());

        blacklist.clear();
        blacklist.add(new BlackList("0", "Нет"));
        blacklist.add(new BlackList("1", "Да"));
        dCombo3.setItems(blacklist);

        if(this.obj.getBlacklist() != null) {
            dCombo3.getSelectionModel().select(this.obj.getBlacklist());
        }
        else {
            dCombo3.getSelectionModel().select(0);
        }


        myImageView = new ImageView();
        EventHandler<ActionEvent> btnLoadEventListener
                = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                /*
                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
                */

                //Show open file dialog
                File file = fileChooser.showOpenDialog(null);

                try {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    BufferedImage resizeImage = ImgUtils.resizeImage(bufferedImage, 150, 150);
                    photo = SwingFXUtils.toFXImage(resizeImage, null);
                    myImageView.setImage(photo);
                } catch (IOException ex) {
                    //Logger.getLogger(JavaFXPixel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        
        Button btnLoad = new Button("Загрузить");
        btnLoad.setOnAction(btnLoadEventListener);

        vBoxImg.getChildren().addAll(btnLoad, myImageView);

        if(this.obj.getHas_photo() != null) {
            if (this.obj.getHas_photo().equals("1")) {
                myImageView.setImage(this.obj.getPhoto());
            }
        }
        else{
            this.obj.setHas_photo("0");
        }

        spinner1.setText(this.obj.getCount_done());
        spinner2.setText(this.obj.getCount_inwork());
        spinner3.setText(this.obj.getCount_wasunavailable());
        spinner4.setText(this.obj.getCount_waschanged());
        spinner5.setText(this.obj.getCount_notperformed());
        spinner6.setText(this.obj.getSumm_price());

        spinner1.setDisable(true);
        spinner2.setDisable(true);
        spinner3.setDisable(true);
        spinner4.setDisable(true);
        spinner5.setDisable(true);
        spinner6.setDisable(true);

        /*
        SpinnerValueFactory svf1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999999, 0);
        spinner1.setValueFactory(svf1);
        spinner1.getStyleClass().add("Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL");
        if(this.obj.getCount_done() != null) {
            spinner1.getValueFactory().setValue(Integer.parseInt(this.obj.getCount_done()));
        }

        SpinnerValueFactory svf2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999999, 0);
        spinner2.setValueFactory(svf2);
        spinner2.getStyleClass().add("Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL");
        if(this.obj.getCount_inwork() != null) {
            spinner2.getValueFactory().setValue(Integer.parseInt(this.obj.getCount_inwork()));
        }

        SpinnerValueFactory svf3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999999, 0);
        spinner3.setValueFactory(svf3);
        spinner3.getStyleClass().add("Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL");
        if(this.obj.getCount_wasunavailable() != null) {
            spinner3.getValueFactory().setValue(Integer.parseInt(this.obj.getCount_wasunavailable()));
        }

        SpinnerValueFactory svf4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999999, 0);
        spinner4.setValueFactory(svf4);
        spinner4.getStyleClass().add("Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL");
        if(this.obj.getCount_waschanged() != null) {
            spinner4.getValueFactory().setValue(Integer.parseInt(this.obj.getCount_waschanged()));
        }

        SpinnerValueFactory svf5 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999999, 0);
        spinner5.setValueFactory(svf5);
        spinner5.getStyleClass().add("Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL");
        if(this.obj.getCount_notperformed() != null) {
            spinner5.getValueFactory().setValue(Integer.parseInt(this.obj.getCount_notperformed()));
        }
        */


        /*
        dCombo1.setItems(regions);
        dCombo1.getSelectionModel().select(this.obj.getRegion_id());
        */


        List<String> region_ids_list = new ArrayList<>();
        if(this.obj.getRegion_id() != null) {
            String region_ids = this.obj.getRegion_id();
            region_ids_list = Arrays.asList(region_ids.split(","));
        }

        regions = WorkerOverviewController.tService.get_region_list();
        checkListView1.setItems(regions);
        for (Region item : regions) {
            if (region_ids_list.contains(item.getId())) {
                checkListView1.getCheckModel().check(item);
            }
        }


        if(this.obj.getId() == null) {
            tabOrders.setDisable(true);
        }
        else {


            tTimelineColumn1
                    .setCellValueFactory((
                            TreeTableColumn.CellDataFeatures<WorkerTimeline, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getId_order()));
            tTimelineColumn2
                    .setCellValueFactory((
                            TreeTableColumn.CellDataFeatures<WorkerTimeline, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getReaction()));
            tTimelineColumn3
                    .setCellValueFactory((
                            TreeTableColumn.CellDataFeatures<WorkerTimeline, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getReason()));
            tTimelineColumn4
                    .setCellValueFactory((
                            TreeTableColumn.CellDataFeatures<WorkerTimeline, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getDt()));
            tTimelineColumn5
                    .setCellValueFactory((
                            TreeTableColumn.CellDataFeatures<WorkerTimeline, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getLink_user()));


            timelineData.clear();
            timelineData = timelineService.get_list_by_worker(this.obj.getId());

            Multimap<String, WorkerTimeline> multiTimeline = ArrayListMultimap.create();

            for (WorkerTimeline item : timelineData) {
                multiTimeline.put(item.getId_order(), item);
            }

            TreeItem<WorkerTimeline> dummyRoot = new TreeItem<>();
            dTimelineOrders.setRoot(dummyRoot);
            dTimelineOrders.setShowRoot(false);
            dTimelineOrders.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            Set<String> timeline_keys = multiTimeline.keySet();
            for (String key : timeline_keys) {
                TreeItem<WorkerTimeline> root = new TreeItem<WorkerTimeline>(new WorkerTimeline("", key, "", "", "", "", "", "", "", ""));

                for (WorkerTimeline item : multiTimeline.get(key)) {
                    TreeItem<WorkerTimeline> tree_item = new TreeItem<WorkerTimeline>(new WorkerTimeline(item.getId(), item.getId_order(), item.getId_reaction(), item.getReaction(), item.getLink_worker_id(), item.getLink_worker(), item.getLink_user_id(), item.getLink_user(), item.getDt(), item.getReason()));
                    root.getChildren().add(tree_item);
                    root.setExpanded(true);
                }

                dummyRoot.getChildren().add(root);
            }

        }

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

        if(this.obj.getSpecialization_id() != null) {
            String spec_ids = this.obj.getSpecialization_id();
            spec_ids_list = Arrays.asList(spec_ids.split(","));
        }

        if(this.obj.getSub_specialization() != null) {
            String sub_spec_ids = this.obj.getSub_specialization();
            sub_spec_ids_list = Arrays.asList(sub_spec_ids.split(","));
        }

        CheckBoxTreeItem<SpecTreeModel> dummyRoot_spec = new CheckBoxTreeItem<>();
        dCheckTreeView.setRoot(dummyRoot_spec);
        dCheckTreeView.setShowRoot(false);
        dCheckTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //dCheckTreeView = new CheckTreeView<SpecTreeModel>(dummyRoot);

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
                    System.out.println(str.trim() + "::" + key + "::" + str.trim().equals(key));
                    root.setSelected(true);
                    dCheckTreeView.getCheckModel().check(root);
                    dCheckTreeView.getSelectionModel().select(root);
                }
            }

            dummyRoot_spec.getChildren().add(root);
        }
        /**/

        tabsWorker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

            @Override
            public void changed(ObservableValue<? extends Tab> arg0,
                                Tab arg1, Tab arg2) {

                if (arg2.equals(tabOrders)) {
                    //TODO: recheck table
                }
            }
        });

        /*
        dCombo2.setItems(specializations);
        dCombo2.getSelectionModel().select(this.obj.getSpecialization_id());


        //System.out.println(dCombo2.getValue());
        String sub_spec = this.obj.getSub_specialization();

        if( dCombo2.getValue() != null) {

            try {
                tcData = WorkerOverviewController.tService.get_overview_ss_list(sub_spec);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String current_choose_id = ((Specialization) dCombo2.getSelectionModel().getSelectedItem()).getId();


            //tsData.clear();
            try {
                tsData = WorkerOverviewController.tService.get_csubspec_list(current_choose_id);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            checkListView1.setItems(tsData);
            for (SubSpecialization item : tsData) {
                if (tcData.contains(item)) {
                    checkListView1.getCheckModel().check(item);
                }
            }


        }

        dCombo2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Specialization>() {
            @Override
            public void changed(ObservableValue<? extends Specialization> arg0, Specialization arg1, Specialization arg2) {
                if (arg2 != null) {

                    //System.out.println(dCombo2.getSelectionModel().getSelectedItem());

                    //tcData.clear();
                    try {
                        tcData = WorkerOverviewController.tService.get_overview_ss_list(sub_spec);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String current_choose_id = ((Specialization) dCombo2.getSelectionModel().getSelectedItem()).getId();

                    //tsData.clear();
                    try {
                        tsData = WorkerOverviewController.tService.get_csubspec_list(current_choose_id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    checkListView1.setItems(tsData);
                    for (SubSpecialization item : tsData) {
                        if (tcData.contains(item)) {
                            checkListView1.getCheckModel().check(item);
                        }
                    }

                    System.out.println(dCombo2.getSelectionModel().getSelectedIndex());
                    System.out.println(dCombo2.getSelectionModel().getSelectedItem());
                    System.out.println(dCombo2.getSelectionModel().selectedIndexProperty());
                    System.out.println(dCombo2.getSelectionModel().selectedItemProperty());

                }
            }
        });
        */



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
    private void handleOk() throws IOException {

        System.out.println(dCheckTreeView.getCheckModel().getCheckedItems());
        System.out.println(dCheckTreeView.getSelectionModel().getSelectedItems());

        if (isInputValid()) {

            obj.setFullname(dField1.getText());
            obj.setPhone(dField2.getText());
            obj.setAnnotation(dField3.getText());
            obj.setCount_done(spinner1.getText());
            obj.setCount_inwork(spinner2.getText());
            obj.setCount_wasunavailable(spinner3.getText());
            obj.setCount_waschanged(spinner4.getText());
            obj.setCount_notperformed(spinner5.getText());
            obj.setSumm_price(spinner6.getText());

            if(photo != null) {
                obj.setPhoto(photo);
                obj.setHas_photo("1");
            }
            else {
                if(this.obj.getHas_photo().equals("0")) {
                    obj.setPhoto(null);
                    obj.setHas_photo("0");
                }
            }

            /*
            obj.setCount_done(dField3.getText());
            obj.setCount_inwork(dField4.getText());
            obj.setCount_wasunavailable(dField5.getText());
            obj.setCount_waschanged(dField6.getText());
            obj.setCount_notperformed(dField7.getText());
            */

            for(BlackList item : blacklist){
                if(dCombo3.getSelectionModel().selectedItemProperty().getValue() != null) {
                    if (item.getTitle() == dCombo3.getSelectionModel().selectedItemProperty().getValue().toString()) {
                        obj.setBlacklist(item.getId());
                    }
                }
            }

            /*
            for(Region item : regions){
                if(dCombo1.getSelectionModel().selectedItemProperty().getValue() != null) {
                    System.out.println(dCombo1.getSelectionModel().selectedItemProperty().getValue());
                    System.out.println(dCombo1.getSelectionModel().selectedItemProperty().getValue().getClass());
                    if (item.getTitle() == dCombo1.getSelectionModel().selectedItemProperty().getValue().toString()) {
                        obj.setRegion_id(item.getId());
                        obj.setRegion_name(item.getTitle());
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
            obj.setSpecialization_id(selected_spec_items);
            obj.setSub_specialization(selected_subspec_items);

            /*
            for(Specialization item : specializations){
                if(dCombo2.getSelectionModel().selectedItemProperty().getValue() != null) {
                    if (item.getTitle() == dCombo2.getSelectionModel().selectedItemProperty().getValue().toString()) {
                        obj.setSpecialization_id(item.getId());
                        obj.setSpecialization_name(item.getTitle());
                    }
                }
            }

            //tcData.clear();
            tcData = checkListView1.getCheckModel().getCheckedItems();
            System.out.println(tcData);
            String selected_subspec_items = "";
            List<String> selected_s_items = new ArrayList<String>();
            for(SubSpecialization item : tcData){
                selected_s_items.add(item.getId());
            }
            System.out.println(selected_s_items);
            selected_subspec_items = String.join(",", selected_s_items);
            System.out.println(selected_subspec_items);
            obj.setSub_specialization(selected_subspec_items);

            */

            rData = checkListView1.getCheckModel().getCheckedItems();
            String selected_region_items = "";
            List<String> selected_r_items = new ArrayList<String>();
            for(Region item : rData){
                selected_r_items.add(item.getId());
            }
            selected_region_items = String.join(",", selected_r_items);
            obj.setRegion_id(selected_region_items);

            try {
                if(obj.getId() != null) {
                    WorkerOverviewController.tService.edit(obj);
                }
                else {
                    WorkerOverviewController.tService.add(obj);
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

        if (checkListView1.getCheckModel().getCheckedItems().isEmpty()) {
            errorMessage += err_text_region;
        }

        if (dCheckTreeView.getCheckModel().getCheckedItems().size() <= 0) {
            errorMessage += err_text_speciazlization;
        }

        /*
        if (dCombo2.getSelectionModel().selectedItemProperty().getValue()  == null) {
            errorMessage += err_text_speciazlization;
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
