package accounts.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterPageController {
    
    @FXML
    private TextField firstNameText, lastNameText, usernameText,
                      passwordText, confirmPasswordText, emailText;

    @FXML
    private RadioButton rButtonNormal, rButtonBusiness;

    @FXML
    private Button registerButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void register(ActionEvent event){

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
