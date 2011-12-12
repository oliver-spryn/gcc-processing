import javax.swing.JApplet;

import processing.core.*;

import edu.gcc.processing.net.*;
import edu.gcc.processing.develop.*;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitException;
import edu.gcc.processing.exceptions.multicaster.MulticasterSendException;
import edu.gcc.processing.exceptions.net.IPAddressNumericException;
import edu.gcc.processing.exceptions.net.IPAddressSyntaxException;
import edu.gcc.processing.gui.TabAlerts;

public class Test extends PApplet {
	public void setup() {
		size(640, 480);
	    
		Multicaster multicast = new Multicaster(this, "239.0.0.12");
				
		try {
			multicast.connect();
		} catch (MulticasterInitException e) {
			// TODO: handle exception
		}
		multicast.joinGroup();
		
		try {
			for(int i = 0; i <= 50; i++) {
			multicast.sendData("Hi");
			}
		} catch (MulticasterSendException e) {
			
		}
	}
}