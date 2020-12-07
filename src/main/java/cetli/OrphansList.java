package cetli;

import java.util.Set;

import cetli.framework.Article;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class OrphansList extends ArticleListWithFocusButtons {
    public OrphansList(Set<Article> orphans) {
        getItems().addAll(orphans);
        VBox.setVgrow(this, Priority.ALWAYS);
        setDraggable(true);
    }
}
