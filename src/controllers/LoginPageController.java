package controllers;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import database.AccountsDatabaseManager;
import form_exception.*;

public class LoginPageController {
    
    @FXML
    private TextField usernameText, passwordText;
    @FXML
    private Button loginButton;

    @FXML
    private Label invalidDataLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private AccountsDatabaseManager dbManager = new AccountsDatabaseManager();

    

    public void login(ActionEvent event) throws IOException{
        String username = usernameText.getText();
        String password = passwordText.getText();

        try{
            dbManager.authenticate(username, password);
            this.gotoExplorerPage(event);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        catch(InvalidDataException e){
            e.printStackTrace();
            invalidDataLabel.setText(e.getMessage());
            passwordText.clear();
        }
    }

    public void gotoRegisterPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//register_page.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void gotoExplorerPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//main_menu_page.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
