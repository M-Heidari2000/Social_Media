package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import controllers.*;

public class MainMenuController implements Initializable{
    
    @FXML
    ImageView profileImageView, explorerImageView, directImageView, groupsImageView, settingsImageView,
              createPostImageView, logoutImageView, myPostsImageView;

    @FXML
    Label usernameLabel;

    Image profileImg, explorerLogo, directLogo, groupsLogo, settingsLogo, createPostLogo, logoutLogo,
          myPostsLogo;

    CurrentUser currentUser = CurrentUser.getCurrentUser();

    Stage stage;
    Parent root;
    Scene scene;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {


            profileImg = new Image(getClass().getResourceAsStream(currentUser.getImgAddress()));
            profileImageView.setImage(profileImg);
            usernameLabel.setText("Welcome " + currentUser.getFirstName() + "!");

            explorerLogo = new Image(getClass().getResourceAsStream("..//static//explorer_menu//logo_explorer.png"));
            explorerImageView.setImage(explorerLogo);

            directLogo = new Image(getClass().getResourceAsStream("..//static//explorer_menu//logo_direct.png"));
            directImageView.setImage(directLogo);

            groupsLogo = new Image(getClass().getResourceAsStream("..//static//explorer_menu//logo_group.png"));
            groupsImageView.setImage(groupsLogo);

            settingsLogo = new Image(getClass().getResourceAsStream("..//static//explorer_menu//logo_settings.png"));
            settingsImageView.setImage(settingsLogo);

            createPostLogo = new Image(getClass().getResourceAsStream("..//static//explorer_menu//logo_create_post.png"));
            createPostImageView.setImage(createPostLogo);

            logoutLogo = new Image(getClass().getResourceAsStream("..//static//explorer_menu//logo_logout.png"));
            logoutImageView.setImage(logoutLogo);

            myPostsLogo = new Image(getClass().getResourceAsStream("..//static//explorer_menu//logo_my_posts.png"));
            myPostsImageView.setImage(myPostsLogo);


        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoExplorerPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//explorer_page.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void gotoSettingsPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//explorer_page.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    
    public void gotoGroupsPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//explorer_page.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    
    public void gotoDirectMessagePage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//explorer_page.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    
    public void gotoCreatePostPage(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//create_post_page.fxml"));
        root = loader.load();
        CreatePostController createPostController = loader.getController();
        createPostController.initialize();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logout(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//login_page.fxml"));
        currentUser.setAuthenticated(false);
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void gotoMyPostsPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//my_posts_page.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}