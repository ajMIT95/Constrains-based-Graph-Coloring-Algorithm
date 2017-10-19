package graphcoloring;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Date;

import graphcoloring.DepthFirstSearch;

public class GraphColoring extends Frame {

	private static final long serialVersionUID = 1L;
	
	Dimension screen;
	int width, height, x, y;
	
	MenuBar MMenu;
	MenuItem MOpen, MExit, MAbout, MClean;
	Menu MTimer;
	CheckboxMenuItem CMITimer, CMIStep, CMILegend;
	CheckboxMenuItem CMIVeryFast, CMIFast, CMIMedium, CMISlow;
	Button BRun, BRestart;
	TextArea TAInformation;
	Panel PNorth, PSouth;
	FileDialog FDOpen;
	Dialog DAbout;
	
	private final Graph graph;
	private DepthFirstSearch dfs;
	
	public static void main(String[] args) {
		File graphFile = null;
		if(args.length != 1)
		{	
		    System.err.println("usage: ./GraphColoring.java graphFile");
			System.exit(1);
		}
		
		graphFile = new File(args[0]);
		new GraphColoring(graphFile);
	}
	
	public GraphColoring(File graphFile) {
		// Main Window
		super("Constrains-based Graph Coloring Algorithm");
		this.setLayout(new BorderLayout());
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// Main Window's Menu
		MMenu = new MenuBar();
		Menu mFile = new Menu("File");
		Menu mView = new Menu("View"); 
		Menu mRun = new Menu("Run");
		Menu mHelp = new Menu("Help");
		
		// File Menu
		MOpen = new MenuItem("Open");
		MExit = new MenuItem("Exit");
		mFile.add(MOpen);
		mFile.addSeparator();
		mFile.add(MExit);
		
		// View Menu
		CMILegend = new CheckboxMenuItem("Legend for nodes");
		CMILegend.setState(true);
		MClean = new MenuItem("Clean message panel");
		mView.add(CMILegend);
		mView.addSeparator();
		mView.add(MClean);
		
		// Run Menu
		CMIStep = new CheckboxMenuItem("Step by step");
		CMIStep.setState(false);
		CMITimer = new CheckboxMenuItem("Timer");
		CMITimer.setState(false);
		mRun.add(CMIStep);
		mRun.add(CMITimer);
		
		// Speed Timer Submenu inside Run Menu
		MTimer = new Menu("Speed Timer");
		MTimer.setEnabled(false);
		CMIVeryFast = new CheckboxMenuItem("Very fast (0s)");
		CMIVeryFast.setState(false);
		CMIFast = new CheckboxMenuItem("Fast (0.1s)");
		CMIFast.setState(false);
		CMIMedium = new CheckboxMenuItem("Medium (0.5s)");
		CMIMedium.setState(false);
		CMISlow = new CheckboxMenuItem("Slow (1s)");
		CMISlow.setState(false);
		MTimer.add(CMIVeryFast);
		MTimer.add(CMIFast);
		MTimer.add(CMIMedium);
		MTimer.add(CMISlow);
		mRun.add(MTimer);
		
		// Help Menu
		MAbout = new MenuItem("About Graph-Coloring-Algorithm");
		mHelp.add(MAbout);
		
		// Adding the menus to the menuBar
		MMenu.add(mFile);
		MMenu.add(mView);
		MMenu.add(mRun);
		MMenu.add(mHelp);
		
		// Listeners of all MenuItems and CheckboxMenuItems
		MOpen.addActionListener(new ControlMenuItem());
		MExit.addActionListener(new ControlMenuItem());
		
		CMILegend.addItemListener(new ControlCheckBox());
		MClean.addActionListener(new ControlMenuItem());
		
		CMIStep.addItemListener(new ControlCheckBox());
		CMITimer.addItemListener(new ControlCheckBox());
	
		CMIVeryFast.addItemListener(new ControlCheckBox());
		CMIFast.addItemListener(new ControlCheckBox());
		CMIMedium.addItemListener(new ControlCheckBox());
		CMISlow.addItemListener(new ControlCheckBox());
		
		MAbout.addActionListener(new ControlMenuItem());
		
		
		// File Dialog to open the graph file
		FDOpen = new FileDialog(this, "Open Graph File", FileDialog.LOAD);
		
		
		// Dialog to show the about info
		DAbout = new Dialog(this, "About");
		DAbout.setLayout(new GridLayout(12, 1));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		width = 400;
		height = 250;
		x = (screen.width - width) / 2;
		y = (screen.height - height) / 2;
		DAbout.setBounds(x, y, width, height);
		DAbout.setResizable(false);
		DAbout.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				DAbout.setVisible(false);
			}
		});
		
		DAbout.add(new Label(""));
		DAbout.add(new Label("Constrains-based Graph Coloring Algorithm Animation Software"));
		DAbout.add(new Label(""));
		DAbout.add(new Label(""));
		
		// Buttons
		BRun = new Button(" Fine step ");
		BRestart = new Button(" Reset search ");
		BRun.setEnabled(false);
		BRestart.setEnabled(false);
		BRun.addActionListener(new ControlButton());
		BRestart.addActionListener(new ControlButton());
		
		// Text Area
		TAInformation = new TextArea("", 5, 70, TextArea.SCROLLBARS_VERTICAL_ONLY);
		TAInformation.setEditable(false);
		TAInformation.setBackground(Color.white);
		
		// Panels
		PNorth = new Panel();
		PNorth.setLayout(new FlowLayout(FlowLayout.LEFT));
		PNorth.setBackground(Color.lightGray);
		PNorth.add(BRun);
		PNorth.add(BRestart);
		
		graph = new Graph();
		graph.setBackground(new Color(90, 138, 212));
		
		PSouth = new Panel();
		PSouth.setLayout(new BorderLayout());
		PSouth.setBackground(Color.lightGray);
		PSouth.add("North", new Label("Message panel"));
		PSouth.add("Center", TAInformation);
		
		// Adding the menuBar and the three panels to the main window
		this.setMenuBar(MMenu);
		this.add("North", PNorth);
		this.add("Center", graph);
		this.add("South", PSouth);
		
		this.setVisible(true);
		
		if (graphFile != null)
			loadGraph(graphFile.getAbsolutePath());
	}
	
	private void loadGraph(String path) {
		try {
			graph.loadGraph(path);
			TAInformation.append((new Date()).toString() + ": Graph file successfully loaded.\n");
			
			// Search Algorithm by default
			dfs = new DepthFirstSearch(graph);
			TAInformation.append("\n" + (new Date()).toString() + ": Depth-First Search algorithm selected.\n");
			
			// Activating the buttons
			BRun.setEnabled(true);
			BRestart.setEnabled(true);
			
			// Repainting the graph
			graph.repaint();
		} catch (Exception e) {
			BRun.setEnabled(false);
			BRestart.setEnabled(false);
			TAInformation.append((new Date()).toString() + ": Error reading graph from file.\n");
		}
		
	}
	
	// Classes to control events
	private class ControlMenuItem implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource().equals(MOpen)) {
				FDOpen.setVisible(true);
				if (FDOpen.getFile() != null) {
					String path = FDOpen.getDirectory() + FDOpen.getFile();
					setTitle("AI-Search Algorithms - " + FDOpen.getFile());
					loadGraph(path);
				}
			} else if (e.getSource().equals(MClean)) {
				TAInformation.setText("");
			} else if (e.getSource().equals(MAbout)) {
				DAbout.setVisible(true);
			} else if (e.getSource().equals(MExit)) {
				System.exit(0);
			}
		}
	}
	
	private class ControlCheckBox implements ItemListener {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource().equals(CMILegend)) {
				graph.showLegend(CMILegend.getState());
				graph.repaint();
			}
			else if (e.getSource().equals(CMIStep)) {
				CMITimer.setState(false);
				MTimer.setEnabled(false);
				BRun.setLabel("Next step");
			}
			else if (e.getSource().equals(CMITimer)) {
				CMIStep.setState(false);
				MTimer.setEnabled(true);
				BRun.setLabel("Start");
			}
			else if (e.getSource().equals(CMIVeryFast)) {
				graph.setTimer(0);
			}
			else if (e.getSource().equals(CMIFast)) {
				graph.setTimer(100);
			}
			else if (e.getSource().equals(CMIMedium)) {
				graph.setTimer(500);
			}
			else if (e.getSource().equals(CMISlow)) {
				graph.setTimer(1000);
			}
		}
	}
	
	private class ControlButton implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String str;
			
			if (e.getSource().equals(BRun)) {
				if (CMIStep.getState()) {
					/* show execution
					str = dfs.getStack();
					if (!str.isEmpty())
						TAInformation.append((new Date()).toString() + ": " + str + "\n");
					if (!dfs.step())
						update("Depth-First Search");*/
				}
				else {
					dfs.run();
					update("Depth-First Search");
				}
				graph.repaint();
			}
			else if (e.getSource().equals(BRestart)) {
				graph.reset();
				dfs.reset();
				TAInformation.append((new Date()).toString() + ": Depth-First Search algorithm restarted.\n");
				graph.repaint();
				
				// Activating the execution button
				BRun.setEnabled(true);
			}
			
		}
	}
	
	private void update(String algorithm) {
		// show statistics
		String[] statistics = graph.getStatistics();
		TAInformation.append((new Date()).toString() + ": " + algorithm + " algorithm statistics.\n");
		for (String stat : statistics)
			TAInformation.append((new Date()).toString() + ": " + stat + "\n");
        
		// Deactivating the execution button
		BRun.setEnabled(false);
	}
}
