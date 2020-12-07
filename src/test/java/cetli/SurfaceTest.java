package cetli;

import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class SurfaceTest extends CetliTest {
    @Test
    public void myNewTest() {
        searchAndFind("Psionic Abilities");
        
        assertThat(childrenList(0).getItems().size(), is(38));
        assertThat(orphansList().getItems().size(), is(3));
        
        clickOn(firstOrphanButton());
        
        assertThat(childrenList(1).getItems().size(), is(22));
        assertThat(orphansList().getItems().size(), is (1));
        
        sleep(1000);
        clickOn(firstOrphanButton());
    }
    
    private void searchAndFind(String s) {
        write(s);
        press(KeyCode.ENTER);
        clickOn(instanceOf(ListCell.class));
        clickOn("#filterButton");
    }
}
