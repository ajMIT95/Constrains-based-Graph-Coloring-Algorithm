package graphcoloring;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Node {

	public enum State {NO_GENERATED, CURRENT, OPENED, CLOSED};
	
	private final String id;
	private int x;
	private int y;
	private double value;
	private boolean isGoal;
	private final List<Successor> successors;
	private List<Integer> domain;
	private int color;
	private State state;
	
	
	// my movements
	private final Successor previous;
	private double costPath;
	
	public Node(String id) {
		this.id = id;
		x = 0;
		y = 0;
		value = 0.0f;
		isGoal = false;
		successors = new ArrayList<>();
		
		state = State.NO_GENERATED;
		previous = new Successor("", 0);
		costPath = 0;
		color = 0;
		domain = new ArrayList<>();
		for (int i = 1; i <= 3; i++)
			domain.add(i);
	}
	
	// Initialization methods
	public void setPositionX(int positionX) {
		this.x = positionX;
	}
	
	public void setPositionY(int positionY) {
		this.y = positionY;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public void setIsGoal(boolean isGoal) {
		this.isGoal = isGoal;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public List<Integer> getDomain() {
		return domain;
	}
	
	public void removeFromDomain(Integer i) {
		domain.remove(i);
	}
	
	public int accessDomain(int i) {
		return domain.get(i);
	}
	
	// Data retrieval methods
	@Override
	public String toString() {
		return id;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getStateColor() {
		Color color;
		
		switch (state.ordinal()) {
			case 0:
				color = Color.white;  // NO GENERATED
				break;
			case 1:
				color = Color.red;    // CURRENT
				break;
			case 2:
				color = Color.green;  // OPENED
				break;
			case 3:
				color = Color.orange; // CLOSED
				break;	
			default:
				color = Color.white;
		}
		
		return color;
	}
	
	public double getValue() {
		return value;
	}
	
	public boolean getIsGoal() {
		return isGoal;
	}
	
	public State getState() {
		return state;
	}
	
	public Integer getColor() {
		return color;
	}
	
	// Successor List methods
	public void addSuccessor(String idSuccessor, double costPath) {
		Successor mySuccessor = new Successor(idSuccessor, costPath);
		successors.add(mySuccessor);
	}
	
	public int getNumSuccessors() {
		return successors.size();
	}
	
	public String getIdSuccessor(int index) {
		return ((Successor) successors.get(index)).getIdSuccessor();
	}
	
	public double getCostSuccessor(int index) {
		return ((Successor) successors.get(index)).getCostSuccessor();
	}
	
	// Previous node methods
	public String getIdPrevious() {
		return previous.getIdSuccessor();
	}
	
	public double getCostPrevious() {
		return previous.getCostSuccessor();
	}
	
	public double getCostPath() {
		return costPath;
	}
	
	public void setIdPrevious(String idPrevious) {
		previous.setIdSuccessor(idPrevious);
	}
	
	public void setCostPrevious(double costPrevious) {
		previous.setCostSuccessor(costPrevious);
	}
	
	public void setCostPath(double costPath) {
		this.costPath = costPath;
	}
	
	// Method for painting on the screen
	public void paintNode(Graphics g) {
		Color color;
		Graphics2D g2 = (Graphics2D) g;
		GradientPaint gradientColor;
		color = this.getStateColor();
		if(color == Color.BLACK)
			gradientColor = new GradientPaint(x - 10, y - 10, color, x + 50, y + 50, Color.WHITE);
		else
			gradientColor = new GradientPaint(x - 10, y - 10, color, x + 50, y + 50, Color.BLACK);
		
		g2.setPaint(gradientColor);
		if (this.color == 1)
			g2.setColor(Color.red);
		else if (this.color == 2)
			g2.setColor(Color.green);
		else if (this.color == 3)
			g2.setColor(Color.blue);
		g2.fill(new Ellipse2D.Double(x - 20, y - 20, 2.0 * 20, 2.0 * 20));
		
		Font font = g.getFont();
		g.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
		g2.setColor(color.brighter());
		//g2.drawString(Double.toString(value), x + 6, y - 8);
		g2.drawString(this.getDomain().toString(), x - 20, y - 20);
		
		if(color == Color.BLACK)
			g2.setColor(Color.WHITE);
		else
			g2.setColor(Color.BLACK);
		
		g2.drawString(id, x - 4, y + 4);
		g.setFont(font);	
	}
	
	
	
	
	
	
	
	// Management of the list of pairs of successors
	public class Successor{
		
		private String idSuccessor;
		private double costSuccessor;
		
		public Successor(String idSuccessor, double costSuccessor) {
			this.idSuccessor = idSuccessor;
			this.costSuccessor = costSuccessor;
		}
		
		public String getIdSuccessor() {
			return idSuccessor;
		}
		
		public double getCostSuccessor() {
			return costSuccessor;
		}
		
		public void setIdSuccessor(String idSuccessor) {
			this.idSuccessor = idSuccessor;
		}
		
		public void setCostSuccessor(double costSuccessor) {
			this.costSuccessor = costSuccessor;
		}
	}
}
