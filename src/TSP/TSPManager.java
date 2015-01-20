package TSP;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
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
		System.out.println("The Nearest Neighbor route is " + route.toString() + " and its distance is " + routeDistance(route));
	}
	
	private void calculateExhaustiveRoute() {
		//ArrayList<ArrayList<Point>> possibilities = generatePermutations();
		PermutationIterator<Point> iterate = new PermutationIterator<Point>(allPoints);
		ArrayList<ArrayList<Point>> possibilities = new ArrayList<ArrayList<Point>>();
		while(iterate.hasNext()) {
			ArrayList<Point> temp = (ArrayList<Point>) iterate.next();
			temp.add(allPoints.get(0)); // return to home
			possibilities.add(temp);
		}
		route = optimalRoute(possibilities);
		System.out.println("The route is " + route.toString() + " and its distance is " + routeDistance(route));
	}
	
	private ArrayList<ArrayList<Point>> generatePermutations() {
		ArrayList<int[]> ret = new ArrayList<int[]>();
		int[] nums = new int[numPoints + 1];
		for(int i = 0; i < numPoints; i++) {
			nums[i] = i;
		}
		nums[numPoints-1] = 0;
		permutation(nums, 0, ret);
		
		ArrayList<ArrayList<Point>> perms = new ArrayList<ArrayList<Point>>();
		for(int[] place : ret) {
			ArrayList<Point> temp = new ArrayList<Point>();
			for(int i = 0; i < place.length - 1; i++) {
				temp.add(allPoints.get(i));
			}
			perms.add(temp);
		}
		//System.out.println(perms.size());
		for(ArrayList<Point> list : perms){
			list.add(allPoints.get(0));
			//System.out.println(list);
		}
		return perms;
	}
	
	
	
	// Algorithm by johk95 
	// https://stackoverflow.com/questions/20906214/permutation-algorithm-for-array-of-integers-in-java
	
	private static void permutation(int[] arr, int pos, ArrayList<int[]> list){
	    if(arr.length - pos == 1)
	        list.add(arr.clone());
	    else
	        for(int i = pos; i < arr.length; i++){
	            swap(arr, pos, i);
	            permutation(arr, pos+1, list);
	            swap(arr, pos, i);
	        }
	}

	private static void swap(int[] arr, int pos1, int pos2){
	    int h = arr[pos1];
	    arr[pos1] = arr[pos2];
	    arr[pos2] = h;
	}
	
	// Algorithm by johk95 
	// https://stackoverflow.com/questions/20906214/permutation-algorithm-for-array-of-integers-in-java
	
	private ArrayList<Point> optimalRoute(ArrayList<ArrayList<Point>> inputs) {
		ArrayList<Point> temp = inputs.get(0);
		double dist = routeDistance(temp);
		for(ArrayList<Point> list : inputs) {
			if(routeDistance(list) <= dist) {
				temp = list;
			}
		}
		return temp;
	}
	
	public static void main(String[] args){
		//TSPManager demo = new TSPManager("points.txt");
		PointGenerator generate = new PointGenerator("output.txt", 4);
		TSPManager demo = new TSPManager("output.txt");
		demo.calculateNearestNeighborRoute();
		demo.calculateExhaustiveRoute();
		
	}
}
