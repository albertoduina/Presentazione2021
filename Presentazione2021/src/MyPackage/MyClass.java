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
		
		new WaitForUserDialog("CIAO").show();
		IJ.log("Questo � il main della Classe MyClass");
		IJ.showMessage("Questo � il messaggio della MyClass");
	}


}
