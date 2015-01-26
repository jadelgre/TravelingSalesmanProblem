package TSP;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TSP_Tests {
	private TSPManager manage;
	
	@Before
	public void setup() {
		manage = new TSPManager("points.txt");
	}

	@Test
	public void testPoints() {
		manage.calculateNearestNeighborRoute();
		//assert.assertEquals(5, manage.getRoute().size());
	}

}
