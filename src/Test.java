import javax.swing.JApplet;

import edu.gcc.processing.net.*;
import edu.gcc.processing.develop.*;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitException;

public class Test extends JApplet {
	public void Test() {
		Multicaster multicast = new Multicaster();
		
		try {
			multicast.connect();
		} catch (MulticasterInitException e) {
			new Message(e.getMessage());
		}
	}
}