import javax.swing.event.EventListenerList;

import processing.core.PApplet;

import edu.gcc.processing.net.*;
import edu.gcc.processing.develop.*;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitException;
import edu.gcc.processing.exceptions.multicaster.MulticasterJoinException;
import edu.gcc.processing.exceptions.multicaster.MulticasterSendException;

public class Test extends PApplet {
	public void setup() {		
		Multicaster multicast = new Multicaster();
		
		try {
			multicast.setup();
		} catch (MulticasterInitException e) {
			new Message("A connection to the multicasting group at " + multicast.multicastIP + ":" + multicast.port + " could not be established.");
		}
		
		try {
			multicast.joinGroup();
		} catch (MulticasterJoinException e) {
			new Message("Although a connection to the multicasting group at " + multicast.multicastIP + ":" + multicast.port + " was made, the group could not be joined.");
		}
		
		try {
			multicast.sendData("Hi folks!");
		} catch (MulticasterSendException e) {
			new Message("Although a connection to the multicasting group at " + multicast.multicastIP + ":" + multicast.port + " was made and the group was joined, the requested data packet could not be sent.");
		}
		
		multicast.recieveData();
		multicast.close();
	}
}
