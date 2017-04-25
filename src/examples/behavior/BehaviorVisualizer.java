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
package examples.behavior;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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
 * This class allows the visualization of the behavior of Individuals in the
 * evolve behavior example.
 * 
 * This class extends EvaluationEnvironment just to be able to execute terminals
 * that depend on the evaluation environment. (TODO find a nicer solution)
 * 
 * Design based on:
 * https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/BorderLayoutDemoProject/src/layout/BorderLayoutDemo.java
 * 
 * @author Johannes Wortmann
 * 
 *         TODO graphics are quite slow and include artifacts.
 */
public class BehaviorVisualizer extends EvaluationEnvironment {

	/**
	 * Visualizes the behavior of a evolved individual.
	 *
	 * <p>
	 * You can call the program in the following way:</br>
	 * program </br>
	 * program [individual.ser] </br>
	 * program [individual.ser] [map.txt]
	 * </p>
	 * 
	 * @param args
	 *            The program arguments (first individual, second map; both
	 *            optional)
	 */
	public static void main(String[] args) {

		Path individualPath = null;
		Path worldPath = Paths.get("src/examples/behavior/maps/branchmap.txt");
		if (args.length > 0) {
			// first argument is path to serialized individual
			individualPath = Paths.get(args[0]);
		}
		if (args.length > 1) {
			// second argument is path to map (if set)
			worldPath = Paths.get(args[1]);
		}

		if (individualPath == null) {
			// load a stored one as backup
			// individualPath = Paths.get("./map1_fit22_gen10.ser"); // gets
			// stuck at first phero
			// individualPath = Paths.get("./map1_fit29_gen30.ser"); // almost
			// random?
			// individualPath = Paths.get("./map1_fit57_gen60.ser"); // gets
			// stuck at phero after branch
			individualPath = Paths.get("./map1_fit67_gen100.ser"); // optimal
		}

		Individual<Boolean> i = null;
		WorldMap w = null;
		try {
			i = loadIndividual(individualPath.toFile());
			w = new WorldMap(worldPath);

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

	private static final Color DEFAULT_COLOR = UIManager.getColor("Panel.background");

	private void createAndShowGUI() {

		// Create and set up the window.
		frame = new JFrame("Behavior Visualizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set up the menu
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");

		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(individualFileFilter);
		fileChooser.addChoosableFileFilter(mapFileFilter);

		loadIndividualMenuItem = new JMenuItem("Load individual");
		loadIndividualMenuItem.addActionListener(createOpenFileActionListener());
		menu.add(loadIndividualMenuItem);

		loadMapMenuItem = new JMenuItem("Load map");
		loadMapMenuItem.addActionListener(createOpenFileActionListener());
		menu.add(loadMapMenuItem);

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(e -> System.exit(0));
		menu.addSeparator();
		menu.add(exitMenuItem);

		menuBar.add(menu);
		frame.setJMenuBar(menuBar);

		// Set up the content pane.
		Container pane = frame.getContentPane();

		forwardBtn = new JButton("Execute Step");
		forwardBtn.addActionListener(e -> executeOnce());

		simulationTimer = new Timer(700, e -> executeOnce());
		playBtn = new JButton("Execute");
		playBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (simulationTimer.isRunning()) {
					simulationTimer.stop();
					playBtn.setText("Execute");
					forwardBtn.setEnabled(true);
				} else {
					if (simulationEnd) {
						// reset
						currentMap.initMap();
						resetTotalFitnessScore();
						setAgentPos(currentMap.getStartPositionX(), currentMap.getStartPositionY());
						setAgentOrientation(currentMap.getStartOrientation());
						updateGUI();
						simulationEnd = false;
						playBtn.setText("Execute");
					} else {
						// (re-)start simulation
						forwardBtn.setEnabled(false);
						simulationTimer.start();
						playBtn.setText("Stop");
					}
				}

			}
		});

		Container navigationBar = new Container();
		navigationBar.setLayout(new FlowLayout());
		navigationBar.add(forwardBtn);
		navigationBar.add(playBtn);
		pane.add(navigationBar, BorderLayout.PAGE_END);

		mapContainer = createMapContainer();
		pane.add(mapContainer, BorderLayout.CENTER);

		updateGUI();

		// Use the content pane's default BorderLayout. No need for
		// setLayout(new BorderLayout());
		// Display the window.
		frame.pack();
		frame.setVisible(true);

		guiReady = true;
	}

	private Container createMapContainer() {
		GridLayout gridLayout = new GridLayout(currentMap.getDimensionY(), currentMap.getDimensionX());
		Container mapContainer = new Container();
		mapContainer.setLayout(gridLayout);

		for (int y = currentMap.getDimensionY() - 1; y >= 0; y--) {
			for (int x = 0; x < currentMap.getDimensionX(); x++) {
				world[x][y] = new ImagePanel();
				mapContainer.add(world[x][y]);
			}
		}

		return mapContainer;
	}

	private JButton forwardBtn;
	private JButton playBtn;
	private JFrame frame;
	private ImagePanel[][] world;

	private Container mapContainer;

	private JMenuBar menuBar;
	private JFileChooser fileChooser;
	private JMenuItem loadIndividualMenuItem;
	private JMenuItem loadMapMenuItem;

	private FileFilter individualFileFilter = new FileNameExtensionFilter("Serialzed Individual", "ser");
	private FileFilter mapFileFilter = new FileNameExtensionFilter("Maps", "txt");

	private boolean guiReady = false;

	private Individual<Boolean> individual;

	private Timer simulationTimer;
	private boolean simulationEnd = false;

	private ExpressionTreeNode<Boolean> currentProgram = null;

	public BehaviorVisualizer(File individualFile, WorldMap world) throws ClassNotFoundException, IOException {
		this(loadIndividual(individualFile), world);
	}

	public BehaviorVisualizer(Individual<Boolean> individual, WorldMap world) {
		super(new WorldMap[] { world }, new ClassicFitnessFunction());
		this.setWorldMap(world);
		this.setIndividual(individual);
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
		if (getFoodConsumed() < currentMap.getFoodAmount()) {
			currentProgram.execute();
			updateGUI();
		} else {
			// reset
			simulationTimer.stop();
			forwardBtn.setEnabled(true);
			playBtn.setText("reset");
			simulationEnd = true;
		}
	}

	private ActionListener createOpenFileActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (e.getSource() == loadIndividualMenuItem) {
						fileChooser.setCurrentDirectory(Paths.get(".").toFile());
						fileChooser.setFileFilter(individualFileFilter);
						int res = fileChooser.showOpenDialog(frame);

						if (res == JFileChooser.APPROVE_OPTION) {
							File f = fileChooser.getSelectedFile();
							setIndividual(BehaviorVisualizer.loadIndividual(f));
						}
					} else if (e.getSource() == loadMapMenuItem) {
						fileChooser.setCurrentDirectory(Paths.get("src/examples/behavior/maps/").toFile());
						fileChooser.setFileFilter(mapFileFilter);
						int res = fileChooser.showOpenDialog(frame);

						if (res == JFileChooser.APPROVE_OPTION) {
							setWorldMap(new WorldMap(fileChooser.getSelectedFile()));
						}
					}
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		};
	}

	public static Individual<Boolean> loadIndividual(File individualSerialized)
			throws IOException, ClassNotFoundException {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(individualSerialized))) {
			return (Individual<Boolean>) in.readObject();
		}
	}

	public void setIndividual(Individual<Boolean> individual) {

		this.individual = new Individual<Boolean>(individual);

		// insert this evaluation environment in the individual
		for (Chromosome<Boolean> c : this.individual.chromosomes) {
			for (Gene<Boolean> g : c.genes) {
				for (GeneTerminal<Boolean> term : g.architecture.potentialTerminals) {
					if (term instanceof EnvironmentDependendTerminal) {
						EnvironmentDependendTerminal<Boolean> envTerm = (EnvironmentDependendTerminal<Boolean>) term;
						envTerm.setEvaluationEnvironment(this);
					}
				}
			}
		}

		currentProgram = this.individual.getExpressionTrees().get(0);
	}

	public void setWorldMap(WorldMap world) {
		this.maps[0] = world;
		super.currentMap = maps[0];
		this.world = new ImagePanel[world.getDimensionX()][world.getDimensionY()];
		super.setAgentPos(world.getStartPositionX(), world.getStartPositionY());
		super.setAgentOrientation(world.getStartOrientation());
		super.resetTotalFitnessScore();

		if (guiReady) {
			Container pane = frame.getContentPane();
			pane.remove(mapContainer);
			mapContainer = createMapContainer();
			pane.add(mapContainer, BorderLayout.CENTER);
			pane.revalidate();
			updateGUI();
		}
	}

	@Override
	protected double evaluateFitness(Individual<Boolean> individual) {
		throw new UnsupportedOperationException("This operation is not supported by the visualizer!");

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
