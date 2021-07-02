/**
 * 
 */
package MyPackage;

import ij.IJ;
import ij.gui.WaitForUserDialog;
import ij.plugin.PlugIn;

/**
 * @author alberto
 *
 */
public class MyClass implements PlugIn {

	public void run(String arg) {
		
		new WaitForUserDialog("SCIAO PIRLA!!!").show();
		IJ.log("Questo è il main della Classe MyClass");
		IJ.showMessage("Questo è il messaggio della MyClass");
	}


}
