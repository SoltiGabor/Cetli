package cetli;

import cetli.framework.Article;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

import static cetli.ArticleEvent.ARTICLE_CLICKED;

public class ArticleList extends ListView<Article> {
    
    private boolean draggable = false;

    public ArticleList() {
        setCellFactory(new ArticleListCellFactory());
    }
    
    public boolean getDraggable() {
        return draggable;
    }
    
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }
    
    protected ListCell<Article> newCell() {
        final ListCell<Article> cell = new ListCell<Article>() {
            @Override
            public void updateItem(Article item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.getTitle());
                } else {
                    setText("");
                }
            }
        };
        
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fireEvent(new ArticleEvent(ARTICLE_CLICKED, cell.getItem()));
                event.consume();
            }
        });
        
        return cell;
    }

    private class ArticleListCellFactory
            implements Callback<ListView<Article>, ListCell<Article>> {

        @Override
        public ListCell<Article> call(ListView<Article> listView) {
            final ListCell<Article> cell = newCell();
            
            if (draggable) {
                cell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (cell.isEmpty()) {
                            return;
                        }
                        Dragboard db = cell.startDragAndDrop(TransferMode.COPY);
                        ClipboardContent content = new ClipboardContent();
                        content.putString(String.valueOf(cell.getItem().getId()));
                        db.setContent(content);
                        event.consume();
                    }
                });
            }
            return cell;
        }
    }
}
