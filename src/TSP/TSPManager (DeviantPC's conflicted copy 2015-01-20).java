package TSP;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class TSPManager {
	private ArrayList<Point> allPoints;
	private ArrayList<Point> visitedPoints;
	private ArrayList<Point> route;
	private int numPoints;
	private double furthest;

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

	private void calculateFurthestDistance() {
		double dist = 0;
		for(Point p : allPoints) {
			for(Point x : allPoints) {
				if(pointDistance(x, p) > dist) dist = pointDistance(x, p);
			}
		}
		furthest = dist;
	}

	// returns nearest unvisited point not including itself
	private Point nearestPoint(Point current) {
		Point temp = current;
		double dist = furthest;
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
		while(allPoints.size() > visitedPoints.size()) {
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

	private void calculateNearestNeighborRoute() {
		nearestNeighborRoute();
		System.out.println("The Nearest Neighbor route is " + route.toString() + "\n and its distance is " + routeDistance(route));
	}

	private void calculateExhaustiveRoute() {
		//ArrayList<ArrayList<Point>> possibilities = generatePermutations();
		PermutationIterator<Point> iterate = new PermutationIterator<Point>(allPoints);
		ArrayList<ArrayList<Point>> possibilities = new ArrayList<ArrayList<Point>>();
		while(iterate.hasNext()) {
			ArrayList<Point> temp = (ArrayList<Point>) iterate.next();
			temp.add(temp.get(0)); // return to the point which the list starts at
			possibilities.add(temp);
		}
		route = optimalRoute(possibilities);
		System.out.println("The optimal route is " + route.toString() + "\n and its distance is " + routeDistance(route));
	}

	private ArrayList<Point> optimalRoute(ArrayList<ArrayList<Point>> inputs) {
		ArrayList<Point> temp = inputs.get(0);
		double dist = routeDistance(temp);
		for(ArrayList<Point> list : inputs) {
			if(routeDistance(list) <= dist) {
				temp = list;
				dist = routeDistance(list);
			}
		}
		return temp;
	}

	private void runTrials(int numTrials, int size) {
		System.out.println("Running " + numTrials + " trials. Size: " + size + " points");
		ArrayList<Double> nearestTimes = new ArrayList<Double>();
		ArrayList<Double> exhaustiveTimes = new ArrayList<Double>();
		PointGenerator generator;
		TSPManager temp;
		long startTime, endTime;
		for(int i = 0; i < numTrials; i++) {
			generator = new PointGenerator("output.txt", size);
			temp = new TSPManager("output.txt");

			startTime = System.nanoTime();
			temp.calculateNearestNeighborRoute();
			endTime = System.nanoTime();
			nearestTimes.add((endTime - startTime) / 1.0E09);

			if(size < 11) {
				startTime = System.nanoTime();
				temp.calculateExhaustiveRoute();
				endTime = System.nanoTime();
				exhaustiveTimes.add((endTime - startTime) / 1.0E09);
			}
		}

		System.out.println("The results:");
		double sum = 0;
		int i = 1;
		for(Double time : nearestTimes) {
			sum += time;
			System.out.println("NN Trial " + i +":   " + time + " seconds");
			i++;
		}
		System.out.println("Average time = " + sum / numTrials + " seconds");
		sum = 0;
		i = 1;
		for(Double time : exhaustiveTimes) {
			sum += time;
			System.out.println("EX Trial " + i +":              " + time + " seconds");
			i++;
		}
		System.out.println("Average time = " + sum / numTrials + " seconds");
	}

	public static void main(String[] args){
		PointGenerator generate = new PointGenerator("output.txt", 1 );
		TSPManager demo = new TSPManager("output.txt");
		demo.runTrials(10, 5000);
		//demo.calculateNearestNeighborRoute();
		//demo.calculateExhaustiveRoute();

	}
}
