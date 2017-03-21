package examples.behavior;

import java.util.ArrayList;

import examples.behavior.world.Field;
import examples.behavior.world.Orientation;
import examples.behavior.world.WorldMap;
import gep.FitnessEnvironment;
import gep.model.ExpressionTreeNode;
import gep.model.Individual;

public class EvaluationEnvironment extends FitnessEnvironment<Boolean> {

	final static int MAX_NUMBER_OF_SIMULATION_TICKS = 40;
	final static double START_FITNESS = 100.0;

	private int posAgentX;
	private int posAgentY;
	public Orientation agentOrientation;

	// TODO maybe change 2d array to 1d array
	public Field[][] grid;

	private int foodConsumed = 0;

	// start with a budget of possible wrong moves.
	private double currentFitnessScore = START_FITNESS;

	// the different maps (fitness cases) for generalization
	private final WorldMap[] maps;

	public EvaluationEnvironment(ArrayList<WorldMap> maps) {
		this.maps = maps.toArray(new WorldMap[maps.size()]);
	}

	/**
	 * Moves the agent to the given position if possible.
	 * 
	 * As the field is surrounded by walls and step size is always one it is
	 * sufficient to check if the target field is not a wall. If these
	 * preconditions are not guaranteed by the caller. This function may not
	 * work as intended.
	 * 
	 * @param x
	 *            The new x position of the agent
	 * @param y
	 *            The new y position of the agent
	 * @return true, if the move was successful, false otherwise.
	 */
	public boolean moveTo(int x, int y) {
		//System.out.println("CALLED");
		if (grid[x][y].isWall()) {
			// give penalty for actually try to move on a field with a wall.
			currentFitnessScore = Math.max(0, currentFitnessScore - 2);
			return false;
		}
		posAgentX = x;
		posAgentY = y;
		if (grid[x][y].isFood()) {
			grid[x][y].removeFood();
			foodConsumed++;
			currentFitnessScore += 22;
		} else {
			// every valid step gives a plus point (makes the agent move)
			currentFitnessScore++;
		}
		return true;
	}

	/**
	 * Returns the x position of the agent
	 * 
	 * @return the x position of the agent
	 */
	public int getPosAgentX() {
		return posAgentX;
	}

	/**
	 * Returns the y position of the agent
	 * 
	 * @return the y position of the agent
	 */
	public int getPosAgentY() {
		return posAgentY;
	}

	/**
	 * Returns the field in front of the agent. Assumes that the field is
	 * surrounded by walls and that the agent is never located there.
	 * 
	 * @return the field in front of the agent
	 */
	public Field getFieldInFront() {
		switch (agentOrientation) {
		case NORTH:
			return grid[posAgentX][posAgentY + 1];
		case EAST:
			return grid[posAgentX + 1][posAgentY];
		case SOUTH:
			return grid[posAgentX][posAgentY - 1];
		case WEST:
			return grid[posAgentX - 1][posAgentY];
		}

		return null; // never be reached
	}

	@Override
	protected double evaluateFitness(Individual<Boolean> individual) {

		resetFitnessScore();

		for (WorldMap map : maps) {

			grid = map.initMap();
			posAgentX = map.getStartPositionX();
			posAgentY = map.getStartPositionY();
			agentOrientation = map.getStartOrientation();

			// single chromosome individuals (only one program)
			ExpressionTreeNode<Boolean> currentProgram = individual.getExpressionTrees().get(0);

			int numberOfTicks = 0;
			while (numberOfTicks < MAX_NUMBER_OF_SIMULATION_TICKS) { // TODO and
																		// food
																		// is
																		// left

				boolean executionResult = currentProgram.execute();

				if (!executionResult) {
					// return currentFitnessScore-1; // TODO makes sense?
				}

				numberOfTicks++; // TODO maybe also couple to number of steps
			}

		}
		
		System.out.println("SCORE: " + currentFitnessScore);

		return this.currentFitnessScore;

		// TODO check if that makes sense... especially wrt. parallelization
		// etc.
	}
	
	private void resetFitnessScore() {
		this.currentFitnessScore = START_FITNESS;
	}

}
