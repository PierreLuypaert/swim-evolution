package enviro;

import java.util.List;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import metier.CreatureGenerator;
import models.Creature;
import models.Node;
import models.Segment;

public class SimulationCreature extends Application {
    public static final int TIME_CLOCK_CYCLE = 60;
    public static final double CLOCK_INCREMENT = 0.2;
    public static final boolean DEBUG_CREATURES = false;
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 900;
    public static final int CREATURE_PER_ROUND = 8;
    public static final int MIN_NODES = 3;
    public static final int MAX_NODES = 6;
    public static final int MAX_SEGMENT = 5;
    private List<Creature> creatures;

    @Override
    public void start(Stage primaryStage) throws CloneNotSupportedException {
        Pane root = new Pane();
        
        // Load your image
        Image backgroundImage = new Image("file:ressources/bg2.png");

     // Create an ImageView for the background image
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(WIDTH);
        backgroundView.setFitHeight(HEIGHT);

        // Apply ColorAdjust effect to the background image
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.3); // Adjust brightness level here (-1.0 to 1.0)
        backgroundView.setEffect(colorAdjust);

        // Add the ImageView to the root pane
        root.getChildren().add(backgroundView);


        // Apply the ColorAdjust effect to the root node
        root.setEffect(colorAdjust);

        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.WHITE);
        
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
        
        
        for (Creature creature : creatures) {
            creature.ajouterAction();
            creature.mutation();
	        
	    }
        for(Creature creature: creatures) {
            root.getChildren().addAll(creature.getShapes());
        }

        
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
                        //creature.ajouterAction();
            		System.out.println(creature.getUsedForce() 
            				+ " | " + creature.getAvgDistanceTraveled() 
            				+ " | " + creature.getNodeCount() 
            				+ " >>>> " + creature.calculateScore());
                    
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
