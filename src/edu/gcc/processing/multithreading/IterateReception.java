package edu.gcc.processing.multithreading;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import processing.core.PApplet;
import edu.gcc.processing.events.IPacketRecievedListener;
import edu.gcc.processing.exceptions.multicaster.MulticasterNotJoinedException;
import edu.gcc.processing.net.Multicaster;

public class IterateReception extends Multicaster implements Runnable {
	private boolean continueLoop = true;
	private EventListenerList listeners;

	public IterateReception(PApplet applet) {
		super(applet);
		
		this.listeners = new EventListenerList();
	}
	
	public void addEventListener(IPacketRecievedListener listener) {
		listeners.add(IPacketRecievedListener.class, listener);
	}
	
	public void removeEventListener(IPacketRecievedListener listener) {
		listeners.remove(IPacketRecievedListener.class, listener);
	}

	public void run() {
		ArrayList packetData;
		Object[] listenerList = listeners.getListenerList();
		
		while(this.continueLoop) {
		//Can we wait a bit, or have we been interrupted?
			try {
				Thread.sleep(3000);
		//If we have been interrupted, then end the loop
			} catch (InterruptedException e) {
				this.continueLoop = false;
				break;
			}
			
		//Send a small message, which will be known by this application as indicating activity
			try {
				packetData = super.recieve();
				
				if (!packetData.get(0).equals(super.lastPacket.get(0)) && !packetData.get(1).equals(super.lastPacket.get(1))) {
					super.lastPacket = packetData;
					
					for (int i = 0; i < listenerList.length; i = i + 2 ) {
						if (listenerList[i] == IPacketRecievedListener.class) {
							((IPacketRecievedListener) listenersList[i + 1]).packetRecieved();
						}
					}
				}
			} catch (MulticasterNotJoinedException e) {
				//This action will be handled automatically by the MulticasterSendException class, which will notify the user of this failure
			}
		}
	}

}
