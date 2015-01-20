package TSP;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.out;
// Generates a a set of points
public class PointGenerator {
	private String fileName;
	private int numPoints;
	PointGenerator(String fileName, int numPoints) {
		this.fileName = fileName;
		this.numPoints = numPoints;
		generate();
	}
	
	private void generate() {
		ArrayList<Point> points = new ArrayList<Point>();
		Random rand = new Random(7);
		while(points.size() < numPoints) {
			int x = rand.nextInt(1000);
			int y = rand.nextInt(1000);
			points.add(new Point(x,y));
		}
		writeToFile(points);
	}
	
	private void writeToFile(ArrayList<Point> points) {
		FileWriter write = null;
		try {
			write = new FileWriter(fileName);
			write.write(Integer.toString(numPoints) + '\n');
			Point lastPoint = points.get(points.size() - 1);
			for(Point p : points) {
				if( p != lastPoint ) write.write(p.toString() + '\n');
				else write.write(p.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				write.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*out.print(numPoints);
		for(Point p : points) {
			out.(p.toString());
		}
		out.close();
		try {
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
	}
}
