package cetli;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cetli.framework.Article;
import cetli.framework.Graph;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

public class FilterPane extends VBox {
    private final SimpleDoubleProperty roomTakenByFamilyViewsWhenClosed = new SimpleDoubleProperty();
    public static final SimpleDoubleProperty openFamilyViewHeight = new SimpleDoubleProperty();
    
    public FilterPane() {
        setSpacing(5);
        bindOpenFamilyViewHeight();
        openFamilyViewWhenFocusLabelClicked();
        removeFamilyViewWhenRemoveButtonPressed();
    }
    
    public void newBaseArticle(Article article) {
        getChildren().clear();
        Graph graph = Graph.defaultGraph();
        graph.setFocusGraphNode(article); //unnecessary?
        FamilyView familyView = new FamilyView(graph);
        familyView.setFocusArticle(article);
        familyView.bindHeights(openFamilyViewHeight);
        refreshRestWhenFocusarticleChanged(familyView);
        getChildren().add(familyView);
        addOrphansList(graph.alternativeOrphans());
    }
    
    private void addOrphansList(Set<Article> orphans) {
        final OrphansList orphansList = new OrphansList(orphans);
        final VBox orphansListBox = new VBox(orphansList);
        orphansListBox.getStyleClass().add("orphansList");
        orphansList.setOnButtonPressed(new EventHandler<ArticleEvent>() {
            @Override
            public void handle(ArticleEvent event) {
                recalculateRoomTakenByFamilyViewsWhenClosed();
                Article selectedOrphan = event.getArticle();
                FamilyView lastFamilyView = lastFamilyView();
                Graph prevGraph = lastFamilyView.graph;
                Graph graph = new Graph(prevGraph);
                FamilyView familyView = new FamilyView(graph);
                familyView.setFocusArticle(selectedOrphan);
                refreshRestWhenFocusarticleChanged(familyView);
                getChildren().remove(orphansListBox);
                familyView.setHeights(0);
                familyView.setClosed(true);
                getChildren().add(familyView);
                open(familyView);
                close(lastFamilyView);
                addOrphansList(familyView.graph.alternativeOrphans());
            }            
        });
        getChildren().add(orphansListBox);
    }
    
    private Article articleOf(Button button) {                 
        ListCell<Article> listCell = 
                (ListCell<Article>)button.getParent().getParent().getParent();
        return listCell.getItem();        
    }
    
    private FamilyView lastFamilyView() {
        FamilyView ret = null;
        for (Node child: getChildren()) {
            if (child instanceof FamilyView) {
                ret = (FamilyView)child;
            }
        }
        return ret;
    }
    
    private void refreshRestWhenFocusarticleChanged(final FamilyView familyView) {
        familyView.focusArticleProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                removeChildrenAfter(familyView);
                addOrphansList(familyView.graph.alternativeOrphans());
            }            
        });
    }
    
    private void removeChildrenAfter(FamilyView familyView) {
        int index = getChildren().indexOf(familyView);
        int numberOfChildren = getChildren().size();
        getChildren().remove(index + 1, numberOfChildren);
    }
    
    private void recalculateRoomTakenByFamilyViewsWhenClosed() {
        Double roomTaken = 0d;
        for (Node child: getChildren()) {
            if (child instanceof FamilyView) {
                roomTaken = roomTaken + ((FamilyView)child).closedHeight();
            }
        }
        roomTakenByFamilyViewsWhenClosed.set(roomTaken);
    }
    
    private void bindOpenFamilyViewHeight() {
        DoubleBinding roomLeftAfterClosedFamilyViews = 
                heightProperty().subtract(roomTakenByFamilyViewsWhenClosed);
        openFamilyViewHeight.bind(roomLeftAfterClosedFamilyViews.multiply(0.66));
    }
    
    private void openFamilyViewWhenFocusLabelClicked() {
        addEventHandler(FamilyEvent.FOCUS_LABEL_CLICKED, new EventHandler<FamilyEvent>() {
            @Override
            public void handle(FamilyEvent event) {
                FamilyView familyView = event.getFamilyView();
                open(familyView);
            }            
        });
    }
    
    private void open(final FamilyView familyView) {
        if (!familyView.isClosed()) return;
        familyView.setClosed(false);
        familyView.showListViews();
        Animation animation = familyView.changeHeightTo(openFamilyViewHeight.get());             
        animation.setOnFinished(new EventHandler() {
            @Override
            public void handle(Event event) {
                familyView.bindHeights(openFamilyViewHeight);
            }            
        });
        closeAllOtherFamilyViews(familyView);
    }
    
    private void close(final FamilyView familyView) {
        if (familyView.isClosed()) return;
        familyView.setClosed(true);
        familyView.unBindHeights();
        Animation animation = familyView.changeHeightTo(familyView.closedHeight());             
        animation.setOnFinished(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (familyView.isClosed()) {
                    familyView.hideListViews();
                }
            }            
        });
    }
    
    private void closeAllOtherFamilyViews(FamilyView familyView) {
        for (Node child : getChildren()) {
            if (child instanceof FamilyView && child != familyView) {
                close((FamilyView)child);
            }
        }
    }
    
    private void removeFamilyViewWhenRemoveButtonPressed() {
        addEventHandler(FamilyEvent.REMOVE_BUTTON_PRESSED, new EventHandler<FamilyEvent>() {
            @Override
            public void handle(FamilyEvent event) {                
                removeFamilyView(event.getFamilyView());
            }
        });
    }
    
    private void removeFamilyView(FamilyView familyView) {
        if (familyView.equals(getChildren().get(0))) {
            getChildren().clear();
        } else {
            removeChildrenAfter(familyView);
            getChildren().remove(familyView);
            addOrphansList(lastFamilyView().graph.alternativeOrphans());
        }
    }
    
    public void removeArticle(Article article) {
        System.out.println("isFxApplicationThread() = " + Platform.isFxApplicationThread());
        List<Node> children = new LinkedList(getChildren());
        for (Node child: children) {
            if (child instanceof FamilyView) {
                FamilyView familyView = (FamilyView)child;
                familyView.removeArticle(article);
                if (familyView.getFocusArticle().equals(article)) {
                    removeFamilyView(familyView);
                }
            } else if (child instanceof OrphansList) {
                ((OrphansList)child).getItems().remove(article);
            }
        }
    }
}