package edu.gcc.processing.multithreading;

import java.awt.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import processing.core.PApplet;
import edu.gcc.processing.develop.Message;
import edu.gcc.processing.events.IPacketRecievedListener;
import edu.gcc.processing.events.PacketRecieved;
import edu.gcc.processing.exceptions.multicaster.MulticasterJoinException;
import edu.gcc.processing.exceptions.multicaster.MulticasterJoinedPreviouslyException;
import edu.gcc.processing.exceptions.multicaster.MulticasterNotJoinedException;
import edu.gcc.processing.net.Multicaster;

public class IterateReception extends Multicaster implements Runnable {
	private boolean continueLoop = true;
	private transient Vector listeners = new Vector();

	public IterateReception(PApplet applet, String room, String uniqueID, String host, String processorURL, int groupMax) {
		super(applet);
		super.room = room;
		super.uniqueID = uniqueID;
		super.instantiate = false;
		super.host = host;
		super.processorURL = processorURL;
		super.groupMax = groupMax;
		super.lastPacket = new ArrayList();
	}
	
	public void addEventListener(IPacketRecievedListener listener) {
		this.listeners.addElement(listener);
	}
	
	public void removeEventListener(IPacketRecievedListener listener) {
		this.listeners.removeElement(listener);
	}

	public void run() {
		ArrayList packetData = new ArrayList();
		
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
				
				if (super.lastPacket.isEmpty()) {
					super.lastPacket = packetData;
				}
				
				if (this.listeners != null && !this.listeners.isEmpty()) {
					PacketRecieved event = new PacketRecieved(this);
					
					Vector targets;
					synchronized (this) {
						targets = (Vector) listeners.clone();
					}
					
					Enumeration e = targets.elements();
					
					while(e.hasMoreElements()) {
						IPacketRecievedListener l = (IPacketRecievedListener) e.nextElement();
						
						if (packetData.get(0) != null && packetData.get(1) != null && !packetData.get(1).equals(super.lastPacket.get(1))) {
							super.lastPacket = packetData;
							
							
							l.packetRecieved(event, super.lastPacket);
							
							if (packetData.get(1).equals("User created")) {
								l.userJoined(event, super.lastPacket);
							}
							
							if (packetData.get(1).equals("User terminated")) {
								l.userLeft(event, super.lastPacket);
							}
							
							if (packetData.get(1).equals("Room closed")) {
								l.roomClosed(event, super.lastPacket);
							}
						}
					}
				}
			} catch (MulticasterNotJoinedException e) {
				//This action will be handled automatically by the MulticasterSendException class, which will notify the user of this failure
			}
		}
	}
}
