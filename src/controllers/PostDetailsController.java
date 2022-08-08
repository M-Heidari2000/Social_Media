package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import database.PostsDatabaseManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PostDetailsController extends MainMenuController{

    @FXML
    Hyperlink postTitleHyperlink, userProfileHyperlink;

    @FXML
    ImageView postImageView, likeImageView;

    @FXML
    Label postBodyLabel, postDateLabel;

    @FXML
    VBox postVBox;

    private Stage stage;
    private Scene scene;
    private Parent parent;
    private PostsDatabaseManager dbManager = new PostsDatabaseManager();
    private TextArea commentTextArea;
    private int currentPostID;
    private boolean liked;
    
    @Override
    public void initializeElements(FXMLLoader loader){
    }

    public void showPost(int postID, String authorName, String title, String body, Timestamp lastEdited, String imageAddress) throws SQLException{
        postTitleHyperlink.setText(title);
        Image postImage = new Image(getClass().getResourceAsStream(imageAddress));
        postImageView.setImage(postImage);
        postBodyLabel.setText(body);
        postDateLabel.setText("last edited on " + lastEdited.toString() + " by ");
        userProfileHyperlink.setText(authorName);
        this.currentPostID = postID;
        this.liked = dbManager.isLiked(postID);
        if (this.liked){
            Image likeImage = new Image(getClass().getResourceAsStream("..//static//post_details//liked.png"));
            likeImageView.setImage(likeImage);
        }
        else{
            Image likeImage = new Image(getClass().getResourceAsStream("..//static//post_details//notLiked.png"));
            likeImageView.setImage(likeImage);
        }

        userProfileHyperlink.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//user_profile_page.fxml"));
                    root = loader.load();
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addExistingComments(String authorName, String body, Timestamp lastEdited){
        VBox commentVBox = new VBox();
        postVBox.getChildren().add(commentVBox);
        commentVBox.setStyle("-fx-border-width: 3");
        commentVBox.setStyle("-fx-border-color: #000000");
        VBox.setMargin(commentVBox, new Insets(20, 20, 20, 20));
        commentVBox.setPadding(new Insets(10, 10, 10, 10));
        Label commentText = new Label(body);
        commentText.setFont(Font.font("Times New Roman", 14));
        commentText.setPrefWidth(Double.MAX_VALUE);
        VBox fillVBox = new VBox();
        fillVBox.setPrefWidth(Double.MAX_VALUE);
        fillVBox.setPrefHeight(20);
        Label commentDate = new Label("last edited by  " + authorName + " on " + lastEdited.toString());
        commentDate.setFont(Font.font("arial", 11));
        commentDate.setAlignment(Pos.BOTTOM_RIGHT);
        commentDate.setPrefWidth(Double.MAX_VALUE);
        commentVBox.getChildren().add(commentText);
        commentVBox.getChildren().add(fillVBox);
        commentVBox.getChildren().add(commentDate);
    }

    public void likePost(MouseEvent event){
        if (!this.liked){
            try {
                dbManager.postLike(this.currentPostID);
                this.liked = true;
                Image likeImage = new Image(getClass().getResourceAsStream("..//static//post_details//liked.png"));
                likeImageView.setImage(likeImage);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                dbManager.postDislike(this.currentPostID);
                this.liked = false;
                Image likeImage = new Image(getClass().getResourceAsStream("..//static//post_details//notLiked.png"));
                likeImageView.setImage(likeImage);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addNewComment(int postID){
        postVBox.setPadding(new Insets(0, 20, 0, 20));
        Label addCommentLabel = new Label("Add your comment!");
        addCommentLabel.setFont(Font.font("Lucida Console", 18));
        addCommentLabel.setPrefWidth(Double.MAX_VALUE);
        Label spaceLabel1 = new Label();
        spaceLabel1.setPrefWidth(Double.MAX_VALUE);
        spaceLabel1.setPrefHeight(100);
        postVBox.getChildren().add(spaceLabel1);
        addCommentLabel.setAlignment(Pos.CENTER_LEFT);
        postVBox.getChildren().add(addCommentLabel);
        VBox commentVBox = new VBox();
        postVBox.getChildren().add(commentVBox);
        commentVBox.setStyle("-fx-border-width: 3");
        commentVBox.setStyle("-fx-border-color: #000000");
        VBox.setMargin(commentVBox, new Insets(20, 20, 20, 20));
        commentTextArea = new TextArea();
        commentTextArea.setId("newCommentTextArea");
        commentVBox.getChildren().add(commentTextArea);
        Button submitCommitButton = new Button("Add comment");
        postVBox.getChildren().add(submitCommitButton);
        TextArea newCommentTextArea = this.commentTextArea;

        submitCommitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                Alert alert = new Alert(AlertType.INFORMATION);
                try{
                    alert.setTitle("Database updated!");
                    alert.setHeaderText("Comment has been added successfully");
                    String body = newCommentTextArea.getText();
                    dbManager.insertComment(postID, body);
                    newCommentTextArea.clear();
                    alert.showAndWait();
                }
                catch(Exception e){
                    e.printStackTrace();
                    alert.setTitle("Operation failed");
                    alert.setHeaderText("Something went wrong! please try again");
                }
            }
        });

    }
}
