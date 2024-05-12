package enviro;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ProjectionPoint extends Application {

	@Override
    public void start(Stage primaryStage) {
        // Création des cercles
        Circle cercle1 = new Circle(350, 350, 5); // Premier point
        Circle cercle2 = new Circle(600, 350, 5); // Deuxième point
        Circle cercle3 = new Circle(475, 500, 5); // Troisième point
        
        // Création du conteneur
        Pane root = new Pane();
        
        // Ajout des cercles au conteneur
        root.getChildren().addAll(cercle1, cercle2, cercle3);
        
        
        
        // Création de la scène
        Scene scene = new Scene(root, 800, 800);
        
        // Configuration de la scène et affichage de la fenêtre
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trois Points");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
