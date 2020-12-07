package cetli;

import java.io.IOException;

import cetli.framework.Article;
import cetli.framework.Graph;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class FamilyView extends VBox {
    
    private final ObjectProperty<Article> focusArticle =
            new SimpleObjectProperty<>();
    private final SimpleBooleanProperty closed = new SimpleBooleanProperty(false);
    
    final Graph graph;
    @FXML private BorderPane focusArticleBox;
    @FXML private Label focusArticleLabel;
    @FXML private ListView<Article> parentsListView;
    @FXML private ListView<Article> childrenListView;

    public FamilyView(Graph graph) {
        loadFXML();
        this.graph = graph;
    }
    
    @FXML
    private void initialize() {
        addNavigation();
    }
        
    public Article getFocusArticle() {
        return focusArticle.get();
    }
    
    public void setFocusArticle(Article article) {
        graph.setFocusGraphNode(article);
        focusArticle.set(article);
        focusArticleLabel.setText(article.getTitle());
        parentsListView.getItems().setAll(graph.getParents(article));
        childrenListView.getItems().setAll(graph.getChildren(article));
    }
    
    public ObjectProperty focusArticleProperty() {return focusArticle;}
    
    public Boolean isClosed() {
        return closed.get();
    }
    
    public void setClosed(Boolean closed) {
        this.closed.set(closed);
    }
    
    public Animation changeHeightTo(final Double newHeight) {
        final Double oldHeight = getHeight();
        Animation animation = new Transition() {
            {setCycleDuration(Duration.millis(500));}
            @Override
            protected void interpolate(double frac) {
                setHeights(oldHeight + (newHeight - oldHeight) * frac);
            }
        };
        animation.play();
        return animation;
    }
    
    public void setHeights(double height) {
        unBindHeights();
        setPrefHeight(height);
        setMinHeight(height);
        setMaxHeight(height);
    }
    
    public void bindHeights(DoubleProperty height) {
        prefHeightProperty().bind(height);
        minHeightProperty().bind(height);
        maxHeightProperty().bind(height);
    }
    
    public void unBindHeights() {
        prefHeightProperty().unbind();
        minHeightProperty().unbind();
        maxHeightProperty().unbind();
    }
    
    @FXML
    private void focusClicked() {
        fireEvent(new ArticleEvent(ArticleEvent.ARTICLE_CLICKED, getFocusArticle()));
        fireEvent(new FamilyEvent(FamilyEvent.FOCUS_LABEL_CLICKED, this));
    }
    
    @FXML
    private void removeFamilyView() {
        fireEvent(new FamilyEvent(FamilyEvent.REMOVE_BUTTON_PRESSED, this));
    }
    
    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/family_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    private void addNavigation() {
        addEventHandler(ArticleEvent.BUTTON_PRESSED, new EventHandler<ArticleEvent>() {
            @Override
            public void handle(ArticleEvent event) {
                setFocusArticle(event.getArticle());
            }            
        });
    }
    
    public double closedHeight() {
        return focusArticleBox.getHeight() + 4;
    }
    
    public void showListViews() {
        childrenListView.setVisible(true);
        parentsListView.setVisible(true);
    }
    
    public void hideListViews() {
        childrenListView.setVisible(false);
        parentsListView.setVisible(false);
    }
    
    public void removeArticle(Article article) {
        childrenListView.getItems().remove(article);
        parentsListView.getItems().remove(article);
        if (graph.articles != null) {
            graph.articles.remove(article);
        }
    }
    
    public void closeChildren() {
        //childrenListView.set
        
    }
}