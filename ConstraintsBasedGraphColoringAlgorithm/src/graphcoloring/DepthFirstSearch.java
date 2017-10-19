package graphcoloring;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import graphcoloring.Graph;
import graphcoloring.Node;
import graphcoloring.Node.State;

public class DepthFirstSearch implements Runnable {
	
	// DFS Variables
	private Graphics g;
	private final Graph graph;
	private Queue<Node> layerNodes;
	private Node root;
	
	// Initialization of the algorithm
	public DepthFirstSearch(Graph graph) {
		this.graph = graph;
		
		if (graph.getNumNodes() > 0)
			init();
	}
	/*
	public String getStack() {
		return stack.toString();
	}*/
	
	public void reset() {
		init();
	}
	
	public void init() {
		g = graph.getGraphics();
		root = graph.getInitialNode();
	}

	@Override
	public void run() {
		run(root, 0);
		graph.paint(g);
	}
	
	public boolean run(Node node, Integer lastColor) {
		System.out.println("Begin iteration node " + node.toString());
		int numSuccessors = node.getNumSuccessors();
		
		for (int j = 0; j < numSuccessors ; j++) {
			Node suc = graph.getNode(node.getIdSuccessor(j));
			suc.setIdPrevious(node.toString());

			System.out.println("Domain " + suc.toString() + suc.getDomain());
			for (int k = 0 ; k < suc.getDomain().size() ; k++) {
				Integer color = suc.getDomain().get(k);
				suc.setColor(color);
				node.getDomain().remove(color);
				System.out.println("Removed color " + color + " from domain node " + node.toString() + node.getDomain());
				int numBoundaries = suc.getNumSuccessors();
				
				for (int l = 0; l < numBoundaries ; l++) {
					Node boundary = graph.getNode(suc.getIdSuccessor(l));
					boundary.removeFromDomain(color);
					boundary.paintNode(g);
					graph.sleep();
					System.out.println("Removed color " + color + " from node " + boundary.toString() + boundary.getDomain());
					if (boundary.getDomain().isEmpty())
						return false;
				}
				
				boolean result = run(suc, color);
				if (result)
					break;
				
			}
			System.out.println("EndRun");
		}
		return true;
	}		
	
	/*
	public boolean step() {
		boolean executing = true;
		switch (Step) {
			case 0:
				System.out.println("Step 0");
				step0();
				break;
			case 1:
				System.out.println("Step 1");
				step1();
				break;
			case 2:
				System.out.println("Step 2");
				step2();
				break;
			case 3:
				System.out.println("Step 3");
				step3();
				break;	
			default:
				executing = false;
		}
		return executing;
	}
	
	// Remove the first node on the stack
	private void step0() {
		if (!stack.isEmpty()) {
			Queue<Node> q = stack.element();
			node = q.remove();
			if (q.isEmpty())
				stack.pop();
			System.out.println(node.toString());
			node.setState(State.CURRENT);
			
			numSuccessors = node.getNumSuccessors();
			for (Integer c : node.getDomain()) {
				boolean checkConstraint = true;
				for (j = 0; j < numSuccessors; j++) {
					suc = graph.getNode(node.getIdSuccessor(j));
					if (suc.getColor() == c) {
						checkConstraint = false;
						break;
					}
				}
				
				node.setColor(c);
				
				node.paintNode(g);
				if (node.getIsGoal())
					Step = 3;
				else {
					j = 0;
					Step = 1;
					layerNodes = new LinkedList<>();
					stack.push(layerNodes);
				}
			}
			
			
		}
	}
	
	
	
	
	// Expand a successor of the first node on the stack
	private void step1() {
		anyExtended = false;
		while (j < numSuccessors && !anyExtended) {
			suc = graph.getNode(node.getIdSuccessor(j));
			if (suc != null && !visited.contains(suc.toString())) {
				layerNodes.add(suc);
				System.out.println("suc: " + suc.toString());
				visited.add(suc.toString());
				suc.setState(State.OPENED);
				suc.setIdPrevious(node.toString());
				suc.setCostPrevious(node.getCostSuccessor(j));
				suc.setCostPath(node.getCostSuccessor(j) + node.getCostPath());
				anyExtended = true;
				for (Integer c : node.getDomain()) {
					if (suc.getColor() == c) {
						checkConstraint = false;
						break;
					}
				}
			}
			
			System.out.println(anyExtended);
			
			if (suc.getIsGoal()) {
				myGoal = suc;
				Step = 2;
			}
			j++;
		}
		System.out.println(layerNodes);
		
		if (j == numSuccessors && anyExtended)
			Step = 2;
		if (j == numSuccessors && !anyExtended) {
			Step = 2;
			step2();
		}
		
	}
	
	// Close the element that was being expanded, indicating that the expansion has finished
	private void step2() {
		node.setState(State.CLOSED);
		
		if (myGoal != null)
			Step = 3;
		else
			Step = 0;
	}
	
	// Point the goal node
	private void step3() {
		myGoal.setState(State.CURRENT);
		Step = 4;
	}*/
}
