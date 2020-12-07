package cetli;

import static cetli.Database.database;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.AfterClass;
import org.junit.Before;
import org.testfx.framework.junit.ApplicationTest;


public abstract class CetliTest extends ApplicationTest {
    
    @Before
    public void setUp() throws Exception {
        database.clearTables();
        database.createTables();
        MyTestData.load();
    }
    
    @AfterClass
    public static void waitALittle() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void start(Stage stage) {
        Main.dressUpStage(stage, "test/mytestdatabase");
        if (stage.getStyle().equals(StageStyle.UNDECORATED))
            stage.initStyle(StageStyle.DECORATED);
        stage.show();
    }
    
    protected Button firstOrphanButton() {
        return from(orphansList()).lookup("->").query();
    }
    
    private void scanButtons() {
        //Set<Node> s = from(childrenList(2)).lookup("->").queryAll();
        Set<Node> set = lookup("->").queryAll();
        List<Node> list = new ArrayList<>(set);
        
        for (Node n : list) {
            moveTo(n);
            HBox hb = (HBox)n.getParent();
            Node n0 = hb.getChildren().get(0);
            System.out.println(n0);
            sleep(100);
        }
    }
    
    protected ArticleList childrenList(int familyViewNo) {
        return (ArticleList)familyView(familyViewNo).getChildren().get(2);
    }
    
    protected ArticleList parentsList(int familyViewNo) {
        return (ArticleList)familyView(familyViewNo).getChildren().get(0);
    }
    
    protected OrphansList orphansList() {
        int size = filterPane().getChildren().size();
        VBox vBox = (VBox)filterPane().getChildren().get(size-1);
        return (OrphansList)vBox.getChildren().get(0);
        
    }
    
    protected FamilyView familyView(int n) {
        return (FamilyView)filterPane().getChildren().get(n);
    }
    
    protected FilterPane filterPane() {
        return lookup("#filterPane").query();
    }
}
