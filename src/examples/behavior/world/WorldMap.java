package examples.behavior.world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import examples.behavior.world.WorldMap;

public class WorldMap {

	private Field[][] grid;

	private ArrayList<Field> foodFields;

	private int startX;
	private int startY;
	private Orientation startOrientation;

	public WorldMap(Path pathToFile) throws IOException {
		readFromFile(pathToFile);
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
			int y = 0;
			while (line != null) {
				String tokens[] = line.split(" ");

				if (tokens.length != dimX) {
					throw new IllegalArgumentException("File format violation: line " + numLinesRead
							+ " does not have the correct length (expected" + dimX + " ).");
				}

				for (int x = 0; x < dimX; x++) {
					int properties;
					if(tokens[x].equals("#")) {
						properties = Field.WALL_MASK;
					} else {
						properties = Integer.parseInt(tokens[x]);
					}
					Field f = new Field(properties);
					grid[x][y] = f;
					if(f.isFood()) {
						foodFields.add(f);
					}
				}

				y++;

				do {
					// not the most efficient solution but unless reading large
					// files negligible.
					line = br.readLine();
					if(line == null) {
						break;
					}
					line = normalizeLine(line);
					numLinesRead++;
				} while (line.isEmpty());
				
			}
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

}
