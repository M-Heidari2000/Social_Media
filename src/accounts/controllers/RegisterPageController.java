package accounts.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import database.AccountsDatabaseManager;
import accounts.form_exception.*;;

public class RegisterPageController {
    
    @FXML
    private TextField firstNameText, lastNameText, usernameText,
                      passwordText, confirmPasswordText, emailText;

    @FXML Label passwordNotMatchLabel;

    @FXML
    private RadioButton rButtonNormal, rButtonBusiness;

    @FXML
    private Button registerButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void register(ActionEvent event) throws IOException{
        String firstName = firstNameText.getText();
        String lastName = lastNameText.getText();
        String username = usernameText.getText();
        String password = passwordText.getText();
        String confirmPassword = confirmPasswordText.getText();
        String email = emailText.getText();
        String accountType = rButtonNormal.isSelected() ? "normal" : "business";
        AccountsDatabaseManager dbManager = new AccountsDatabaseManager();
        try {
            if (!password.equals(confirmPassword)){
                throw new PasswordNotMatch("Passwords doesn't match");
            }
            dbManager.insert(username, email, password, firstName, lastName, accountType);
            this.gotoLoginPage(event);
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (PasswordNotMatch e){
            passwordNotMatchLabel.setText("Passwords didn't match");
        }
    }

    public void gotoLoginPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//login_page.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
