package enviro;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import metier.CreatureGenerator;
import models.Creature;
import models.Node;
import models.Segment;

public class SimulationCreature extends Application {
    public static final int TIME_CLOCK_CYCLE = 60;
    public static final double CLOCK_INCREMENT = 0.2;
    public static final int CYCLE_TO_CALCULATE_SCORE = 10;
    public static final boolean DEBUG_CREATURES = false;
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 900;
    public static final int CREATURE_PER_ROUND = 20;
    public static final int MIN_NODES = 3;
    public static final int MAX_NODES = 6;
    public static final int MAX_SEGMENT = 5;
    private List<Creature> creatures;
    private List<Creature> creaturesLastRound;

    @Override
    public void start(Stage primaryStage) throws CloneNotSupportedException {
    	if(this.creaturesLastRound == null)
    		this.creaturesLastRound = new ArrayList<Creature>();
    	
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
        if(this.creaturesLastRound.size()==0)
        {
        	this.creatures = generator.generateCreatures((this.creaturesLastRound.size() == 0 ? CREATURE_PER_ROUND : CREATURE_PER_ROUND/2));
        	for (Creature creature : this.creatures) {
        	    creature.ajouterAction();
        	}
        }
        else
        {
        	this.creatures = new ArrayList<Creature>(this.creaturesLastRound);
        	// Appel de setColor(Color.RED) sur chaque créature de creaturesLastRound
        	for (Creature creature : this.creatures) {
        	    creature.resetPosition();
        	}
        	
        	for (Creature creature : this.creaturesLastRound) {
        		Creature copy = new Creature(creature); // Ou utiliser un constructeur de copie
        	    copy.mutation(-1);
        	    //copy.setColor(Color.RED);
        	    this.creatures.add(copy);
        	}
        	System.out.println("Creatures dans le jeu : " + this.creatures.size());
        }
        //creatures.add(creatureCopy);
        
        /*for(Creature creature: creatures) {
            root.getChildren().addAll(creature.getShapes());
        }*/
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
        /*scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.X) {
            	/*for (Creature creature : creatures) {
                        //creature.ajouterAction();
            		System.out.println(creature.getUsedForce() 
            				+ " | " + creature.getAvgDistanceTraveled() 
            				+ " | " + creature.getNodeCount() 
            				+ " >>>> " + creature.calculateScore());
                    //creature.resetPosition();
                }
            	
            }
        });*/
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            timer.stop(); // Arrête le AnimationTimer après 2 secondes
            primaryStage.close(); // Ferme la fenêtre actuelle

            // Création d'une nouvelle liste des meilleures créatures
            this.creaturesLastRound = this.creatures.stream()
                    .sorted(Comparator.comparingDouble(Creature::calculateScore).reversed())
                    .limit(CREATURE_PER_ROUND /2)
                    .collect(Collectors.toList());

            
         // Exécution de la suite sur le thread de l'application JavaFX
            Platform.runLater(() -> {
                // Création d'une nouvelle instance de Stage pour la nouvelle fenêtre
                //Stage newPrimaryStage = new Stage();
                try {
                    // Lancement de la simulation avec les meilleures créatures
                    start(primaryStage);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            });
        });
        delay.play();


        
    }

    private void update() {
        // Update creature positions
        for (Creature creature : this.creatures) {
            creature.update();
            
        }
    }

    public static void main(String[] args) {
    	System.out.println("test");
        launch(args);
    }
}
