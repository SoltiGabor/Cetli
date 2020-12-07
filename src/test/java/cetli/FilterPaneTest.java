package cetli;

import static cetli.Database.database;

import cetli.framework.Article;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

public class FilterPaneTest extends CetliTest {

    private FilterPane filterPane;

    @Before
    public void setUp() throws Exception {
        database.clearTables();
        database.createTables();
        MyTestData.load();
        Platform.runLater(() -> {
            Article article = Database.database.getArticle(1);
            filterPane.newBaseArticle(article);
        });
        sleep(1000);
    }

    @Test
    public void myTest() {
        Label focusArticleLabel = from(familyView(0)).lookup("#focusArticleLabel").query();
        verifyThat(focusArticleLabel, hasText("Psionic Abilities"));
        assertThat(orphansList().getItems().size(), is(3));
        sleep(1000);
    }

    @Override
    protected FamilyView familyView(int i) {
        return lookup(isA(FamilyView.class)).nth(i).query();
    }
    
    @Override
    protected FilterPane filterPane() {
        return lookup(isA(FilterPane.class)).query();
    }

    @Override
    public void start(Stage stage) {
        Database.database = new Database("test/mytestdatabase");
        filterPane = new FilterPane();
        Scene scene = new Scene(filterPane, 250, 600);
        scene.getStylesheets().add("cetliStyle.css");
        stage.setScene(scene);
        if (stage.getStyle().equals(StageStyle.UNDECORATED)) {
            stage.initStyle(StageStyle.DECORATED);
        }
        stage.show();
    }
}
