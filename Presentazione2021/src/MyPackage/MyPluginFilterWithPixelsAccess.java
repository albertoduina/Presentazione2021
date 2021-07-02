package MyPackage;

import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.measure.Calibration;

import java.awt.*;
import ij.plugin.filter.*;

public class MyPluginFilterWithPixelsAccess implements PlugInFilter {
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) {
		new WaitForUserDialog("CIAO 001").show();
		this.imp = imp;
		return DOES_ALL;
	}

	public void run(ImageProcessor ip) {
		new WaitForUserDialog("CIAO 002").show();
		IJ.log("MY_PACKAGE");
		// ora supponiamo di voler effettuare dei calcoli sull'imagine attualmente
		// aperta non conosciamo nulla di questa immagine, ma vogliamo posizionare una
		// ROI circolare col centro a x=75 ed Y=80 con diametro=20 (misure in pixel)
		// STAVOLTA PERO'VEDIAMO COME ACCEDERE DIRETTAMENTE AI PIXELS
		// TENENDO PERO'CONTO CHE SE L'IMMAGINE E'CALIBRATA, NOI DOBBIAMO /DOVREMMO POI
		// TENERNE CONTO
		// test per commit

		int x1 = 75;
		int y1 = 80;
		int d1 = 60;
		ImagePlus imp1 = IJ.getImage();
		//
		// desideriamo anche tenere una traccia grafica di quello che facciamo usando
		// l'Overlay che permette di sovrapporre traccia delle ROI ed altro all'imagine,
		// ma senza interferire affatto col contenuto dell'immagine e dei pixels. E'una
		// cosa facoltativa, non c'entra nulla con i calcoli
		//
		Overlay over1 = new Overlay();
		imp1.setOverlay(over1);

		//
		// dalle API ImagePlus.setRoi: genera una selezione rettangolare,
		// OvalRoi (int x, int y, int width, int height) Creates an OvalRoi.
		// dobbiamo tenere conto che creiamo la ROI passando le coordinate del punto di
		// origine del quadrato: lo spigolo in alto a sx
		//
		imp1.setRoi(new OvalRoi(x1 - d1 / 2, y1 - d1 / 2, d1, d1));

		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();

		ImageStatistics stat1 = imp1.getStatistics();
		double roi1area = stat1.area;
		double roi1mean = stat1.mean;
		double roi1stdDev = stat1.stdDev;
		double roi1x = stat1.roiX;
		double roi1y = stat1.roiY;
		double roi1width = stat1.roiWidth;
		double roi1height = stat1.roiHeight;

		IJ.log("statistiche calibrate x=" + roi1x + " y=" + roi1y + " width= " + roi1width + " roi1heighth= "
				+ roi1height);
		IJ.log("statistiche calibrate aera=" + roi1area + " mean=" + roi1mean + " stdDev= " + roi1stdDev);

		//
		// rimuovo temporaneamente la calibrazione
		//
//		ImageProcessor ip1 = imp1.getProcessor();
		Calibration cal1 = imp1.getCalibration();
		imp1.setCalibration(null);
		ImageStatistics stat2 = imp1.getStatistics();

		IJ.log("statistiche noncalibrate x=" + stat2.roiX + " y=" + stat2.roiY + " width= " + stat2.roiWidth
				+ " roi2heighth= " + stat2.roiHeight);
		IJ.log("statistiche noncalibrate aera=" + stat2.area + " mean=" + stat2.mean + " stdDev= " + stat2.stdDev);

		//
		// ripristino la calibrazione e verifico cosa accade
		//
		imp1.setCalibration(cal1);
		ImageStatistics stat3 = imp1.getStatistics();

		IJ.log("statistiche RIcalibrate x=" + stat3.roiX + " y=" + stat3.roiY + " width= " + stat3.roiWidth
				+ " roi2heighth= " + stat3.roiHeight);
		IJ.log("statistiche RIcalibrate aera=" + stat3.area + " mean=" + stat3.mean + " stdDev= " + stat3.stdDev);
		//
		// mettiamo la traccia della ROI sull'Overlay (facoltativo)
		Roi roi1 = imp1.getRoi();
//		roi1.setStroke(BasicStroke.CAP_BUTT);
		Color rosso = Color.RED;

		roi1.setStrokeColor(rosso);
		over1.addElement(imp1.getRoi());

	}

	public void demoPixelPertainRoiTest(ImageProcessor ip) {
		/**
		 * questo e' solo un appunto sui metodi che possiamo utilizzare per stabilire se
		 * un pixel e' all'interno di una ROI.
		 */

		// byte[] pixels = (byte[]) ip.getPixels();
		// int width = ip.getWidth();
		Rectangle r = ip.getRoi();
		ImageProcessor mask = ip.getMask();
		for (int y = r.y; y < (r.y + r.height); y++) {
			for (int x = r.x; x < (r.x + r.width); x++) {
				if (mask == null || mask.getPixel(x - r.x, y - r.y) != 0) {
					// ...DO What YOU WISH TO DO
					/**
					 * Roi roi = Imp.getRoi(); Rectangle rect = roi.getBounds(); rx = rect.x; ry =
					 * rect.y; w = rect.width; h = rect.height; for(int y=ry; y<ry+h; y++) { for(int
					 * x=rx; x<rx+w; x++) { if(roi.contains(x, y)) {
					 */

				}
			}
		}
	}

}