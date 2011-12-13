import java.util.ArrayList;

import javax.swing.JApplet;

import processing.core.*;

import edu.gcc.processing.net.*;
import edu.gcc.processing.develop.*;
import edu.gcc.processing.events.PacketRecieved;
import edu.gcc.processing.events.PacketRecievedHandler;
import edu.gcc.processing.exceptions.multicaster.MulticasterJoinException;
import edu.gcc.processing.exceptions.multicaster.MulticasterJoinedPreviouslyException;

public class Test extends PApplet {
	public void setup() {
		size(640, 480);
	    
		Multicaster multicast = new Multicaster(this);
		multicast.groupMax = 50;
		
		try {
			multicast.joinRoom("Lego");
		} catch (MulticasterJoinedPreviouslyException e) {
			
		} catch (MulticasterJoinException e) {
			
		}
		
		multicast.reciever.addEventListener(new PacketRecievedHandler() {
			public void packetRecieved(PacketRecieved e, ArrayList data) {
				new Message(data);
			}
			
			public void userJoined(PacketRecieved e, ArrayList data) {
				new Message(data);
			}
		});
	}
}