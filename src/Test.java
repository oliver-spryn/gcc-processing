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
			multicast.joinRoom("Tardis");
		} catch (MulticasterJoinedPreviouslyException e) {
			
		} catch (MulticasterJoinException e) {
			
		}
		
		multicast.reciever.addEventListener(new PacketRecievedHandler() {
			public void packetRecieved(PacketRecieved e, ArrayList data) {
				new Message(data.get(1));
			}
		});
	}
}