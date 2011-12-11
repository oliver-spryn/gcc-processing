package edu.gcc.processing.multithreading;

import processing.core.PApplet;

import edu.gcc.processing.exceptions.multicaster.MulticasterInitException;
import edu.gcc.processing.exceptions.multicaster.MulticasterSendException;
import edu.gcc.processing.gui.TabAlerts;
import edu.gcc.processing.net.Multicaster;
import gcc.edu.processing.events.PacketRecieved;

/**
 * Periodically broadcast short message indicating that the current
 * multicasting group is full, and cannot accept new members. This
 * class is desgined to be fired by the Thread class's start() method,
 * and run in parallel with the rest of the application.
 *
 * @category   Networking/Multicasting/Multithreading
 * @package    edu.gcc.processing.net
 * @access     public
 * @since      v0.1 Dev
 */

public class KeepGroupClosed extends Multicaster implements Runnable {
/**
 * A boolean which governs the loop in the run() method. This variable
 * will be set to false whenever the Thread() class is interrupted, 
 * causing its public static method sleep() to throw an 
 * InterruptedException error, and from there, the appropriate catch 
 * block will set it to false, and ultimately end the loop.
 *
 * @access     private
 * @var        boolean
 */
	private boolean continueLoop = true;
	
/**
 * This is the constructor method, which gloablizes the references to the
 * parameters it receives for use in the rest of this class, and also
 * silently connects to and joins the appropriate group on th multicast
 * host.
 *
 * @access     public
 * @param      PApplet     applet      A reference to the object which extends the PApplet class
 * @param      String      IPAddress   A string containing the IP address of the multicast host
 * @return     void
 * @since      v0.1 Dev
 */
	
	public KeepGroupClosed(PApplet applet, String IPAddress) {
	//Call the super constructor and globalize the parameters that were passed in
		super(applet, IPAddress);
		super.PAppletRef = applet;
		super.IPAddress = IPAddress;
		
	//Silently connect to the multicast host
		try {
			super.connect();
		} catch (MulticasterInitException e) {
			//This action will be handled automatically by the MulticasterInitException class, which will notify the user of this failure
		}
	}
	
/**
 * This is the method that is fired by the Thread class's start() method,
 * and runs in parallel with other processes. Its sole purpose is to
 * periodically broadcast an "Active" message, indicating that the current
 * group is closed to new members.
 *
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	
	public void run() {
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
				super.sendData("Active");
			} catch (MulticasterSendException e) {
				//This action will be handled automatically by the MulticasterSendException class, which will notify the user of this failure
			}
		}
	}
}