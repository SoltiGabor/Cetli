package cetli;

import java.util.Random;

import cetli.framework.Article;
import cetli.framework.Graph;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FamilyViewDemo extends Application {

    Random random = new Random();

    @Override
    public void start(Stage stage) throws Exception {
        Database.database = new Database("test/mytestdatabase");
        VBox box = new VBox();
        Article article
                = Article.forId(1);
        FamilyView familyView1 = new FamilyView(Graph.defaultGraph());
        familyView1.setFocusArticle(article);
        box.getChildren().add(familyView1);

        FamilyView familyView2 = new FamilyView(Graph.defaultGraph());
        familyView2.setFocusArticle(article);
        box.getChildren().add(familyView2);
        
        FamilyView familyView3 = new FamilyView(Graph.defaultGraph());
        familyView3.setFocusArticle(article);
        box.getChildren().add(familyView3);

        familyView2.setOnMouseClicked((MouseEvent event) -> {
            //familyView.changeHeightTo(random.nextDouble()*100+50);
            System.out.println(familyView2.getPrefHeight());
            Animation animation = new Transition() {
                {setCycleDuration(Duration.millis(1000));}
                @Override
                protected void interpolate(double frac) {
//                    familyView2.setBackground(
//                            new Background(
//                            new BackgroundFill(
//                            Color.color(frac, frac, frac), CornerRadii.EMPTY, Insets.EMPTY)));
                    familyView2.setMaxHeight(frac * 100);
                    familyView2.setMinHeight(frac * 100);
                    //familyView2.setPrefHeight(frac * 100);
                    
                }
            };
            animation.setOnFinished(new EventHandler() {
                @Override
                public void handle(Event event) {
                    familyView2.setMinHeight(-1);
                    familyView2.setMaxHeight(-1);
                    familyView2.setMinHeight(-1);
                }            
        });
            animation.play();
            //familyView1.closeChildren();
        });

        box.getChildren().add(new ListView());

        Scene scene = new Scene(box, 200, 300);
        scene.getStylesheets().add("cetliStyle.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
