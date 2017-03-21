package examples.behavior.fitness;

public abstract class BehaviorFitnessFunction {
	
	protected double currentFitnessScore;
	
	public BehaviorFitnessFunction() {
		resetFitnessScorePerMap();
	}
	
	abstract public void applyNoMovementPenalty();
	
	abstract public void applyWalkIntoWallPenalty();
	
	abstract public void applyFoodConsumedBonus();
	
	abstract public void applyValidMovementBonus();
	
	abstract public void resetFitnessScorePerMap();
	
	public double getCurrentScore() {
		return this.currentFitnessScore;
	}

}
