package cetli.listviewdemo;

import cetli.ArticleList;

import java.util.LinkedList;
import java.util.List;

import cetli.framework.Article;
import javafx.fxml.FXML;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import static cetli.Database.database;

public class ListViewDemoController {

    @FXML
    private ArticleList children;

    @FXML
    private Button myButton;

    public void initialize() {
        children.getItems().setAll(database.getArticle(1).getChildren());
    }

    @FXML
    private void myButtonPressed(ActionEvent event) throws Exception {
        Set<Article> items = database.getArticle(1158).getChildren();
        System.out.println(items);
        children.getItems().setAll(items);
        System.out.println(children.getItems());
    }

    private List<Article> forSearch(String searchTerm) {
        List<Article> ret = new LinkedList();
        List<Integer> articleIDs = database.search(searchTerm);
        for (int id : articleIDs) {
            ret.add(database.getArticle(id));
        }
        return ret;
    }
}
