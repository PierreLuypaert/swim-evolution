package enviro;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class DraggableCamera extends Group {
    private double lastX;
    private double lastY;

    public DraggableCamera() {
        setOnMousePressed(this::onMousePressed);
        setOnMouseDragged(this::onMouseDragged);
    }

    void onMousePressed(MouseEvent event) {
        lastX = event.getX();
        lastY = event.getY();
    }

    void onMouseDragged(MouseEvent event) {
        double deltaX = event.getX() - lastX;
        double deltaY = event.getY() - lastY;

        // Déplacez la caméra par rapport au déplacement de la souris
        setTranslateX(getTranslateX() + deltaX);
        setTranslateY(getTranslateY() + deltaY);

        lastX = event.getX();
        lastY = event.getY();
    }
}
