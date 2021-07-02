package MyPackage;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class MyImageInverterSlow implements PlugInFilter {

	public int setup(String args, ImagePlus im) {
		return DOES_8G; // t
	}

	public void run(ImageProcessor ip) {
		int width = ip.getWidth();
		int height = ip.getHeight();

		// iterate over all image coordinates (u,v)
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				int p = ip.getPixel(u, v);
				ip.putPixel(u, v, 255 - p);
			}
		}
	}

}
