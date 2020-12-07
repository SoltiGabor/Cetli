package cetli;

import javafx.event.ActionEvent;
import javafx.event.EventType;

public class FamilyEvent extends ActionEvent {
    public static final EventType<FamilyEvent> FOCUS_LABEL_CLICKED = 
            new EventType(ActionEvent.ACTION, "FOCUS_LABEL_CLICKED");
    public static final EventType<FamilyEvent> REMOVE_BUTTON_PRESSED = 
            new EventType(ActionEvent.ACTION, "REMOVE_BUTTON_PRESSED");
    private final FamilyView familyView;
    
    public FamilyEvent(EventType<FamilyEvent> eventType, FamilyView familyView) {
        this.eventType = eventType;
        this.familyView = familyView;
    }
    
    public FamilyView getFamilyView() {
        return familyView;
    }
}
