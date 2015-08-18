package controller.workers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.*;
import org.controlsfx.control.CheckListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkerEditDialogController {

    @FXML
    private TextField dField1;
    @FXML
    private TextField dField2;
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
    @FXML
    private ComboBox dCombo2;
    @FXML
    private CheckListView<SubSpecialization> checkListView1;
    @FXML
    private Spinner spinner1;
    @FXML
    private Spinner spinner2;
    @FXML
    private Spinner spinner3;
    @FXML
    private Spinner spinner4;
    @FXML
    private Spinner spinner5;




    private Stage dialogStage;
    private boolean okClicked = false;
    private Worker obj;

    public static ObservableList<Region> regions;
    public static ObservableList<Specialization> specializations;
    public static ObservableList<SubSpecialization> tsData = FXCollections.observableArrayList();
    public static ObservableList<SubSpecialization> tcData = FXCollections.observableArrayList();

    public static String err_title = "Незаполненные поля";
    public static String err_header = "Пожалуйста, заполните текстовые поля!";
    public static String err_text = "Вы не ввели название!\n";

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
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
    public void setData(Object obj) throws SQLException {
        // TODO: refactoring with invoke
        this.obj = (Worker) obj;
        dField1.setText(this.obj.getFullname());
        dField2.setText(this.obj.getPhone());
        /*
        dField3.setText(this.obj.getCount_done());
        dField4.setText(this.obj.getCount_inwork());
        dField5.setText(this.obj.getCount_wasunavailable());
        dField6.setText(this.obj.getCount_waschanged());
        dField7.setText(this.obj.getCount_notperformed());
        */

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

        regions = WorkerOverviewController.tService.get_region_list();
        dCombo1.setItems(regions);
        dCombo1.getSelectionModel().select(this.obj.getRegion_id());
        specializations = WorkerOverviewController.tService.get_specialization_list();
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
    private void handleOk() {
        if (isInputValid()) {

            obj.setFullname(dField1.getText());
            obj.setPhone(dField2.getText());
            obj.setCount_done(Integer.toString((Integer) spinner1.getValue()));
            obj.setCount_inwork(Integer.toString( (Integer) spinner2.getValue() ));
            obj.setCount_wasunavailable(Integer.toString( (Integer) spinner3.getValue() ));
            obj.setCount_waschanged(Integer.toString( (Integer) spinner4.getValue() ));
            obj.setCount_notperformed(Integer.toString( (Integer) spinner5.getValue() ));

            /*
            obj.setCount_done(dField3.getText());
            obj.setCount_inwork(dField4.getText());
            obj.setCount_wasunavailable(dField5.getText());
            obj.setCount_waschanged(dField6.getText());
            obj.setCount_notperformed(dField7.getText());
            */

            for(Region item : regions){
                if (item.getTitle() == dCombo1.getSelectionModel().selectedItemProperty().getValue().toString()) {
                    obj.setRegion_id(item.getId());
                    obj.setRegion_name(item.getTitle());
                }
            }

            for(Specialization item : specializations){
                if (item.getTitle() == dCombo2.getSelectionModel().selectedItemProperty().getValue().toString()) {
                    obj.setSpecialization_id(item.getId());
                    obj.setSpecialization_name(item.getTitle());
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
