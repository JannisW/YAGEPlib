package examples.behavior.world;

public enum Orientation {
	NORTH, EAST, SOUTH, WEST;

	public static Orientation fromString(String str) {
		switch (str.toUpperCase()) {
		case "NORTH":
			return NORTH;
		case "EAST":
			return EAST;
		case "SOUTH":
			return SOUTH;
		case "WEST":
			return WEST;
		default:
			throw new IllegalArgumentException(str + " can't be converted to a orientation");
		}
	}
}