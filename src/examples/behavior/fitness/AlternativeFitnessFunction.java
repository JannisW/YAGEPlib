package examples.behavior.fitness;

public class AlternativeFitnessFunction extends BehaviorFitnessFunction {
	
	protected final static double START_FITNESS = 100.0;
	
	@Override
	public void applyNoMovementPenalty() {
		currentFitnessScore = 0.0;
	}
	
	@Override
	public void resetFitnessScorePerMap() {
		this.currentFitnessScore = START_FITNESS;
	}

	@Override
	public void applyWalkIntoWallPenalty() {
		currentFitnessScore = Math.max(0, currentFitnessScore - 2);
	}

	@Override
	public void applyFoodConsumedBonus() {
		currentFitnessScore += 22.0;
	}

	@Override
	public void applyValidMovementBonus() {
		currentFitnessScore += 1.0;
	}

}
