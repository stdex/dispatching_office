package controller.orders;

import controller.MainViewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.OrderStatus;
import model.User;
import model.UserGroup;
import utils.FxUtils;

import java.sql.SQLException;


public class OrderStatusEditDialogController {

    @FXML
    private TextField dField1;

    @FXML
    private ColorPicker dColor1;


    private Stage dialogStage;
    private boolean okClicked = false;
    private OrderStatus obj;

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
    public void setData(Object obj) {
        this.obj = (OrderStatus) obj;
        dField1.setText(this.obj.getTitle());
        dField1.setDisable(true);
        if(this.obj.getColor() != null) {
            System.out.println(this.obj.getColor());
            if(this.obj.getColor().equals("")) {
                dColor1.setValue(Color.WHITE);
            }
            else {
                dColor1.setValue(Color.web(this.obj.getColor()));
            }

        }

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

            obj.setTitle(dField1.getText());
            obj.setColor(FxUtils.toRGBCode(dColor1.getValue()));

            try {
                if(obj.getId() != null) {
                    OrderStatusOverviewController.tService.edit(obj);
                    //MainViewController.recheck_table();
                }
                else {
                    OrderStatusOverviewController.tService.add(obj);
                    //MainViewController.recheck_table();
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