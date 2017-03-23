/*
 * Copyright 2017 Johannes Wortmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.behavior.world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;

public class WorldMap {

	private Field[][] grid;

	private ArrayList<Field> foodFields;

	private HashSet<Field> markedFields;
	// TODO instead of hashset array of references to list elemeents could
	// achieve guaranteed O(1) performance instead of just average

	private int startX;
	private int startY;
	private Orientation startOrientation;

	public WorldMap(Path pathToFile) throws IOException {
		readFromFile(pathToFile);
		printCurrentMapState();
	}

	public WorldMap(final int dimX, final int dimY) {

		grid = new Field[dimX][dimY];

		// build outer walls
		for (int x = 0; x < dimX; x++) {
			grid[x][0] = new Field(Field.WALL_MASK);
			grid[x][dimY - 1] = new Field(Field.WALL_MASK);
		}
		for (int y = 0; y < dimY; y++) {
			grid[0][y] = new Field(Field.WALL_MASK);
			grid[dimX - 1][y] = new Field(Field.WALL_MASK);
		}

		// build inner fields (all empty first)
		for (int x = 1; x < dimX; x++) {
			for (int y = 1; y < dimY; y++) {
				grid[x][y] = new Field();
			}
		}

		foodFields = new ArrayList<Field>();
		markedFields = new HashSet<Field>();

		startX = dimX / 2;
		startY = dimY / 2;
		startOrientation = Orientation.EAST;
	}

	public void addFood(int x, int y) {
		Field f = grid[x][y];
		f.setFood();
		foodFields.add(f);
	}

	public int getStartPositionX() {
		return startX;
	}

	public int getStartPositionY() {
		return startY;
	}

	public void setStartPosition(int startX, int startY, Orientation startOrientation) {
		this.startX = startX;
		this.startY = startY;
		this.startOrientation = startOrientation;
	}

	public Orientation getStartOrientation() {
		return startOrientation;
	}

	public Field[][] initMap() {

		// redistribute food
		for (Field field : foodFields) {
			field.setFood();
		}

		// reset marker
		for (Field field : markedFields) {
			field.removeMarker();
		}
		markedFields.clear();

		// TODO redistribute other stuff

		return grid;
	}

	/**
	 * Returns the amount of food that is on the map.
	 * 
	 * @return the amount of food on the map
	 */
	public int getFoodAmount() {
		return foodFields.size();
	}

	/**
	 * Sets the marker flag of the field where the agent stays to the provided
	 * boolean value.
	 * 
	 * @param marked
	 *            true if marker should be set, false if it should be removed.
	 */
	public void setMarkerOnCurrentPosition(int x, int y, boolean marked) {
		if (marked) {
			grid[x][y].setMarker();
			markedFields.add(grid[x][y]);
		} else {
			grid[x][y].removeMarker();
			markedFields.remove(grid[x][y]);
		}
	}

	private void readFromFile(Path pathToFile) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(pathToFile.toFile()))) {

			int numLinesRead = 0;

			// TODO add checks (for null after every read) and exceptions for
			// format violation (check if map valid: walls around etc.)

			// read header
			String line;
			do {
				// skip comments before header
				line = normalizeLine(br.readLine());
				numLinesRead++;
			} while (line.isEmpty());
			// TODO allow arbitrary amount of line comments also later

			// parse field dimensions
			String[] parts = line.split(",");
			final int dimX = Integer.parseInt(parts[0].trim());
			final int dimY = Integer.parseInt(parts[1].trim());
			grid = new Field[dimX][dimY];

			// parse start position
			line = normalizeLine(br.readLine());
			numLinesRead++;
			parts = line.split(",");
			startX = Integer.parseInt(parts[0].trim());
			startY = Integer.parseInt(parts[1].trim());

			// parse start orientation
			line = normalizeLine(br.readLine());
			numLinesRead++;
			startOrientation = Orientation.fromString(line);

			// parse amount of food on map
			line = normalizeLine(br.readLine());
			numLinesRead++;
			int amountOfFoodOnMap = Integer.parseInt(line);
			foodFields = new ArrayList<Field>(amountOfFoodOnMap);

			do {
				// skip lines between header and actual map
				line = normalizeLine(br.readLine());
				numLinesRead++;
			} while (line.isEmpty());

			// parse map
			int y = dimY - 1;
			while (line != null) {

				if (y < 0) {
					throw new IllegalArgumentException(
							"File format violation: to many entries in y direction of the map (expected" + dimY
									+ " ).");
				}

				String tokens[] = line.split(" ");

				if (tokens.length != dimX) {
					throw new IllegalArgumentException("File format violation: line " + numLinesRead
							+ " does not have the correct length (expected" + dimX + " ).");
				}

				for (int x = 0; x < dimX; x++) {
					int properties;
					if (tokens[x].equals("#")) {
						properties = Field.WALL_MASK;
					} else {
						properties = Integer.parseInt(tokens[x]);
					}
					Field f = new Field(properties);
					grid[x][y] = f;
					if (f.isFood()) {
						foodFields.add(f);
					}
				}

				y--;

				do {
					// not the most efficient solution but unless reading large
					// files negligible.
					line = br.readLine();
					if (line == null) {
						break;
					}
					line = normalizeLine(line);
					numLinesRead++;
				} while (line.isEmpty());

			}
			
			markedFields = new HashSet<>();
		}
	}

	private static String normalizeLine(String line) {
		if (line == null) {
			throw new IllegalArgumentException("File format violation: missing line ");
			// TODO better error handling...
		}
		int commentMarker = line.indexOf('%');
		if (commentMarker != -1) {
			line = line.substring(0, commentMarker);
		}
		return line.trim();
	}

	public void printCurrentMapState() {
		printCurrentMapState(startX, startY, startOrientation);
	}

	public void printCurrentMapState(int agentX, int agentY, Orientation agentOrientation) {
		System.out.println();
		for (int y = grid[0].length - 1; y >= 0; y--) {
			System.out.print(y + "\t");
			for (int x = 0; x < grid.length; x++) {
				String fieldStr;
				if (grid[x][y].isWall()) {
					fieldStr = "# ";
				} else if (x == agentX && agentY == y) {
					fieldStr = getAgentChar(agentOrientation) + " ";
				} else if (grid[x][y].isEmpty()) {
					fieldStr = "  ";
				} else {
					fieldStr = grid[x][y].getProperties() + " ";
				}
				System.out.print(fieldStr);
			}
			System.out.println();
		}
		System.out.println();
	}

	private static char getAgentChar(final Orientation orientation) {
		switch (orientation) {
		case NORTH:
			return '^';
		case EAST:
			return '>';
		case SOUTH:
			return 'V';
		case WEST:
			return '<';
		}
		throw new IllegalArgumentException("invalid orientation");
	}

}
