package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import database.AccountsDatabaseManager;
import database.PostsDatabaseManager;
import form_exception.PostNotFoundException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.regex.*;

public class ExplorerController extends MainMenuController{
    
    @FXML
    private VBox postsVBox;

    private PostsDatabaseManager dbManager = new PostsDatabaseManager();
    private AccountsDatabaseManager accountdbManager = new AccountsDatabaseManager(); 

    @Override
    public void initializeElements(FXMLLoader loader) throws SQLException, IOException{
        dbManager.getAllPosts(loader);
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public String shortenBody(String body){
        int limit = 200;
        return body.length() > limit ? (body.substring(0, limit) + " ...") : body;
    }

    public void addPost(int postID, String title, String authorName, String body, String imageAddress, Timestamp lastEdited){
        VBox newVBox = new VBox();
        HBox newHBox = new HBox();
        newHBox.setPadding(new Insets(0, 10, 0, 10));
        newVBox.setStyle("-fx-border-width: 2;");
        newVBox.setStyle("-fx-border-color: #000000;");
        VBox.setMargin(newHBox, new Insets(10, 0, 10, 0));

        Image image = new Image(getClass().getResourceAsStream(imageAddress));
        ImageView newImageView = new ImageView(image);
        newImageView.setFitHeight(100);
        newImageView.setFitWidth(136);
        newImageView.setPreserveRatio(false);

        VBox newVboxTitle = new VBox();
        Hyperlink newHyperLink = new Hyperlink(title);
        newHyperLink.setStyle("-fx-font: 18 arial;");
        newHyperLink.setPadding(new Insets(0, 10, 0, 0));
        newHyperLink.setId("postNumber" + Integer.toString(postID));
        Label newBodyLabel = new Label(this.shortenBody(body));
        newBodyLabel.setStyle("-fx-font: 13 arial");
        newBodyLabel.setPadding(new Insets(10, 10, 10, 0));
        HBox newDateHbox = new HBox();
        Label newDateLabel = new Label("last edited on " + lastEdited.toString() + " by");
        Hyperlink userProfileHyperlink = new Hyperlink(authorName);
        userProfileHyperlink.setAlignment(Pos.CENTER);
        userProfileHyperlink.setStyle("-fx-font: 11 arial");
        userProfileHyperlink.setPadding(new Insets(10, 0, 0, 0));
        newDateLabel.setStyle("-fx-font: 11 arial");
        newDateLabel.setPadding(new Insets(10, 5, 0, 0));
        newDateHbox.getChildren().add(newDateLabel);
        newDateHbox.getChildren().add(userProfileHyperlink);
        newVboxTitle.getChildren().add(newHyperLink);
        newVboxTitle.getChildren().add(newBodyLabel);
        newVboxTitle.getChildren().add(newDateHbox);
        HBox.setMargin(newVboxTitle, new Insets(0, 0, 0, 10));

        postsVBox.getChildren().add(newVBox);
        newVBox.getChildren().add(newHBox);
        newHBox.getChildren().add(newImageView);
        newHBox.getChildren().add(newVboxTitle);

        userProfileHyperlink.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//user_profile_page.fxml"));
                    root = loader.load();
                    UserProfileController userProfileController = loader.getController();
                    userProfileController.initializeElements(loader);
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    accountdbManager.getProfile(loader, authorName);
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        newHyperLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//post_details_page.fxml"));
                    root = loader.load();
                    PostDetailsController postDetailsController = loader.getController();
                    postDetailsController.initializeElements(loader);
                    
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    String postIDString = ((Control)event.getSource()).getId();
                    int postID = -1;
                    String postIDRegex = "postNumber(\\d+)";
                    Pattern postIDPattern = Pattern.compile(postIDRegex);
                    Matcher postIDMatcher = postIDPattern.matcher(postIDString);

                    if (postIDMatcher.find()){
                        postID = Integer.parseInt(postIDMatcher.group(1));
                    }
                    else{
                        throw new PostNotFoundException("Post doesn't exist");
                    }

                    dbManager.getPostDetails(loader, postID);
                    dbManager.getPostComments(loader, postID);
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
