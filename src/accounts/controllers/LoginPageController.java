package accounts.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginPageController {
    
    @FXML
    private TextField usernameText, passwordText;
    @FXML
    private Button loginButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void login(ActionEvent event){
        String username = usernameText.getText();
        String password = passwordText.getText();
        System.out.println(username);
        System.out.println(password);
    }

    public void gotoRegisterPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//register_page.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
