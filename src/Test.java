import javax.swing.JApplet;

import processing.core.*;

import edu.gcc.processing.net.*;
import edu.gcc.processing.develop.*;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitException;
import edu.gcc.processing.exceptions.net.IPAddressNumericException;
import edu.gcc.processing.exceptions.net.IPAddressSyntaxException;
import edu.gcc.processing.gui.TabAlerts;

public class Test extends PApplet {
	public void setup() {
		size(640, 480);
	    
		Multicaster multicast = new Multicaster(this, "239.255.255.255");
		
		try {
			multicast.connect();
		} catch (MulticasterInitException e) {
			// TODO: handle exception
		}
		
		multicast.closeAccess();
	}
}