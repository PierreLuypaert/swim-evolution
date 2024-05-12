package models;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.List;

import enviro.SimulationCreature;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class Creature {

    //private List<Node> nodes;
    private List<Segment> segments;
    private Set<Node> addedNodes;

    public Creature() {
    	this.segments = new ArrayList<Segment>();
        this.addedNodes = new HashSet<>();
    	
    	/*Node nodeA = new Node(Color.RED, 100, 800);
    	Node nodeB = new Node(Color.BLUE, 200, 600);
    	Node nodeC = new Node(Color.GREEN, 300, 800);
    	Node nodeD = new Node(Color.BLUEVIOLET, 200, 500);
    	

    	Node nodeE = new Node(Color.WHITE, 100, 700);
    	Node nodeF = new Node(Color.CHOCOLATE, 300, 700);
    	Node nodeG = new Node(Color.BLACK, 200, 400);
    	Node nodeend = new Node(Color.BEIGE, 200, 900);
    	

    	Segment seg = new Segment(nodeA,nodeB);
    	Segment seg2 = new Segment(nodeB,nodeC);
    	Segment seg4 = new Segment(nodeE,nodeD);
    	Segment seg5 = new Segment(nodeD,nodeF);
    	Segment seg3 = new Segment(nodeB,nodeD);
    	//Segment seg6 = new Segment(nodeD,nodeG);
    	//Segment seg7 = new Segment(nodeB,nodeend);

    	this.segments.add(seg);
    	this.segments.add(seg2);*/
    	//this.segments.add(seg4);
    	//this.segments.add(seg5);
    	//this.segments.add(seg3);
    	//this.segments.add(seg6);
    	//this.segments.add(seg7);
    }
    
    public boolean ajouterSegment(Segment segment) {
    	if(!this.segments.contains(segment))
    	{
    		this.segments.add(segment);
    		return true;
    	}
    	return false;
    }
    
    public void ajouterAction() {
        for (Segment segment : segments) {
            Node leftNode = segment.getNodeLeft();
            Node rightNode = segment.getNodeRight();
            
            // Vérifier si le nœud gauche a exactement deux voisins
            if (leftNode.getNeighboorCount() >= 2) {
                leftNode.ajouterAction(new ActionType(0, 140));
                leftNode.ajouterAction(new ActionType(30, 10));
            	return;
            } else if (rightNode.getNeighboorCount() >= 2) {
                rightNode.ajouterAction(new ActionType(0, 140));
                rightNode.ajouterAction(new ActionType(30, 10));  
                return;
            }
    
            
            
            // Vérifier si le nœud droit a exactement deux voisins
            /*}*/
        }
    }

    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<>();
        for (Segment seg : this.segments) {
            Node nodeLeft = seg.getNodeLeft();
            Node nodeRight = seg.getNodeRight();
            shapes.add(seg.getShape());

            // Vérifier si le nœud gauche a déjà été ajouté
            if (!addedNodes.contains(nodeLeft)) {
                shapes.add(nodeLeft.getShape());
                addedNodes.add(nodeLeft);
            }

            // Vérifier si le nœud droit a déjà été ajouté
            if (!addedNodes.contains(nodeRight)) {
                shapes.add(nodeRight.getShape());
                addedNodes.add(nodeRight);
            }
        }
        return shapes;
    }

    
    public void update() {
    	//appliquer un mouvement
    	//mettre à jour l'UI
    	
    	//rapprocher les noeuds pour qu'ils respectent la taille des segments
    	this.rapprocherNoeuds();
    	
    	this.updateNodes();
    	
    	//mettre à jour l'UI 
    }    
    
    private void updateNodes() {
    	for(Node node: this.addedNodes) 
    		node.update();
    }
    
    
    private void rapprocherNoeuds() {
	    double desiredLength = 100; // Exemple de longueur souhaitée

    	for (Segment seg : this.segments) {
    		
    		//un noeud ne se fait pas attirer par son voisin si ce voisin n'a pas d'autre voisin
    	    // Longueur souhaitée du segment

    	    // Calculer la longueur actuelle du segment
    	    double currentLength = seg.getLength();

    	    // Calculer la différence entre la longueur souhaitée et la longueur actuelle
    	    double lengthDiff = desiredLength - currentLength;

    	    // Déterminer la direction du mouvement (unité)
    	    double deltaX = (seg.getNodeRight().getX() - seg.getNodeLeft().getX()) / currentLength;
    	    double deltaY = (seg.getNodeRight().getY() - seg.getNodeLeft().getY()) / currentLength;

    	    // Appliquer des forces en fonction de la différence de longueur
    	    if (lengthDiff > 0) {
    	        // Les nœuds sont trop proches, appliquer une force extérieure
    	        double forceX = deltaX * lengthDiff * 1; // Force extérieure
    	        double forceY = deltaY * lengthDiff *  1; // Force extérieure
    	        if (seg.getNodeLeft().getNeighboorCount() != 1)
    	        	seg.getNodeRight().applyForce(forceX, forceY);
    	        if (seg.getNodeRight().getNeighboorCount() != 1)
    	        	seg.getNodeLeft().applyForce(-forceX, -forceY);
	        	
    	        	
    	    } else {
    	        // Les nœuds sont trop éloignés, appliquer une force intérieure
    	        double forceX = deltaX * lengthDiff *  1; // Force intérieure
    	        double forceY = deltaY * lengthDiff *  1; // Force intérieure
    	        if (seg.getNodeRight().getNeighboorCount() != 1)
    	        	seg.getNodeLeft().applyForce(-forceX, -forceY);
    	        if (seg.getNodeLeft().getNeighboorCount() != 1)
    	        	seg.getNodeRight().applyForce(forceX, forceY);
    	    }

        	//this.segments.get(0).getNodeRight().applyForce(0, 15);
        	//this.segments.get(1).getNodeRight().applyForce(0, 3);
    	    // Mettre à jour l'interface utilisateur
    	    seg.update();
    	}
    }
    
    private boolean isSegmentAlreadyExisting(Segment segment) {
    	for(Segment seg : this.segments) 
    	{
    		if( seg.equals(segment))
    			return true;
    	}
    	return false;
    }

}
