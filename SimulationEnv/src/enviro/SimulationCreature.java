package enviro;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import metier.CreatureGenerator;
import models.Creature;
import models.Node;
import models.Segment;

public class SimulationCreature extends Application {
    public static final int TIME_CLOCK_CYCLE = 60;
    public static final double CLOCK_INCREMENT = 0.1;
    public static final boolean DEBUG_CREATURES = true;
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 900;
    public static final int CREATURE_PER_ROUND = 2;
    public static final int MIN_NODES = 3;
    public static final int MAX_NODES = 6;
    public static final int MAX_SEGMENT = 5;
    private static final int NUM_CREATURES = 1;
    private List<Creature> creatures;

    @Override
    public void start(Stage primaryStage) throws CloneNotSupportedException {
        Group root = new Group();

        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.WHITE);
        scene.setFill(Color.LIGHTBLUE); 

        
        primaryStage.setTitle("Creature Evolution Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        CreatureGenerator generator = new CreatureGenerator();
        
        
       // Create creatures
        creatures = generator.generateCreatures();
    	
        //creatures.add(creatureCopy);
        
        /*for(Creature creature: creatures) {
            root.getChildren().addAll(creature.getShapes());
        }*/
        
        

        Creature creatureCopy = creatures.get(0);
        creatureCopy.ajouterAction();
        creatureCopy.mutation();
        root.getChildren().addAll(creatureCopy.getShapes());

        
        //root.getChildren().add(new Line(200,0,200,900));     // Start simulation loop
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
