package cetli;

import cetli.framework.Article;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import static cetli.ArticleEvent.ARTICLE_CLICKED;
import static cetli.ArticleEvent.BUTTON_PRESSED;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ArticleListWithButtons extends ArticleList {

    public final ObjectProperty<EventHandler<ArticleEvent>> onButtonPressedProperty
            = new SimpleObjectProperty<>();
    protected String buttonText;
    protected Paint buttonTextFill;

    @Override
    protected ListCell<Article> newCell() {
        final Label label = new Label();
        final Button button = new Button(buttonText);
        button.setTextFill(buttonTextFill);
        //button.setFont(Font.font(5));
        button.setPadding(new Insets(0,2,0,2));
        final HBox hBox = new HBox();
        hBox.setSpacing(10);
        label.setPrefWidth(0);
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);
        hBox.getChildren().addAll(label, button);

        final ListCell<Article> cell = new ListCell<Article>() {
            @Override
            public void updateItem(Article article, boolean empty) {
                super.updateItem(article, empty);
                if (!empty) {
                    setText(null);
                    label.setText(article.getTitle());
                    setGraphic(hBox);
                } else {
                    setGraphic(null);
                }
            }
        };
        cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        EventHandler<MouseEvent> articleClickedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fireEvent(new ArticleEvent(ARTICLE_CLICKED, cell.getItem()));
                event.consume();
            }
        };

        label.setOnMouseClicked(articleClickedHandler);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getOnButtonPressed() == null) {
                    fireEvent(new ArticleEvent(BUTTON_PRESSED, cell.getItem()));
                } else {
                    getOnButtonPressed().handle(new ArticleEvent(BUTTON_PRESSED, cell.getItem()));
                }
                event.consume();
            }
        });
        return cell;
    }

    public EventHandler<ArticleEvent> getOnButtonPressed() {
        return onButtonPressedProperty.get();
    }

    public void setOnButtonPressed(EventHandler<ArticleEvent> eventHandler) {
        onButtonPressedProperty.set(eventHandler);
    }
}
