package metier;

import java.util.ArrayList;
import java.util.List;

import enviro.SimulationCreature;
import javafx.scene.paint.Color;
import models.Creature;
import models.Node;
import models.Segment;

public class CreatureGenerator {

	private List<Creature> creatures;
	
	public CreatureGenerator() {
		this.creatures = new ArrayList<Creature>();
	}
	
	public List<Creature> generateCreatures() {
		for(int i=0; i<SimulationCreature.CREATURE_PER_ROUND/2;i++)
		{
			Creature creature = new Creature();
			if( !SimulationCreature.DEBUG_CREATURES)
			{
		        int numNodes =  SimulationCreature.MIN_NODES;/*(int) (Math.random() * (SimulationCreature.MAX_NODES - SimulationCreature.MIN_NODES + 1)) + SimulationCreature.MIN_NODES;*/
				Node[] nodes = new Node[numNodes];
				for(int j=0; j < nodes.length; j++) {
					if (j>0)
					{
						//int randomX = (int) ((int) (Math.random() * (SimulationCreature.WIDTH - nodes[j-1].getX() + 1)) + nodes[j-1].getX());
						//int randomY = (int) ((int) (Math.random() * (SimulationCreature.HEIGHT - 0 + 1)) );
						nodes[j] = new Node();
						creature.ajouterSegment(new Segment(creature, nodes[j-1], nodes[j]));
					}
					else
						nodes[j] = new Node();
					
				}
				
				//on a numNodes
				

			
				// le min de segment est égal au num de node-1
				// GENERATION ALEATOIRE DE SEGMENTS
				/*int numSegment =  0;
				while(numSegment!=0) {
					int leftNodeIndex = (int) (Math.random() * numNodes);
				    int rightNodeIndex = (int) (Math.random() * numNodes);
				    
				    Node nodeLeft = nodes[leftNodeIndex];
				    Node nodeRight = nodes[rightNodeIndex];
				    
				    // Vérifier que les nœuds sélectionnés sont différents
				    if (nodeLeft.getId() != nodeRight.getId()) {
				        Segment segment = new Segment(nodeLeft, nodeRight);
				        if (creature.ajouterSegment(segment))
				        	numSegment--;
				    } 
				}*/
			}
			
			this.creatures.add(creature);
		}
		return this.creatures;
		
	}
	
	
}
