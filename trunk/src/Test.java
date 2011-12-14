import java.util.ArrayList;

import javax.swing.JApplet;

import processing.core.*;

import edu.gcc.processing.net.*;
import edu.gcc.processing.develop.*;
import edu.gcc.processing.events.PacketRecieved;
import edu.gcc.processing.events.PacketRecievedHandler;
import edu.gcc.processing.exceptions.multicaster.MulticasterJoinException;
import edu.gcc.processing.exceptions.multicaster.MulticasterJoinedPreviouslyException;
import edu.gcc.processing.exceptions.multicaster.MulticasterNotJoinedException;

public class Test extends PApplet {
	public void setup() {
		size(640, 480);
	    
		Multicaster multicast = new Multicaster(this);
		
		try {
			multicast.joinRoom("Lego");
		} catch (MulticasterJoinedPreviouslyException e) {
			
		} catch (MulticasterJoinException e) {
			
		}
	}
}