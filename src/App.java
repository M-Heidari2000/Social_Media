import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application{
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes//login_page.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Image icon = new Image("static//icon.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
}
