package models;

import java.util.ArrayList;
import java.util.List;

import enviro.SimulationCreature;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Node {
	public static int ID_NODE = 0;
	private int id;
	private List<Segment> segments;    
    private static final double SIZE = 10.0;
    private Circle shape;
    private double x, y;
    private double forceX;
    private double forceY;
    private Color color;
    private List<Muscle> muscles;
    

	public Node(Color color, int x, int y) {
		this.id = Node.ID_NODE++;
		this.color=color;
		this.segments = new ArrayList<Segment>();
        this.x = x;
        this.y = y;
        this.muscles = new ArrayList<Muscle>();
        
		shape = new Circle(SIZE);
        shape.setFill(color);
        shape.setTranslateX(x);
        shape.setTranslateY(y);
	}
	

	
	public Node(Color color) {
        this(color, (int) Math.round(Math.random() * SimulationCreature.WIDTH), 
                  (int) Math.round(Math.random() * SimulationCreature.HEIGHT));
    }
	public Node() {
		this(Color.BLUE);
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
	
	void ajouterAction(int indexMuscle, ActionType action) {
		Muscle muscle;
		if (indexMuscle >= this.muscles.size())
		{
			muscle = new Muscle(this, this.segments.get(0).getNodeLeft(),this.segments.get(1).getNodeRight(), null);
			this.muscles.add(muscle);
		}
		else
			muscle = this.muscles.get(indexMuscle);
		
		muscle.ajouterAction(action);
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
        double deltaTime = InternalClock.CLOCK_INCREMENT; // Pas de temps pour la simulation (à ajuster selon vos besoins)
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
	
	
	
}
