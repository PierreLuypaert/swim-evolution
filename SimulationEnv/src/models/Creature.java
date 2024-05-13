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

public class Creature implements Cloneable {

    //private List<Node> nodes;
    private List<Segment> segments;
    private Set<Node> addedNodes;

    public Creature() {
    	this.segments = new ArrayList<Segment>();
        this.addedNodes = new HashSet<>();
        if(SimulationCreature.DEBUG_CREATURES)
        {
    	
	    	Node nodeA = new Node(Color.RED, 100, 800);
	    	Node nodeB = new Node(Color.BLUE, 200, 600);
	    	Node nodeC = new Node(Color.GREEN, 300, 800);
	    	Node nodeD = new Node(Color.BLUEVIOLET, 200, 400);
	    	
	
	    	Node nodeE = new Node(Color.WHITE, 100, 600);
	    	Node nodeF = new Node(Color.CHOCOLATE, 300, 600);
	    	/*Node nodeG = new Node(Color.BLACK, 200, 400);
	    	Node nodeend = new Node(Color.BEIGE, 200, 900);*/
	    	
	
	    	Segment seg = new Segment(nodeA,nodeB);
	    	Segment seg2 = new Segment(nodeB,nodeC);
	    	Segment seg3 = new Segment(nodeB,nodeD);
	    	Segment seg4 = new Segment(nodeD,nodeE);
	    	Segment seg5 = new Segment(nodeD,nodeF);
	    	/*//Segment seg6 = new Segment(nodeD,nodeG);
	    	//Segment seg7 = new Segment(nodeB,nodeend);*/
	
	    	this.segments.add(seg);
	    	this.segments.add(seg2);
	    	this.segments.add(seg3);
	    	this.segments.add(seg4);
	    	this.segments.add(seg5);
	    	//this.segments.add(seg6);
	    	//this.segments.add(seg7);
        }
    }
    
    public Creature(Creature other) {
    	this.segments = other.segments;
        this.addedNodes = new HashSet<>();
    }
    
    public Object clone() throws CloneNotSupportedException 
    { 
        return super.clone(); 
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
            
            // Vérifier si le nœud gauche a deux voisins
            if (leftNode.getNeighboorCount() >= 2) {
                leftNode.ajouterActionToMuscle(0, new ActionType(0, 140));
                leftNode.ajouterActionToMuscle(0, new ActionType(30, 10));
                if(leftNode.getNeighboorCount()>=3)
                {
                    leftNode.ajouterActionToMuscle(1, new ActionType(60, 140));
                    leftNode.ajouterActionToMuscle(1, new ActionType(90,10));
                }
            	return;
            } else if (rightNode.getNeighboorCount() >= 2) {
                rightNode.ajouterActionToMuscle(0, new ActionType(0, 140));
                rightNode.ajouterActionToMuscle(0, new ActionType(30, 10));  
                if(rightNode.getNeighboorCount()>=3)
                {
                	rightNode.ajouterActionToMuscle(1, new ActionType(60, 140));
                	rightNode.ajouterActionToMuscle(1, new ActionType(90,10));
                }
                return;
            }
        }
    }
    
    public void mutation() {
        // LES MUTATIONS DOIVENT SE FAIRE SUR DES OBJETS COPIES AVANT LE LANCEMENT DU JEU
        // mutations possibles :
        // changement de la position d'un noeud
        // changement de la vitesse d'horloge
        // ajout ou suppression d'un ou plusieurs membres, mais plus rare
        // ajout d'un muscle

        boolean performed = false;
        while (!performed) {
            int probability = (int) (Math.random() * (100 - 1));
            if(addedNodes==null || addedNodes.isEmpty())
            {
            	for (Segment seg : this.segments) {
                    Node nodeLeft = seg.getNodeLeft();
                    Node nodeRight = seg.getNodeRight();

                    // Vérifier si le nœud gauche a déjà été ajouté
                    if (!addedNodes.contains(nodeLeft)) {
                        addedNodes.add(nodeLeft);
                    }

                    // Vérifier si le nœud droit a déjà été ajouté
                    if (!addedNodes.contains(nodeRight)) {
                        addedNodes.add(nodeRight);
                    }
                }
            }
            int indexRandom = (int) (Math.random() * addedNodes.size()); // Génère un index aléatoire
            Node[] nodesArray = addedNodes.toArray(new Node[0]); // Convertit HashSet en tableau
            Node randomNode = nodesArray[indexRandom];
            if (probability >= 0 && probability < 25) {
                // CHANGEMENT DE POSITION D'UN NOEUD;
                randomNode.setX(randomNode.getX() + Math.random() * 200 - 100);
                randomNode.setY(randomNode.getY() + Math.random() * 200 - 100);
                performed = true;
                System.out.println("Changement de position d'un nœud effectué.");
            } else if (probability >= 25 && probability < 55) {
                // CHANGEMENT DE TIMING OU D'ANGLE D'UN MUSCLE;
                Muscle randomMuscle = randomNode.getRandomMuscle();
                if (randomMuscle != null) {
                    ActionType action = randomMuscle.getRandomActionType();
                    if (probability % 2 == 0) {
                        // changement de la timestamp de l'action
                        int addedClockInterval = (int) ((int) (Math.random() * (61 - 30)) * SimulationCreature.CLOCK_INCREMENT);
                        if (action.getTime() + addedClockInterval >= 0 && action.getTime() + addedClockInterval <= SimulationCreature.TIME_CLOCK_CYCLE - 10) {
                            performed = true;
                            System.out.println("Changement de timing d'un muscle effectué (" + action.getTime() + " -> " +(int)(action.getTime()+addedClockInterval) + ").");
                            action.setTime(action.getTime() + addedClockInterval);
                        }
                    } else {
                        // changement de l'angle de l'action
                        int addedAngle = (int) ((int) (Math.random() * (61 - 30)));
                        if (action.getAngleValue() + addedAngle >= 10 && action.getAngleValue() + addedAngle <= 170) {
                            performed = true;
                            System.out.println("Changement d'angle d'un muscle effectué (" + action.getAngleValue() + " -> "+ (int)(action.getAngleValue()+addedAngle) + ").");
                            action.setAngleValue(action.getAngleValue() + addedAngle);
                        }
                    }
                }
            } else if (probability >= 55 && probability < 70) {
                // AJOUT D'UN MEMBRE
                Node newNode = new Node();
                this.segments.add(new Segment(newNode, randomNode));
                performed = true;
                System.out.println("Ajout d'un membre effectué.");
            } else {
                // AJOUT OU SUPPRESSION D'UN MUSCLE
                if (randomNode.getNeighboorCount() >= 2) {
                    if (probability % 3 == 0) {
                        // suppression d'un muscle
                        if (randomNode.deleteRandomMuscle())
                        {
                            performed = true;
                            System.out.println("Suppression d'un muscle effectuée.");
                        }
                    } else {
                        int actionTime1 = (int) (Math.random() * SimulationCreature.TIME_CLOCK_CYCLE);
                        int angleValue1 = (int) (Math.random() * 170);
                        
                        int actionTime2 = (int) (Math.random() * SimulationCreature.TIME_CLOCK_CYCLE);
                        int angleValue2 = (int) (Math.random() * 170);
                        if (probability < 87) {
                            // modification d'un muscle
                            Muscle randomMuscle = randomNode.getRandomMuscle();
                            if (randomMuscle != null) {
                                randomMuscle.ajouterAction(new ActionType(actionTime1, angleValue1));
                                performed = true;
                                System.out.println("Modification d'un muscle effectuée. Nouveau: " + actionTime1 + ";" + angleValue1);
                            }
                        } else {
                            // ajout d'un muscle
                            if (randomNode.getNeighboorCount() >= 2 && randomNode.getNeighboorCount() != randomNode.getMuscleCount()+1) {
                                randomNode.ajouterMuscle(new ActionType(actionTime1, angleValue1), new ActionType(actionTime2, angleValue2));
                                performed = true;
                                System.out.println("Ajout d'un muscle effectué.");
                            }
                        }
                    }

                }
            }
        }

    }


    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<>();
        this.addedNodes = new HashSet<>();
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
    	        //if (seg.getNodeLeft().getNeighboorCount() != 1)
    	        	seg.getNodeRight().applyForce(forceX, forceY);
    	        //if (seg.getNodeRight().getNeighboorCount() != 1)
    	        	seg.getNodeLeft().applyForce(-forceX, -forceY);
	        	
    	        	
    	    } else {
    	        // Les nœuds sont trop éloignés, appliquer une force intérieure
    	        double forceX = deltaX * lengthDiff *  1; // Force intérieure
    	        double forceY = deltaY * lengthDiff *  1; // Force intérieure
    	        //if (seg.getNodeRight().getNeighboorCount() != 1)
    	        	seg.getNodeLeft().applyForce(-forceX, -forceY);
    	        //if (seg.getNodeLeft().getNeighboorCount() != 1)
    	        	seg.getNodeRight().applyForce(forceX, forceY);
    	    }

        	//this.segments.get(0).getNodeRight().applyForce(0, 15);
        	//this.segments.get(1).getNodeRight().applyForce(0, 3);
    	    // Mettre à jour l'interface utilisateur
    	    seg.update();
    	}
    }
    
    /*private boolean isSegmentAlreadyExisting(Segment segment) {
    	for(Segment seg : this.segments) 
    	{
    		if( seg.equals(segment))
    			return true;
    	}
    	return false;
    }*/

}
