package models;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class Segment {
	private Node nodeLeft;
	private Node nodeRight;
	private Line line;
	
	public Segment(Node nodeLeft, Node nodeRight) {
		this.nodeLeft  = nodeLeft;
		this.nodeRight = nodeRight;
		this.nodeLeft.addSegment(this);
		this.nodeRight.addSegment(this);
		this.line = new Line(nodeLeft.getX(), nodeLeft.getY(), nodeRight.getX(), nodeRight.getY());
	}

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
}