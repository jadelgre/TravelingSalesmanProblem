package TSP;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
public class TSPManager {
	private ArrayList<Point> allPoints;
	private ArrayList<Point> route;
	private int numPoints;
	private int visitedNum = 1;
	private String filename = "10.txt";

	TSPManager() {
/*		Scanner scan = new Scanner(System.in);
		System.out.println("Do you want to load points from a file");
		String response = scan.next();
		if(response.contains("y") || response.contains("Y")) {
			System.out.println("Enter the filename:");
			filename = scan.next();
		} else {
			System.out.println("Enter the number of points to be randomly generated");
			numPoints = Integer.parseInt(scan.next());
			PointGenerator generate = new PointGenerator(filename, numPoints);
		}
		readFile();
		this.calculateNearestNeighborRoute();
		this.calculateExhaustiveRoute();*/
	}

	private void setFile(String input) {
		filename = input;
		readFile();
	}

	private void readFile() {
		FileReader read = null;
		try {
			read = new FileReader(filename);
		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(0);
		}
		Scanner scan = new Scanner(read);
		numPoints = scan.nextInt();
		allPoints = new ArrayList<Point>();
		route = new ArrayList<Point>();
		while(scan.hasNextLine()) {
			allPoints.add(new Point( scan.nextInt(), scan.nextInt()));
		}
		scan.close();
	}
	//returns distance between two points
	private double pointDistance(Point a, Point b) {
		double x = Math.pow(a.getX() - b.getX(),2);
		double y = Math.pow(a.getY() - b.getY(),2);
		return Math.sqrt(x+y);
	}

	// returns nearest unvisited point not including itself
	private Point nearestPoint(Point current) {
		Point temp = current;
		double dist = Double.POSITIVE_INFINITY;
		for(Point p : allPoints) {
			if(!p.beenVisited()) {
				double calc = pointDistance(temp, p);
				if(calc <= dist && calc > 0.0) {
					dist = calc;
					temp = p;
				}
			}
		}
		return temp;
	}

	private void nearestNeighborRoute() {
		Point location = allPoints.get(0);
		route.add(location);
		int numPoints = allPoints.size();
		location.visited();
		while(numPoints > visitedNum) {
			location = nearestPoint(location);
			location.visited();
			visitedNum++;
			route.add(location);
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

	public void calculateNearestNeighborRoute() {
		nearestNeighborRoute();
		System.out.println("The Nearest Neighbor route is " + route.toString() + "\n and its distance is " + routeDistance(route));
	}

	public void calculateExhaustiveRoute() {
		PermutationIterator<Point> iterate = new PermutationIterator<Point>(allPoints);
		ArrayList<ArrayList<Point>> possibilities = new ArrayList<ArrayList<Point>>();
		possibilities.addAll((Collection<? extends ArrayList<Point>>) generatePerm( allPoints ));
		//System.out.println(possibilities.size());
		for(ArrayList<Point> list : possibilities) {
			list.add(list.get(0)); // wrap around
		}
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
	
	// http://stackoverflow.com/questions/10305153/generating-all-possible-permutations-of-a-list-recursively

	public List<List<Point>> generatePerm(List<Point> original) {
		if (original.size() == 0) { 
			List<List<Point>> result = new ArrayList<List<Point>>();
			result.add(new ArrayList<Point>());
			return result;
		}
		Point firstElement = original.remove(0);
		List<List<Point>> returnValue = new ArrayList<List<Point>>();
		List<List<Point>> permutations = generatePerm(original);
		for (List<Point> smallerPermutated : permutations) {
			for (int index=0; index <= smallerPermutated.size(); index++) {
				List<Point> temp = new ArrayList<Point>(smallerPermutated);
				temp.add(index, firstElement);
				returnValue.add(temp);
			}
		}
		return returnValue;
	}

	public ArrayList<Point> getRoute() {
		return route;
	}

	public static void main(String[] args){
		//PointGenerator generate = new PointGenerator("output.txt", 5 );
		//TSPManager demo = new TSPManager("output.txt");
		TSPManager demo = new TSPManager();
		demo.calculateNearestNeighborRoute();
		demo.calculateExhaustiveRoute();

	}


}
