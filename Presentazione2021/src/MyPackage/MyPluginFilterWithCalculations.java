package MyPackage;

import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.io.Opener;
import ij.measure.Calibration;
import ij.measure.CurveFitter;

import java.awt.*;
import java.util.ArrayList;

import ij.plugin.DICOM;
import ij.plugin.filter.*;

public class MyPluginFilterWithCalculations implements PlugInFilter {
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}

	public void run(ImageProcessor ip) {

		//
		// posizioniamo una ROI rettangolare sull'immagine, con il centro (???) del
		// rettangolo
		// in x1= 75, y1=80 e lati a1=60 e b1=50
		//
		int x1 = 75;
		int y1 = 80;
		int a1 = 60;
		int b1 = 30;
		//
		// prendiamo l'immagine aperta (ci deve essere obbligatoriamente)
		//
		ImagePlus imp1 = IJ.getImage();
		//
		// assegnamo anche un Overlay all'immagine (una specie di lucido su cui
		// disegnare, senza alterare il conetnuto dei pixel od altro)
		//
		Overlay over1 = new Overlay();
		imp1.setOverlay(over1);
		//
		// ora disegnamo la ROI quadrata. Dobbiamo tenere conto che, qualsiasi tipologia
		// di ROI andiamo a posizionare dobbiamo sempre passare le coordinate
		// dell'angolo in altro a sinistra del rettangolo che circonda l'immagine, detto
		// BoundingRectangle. Per cui se vogliamo centrare il quadrato su 75,80 dobbiamo
		// calcolare le coordinate effettive dello spigolo
		//
		imp1.setRoi(x1 - a1 / 2, y1 - b1 / 2, a1, b1);
		//
		// disegnamo il rettangolo in rosso sull'overlay
		//
		Roi roi1 = imp1.getRoi();
		roi1.setStrokeColor(Color.RED); // il bordo ROI diventa rosso
		over1.addElement(imp1.getRoi()); // mettiamo la ROI nell'overlay
		//
		// ora, come si fa nell'uso manuale, acquisiamo contemporaneamente tutte le
		// statistioche della ROI, poi useremo solo quelle che ci interessano
		//
		ImageStatistics stat1 = imp1.getStatistics();
		IJ.log("la ROI rettangolare ha coordinate= " + stat1.roiX + "," + stat1.roiY + " area= " + stat1.area
				+ " media= " + stat1.mean + " devStandard= " + stat1.stdDev);
		//
		// POtremmo addirittura permettere all'operatore di posizionare la ROI dove
		// interessa,per poterlo fare durante l'esecuzione del nostro plugin dobbiamo
		// ricorrere a quello che si definisce un dialogo non modale, cioe' un dialogio
		// che, mentre viene mostrato a video, lascia libero l'operatore di interagire
		// con ImageJ e l'immagine (di sicuro combinera'belinate ...)
		//
		new WaitForUserDialog("prova a modificare posizione e dimensioni dela ROI rettangolare, poi premi OK").show();
		roi1 = imp1.getRoi(); // non lo utilizzo, potrei non metterlo
		stat1 = imp1.getStatistics();
		IJ.log("la ROI rettangolare ORA ha coordinate= " + stat1.roiX + "," + stat1.roiY + " area= " + stat1.area
				+ " media= " + stat1.mean + " devStandard= " + stat1.stdDev);
		//
		// passiamo ora ad una ROI circolare di centro x=50 ed y=75 e diametro d=40
		//
		int x2 = 50;
		int y2 = 75;
		int d2 = 40;

		imp1.setRoi(new OvalRoi(x2 - d2 / 2, y2 - d2 / 2, d2, d2));
		Roi roi2 = imp1.getRoi();
		roi2.setStrokeColor(Color.GREEN); // il bordo ROI diventa verde
		over1.addElement(imp1.getRoi()); // mettiamo la ROI nell'overlay
		//
		ImageStatistics stat2 = imp1.getStatistics();
		IJ.log("la ROI circolare ha coordinate= " + stat2.roiX + "," + stat2.roiY + " area= " + stat2.area + " media= "
				+ stat2.mean + " devStandard= " + stat2.stdDev);
		//
		// finora abbiamo usato una immagine, probabilmente calibrata in mm e, magari
		// anche calibrata in densita' ora supponiamo di volere le misure lineari in
		// pixels, possiamo disabilitare la calibrazione in mm e portarla apixel,
		// acquisire una statistica e ripristinare la calibrazione precedente
		//
		Calibration cal1 = imp1.getCalibration();
		imp1.setCalibration(null);
		ImageStatistics stat3 = imp1.getStatistics();

		IJ.log("la ROI circolare ha coordinate in pixel= " + stat3.roiX + "," + stat3.roiY + " area= " + stat3.area
				+ " media= " + stat3.mean + " devStandard= " + stat3.stdDev);
		//
		// ripristino la calibrazione e verifico cosa accade
		//
		imp1.setCalibration(cal1);
		ImageStatistics stat4 = imp1.getStatistics();

		IJ.log("la ROI circolare ha coordinate in mm= " + stat4.roiX + "," + stat4.roiY + " area= " + stat4.area
				+ " media= " + stat4.mean + " devStandard= " + stat4.stdDev);
		//
		// accedo ora ai pixel dell'imagine e cerco di effettuare i calcoli di alcune
		// statistiche del cerchio a basso livello, leggendo i pixel uno ad uno. Questo
		// potrebbe essere necessario per implementare particolari algoritmi di
		// filtraggio
		// siccome sono riuscito a fare una bella confusione, esperimento questo:
		//
		ImageProcessor ip1 = imp1.getProcessor();

		int[] tuttipixel = extractRoiPixels(ip1);
		//
		// se tutto andasse bene, mi aspetto che area, media e, magari, ds siano uguali
		// a quelle calcolate da ImageJ
		//
		int area = tuttipixel.length;
		double somma = 0;
		for (int i1 = 0; i1 < tuttipixel.length; i1++) {
			somma = somma + tuttipixel[i1];
		}

		double stddev = vetSdKnuth(tuttipixel);

		double media = somma / area;
		IJ.log("la ROI circolare CALCOLATA A MANINA ha coordinate in mm= " + stat4.roiX + "," + stat4.roiY + " area= "
				+ area + " media= " + media + " devStandard= " + stddev);

		//
		// ora voglio marcare ROSSO tutti i pixel che fanno parte della ROI circolare,
		// per verificare visivamente di come sia composta la ROI circolare
		//
		imp1.setRoi(new OvalRoi(x2 - d2 / 2, y2 - d2 / 2, d2, d2));
		imp1.updateAndDraw();
		new WaitForUserDialog("prova a modificare posizione e dimensioni dela OVALROI, poi premi OK").show();

//		ip1 = imp1.getProcessor();
		ImageProcessor mask1 = imp1.getMask();
		over1.clear();
		roi2 = imp1.getRoi();
		roi2.setStrokeColor(Color.BLUE); // il bordo ROI diventa blu
		over1.addElement(imp1.getRoi()); // mettiamo la ROI nell'overlay
		Rectangle r1 = ip1.getRoi();
		if (mask1 == null)
			new WaitForUserDialog("MASK1 == NULL !!!!!").show();
//		imp1.killRoi();
		for (int y4 = r1.y; y4 < (r1.y + r1.height); y4++) {
			for (int x4 = r1.x; x4 < (r1.x + r1.width); x4++) {
				if (mask1 == null) {
					imp1.setRoi(x4, y4, 1, 1);
					imp1.getRoi().setStrokeColor(Color.YELLOW);
					over1.addElement(imp1.getRoi());
				} else if (mask1.get(x4 - r1.x, y4 - r1.y) > 0) {
					//
					// questi sono i pixel che fanno parte della ROI, sui quali fare i calcoli
					//
					imp1.setRoi(x4, y4, 1, 1);
					imp1.getRoi().setStrokeColor(Color.GREEN);
					over1.addElement(imp1.getRoi());
				} else {
					imp1.setRoi(x4, y4, 1, 1);
					imp1.getRoi().setStrokeColor(Color.RED);
					over1.addElement(imp1.getRoi());
				}
			}
		}
		imp1.killRoi();
		IJ.log("INGRANDIRE L'IMMAGINE PER VEDERE I BORDI PIXEL COLORATI");

		String aux1 = (String) imp.getProperty("Info");
		String s1 = new String();
		String manufacturer1 = readDicomParameter(aux1, "0008,0070");
		IJ.log("---- immagine aperta ----");
		IJ.log("- costruttore= " + manufacturer1);

		if (cal1.calibrated()) {
			s1 += " \n";
			int curveFit = cal1.getFunction();
			s1 += "Calibration function: " + "\n";
			if (curveFit == Calibration.UNCALIBRATED_OD)
				s1 += "Uncalibrated OD\n";
			else if (curveFit == Calibration.CUSTOM)
				s1 += "Custom lookup table\n";
			else
				s1 += CurveFitter.fList[curveFit] + "\n";
			double[] c1 = cal1.getCoefficients();
			if (c1 != null) {
				s1 += "  a: " + IJ.d2s(c1[0], 6) + "\n";
				s1 += "  b: " + IJ.d2s(c1[1], 6) + "\n";
			}
		} else {
			s1 += "Uncalibrated\n";
		}

		IJ.log("Calibrazione= " + s1);
	}

	public static void setMyPrecision() {
		Analyzer.setPrecision(9);
	}

	public static boolean isDicomImage(String fileName1) {
		// IJ.redirectErrorMessages();
		int type = (new Opener()).getFileType(fileName1);
		if (type != Opener.DICOM) {
			return false;
		}
		ImagePlus imp1 = new Opener().openImage(fileName1);
		if (imp1 == null) {
			return false;
		}
		String info = new DICOM().getInfo(fileName1);
		if (info == null || info.length() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static String readDicomParameter(String header, String userInput) {
		// N.B. userInput => 9 characs [group,element] in format: xxxx,xxxx (es:
		// "0020,0013")
		// boolean bAbort;
		String attribute = "???";
		String value = "???";
		if (header != null) {
			int idx1 = header.indexOf(userInput);
			int idx2 = header.indexOf(":", idx1);
			int idx3 = header.indexOf("\n", idx2);
			if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0) {
				try {
					attribute = header.substring(idx1 + 9, idx2);
					attribute = attribute.trim();
					value = header.substring(idx2 + 1, idx3);
					value = value.trim();
					return (value);
				} catch (Throwable e) { // Anything else
					new WaitForUserDialog("Value problem").show();
					return (value);
				}
			} else {
				attribute = "MISSING";
				return (attribute);
			}
		} else {
			return (null);
		}
	}

	/**
	 * Estrae i valori dei pixel di una qualsivoglia ROI contenuta in un
	 * ImageProcessor
	 * 
	 * @param ip1 ImageProcessor in input
	 * @return
	 */
	public static int[] extractRoiPixels(ImageProcessor ip1) {

		ArrayList<Integer> arr1 = new ArrayList<Integer>();
		Rectangle r1 = ip1.getRoi();
		ImageProcessor mask1 = ip1.getMask();
		for (int y1 = r1.y; y1 < (r1.y + r1.height); y1++) {
			for (int x1 = r1.x; x1 < (r1.x + r1.width); x1++) {
				if (mask1 == null || mask1.get(x1 - r1.x, y1 - r1.y) > 0) {
					arr1.add((int) ip1.get(x1, y1));
				}
			}
		}
		int[] outArr = new int[arr1.size()];
		int i1 = 0;
		for (Integer n : arr1) {
			outArr[i1++] = n;
		}

		return outArr;
	}

	public static double vetSdKnuth(int[] data) {
		final int n = data.length;
		if (n < 2) {
			return Double.NaN;
		}
		double avg = (double) data[0];
		double sum = 0;
		// yes, i1 below starts from 1
		for (int i1 = 1; i1 < data.length; i1++) {
			double newavg = avg + (data[i1] - avg) / (i1 + 1);
			sum += (data[i1] - avg) * (data[i1] - newavg);
			avg = newavg;
		}
		return Math.sqrt(sum / (n - 1));
	}

}
