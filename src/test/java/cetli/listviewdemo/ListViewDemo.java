package cetli.listviewdemo;

import cetli.Database;
import static cetli.Database.database;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListViewDemo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Database.database = new Database("test/mytestdatabase");
        primaryStage.setTitle("My Title");
        Pane myPane = makeRootFromScratch();
        Scene myScene = new Scene(myPane, 100, 400);
        myScene.getStylesheets().add("cetliStyle.css");
        primaryStage.setScene(myScene);
        primaryStage.show();
    }
    
    private Pane makeRootFromFXML() throws Exception {
        return (Pane) FXMLLoader.load(getClass().getResource("list_test.fxml"));
    }
    
    private Pane makeRootFromScratch() {
        Button b = new Button("Click");
        ListView al = new ListView();
        al.getItems().setAll(database.getArticle(1).getChildren());
        AnchorPane ap = new AnchorPane();
        ap.getChildren().add(al);
        AnchorPane.setTopAnchor(al, 0.0);
        AnchorPane.setBottomAnchor(al, 0.0);
        AnchorPane.setRightAnchor(al, 0.0);
        AnchorPane.setLeftAnchor(al, 0.0);
        
        //al.setPrefHeight(Double.MAX_VALUE);
        VBox box = new VBox();
        box.setStyle("-fx-background-color: PEACHPUFF;");
        //b.setOnAction(e -> System.out.println(al.getContentBias()));
        //box.getChildren().addAll(ap, b);
        return (Pane)ap;
    }
    
    private Region myRegion() {
        Region ret = new Region();
        return ret;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
