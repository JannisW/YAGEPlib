package examples.behavior;

import gep.FitnessEnvironment;
import gep.model.Individual;

public class EvaluationEnvironment extends FitnessEnvironment {

	private int posAgentX;
	private int posAgentY;
	public Orientation agentOrientation;

	// TODO change 2d array to 1d array
	public final Field[][] grid;

	public class Field {
		private static final int WALL_MASK = 0x01;
		private static final int FOOD_MASK = 0x02;

		private int properties;

		public boolean isEmpty() {
			return properties == 0;
		}

		public boolean isWall() {
			return (properties & WALL_MASK) != 0;
		}

		public boolean isFood() {
			return (properties & FOOD_MASK) != 0;
		}
	}

	public enum Orientation {
		NORTH, EAST, SOUTH, WEST;
	}

	public EvaluationEnvironment(int dimX, int dimY) {
		this.grid = new Field[dimX][dimY];
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
		if (grid[x][y].isWall()) {
			return false;
		}
		posAgentX = x;
		posAgentY = y;
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
	protected double evaluateFitness(Individual individual) {
		// TODO Auto-generated method stub
		
		// check if that makes sense... especially wrt. parallelization etc.
		return 0;
	}

}
