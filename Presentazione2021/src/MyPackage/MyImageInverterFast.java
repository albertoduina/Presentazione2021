package MyPackage;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;


public class MyImageInverterFast implements PlugIn {

	public void run(String args) {
		ImagePlus im = IJ.getImage();
		if (im.getType() != ImagePlus.GRAY8) {
			IJ.error("8-bit grayscale image required");
			return;
		}

		ImageProcessor ip = im.getProcessor();
		int width = ip.getWidth();
		int height = ip.getHeight();

		// iterate over all image coordinates (u,v)
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				int p = ip.get(u, v);
				ip.set(u, v, 255 - p);
			}
		}

		im.updateAndDraw(); // redraw the modified image
	}
}
