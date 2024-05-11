package metier;

import java.util.ArrayList;
import java.util.List;

import enviro.SimulationCreature;
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
	        int numNodes = (int) (Math.random() * (SimulationCreature.MAX_NODES - SimulationCreature.MIN_NODES + 1)) + SimulationCreature.MIN_NODES;
			Node[] nodes = new Node[numNodes];
			for(int j=0; j < nodes.length; j++) {
				nodes[j] = new Node();
			}
			
			//faire des segments aléatoires
			Segment[] segments = new Segment[numNodes-1];
			for(int k=0; k < segments.length ; k++) 
			{
				segments[k] = new Segment(nodes[k], nodes[k+1]);
				creature.ajouterSegment(segments[k]);
			}
			this.creatures.add(creature);
		}
		return this.creatures;
		
	}
	
	
}
