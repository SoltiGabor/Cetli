package cetli;

import static cetli.Database.database;

import cetli.framework.Article;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.testfx.api.FxAssert;
import static org.testfx.matcher.control.ListViewMatchers.hasListCell;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class MiscTests extends CetliTest {

    @Test
    public void loadTest() {
        ListView list = lookup("#searchList").query();
        write("Psionic Abilities");
        press(KeyCode.ENTER);
        Article a = database.getArticle(database.search("hello").get(0));
        assertThat(list, hasListCell(a));
    }

    @Test
    public void myTest() {
        write("Egy dolog");
        clickOn("#newArticleButton");
        FxAssert.verifyThat("#titleField", hasText("Egy dolog"));
    }

    @Test
    public void myOtherTest() {
        point("#searchField");
        write("Egy dolog");
        FxAssert.verifyThat("#searchField", hasText("Egy dolog"));
        clickOn("#newArticleButton");
    }

    @Test
    public void dummy() {
    }
}
