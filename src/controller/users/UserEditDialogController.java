package controller.users;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import model.UserGroup;

import java.sql.SQLException;

/**
 * Dialog to edit details of a person.
 * 
 * @author Marco Jakob
 */
public class UserEditDialogController {

    @FXML
    private TextField dField1;
    @FXML
    private TextField dField2;
    @FXML
    private TextField dField3;

    @FXML
    private ComboBox dCombo1;

    private Stage dialogStage;
    private User user;
    private boolean okClicked = false;
    public static ObservableList<UserGroup> groups;

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
     * @param user
     */
    public void setUser(User user) {
        this.user = user;

        dField1.setText(user.getLogin());
        dField2.setText(user.getPassword());
        dField3.setText(user.getFullname());

        if(user.getPassword() != null) {
            dField2.setEditable(false);
        }

        try {
            groups = UserOverviewController.u_service.get_status_list();
            dCombo1.setItems(groups);
            dCombo1.getSelectionModel().select(user.getGroup_id());
             /*
            dCombo1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserGroup>() {
                @Override
                public void changed(ObservableValue<? extends UserGroup> arg0, UserGroup arg1, UserGroup arg2) {
                    if (arg2 != null) {
                        System.out.println(dCombo1.getSelectionModel().getSelectedIndex());
                        System.out.println(dCombo1.getSelectionModel().getSelectedItem());
                        System.out.println(dCombo1.getSelectionModel().selectedIndexProperty());
                        System.out.println(dCombo1.getSelectionModel().selectedItemProperty());
                    }
                }
            });
            */

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //dCombo1.setItems();
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


            user.setLogin(dField1.getText());
            user.setPassword(dField2.getText());
            user.setFullname(dField3.getText());

            for(UserGroup item : groups){
                if (item.getGroup_name() == dCombo1.getSelectionModel().selectedItemProperty().getValue().toString()) {
                    user.setGroup_id(item.getId());
                    user.setGroup_name(item.getGroup_name());
                }
            }

            System.out.println(user);

            try {
                if(user.getId() != null) {
                    UserOverviewController.u_service.edit(user);
                }
                else {
                    UserOverviewController.u_service.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            /**/
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
            errorMessage += "Вы не ввели логин!\n";
        }
        if (dField2.getText() == null || dField2.getText().length() == 0) {
            errorMessage += "Вы не ввели пароль!\n";
        }
        if (dField3.getText() == null || dField3.getText().length() == 0) {
            errorMessage += "Вы не ввели ФИО!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Незаполненные поля");
            alert.setHeaderText("Пожалуйста, заполните текстовые поля!");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            
            return false;
        }
    }
}