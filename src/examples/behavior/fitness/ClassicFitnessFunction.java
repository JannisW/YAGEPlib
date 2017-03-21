package examples.behavior.fitness;

public class ClassicFitnessFunction extends BehaviorFitnessFunction {
	
	@Override
	public void applyNoMovementPenalty() {
		// do nothing
	}
	
	@Override
	public void resetFitnessScorePerMap() {
		this.currentFitnessScore = 0.0;
	}

	@Override
	public void applyWalkIntoWallPenalty() {
		// do nothing
	}

	@Override
	public void applyFoodConsumedBonus() {
		this.currentFitnessScore += 1.0;
		
	}

	@Override
	public void applyValidMovementBonus() {
		// do nothing
	}

}
