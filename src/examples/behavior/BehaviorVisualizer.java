package examples.behavior;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import examples.behavior.fitness.ClassicFitnessFunction;
import examples.behavior.fitness.EvaluationEnvironment;
import examples.behavior.terminals.EnvironmentDependendTerminal;
import examples.behavior.world.Field;
import examples.behavior.world.Orientation;
import examples.behavior.world.WorldMap;
import gep.model.Chromosome;
import gep.model.ExpressionTreeNode;
import gep.model.Gene;
import gep.model.GeneTerminal;
import gep.model.Individual;

/**
 * Based on:
 * https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/BorderLayoutDemoProject/src/layout/BorderLayoutDemo.java
 */

/*
 * extends EvaluationEnvironment just to be able to use them with the terminals
 * TODO better solution
 */
public class BehaviorVisualizer extends EvaluationEnvironment {

	public static void main(String[] args) {

		// Path indivualPath = Paths.get(args[0]);
		Path indivualPath = Paths.get("./test.ser");

		Individual<Boolean> i = null;
		WorldMap w = null;
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(indivualPath.toFile()))) {
			i = (Individual) in.readObject();
			w = new WorldMap(Paths.get("src/examples/behavior/maps/branchmap.txt")); // TODO
																						// path
																						// ...
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.err.println(c.getMessage());
			c.printStackTrace();
			return;
		}

		BehaviorVisualizer visualizer = new BehaviorVisualizer(i, w);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				visualizer.createAndShowGUI();
			}
		});
	}
	
	private static final Color DEFAULT_COLOR = UIManager.getColor ( "Panel.background" );

	private void createAndShowGUI() {

		// Create and set up the window.
		frame = new JFrame("Behavior Visualizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		Container pane = frame.getContentPane();

		JButton forwardBtn = new JButton("Execute");
		forwardBtn.addActionListener(e -> executeOnce());

		Container navigationBar = new Container();
		navigationBar.setLayout(new FlowLayout());
		navigationBar.add(forwardBtn);
		pane.add(navigationBar, BorderLayout.PAGE_END);

		GridLayout gridLayout = new GridLayout(currentMap.getDimensionY(), currentMap.getDimensionX());
		Container mapContainer = new Container();
		mapContainer.setLayout(gridLayout);

		for (int y = currentMap.getDimensionY() - 1; y >= 0; y--) {
			for (int x = 0; x < currentMap.getDimensionX(); x++) {
				world[x][y] = new ImagePanel();
				mapContainer.add(world[x][y]);
			}
		}

		pane.add(mapContainer);

		updateGUI();

		// Use the content pane's default BorderLayout. No need for
		// setLayout(new BorderLayout());
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private JFrame frame;
	private ImagePanel[][] world;

	Individual<Boolean> individual;

	private ExpressionTreeNode<Boolean> currentProgram = null;

	public BehaviorVisualizer(Individual<Boolean> individual, WorldMap world) {
		super(new WorldMap[] { world }, new ClassicFitnessFunction());
		this.individual = individual;
		super.currentMap = maps[0];
		this.world = new ImagePanel[world.getDimensionX()][world.getDimensionY()];
		super.setAgentPos(world.getStartPositionX(), world.getStartPositionY());
		super.setAgentOrientation(world.getStartOrientation());

		// insert this evaluation environment in the individual
		for (Chromosome<Boolean> c : individual.chromosomes) {
			for (Gene<Boolean> g : c.genes) {
				for (GeneTerminal<Boolean> term : g.architecture.potentialTerminals) {
					if (term instanceof EnvironmentDependendTerminal) {
						EnvironmentDependendTerminal<Boolean> envTerm = (EnvironmentDependendTerminal<Boolean>) term;
						envTerm.setEvaluationEnvironment(this);
					}
				}
			}
		}
		
		currentProgram = individual.getExpressionTrees().get(0);
	}

	private void updateGUI() {
		// thats definitely not the fastest method to achieve that
		for (int x = 0; x < world.length; x++) {
			for (int y = 0; y < world[0].length; y++) {
				Field field = currentMap.getField(x, y);
				world[x][y].removeAgentFromField();
				world[x][y].setMarkerOnField(field.isMarker());
				if (field.isWall()) {
					world[x][y].setBackground(Color.BLACK);
				} else if (field.isFood()) {
					world[x][y].setBackground(Color.GREEN);
				} else if (field.isPhero()) {
					world[x][y].setBackground(Color.YELLOW);
				} else {
					world[x][y].setBackground(DEFAULT_COLOR);
				}
			}
		}
		world[getPosAgentX()][getPosAgentY()].setAgentOnField(getAgentOrientation());
		frame.repaint();
		frame.revalidate();
	}

	private void executeOnce() {
		currentProgram.execute();
		updateGUI();
	}

	@Override
	protected double evaluateFitness(Individual<Boolean> individual) {

		// grid = map.initMap();
		setAgentPos(currentMap.getStartPositionX(), currentMap.getStartPositionY());
		setAgentOrientation(currentMap.getStartOrientation());

		// single chromosome individuals (only one program)
		ExpressionTreeNode<Boolean> currentProgram = individual.getExpressionTrees().get(0);

		int numberOfTicks = 0;
		while (numberOfTicks < MAX_NUMBER_OF_SIMULATION_TICKS) { // TODO and
																	// food
																	// is
																	// left

			boolean executionResult = currentProgram.execute();

			numberOfTicks++;
		}

		return individual.getFitness();

	}

	public static class ImagePanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5540680032269852145L;
		
		private Orientation orientation;
		private boolean agentOnField;
		private boolean markerOnField;

		public ImagePanel() {
			orientation = Orientation.EAST;
			agentOnField = false;
		}

		public void setAgentOnField(Orientation orientation) {
			this.orientation = orientation;
			agentOnField = true;
		}

		public void removeAgentFromField() {
			agentOnField = false;
		}
		
		public void setMarkerOnField(boolean marker) {
			markerOnField = marker;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (markerOnField) {
				g.setColor(Color.BLACK);
				g.drawLine(0, 0, getWidth(), getHeight());
				g.drawLine(0, getHeight(), getWidth(), 0);
			}
			if (agentOnField) {
				int[] xs;
				int[] ys;
				switch (this.orientation) {
				case EAST:
					xs = new int[] { 0, 0, getWidth() };
					ys = new int[] { 0, getHeight(), getHeight() / 2 };
					break;
				case NORTH:
					xs = new int[] { 0, getWidth(), getWidth() / 2 };
					ys = new int[] { getHeight(), getHeight(), 0 };
					break;
				case SOUTH:
					xs = new int[] { 0, getWidth(), getWidth() / 2 };
					ys = new int[] { 0, 0, getHeight() };
					break;
				case WEST:
					xs = new int[] { getWidth(), getWidth(), 0 };
					ys = new int[] { 0, getHeight(), getHeight() / 2 };
					break;
				default:
					return;
				}
				g.setColor(Color.RED);
				g.fillPolygon(xs, ys, xs.length);
			}
		}

	}

}
