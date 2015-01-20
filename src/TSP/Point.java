package TSP;

public class Point implements Comparable {
	private int x;
	private int y;
	
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
	
	public Boolean equals(Point other) {
		if(this.x == other.getX() && this.y == other.getY()) return true;
		return false;
	}
	
	public String toString() {
		return x + " " + y;
	}
}
