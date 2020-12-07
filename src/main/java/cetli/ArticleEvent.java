package cetli;

import cetli.framework.Article;
import javafx.event.ActionEvent;
import javafx.event.EventType;

public class ArticleEvent extends ActionEvent {
    public static final EventType<ArticleEvent> ARTICLE_CLICKED = 
            new EventType(ActionEvent.ACTION, "ARTICLE_CLICKED");
    public static final EventType<ArticleEvent> BUTTON_PRESSED =
            new EventType(ActionEvent.ACTION, "BUTTON_PRESSED");
    private final Article article;
    
    public ArticleEvent(EventType<ArticleEvent> eventType, Article article) {
        this.eventType = eventType;
        this.article = article;
    }
    
    public Article getArticle() {
        return article;
    }
}