package cetli;

import static cetli.Database.database;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import cetli.framework.Article;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class MainController {
    private Stage stage;
    private Article activeArticle;
    
    @FXML private StackPane root;

    @FXML private TextField searchField;
    @FXML private ArticleList searchList;
    
    @FXML private SplitPane articleView;
    @FXML private Button historyButton;
    @FXML private TextField titleField;
    @FXML private TextArea contentArea;
    @FXML private TextField uriField;
    @FXML private ArticleListWithRemoveButtons parents;
    @FXML private ArticleListWithRemoveButtons children;
    
    @FXML private FilterPane filterPane;
    
    @FXML private ArticleList history;
    
    // TODO Is changing this to "@FXML public void initialize()" problematic?
    void setup() {
        addAutoSave();
        activateArticleWhenArticleClicked();
        bindHistoryLayout();
    }
    
    private void addAutoSave() {
        ChangeListener<Boolean> focusLostListener = 
                (observable, oldVal, newVal) -> {
                    if (newVal == false) {
                    saveActiveArticle();
                }
        };
        titleField.focusedProperty().addListener(focusLostListener);
        contentArea.focusedProperty().addListener(focusLostListener);
        uriField.focusedProperty().addListener(focusLostListener);   
    }
    
    private void saveActiveArticle() {
        if (getActiveArticle() != null) {
            getActiveArticle().setTitle(titleField.getText());
            getActiveArticle().setContent(contentArea.getText());
            getActiveArticle().setUri(uriField.getText());
            getActiveArticle().save();
        }
    }
    
    private void activateArticleWhenArticleClicked() {
        EventType et = ArticleEvent.ARTICLE_CLICKED;
        EventHandler eh = new EventHandler<ArticleEvent>() {
            @Override
            public void handle(ArticleEvent event) {
                activateArticle(event.getArticle());
            }          
        };
        root.addEventHandler(et, eh);
    }
    
    private void bindHistoryLayout() {
        history.maxWidthProperty().bind(root.widthProperty().multiply(0.15));
        history.maxHeightProperty().bind(root.heightProperty().multiply(0.75));
    }
    
    @FXML private void showHistory() {
        Point2D historyButtonPosition = historyButton.localToScene(new Point2D(-70, 20));
        history.setTranslateX(historyButtonPosition.getX());
        history.setTranslateY(historyButtonPosition.getY());
        history.setVisible(true);
    }
    
    @FXML private void hideHistory() {
        history.setVisible(false);
    }
    
    @FXML private void goBack() {
        if (history.getItems().size() <=1) return;
        history.getItems().remove(activeArticle);
        activateArticle(history.getItems().get(0));
    }

    @FXML
    private void search(ActionEvent event) {
        final String searchTerm = searchField.getText().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Integer> articleIds = database.search(searchTerm);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        searchList.getItems().clear();
                        for (Integer id : articleIds) {
                            searchList.getItems().add(Article.forId(id));
                        }
                    }
                });
            }
        }).start();
    }

    @FXML
    private void newArticle() {
        String title = searchField.getText();
        if (title.equals("")) {
            return;
        }
        Article article = Article.newArticle(title);
        activateArticle(article);
    }
    
    @FXML private void articleDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.COPY);
        event.consume();
    }
    @FXML private void parentDropped(DragEvent event) {
        Integer id = droppedId(event);
        if (id == null) return;
        Article parent = Article.forId(id);
        if (activeArticle.getParents().contains(parent)) return;
        parents.getItems().add(0, parent);
        activeArticle.addParent(parent);
    }
    
    @FXML private void childDropped(DragEvent event) {
        Integer id = droppedId(event);
        if (id == null) return;
        Article child = Article.forId(id);
        if (activeArticle.getChildren().contains(child)) return;
        children.getItems().add(0, child);
        activeArticle.addChild(child);
    }
    
    private Integer droppedId(DragEvent event) {
        int ret;
        Dragboard db = event.getDragboard();
        if (!db.hasString()) return null;
        try {
            ret = Integer.valueOf(db.getString());
        } catch (NumberFormatException e) {
            return null;
        }
        return ret;
    }
    
    @FXML
    private void importDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles() || dragboard.hasUrl()) {
            event.acceptTransferModes(TransferMode.COPY);
        event.consume();
        }
    }
    
    @FXML
    private void importDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            File file = dragboard.getFiles().get(0);
            if (file.isFile()) {
                String title = file.getName();
                String uri = file.getPath();
                Article article = Article.newArticle(title);
                article.setUri(uri);
                article.save();
                activateArticle(article);
            }
            event.consume();
        } else if (dragboard.hasUrl()) {
            String title = webPageTitle(dragboard);
            String uri = dragboard.getUrl();
            Article article = Article.newArticle(title);
            article.setUri(uri);
            article.save();
            activateArticle(article);
            event.consume();
        }
    }
    private String webPageTitle(Dragboard dragboard) {
        String ret = null;
        for (DataFormat dataFormat : dragboard.getContentTypes()) {
            String dataFormatName = dataFormat.toString();
            if (dataFormatName.startsWith("[message/external-body")) {
                int beginIndex = dataFormatName.indexOf("name=") + 6;
                int endIndex = dataFormatName.length() - 6;
                ret = dataFormatName.substring(beginIndex, endIndex);
            }
        }
        return ret;
    }
    
    @FXML
    private void articleClicked(ArticleEvent event) {
        activateArticle(event.getArticle());
    }
    
    private void activateArticle(Article article) {
        saveActiveArticle();
        if (article != null) {
            setActiveArticle(article);
            articleView.setDisable(false);
            titleField.setText(article.getTitle());
            contentArea.setText(article.getContent());
            uriField.setText(article.getUri());
            parents.getItems().setAll(article.getParents());
            parents.scrollTo(0);
            children.getItems().setAll(article.getChildren());
            children.scrollTo(0);
            history.getItems().remove(article);
            history.getItems().add(0, article);
        }
    }
    
    @FXML
    private void removeParentPressed(ArticleEvent event) {
        final Article parent = event.getArticle();
        popup("Do you want to remove " + parent + " from the parents of "
              + getActiveArticle() + "?", new Runnable() {
            @Override
            public void run() {
                parents.getItems().remove(parent);
                getActiveArticle().removeParent(parent);
            }
        });
    }
    
    @FXML
    private void removeChildPressed(ArticleEvent event) {
        final Article child = event.getArticle();
        popup("Do you want to remove " + child + " from the children of "
              + getActiveArticle() + "?", new Runnable() {
            @Override
            public void run() {
                children.getItems().remove(child);
                getActiveArticle().removeChild(child);
            }
        });
    }
    
    @FXML
    private void deleteArticle() {
        popup("Do you want to delete " + getActiveArticle() + "?", new Runnable(){
            @Override
            public void run() {
                history.getItems().remove(getActiveArticle());
                searchList.getItems().remove(getActiveArticle());
                filterPane.removeArticle(getActiveArticle());
                getActiveArticle().delete();
                getActiveArticle().setTitle("--DELETED--");
                setActiveArticle(null);        
                parents.getItems().clear();
                children.getItems().clear();
                articleView.setDisable(true);
            }
        });
    }
    
    @FXML
    public void go() {
        String uri = uriField.getText();
        if (uri.equals("")) return;
        if (uri.startsWith("http://") || uri.startsWith("https://")) {
            try {
                Desktop.getDesktop().browse(URI.create(uri));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Desktop.getDesktop().open(new File(uri));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    @FXML
    private void filterButtonPressed(ActionEvent event) {
        filterPane.newBaseArticle(getActiveArticle());        
    }
        
    public Article getActiveArticle() {
        return activeArticle;
    }
    
    public void setActiveArticle(Article activeArticle) {
        this.activeArticle = activeArticle;
    }
    
    private void popup(String question, final Runnable doIt) {
        Button no = new Button("NO");
        no.setCancelButton(true);
        Button yes = new Button("Yes");
        yes.setDefaultButton(true);
        Label message = new Label(question);
        message.setWrapText(true);
        
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().add(yes);
        hbox.getChildren().add(no);
        hbox.setAlignment(Pos.CENTER);
        
        VBox vbox = new VBox();
        vbox.getStyleClass().add("popup");
        vbox.getChildren().add(message);
        vbox.getChildren().add(hbox);
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(300);
        
        final Popup popup = new Popup();
        popup.getContent().add(vbox);
        
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                root.setDisable(false);
                popup.hide();
            }
        });
        
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                doIt.run();
                root.setDisable(false);
                popup.hide();
            }
        });
        
        root.setDisable(true);
        popup.show(stage);
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
