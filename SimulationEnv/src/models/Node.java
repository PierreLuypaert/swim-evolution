package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import enviro.SimulationCreature;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Node implements Cloneable {
	public static int ID_NODE = 0;
	private int id;
	private List<Segment> segments;    
    private static final double SIZE = 10.0;
    private Circle shape;
    private double x, y;
    private double xStart, yStart;
    private double forceX;
    private double forceY;
    private Color color;
    private List<Muscle> muscles;
    private boolean isCopy = false;
    
    
    // Tableau de couleurs de bleu
    private static final Color[] BLUE_SHADES = {
        Color.CADETBLUE,
        Color.AQUA,
        Color.BLUE,
        Color.LIGHTSTEELBLUE
        // Ajoutez d'autres nuances de bleu selon vos préférences
    };

	public Node(int x, int y) {
		this.id = Node.ID_NODE++;
        this.color = BLUE_SHADES[(int) (Math.random() * BLUE_SHADES.length)];
		this.segments = new ArrayList<Segment>();
        this.x = x;
        this.y = y;
        this.xStart = x;
        this.yStart = y;
        this.muscles = new ArrayList<Muscle>();
        
		shape = new Circle(SIZE);
        shape.setFill(this.color);
        shape.setStroke(Color.GREY); // Set the border color
        shape.setStrokeWidth(2); // Set the border width
        shape.setTranslateX(x);
        shape.setTranslateY(y);
        this.isCopy = false;
	}
	

	// Copy constructor
    public Node(Node other) {
        this.id = other.id;
        // Assuming segments are mutable and need to be deep copied
        this.segments = new ArrayList<>();
        this.shape = new Circle(other.shape.getCenterX(), other.shape.getCenterY(), other.shape.getRadius());
        this.shape.setFill(other.shape.getFill());
        this.shape.setStroke(other.shape.getStroke());
        this.shape.setStrokeWidth(other.shape.getStrokeWidth());
        this.x = other.x;
        this.y = other.y;
        this.xStart = other.xStart;
        this.yStart = other.yStart;
        this.forceX = 0;
        this.forceY = 0;
        this.color = other.color;
        this.muscles = new ArrayList<>();
        // Assuming muscles are mutable and need to be deep copied
        this.isCopy = true;

    }
	
	public Node() {
        this((int) Math.round(Math.random() * SimulationCreature.WIDTH), 
             (int) Math.round(Math.random() * SimulationCreature.HEIGHT));
    }
	
	void restoreMuscle(Node other) {
        Node nodeLeft; 
        Node nodeRight;
        if(this.segments.size() <= 1) return;
        for (Muscle muscle : other.muscles) {
            Segment segmentNodeLeft = this.segments.stream()
                    .filter(seg -> seg.getNodeLeft().getId() == muscle.getNodeLeft().getId())
                    .findFirst()
                    .orElse(null);
            
            if(segmentNodeLeft==null)
            {
                segmentNodeLeft = this.segments.stream()
                        .filter(seg -> seg.getNodeRight().getId() == muscle.getNodeLeft().getId())
                        .findFirst()
                        .orElse(null);
                nodeLeft = segmentNodeLeft.getNodeRight();
                
            } else {
                nodeLeft = segmentNodeLeft.getNodeLeft();
            }
            

            Segment segmentNodeRight = this.segments.stream()
                    .filter(seg -> seg.getNodeRight().getId() == muscle.getNodeRight().getId())
                    .findFirst()
                    .orElse(null);
            
            if(segmentNodeRight==null)
            {
                segmentNodeRight = this.segments.stream()
                        .filter(seg -> seg.getNodeLeft().getId() == muscle.getNodeRight().getId())
                        .findFirst()
                        .orElse(null);
                nodeRight = segmentNodeRight.getNodeLeft();
                
            } else {
                nodeRight = segmentNodeRight.getNodeRight();
            }
                
            this.muscles.add(new Muscle(muscle, this, nodeLeft, nodeRight)); // Assuming Muscle class has a copy constructor
        }
	}
	
	void resetPosition() {
		this.x = this.xStart;
		this.y = this.yStart;
	}
	
	void addSegment(Segment segment) {
		this.segments.add(segment);
	}
	
	/*void ajouterActions(int indexMuscle, List<ActionType> actions) {
		Muscle muscle;
		if (indexMuscle >= this.muscles.size())
		{
			muscle = new Muscle(this, this.segments.get(0).getNodeLeft(),this.segments.get(1).getNodeRight(), actions);
			this.muscles.add(muscle);
		}
		else
		{
			muscle = this.muscles.get(indexMuscle);
			muscle.ajouterActions(actions);
		}
	}*/
	
	void ajouterActionToMuscle(int indexMuscle, ActionType action) {
		Muscle muscle;
		if (indexMuscle >= this.muscles.size())
		{
			if(indexMuscle==0)
				muscle = new Muscle(this, this.segments.get(0).getNodeLeft(),this.segments.get(1).getNodeRight(), null);	
			else
				muscle = new Muscle(this, this.segments.get(1).getNodeRight(),this.segments.get(2).getNodeRight(), null);	

			
			this.muscles.add(muscle);
		}
		else
			muscle = this.muscles.get(indexMuscle);
		
		muscle.ajouterAction(action);
	}
	
	void ajouterMuscle(ActionType action1, ActionType action2) {
        int index1 = (int) (Math.random() * segments.size());
        int index2 = index1;
        while (index2 == index1) {
            index2 = (int) (Math.random() * segments.size());
        }
        
        Node nodeLeft = this.segments.get(index1).getNodeLeft();
        if(nodeLeft.getId() == this.getId())
        	nodeLeft = this.segments.get(index1).getNodeRight();
        

        
        Node nodeRight= this.segments.get(index2).getNodeRight();
        if(nodeRight.getId() == this.getId())
        	nodeRight = this.segments.get(index2).getNodeLeft();
        		
        Muscle muscle = new Muscle(this, nodeLeft, nodeRight, null);
        muscle.ajouterAction(action1);
        muscle.ajouterAction(action2);
        System.out.println("Action1 - Timestamp: " + action1.getTime() + " Angle: " + action1.getAngleValue());
        System.out.println("Action2 - Timestamp: " + action2.getTime() + " Angle: " + action2.getAngleValue());
        this.muscles.add(muscle);
	}
	

    // Méthode pour appliquer une force au nœud
    void applyForce(double forceX, double forceY) {
        this.forceX += forceX;
        this.forceY += forceY;
        this.move();
    }

    // Méthode pour déplacer le nœud en fonction des forces appliquées
    void move() {
        // Déplacer le nœud en fonction des forces appliquées
        double deltaTime = SimulationCreature.CLOCK_INCREMENT; // Pas de temps pour la simulation (à ajuster selon vos besoins)
        double accelerationX = forceX; // Calculer l'accélération en fonction de la force
        double accelerationY = forceY; // Calculer l'accélération en fonction de la force
        double velocityX = accelerationX * deltaTime; // Calculer la vitesse en fonction de l'accélération et du pas de temps
        double velocityY = accelerationY * deltaTime; // Calculer la vitesse en fonction de l'accélération et du pas de temps
        double newX = getX() + velocityX; // Calculer la nouvelle position en fonction de la vitesse
        double newY = getY() + velocityY; // Calculer la nouvelle position en fonction de la vitesse
        
        setX(newX); // Mettre à jour la position du nœud
        setY(newY); // Mettre à jour la position du nœud
        
        // Réinitialiser les forces après le déplacement
        forceX= 0;
        forceY= 0;
    }
    

    void addForceToCreature(double forceX, double forceY) {
    	this.segments.get(0).getCreature().addUsedForce(forceX, forceY);
    }
    
    void update() {
    	for(Muscle muscle : this.muscles)
    		muscle.perform();
		
		/*System.out.println(String.format("NODE %03.0f;%03.0f;%03.0f       X: %f", 
                this.color.getRed(), 
                this.color.getGreen(), 
                this.color.getBlue(), 
                this.getX()));*/

    }


	Circle getShape() {
		return this.shape;
	}
    
	void setX(double x) {
		this.x = x;
        shape.setTranslateX(x);
	}

	void setY(double y) {
		this.y = y;
        shape.setTranslateY(y);
	}

    public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	int getNeighboorCount() {
		return this.segments.size();
	}
	
	public int getId() { 
		return this.id;
	}
	
	public Muscle getRandomMuscle() {
		if(this.muscles.size() != 0)
			return this.muscles.get((int) (Math.random() * muscles.size()));
		return null;
	}
	
	public int getMuscleCount() {
		return this.muscles.size();
	}
	public boolean deleteRandomMuscle() {
		if(this.muscles.size() == 0) return false;
		int muscleToRemoveIndex = (int) (Math.random() * muscles.size());
		this.muscles.get(muscleToRemoveIndex).delete();
        this.muscles.remove(muscleToRemoveIndex); // Supprime le muscle à l'index aléatoire
        return true; // Indique que la suppression a réussi
	}
	
	public void setColor(Color color) {
		this.shape.setFill(color);
	}
	
    // Override clone method to make a deep copy
    @Override
    public Node clone() {
        return new Node(this);
    }
	
	
	
}
