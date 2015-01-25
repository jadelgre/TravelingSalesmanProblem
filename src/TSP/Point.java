package TSP;

public class Point {
	private int x;
	private int y;
	private Boolean visited = false;
	
	Point( int a, int b) {
		x = a;
		y = b;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void visited() {
		visited = true;
	}
	
	public Boolean beenVisited() {
		return visited;
	}
	
	public Boolean equals(Point other) {
		if(this.x == other.getX() && this.y == other.getY()) return true;
		return false;
	}
	
	public String toString() {
		return x + " " + y;
	}
}
