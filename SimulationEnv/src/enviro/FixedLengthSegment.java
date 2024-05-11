package enviro;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class FixedLengthSegment extends Application {

    private Circle point1;
    private Circle point2;
    private Line segment;
    private final double fixedLength = 100; // Longueur fixe du segment
    private final double attractionFactor = 0.5; // Facteur d'attraction entre les points

    @Override
    public void start(Stage primaryStage) {
        point1 = new Circle(150, 150, 5, Color.RED);
        point2 = new Circle(250, 150, 5, Color.BLUE);

        segment = new Line();
        updateSegment();

        // Déplacer les points avec la souris
        point1.setOnMouseDragged(e -> {
            point1.setCenterX(e.getX());
            point1.setCenterY(e.getY());
        });

        point2.setOnMouseDragged(e -> {
            point2.setCenterX(e.getX());
            point2.setCenterY(e.getY());
        });

        Pane root = new Pane();
        root.getChildren().addAll(segment, point1, point2);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Fixed Length Segment");
        primaryStage.show();

        // Démarrer la boucle de simulation
        startSimulation();
    }

    private void updateSegment() {
        segment.setStartX(point1.getCenterX());
        segment.setStartY(point1.getCenterY());
        segment.setEndX(point2.getCenterX());
        segment.setEndY(point2.getCenterY());
    }

    private void startSimulation() {
        // Boucle de simulation
        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    // Calculer le vecteur de direction entre les points
                    double deltaX = point2.getCenterX() - point1.getCenterX();
                    double deltaY = point2.getCenterY() - point1.getCenterY();

                    // Calculer la distance actuelle entre les points
                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                    // Calculer la force d'attraction
                    double attractionForce = (distance - fixedLength) * attractionFactor;

                    // Appliquer la force aux points
                    if (distance > 0) {
                        double forceX = attractionForce * deltaX / distance;
                        double forceY = attractionForce * deltaY / distance;

                        // Mettre à jour les positions des points
                        point1.setCenterX(point1.getCenterX() + forceX);
                        point1.setCenterY(point1.getCenterY() + forceY);
                        point2.setCenterX(point2.getCenterX() - forceX);
                        point2.setCenterY(point2.getCenterY() - forceY);

                        // Mettre à jour l'affichage
                        updateSegment();
                    }
                });

                // Pause pour la prochaine itération
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
