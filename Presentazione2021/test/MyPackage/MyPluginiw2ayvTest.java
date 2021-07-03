package MyPackage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ij.IJ;
import ij.ImageJ;

public class MyPluginiw2ayvTest {
	@Before
	public void setUp() throws Exception {
		new ImageJ(ImageJ.NORMAL);
	}

	@After
	public void tearDown() throws Exception {
		// new WaitForUserDialog("Do something, then click OK.").show();

	}

	@Test
	public final void testMain() {


		My_Pluginiw2ayv myPluginiw2ayv = new My_Pluginiw2ayv();
		myPluginiw2ayv.main();
		//		assertTrue(UtilAyv.compareVectors(expected, result, ""));
	}



}
