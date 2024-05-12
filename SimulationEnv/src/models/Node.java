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
	
	
	
}
