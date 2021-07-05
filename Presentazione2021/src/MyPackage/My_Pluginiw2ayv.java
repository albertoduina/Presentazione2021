package MyPackage;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import ij.io.Opener;
import ij.plugin.PlugIn;

public class My_Pluginiw2ayv implements PlugIn {

	public void run(String arg) {
		// secondo commit
		// ulteriore commit lo possino !!

		main();

	}

	public void main() {
		// commento aggiunto per commit

		new WaitForUserDialog("ECCOMI, sono il vostro affezionato My_Pluginiw2ayv").show();

		Opener opener = new Opener();
		ImagePlus imp1 = opener.openImage("F:/dati/001");
		imp1.show();

		IJ.log("POTA, ho aperto " + imp1.getTitle()+ " come da istruzioni, CHE VOET?");

	}

	public void showAbout() {

		IJ.showMessage("TANTO VA IL PROGRAMMATORE ALLA TASTIERA CHE CI LASCIA LO ZAMPINO");

	}

}
