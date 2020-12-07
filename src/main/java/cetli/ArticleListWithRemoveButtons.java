package cetli;

import javafx.scene.paint.Paint;

public class ArticleListWithRemoveButtons extends ArticleListWithButtons {
    
    public ArticleListWithRemoveButtons() {
        buttonText = "X";
        buttonTextFill = Paint.valueOf("red");
    }
}
