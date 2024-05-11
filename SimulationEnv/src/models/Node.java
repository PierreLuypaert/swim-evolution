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
	private static final double MAX_SPEED = 3.0;
    private static final double SIZE = 10.0;
    private Circle shape;
    private Color color;
    private double x, y;
	
    private double forceX;
    private double forceY;
    
    private int angleValue = 0;
    
    private InternalClock clock;
    
    private SensContraction sensContraction = SensContraction.INTERIEUR;
    
    private enum SensContraction {
        INTERIEUR,
        EXTERIEUR
    }
	
	public Node(Color color, int x, int y) {
		this.id = Node.ID_NODE++;
		this.color=color;
		this.segments = new ArrayList<Segment>();
		shape = new Circle(SIZE);
        shape.setFill(color);
        this.x = x;
        this.y = y;
        shape.setTranslateX(x);
        shape.setTranslateY(y);
	}
	

	
	public Node(Color color) {
        this(color, (int) Math.round(Math.random() * SimulationCreature.WIDTH), 
                  (int) Math.round(Math.random() * SimulationCreature.HEIGHT));
    }
	public Node() {
		this(Color.BLUE, 0,0);
	}
	
	void addSegment(Segment segment) {
		this.segments.add(segment);
		if (this.segments.size()==2 && this.clock==null)
			this.clock = new InternalClock(this);
	}
	
	void ajouterAction(ActionType action) {
		if (this.clock!=null)
			this.clock.ajouterAction(action);
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
    
    void contractionMeduse() {
        if (this.angleValue == 0) return;

        //long startTime = System.nanoTime();
        // Récupérer les nœuds gauche et droit
        
        Node nodeLeft = this.segments.get(0).getNodeLeft();
        Node nodeRight = this.segments.get(1).getNodeRight();
        

        // Obtenir le temps de fin
        /*long endTime = System.nanoTime();

        // Calculer le temps écoulé
        long elapsedTime = endTime - startTime;

        // Afficher le temps écoulé
        System.out.println("Temps écoulé : " + elapsedTime + " millisecondes");*/

        // Calculer les coordonnées du point moyen entre nodeLeft et nodeRight
        double midX = (nodeLeft.getX() + nodeRight.getX()) / 2.0;
        double midY = (nodeLeft.getY() + nodeRight.getY()) / 2.0;

        // Calculer les coordonnées du vecteur perpendiculaire
        double vecX = nodeRight.getX() - nodeLeft.getX();
        double vecY = nodeRight.getY() - nodeLeft.getY();
        double perpVecX = -vecY;
        double perpVecY = vecX;

        // Normaliser le vecteur perpendiculaire
        double norm = Math.sqrt(perpVecX * perpVecX + perpVecY * perpVecY);
        perpVecX /= norm;
        perpVecY /= norm;

        // Calculer les coordonnées du point imaginaire
        double imaginaryPointX = midX + perpVecX * norm;
        double imaginaryPointY = midY + perpVecY * norm;

        // Calculer le vecteur de force pour chaque nœud
        double forceXLeft = (imaginaryPointX - nodeLeft.getX()) * 0.05;
        double forceYLeft = (imaginaryPointY - nodeLeft.getY()) * 0.05;
        

        // Calculer le vecteur de force pour chaque nœud
        double forceXRight = (imaginaryPointX - nodeRight.getX()) * 0.05;
        double forceYRight = (imaginaryPointY - nodeRight.getY()) * 0.05;
        
        
        double deltaX = nodeLeft.getX() - nodeRight.getX();
        double deltaY = nodeLeft.getY() - nodeRight.getY();
        // Calculer l'angle entre les vecteurs

        double forceX = deltaX * 0.1; // Calculer la force en X
        double forceY = deltaY * 0.1; // Calculer la force en Y

        
     // Calculer les différences en coordonnées X et Y entre nodeLeft et thisNode
        double deltaXLeft = this.getX() - nodeLeft.getX();
        double deltaYLeft = this.getY() - nodeLeft.getY();

        // Calculer les différences en coordonnées X et Y entre thisNode et nodeRight
        double deltaXRight = nodeRight.getX() - this.getX();
        double deltaYRight = nodeRight.getY() - this.getY();

        // Calculer la distance euclidienne entre thisNode et nodeRight
        double distanceRight = Math.sqrt(deltaXRight * deltaXRight + deltaYRight * deltaYRight);
        
     // Calculer les produits scalaires des vecteurs
        double dotProduct = deltaXLeft * deltaXRight + deltaYLeft * deltaYRight;

        // Calculer la norme des vecteurs
        double normLeft = Math.sqrt(deltaXLeft * deltaXLeft + deltaYLeft * deltaYLeft);
        double normRight = Math.sqrt(deltaXRight * deltaXRight + deltaYRight * deltaYRight);

        // Calculer le cosinus de l'angle entre les vecteurs
        double cosAngle = dotProduct / (normLeft * normRight);

        // Calculer l'angle en radians
        double angleRadians = Math.acos(cosAngle);

        // Convertir l'angle en degrés
        double angleDegrees = 180-Math.toDegrees(angleRadians);
        
        //System.out.println("Objectif: " + this.angleValue + " | Valeur : " + angleDegrees);
        // Appliquer la force aux nœuds
		double moyenneForceX = (Math.abs(forceXLeft)+Math.abs(forceXRight))/2;
		double moyenneForceY = (forceYLeft+forceYRight)/2;
        if (this.sensContraction == SensContraction.INTERIEUR)
        {
	    	if(angleDegrees>=this.angleValue)
	    	{

		        this.applyForce(moyenneForceX, -moyenneForceY);
		        this.applyForce(-moyenneForceX, -moyenneForceY);
		        nodeRight.applyForce(-moyenneForceX, moyenneForceY);
		        nodeLeft.applyForce(moyenneForceX, moyenneForceY);
		        //this.applyForce(0, -(Math.abs(forceXLeft)+Math.abs(forceXRight)));
	    	}
	        //this.applyForce((-(Math.abs(forceXLeft)+Math.abs(forceXRight))/2)*180/angleDegrees/10, (-forceYLeft-forceYRight)*180/angleDegrees/10);
        } else {
        	if(angleDegrees<=this.angleValue)
        	{
    	        // Appliquer la force aux nœuds
		        this.applyForce(-moyenneForceX, moyenneForceY);
		        this.applyForce(moyenneForceX, moyenneForceY);
    	        nodeRight.applyForce(moyenneForceX, -moyenneForceY);
    	        nodeLeft.applyForce(-moyenneForceX, -moyenneForceY);
    	        //this.applyForce(forceXLeft*2, forceYLeft*2);
    	        //this.applyForce(forceXRight*2, forceYRight*2);
    	        //this.applyForce(forceXLeft+forceXRight, +forceYLeft+forceYRight);
        	}
        }
    	
    }



    public void mouvementMeduse(int angleValue) {
    	if (angleValue > this.angleValue)
    	{	
    		this.sensContraction = SensContraction.EXTERIEUR;
    	} else {
    		this.sensContraction = SensContraction.INTERIEUR;
    	}
    	//System.out.println("Noeud " + this.id + " CONTRACTION " + this.angleValue);
    	this.angleValue = angleValue;
    }
    
    void update() {
    	if (this.clock!=null) 
    		this.clock.execute();
    	
		if (this.angleValue!=0)
	    	this.contractionMeduse();
		
		System.out.println(String.format("NODE %03.0f;%03.0f;%03.0f       X: %f", 
                this.color.getRed(), 
                this.color.getGreen(), 
                this.color.getBlue(), 
                this.getX()));

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

    double getX() {
		return x;
	}

	double getY() {
		return y;
	}
	
	int getNeighboorCount() {
		return this.segments.size();
	}
	
	int getId() { 
		return this.id;
	}
	
	
	
}
