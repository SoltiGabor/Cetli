package cetli;

import cetli.framework.Article;
import cetli.framework.Graph;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class FamilyViewTest extends ApplicationTest {
    FamilyView familyView;
    
    @Test
    public void myTest() {
        moveTo(familyView);
        moveBy(0, 155);
        press(MouseButton.PRIMARY);
        moveBy(0, -50);
        release(MouseButton.PRIMARY);
        sleep(1000);
    }
    
    @Override
    public void start(Stage stage) {        
        Database.database = new Database("test/mytestdatabase");
        Article article = Article.forId(1);
        familyView = new FamilyView(Graph.defaultGraph());
        familyView.setFocusArticle(article);
        
        Scene scene = new Scene(familyView, 200, 300);
        scene.getStylesheets().add("cetliStyle.css");
        stage.setScene(scene);
        if (!stage.isShowing()) stage.initStyle(StageStyle.DECORATED);
        stage.show();
    }
}
