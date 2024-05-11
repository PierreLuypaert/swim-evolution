package enviro;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import models.Creature;

public class SimulationCreature extends Application {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 900;
    private static final int NUM_CREATURES = 1;
    private Creature[] creatures;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();

        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.WHITE);
        scene.setFill(Color.LIGHTBLUE); 

        
        primaryStage.setTitle("Creature Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

       // Create creatures
        creatures = new Creature[NUM_CREATURES];
        for (int i = 0; i < NUM_CREATURES; i++) {
            creatures[i] = new Creature();
            root.getChildren().addAll(creatures[i].getShapes());
        }

        root.getChildren().add(new Line(200,0,200,900));     // Start simulation loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
        
     // Dans la méthode start() de votre classe SimulationCreature
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.X) {
            	for (Creature creature : creatures) {
                        creature.ajouterAction();
                    
                }
            }
        });
    }

    private void update() {
        // Update creature positions
        for (Creature creature : creatures) {
            creature.update();
            
        }
    }

    public static void main(String[] args) {
    	System.out.println("test");
        launch(args);
    }
}