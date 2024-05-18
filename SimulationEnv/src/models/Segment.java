package models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class Segment implements Cloneable{
	private Node nodeLeft;
	private Node nodeRight;
	private Line line;
	private Creature creature;
	
	public Segment(Creature creature, Node nodeLeft, Node nodeRight, Segment other) {
		this.creature = creature;
		this.nodeLeft  = nodeLeft;
		this.nodeRight = nodeRight;
		this.nodeLeft.addSegment(this);
		this.nodeRight.addSegment(this);
		this.line = new Line(nodeLeft.getX(), nodeLeft.getY(), nodeRight.getX(), nodeRight.getY());
		this.line.setStroke(Color.WHITE);
        this.line.setStrokeWidth(1.6);
        if(other!=null) {
            this.nodeLeft.restoreMuscle(other.getNodeLeft());
            this.nodeRight.restoreMuscle(other.getNodeRight());
        }

	}
	
	public Segment(Creature creature, Node nodeLeft, Node nodeRight) {
		this(creature, nodeLeft, nodeRight, null);

	}
	
    // Copy constructor
    /*public Segment(Segment other) {
    	//NON, LE NOEUD A PEUT ETRE DEJA ETE CREE
        this.nodeLeft = new Node(other.nodeLeft); // Assuming Node class has a copy constructor
        this.nodeRight = new Node(other.nodeRight); // Assuming Node class has a copy constructor
		this.nodeLeft.addSegment(this);
		this.nodeRight.addSegment(this);
		this.nodeLeft.restoreMuscle(other.nodeLeft);
		this.nodeRight.restoreMuscle(other.nodeRight);
        // Note: You might need to update the Node constructor if it has other dependencies
        this.line = new Line(other.line.getStartX(), other.line.getStartY(), other.line.getEndX(), other.line.getEndY());
        this.line.setStroke(other.line.getStroke());
        this.line.setStrokeWidth(other.line.getStrokeWidth());
        // Assuming Creature is immutable or does not need to be cloned
        this.creature = other.creature;
    }*/

    Node getNodeLeft() {
        /*if (nodeLeft.getX() > nodeRight.getX()) {
            return nodeLeft;
        } else {
            return nodeRight;
        }*/
    	return this.nodeLeft;
    }

    // Méthode pour retourner le nœud le plus à droite
    Node getNodeRight() {
        /*if (nodeLeft.getX() > nodeRight.getX()) {
            return nodeRight;
        } else {
            return nodeLeft;
        }*/
    	return this.nodeRight;
    }
	
	double getLength() {
	    double deltaX = nodeRight.getX() - nodeLeft.getX();
	    double deltaY = nodeRight.getY() - nodeLeft.getY();
	    return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

	void update() {
		this.line.setStartX(nodeLeft.getX());
		this.line.setStartY(nodeLeft.getY());
		this.line.setEndX(nodeRight.getX());
		this.line.setEndY(nodeRight.getY());
	}
	
	Shape getShape() { return this.line; }
	
	@Override
	public boolean equals(Object seg) {
	 
	    // If the object is compared with itself then return true  
	    if (seg == this) {
	        return true;
	    }
	    
	    // Check if the object is null or not an instance of Segment
	    if (seg == null || getClass() != seg.getClass()) {
	        return false;
	    }
	    
	    Segment segment = (Segment) seg;

	    // Check if the nodes are exactly the same
	    boolean exactlySame = (this.getNodeLeft().getId() == segment.getNodeLeft().getId() && 
	                           this.getNodeRight().getId() == segment.getNodeRight().getId());
	    
	    // Check if nodes are swapped
	    boolean nodesSwapped = (this.getNodeLeft().getId() == segment.getNodeRight().getId() && 
	                            this.getNodeRight().getId() == segment.getNodeLeft().getId());
	    
	    // Check if only the left node is swapped
	    boolean leftNodeSwapped = (this.getNodeLeft().getId() == segment.getNodeRight().getId() && 
	                               this.getNodeRight().getId() == segment.getNodeLeft().getId());
	    
	    // Check if only the right node is swapped
	    boolean rightNodeSwapped = (this.getNodeLeft().getId() == segment.getNodeLeft().getId() && 
	                                this.getNodeRight().getId() == segment.getNodeRight().getId());
	    
	    // Check if either nodes are swapped or if only one node is swapped
	    return exactlySame || nodesSwapped || leftNodeSwapped || rightNodeSwapped;
	}

	Creature getCreature() {
		return this.creature;
	}

}
