package cetli;

import java.io.IOException;
import java.nio.charset.Charset;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    public static final boolean USE_TEST_DATABASE = false;

    @Override
    public void start(Stage stage) {
        if (USE_TEST_DATABASE) {
            dressUpStage(stage, "test/mytestdatabase");
        } else {
            dressUpStage(stage, "myarticles");
        }
        stage.show();
    }

    protected static void dressUpStage(Stage stage, String dbName) {

        try {
            Database.database = new Database(dbName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/main.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MainController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setup();

        Scene scene = new Scene(root, screenWidth() * 0.7, screenHeight() * 0.7);
        scene.getStylesheets().add("/cetliStyle.css");

        stage.setScene(scene);
        stage.setTitle(new String("Gépi cetliző".getBytes(), Charset.forName("UTF-8")));
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/notepad.png")));
    }

    private static double screenWidth() {
        return Screen.getPrimary().getVisualBounds().getWidth();
    }

    private static double screenHeight () {
        return Screen.getPrimary().getVisualBounds().getHeight();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
