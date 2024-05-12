package models;

import java.util.ArrayList;
import java.util.List;

import utils.SensContraction;



public class Muscle {
	private Node currentNode;
	private Node nodeLeft;
	private Node nodeRight;
    private int angleValue = 0;
    private SensContraction sensContraction = SensContraction.INTERIEUR;
    private List<ActionType> actions; 
	
	
    private InternalClock clock;

	public Muscle(Node currentNode, Node nodeLeft, Node nodeRight, List<ActionType> actions) {
		this.currentNode = currentNode;
		this.nodeLeft = nodeLeft;
		this.nodeRight = nodeRight;
		this.clock = new InternalClock(this);
		if(actions != null)
			this.actions = actions;
		else
			this.actions = new ArrayList<ActionType>();
	}
	
	int getActionsCount() {
		return this.actions.size();
	}
	
	public List<ActionType> getActions() {
		return this.actions;
	}
	
	void ajouterAction(ActionType action) {
		this.actions.add(action);
	}
	
	public void perform() {
		if(this.clock!=null) {
			this.clock.execute();		
			if(this.angleValue!=0)
				this.contractionMeduse();
		}
	}
	
	public ActionType getRandomActionType() {
        return this.actions.get((int) (Math.random() * this.actions.size()));
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
    
    void delete() {
    	this.clock = null;
    	this.actions = new ArrayList<ActionType>();
    }
	
    void contractionMeduse() {
    	//REVOIR TOUS CES CALCULS POUR QUILS MARCHENT DANS NIMPORTE QUEL SENS
    	//IL FAUT QUE CE SOIT CLAIR
    	//commencer par calculer le point imaginaire
        if (this.angleValue == 0) return;

        // Récupérer les nœuds gauche et droit
        
        // Calculer les coordonnées du point moyen entre nodeLeft et nodeRight
        double midX = (nodeLeft.getX() + nodeRight.getX()) / 2.0;
        double midY = (nodeLeft.getY() + nodeRight.getY()) / 2.0;
        
        //Calcul du point imaginaire, qui est la symétrie du node this par rapport au segment [nodeLeft;nodeRight]
        double imaginaryPointX = this.currentNode.getX() + 2*(Math.abs(midX-this.currentNode.getX()) * (midX < this.currentNode.getX() ? -1 : 1));
        double imaginaryPointY = this.currentNode.getY() + 2*(Math.abs(midY-this.currentNode.getY()) * (midY < this.currentNode.getY() ? -1 : 1));

        
        // Calculer le vecteur de force pour chaque nœud
        double forceXLeft = (imaginaryPointX - nodeLeft.getX()) * 0.05;
        double forceYLeft = (imaginaryPointY - nodeLeft.getY()) * 0.05;
        

        // Calculer le vecteur de force pour chaque nœud
        double forceXRight = (imaginaryPointX - nodeRight.getX()) * 0.05;
        double forceYRight = (imaginaryPointY - nodeRight.getY()) * 0.05;
        
        
        double deltaX = nodeLeft.getX() - nodeRight.getX();
        double deltaY = nodeLeft.getY() - nodeRight.getY();
        // Calculer l'angle entre les vecteurs


        
     // Calculer les différences en coordonnées X et Y entre nodeLeft et thisNode
        double deltaXLeft = this.currentNode.getX() - nodeLeft.getX();
        double deltaYLeft = this.currentNode.getY() - nodeLeft.getY();

        // Calculer les différences en coordonnées X et Y entre thisNode et nodeRight
        double deltaXRight = nodeRight.getX() - this.currentNode.getX();
        double deltaYRight = nodeRight.getY() - this.currentNode.getY();

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
        
        //LE PROBLEME EST QUE LA MEME FORCE EST APPLIQUEE AUX DEUX NOEUDS ALORS QU'ILS SONT à UNE DISTANCE DIFFERENTE
        
        //System.out.println("Objectif: " + this.angleValue + " | Valeur : " + angleDegrees);
        // Appliquer la force aux nœuds
		double moyenneForceX = (Math.abs(forceXLeft)+Math.abs(forceXRight))/2;
		double moyenneForceY = (forceYLeft+forceYRight)/2;
		double magnitudeAngle = angleDegrees/180*3;
        if (this.sensContraction == SensContraction.INTERIEUR)
        {
	    	if(angleDegrees>=this.angleValue)
	    	{
		        this.currentNode.applyForce(-forceXLeft*magnitudeAngle, -forceYLeft*magnitudeAngle);
	    		this.currentNode.applyForce(-forceXRight*magnitudeAngle, -forceYRight*magnitudeAngle);
		        nodeLeft.applyForce(forceXLeft, forceYLeft);
		        nodeRight.applyForce(forceXRight, forceYRight );
		        //this.applyForce(0, -(Math.abs(forceXLeft)+Math.abs(forceXRight)));
	    	}
	        //this.applyForce((-moyenneForceX)*angleDegrees/20, (-forceYLeft-forceYRight)*angleDegrees/20);
        } else {
        	if(angleDegrees<=this.angleValue)
        	{
    	        // Appliquer la force aux nœuds
		        this.currentNode.applyForce(forceXLeft*1/magnitudeAngle*0.1, forceYLeft*1/magnitudeAngle*0.1);
	    		this.currentNode.applyForce(forceXRight*0.2, forceYRight*0.2);
		        nodeRight.applyForce(-forceXRight, -forceYRight );
		        nodeLeft.applyForce(-forceXLeft, -forceYLeft);
    	        //this.applyForce(forceXLeft*2, forceYLeft*2);
    	        //this.applyForce(forceXRight*2, forceYRight*2);
    	        //this.applyForce(forceXLeft+forceXRight, +forceYLeft+forceYRight);
        	}
        }
    	
    }
}
