package models;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;


public class InternalClock {
    private static final int TIME_CLOCK_CYCLE = 120;
    public static final double CLOCK_INCREMENT = 0.2;
    private double currentTime = 0;
    private int nextIndexExecution=0;
	private List<ActionType> actions;
	private Muscle muscle;
	
	public InternalClock(Muscle muscle) {
		this.actions = new ArrayList<ActionType>();
		this.muscle = muscle;
	}
	
	void execute() {
		updateActions();
		if (this.actions.size() > 0)
		{		
			if (Utils.compareDoubles(this.actions.get(this.nextIndexExecution).getTime(),this.currentTime,0.001))
			{
				//System.out.println("Envoi de l'ordre pour le noeud " + this.node.getId());
				this.muscle.mouvementMeduse(this.actions.get(this.nextIndexExecution).getAngleValue());
				if(this.nextIndexExecution+1 < this.actions.size())
					this.nextIndexExecution++;
				else
					this.nextIndexExecution=0;
			}
			
			
			this.currentTime += InternalClock.CLOCK_INCREMENT;
			if (this.currentTime >= InternalClock.TIME_CLOCK_CYCLE)
				this.currentTime=0;
		}
	}
	
	private void updateActions() {
		if ( this.muscle.getActionsCount() != this.actions.size())
			this.actions = this.muscle.getActions();
	}
	
}
