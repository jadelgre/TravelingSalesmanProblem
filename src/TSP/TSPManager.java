package TSP;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class TSPManager {
	private ArrayList<Point> allPoints;
	private ArrayList<Point> visitedPoints;
	private ArrayList<Point> route;
	private int numPoints;

	TSPManager(String filename) {
		FileReader read = null;
		try {
			read = new FileReader(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			System.exit(0);
		}
		Scanner scan = new Scanner(read);
		numPoints = scan.nextInt();
		allPoints = new ArrayList<Point>();
		visitedPoints = new ArrayList<Point>();
		route = new ArrayList<Point>();
		while(scan.hasNextLine()) {
			allPoints.add(new Point( scan.nextInt(), scan.nextInt()));
		}
		for(Point p : allPoints) {
			//System.out.println(p.getX() + " " + p.getY());
		}
		nearestNeighborRoute();
		System.out.println("The route is " + route.toString() + " and its distance is " + routeDistance(route));
	}
	//returns distance between two points
	private double pointDistance(Point a, Point b) {
		double x = Math.pow(a.getX() - b.getX(),2);
		double y = Math.pow(a.getY() - b.getY(),2);
		return Math.sqrt(x+y);
	}
	
	private double furthestPoint(Point current) {
		double dist = 0;
		for(Point p : allPoints) {
			if(!visitedPoints.contains(p) && pointDistance(current, p) > dist) dist = pointDistance(current, p);
		}
		return dist;
	}
	
	// returns nearest unvisited point not including itself
	private Point nearestPoint(Point current) {
		Point temp = current;
		double dist = furthestPoint(temp);
		for(Point p : allPoints) {
			if(!visitedPoints.contains(p) && pointDistance(temp, p) <= dist && !current.equals(p)) {
				dist = pointDistance(temp, p);
				temp = p;
			}
		}
		return temp;
	}
	
	private void nearestNeighborRoute() {
		Point location = allPoints.get(0);
		route.add(location);
		visitedPoints.add(location);
		int i = 0;
		while(allPoints.size() > visitedPoints.size()) {
			i++;
			location = nearestPoint(location);
			route.add(location);
			visitedPoints.add(location);
		}
		route.add(allPoints.get(0)); // return home
	}
	
	private double routeDistance(ArrayList<Point> path) {
		
		double dist = 0;
		for(int i = 0; i < path.size() - 1; i++){
			dist+=pointDistance(path.get(i),path.get(i+1));
		}
		return dist;
	}
	
	
	public static void main(String[] args){
		TSPManager demo = new TSPManager("points.txt");
		
	}
}
