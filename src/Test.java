import javax.swing.JApplet;

import processing.core.*;

import edu.gcc.processing.net.*;
import edu.gcc.processing.develop.*;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitException;
import edu.gcc.processing.exceptions.net.IPAddressNumericException;
import edu.gcc.processing.exceptions.net.IPAddressSyntaxException;

public class Test extends PApplet {
	public void setup() {
		Multicaster multicast = new Multicaster("239.255.255.255");
		
		try {
			multicast.connect();
		} catch (MulticasterInitException e) {
			// TODO: handle exception
		}
		
		multicast.groupStatus();
	}
}