package edu.gcc.processing.net;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Timer;

import processing.core.*;

import edu.gcc.processing.develop.Message;
import edu.gcc.processing.exceptions.multicaster.MulticasterGroupClosedException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitIPException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitPortException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitSecurityException;
import edu.gcc.processing.exceptions.multicaster.MulticasterSendException;
import edu.gcc.processing.exceptions.net.IPAddressNumericException;
import edu.gcc.processing.exceptions.net.IPAddressSyntaxException;
import edu.gcc.processing.gui.TabAlerts;
import edu.gcc.processing.multithreading.KeepGroupClosed;
import gcc.edu.processing.events.IPacketRecievedListener;

/**
 * Send a printed message to the console. For development use only.
 *
 * @category   Networking/Multicasting
 * @package    edu.gcc.processing.net
 * @access     public
 * @since      v0.1 Dev
 */

public class Multicaster {
	public int port = 5540;
	
	protected String IPAddress;
	protected PApplet PAppletRef;
	
	private MulticastSocket msConn;
	private InetAddress netAddr;
	
/**
 * The constructor method, which is responsible for assigning the
 * constructor parameters to protected instance variables for global
 * use.
 *
 * @access     public
 * @param      PApplet     applet      A reference to the object which extends the PApplet class
 * @param      String      IPAddress   A string containing the IP address of the multicast host
 * @return     void
 * @since      v0.1 Dev
 */
	
	public Multicaster(PApplet applet, String IPAddress) {
		this.PAppletRef = applet; 
		this.IPAddress = IPAddress;
	}
	
/**
 * Validate that "this.IPAddress" is indeed an IP address, and
 * furthermore is an IP address that is assigned to LAN mulitcasting
 * (between 239.0.0.0 - 239.255.255.255).
 *
 * @access     private
 * @return     void
 * @throws     IPAddressNumericException, IPAddressSyntaxException
 * @since      v0.1 Dev
 */
	
	private void IPValidate() throws IPAddressNumericException, IPAddressSyntaxException {
	//Split the IP address by the period
		String[] addressSplitter = this.IPAddress.split("[.]");
		
	//Check to see if this is a number		
		try {
			String IPNumbers = this.IPAddress.replace(".", "");
			Long.parseLong(IPNumbers);
		} catch (NumberFormatException e) {
			throw new IPAddressNumericException("The host IP address is invalid. IP addresses are formatted as x.x.x.x.", e, this.PAppletRef);
		}
		
	//Check for x.x.x.x
		if (!this.IPAddress.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]).){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")) {
			throw new IPAddressSyntaxException(this.IPAddress + " is not a valid LAN IPv4 multicaster address.", this.PAppletRef);
		}
		
	//Check for 239.x.x.x
		if (Integer.parseInt(addressSplitter[0]) != 239) {
			throw new IPAddressSyntaxException(this.IPAddress + " is not a valid LAN IPv4 multicaster address.", this.PAppletRef);
		}
		
	//Check for 239.0.x.x - 239.255.x.x
		if (Integer.parseInt(addressSplitter[1]) < 0 || Integer.parseInt(addressSplitter[1]) > 255) {
			throw new IPAddressSyntaxException(this.IPAddress + " is not a valid LAN IPv4 multicaster address.", this.PAppletRef);
		}
		
	//Check for 239.0.0.x - 239.255.255.x
		if (Integer.parseInt(addressSplitter[2]) < 0 || Integer.parseInt(addressSplitter[2]) > 255) {
			throw new IPAddressSyntaxException(this.IPAddress + " is not a valid LAN IPv4 multicaster address.", this.PAppletRef);
		}
		
	//Check for 239.0.0.0 - 239.255.255.255
		if (Integer.parseInt(addressSplitter[3]) < 0 || Integer.parseInt(addressSplitter[3]) > 255) {
			throw new IPAddressSyntaxException(this.IPAddress + " is not a valid LAN IPv4 multicaster address.", this.PAppletRef);
		}
	}
	
/**
 * Silently join a multicast group, without broadcasting your presence,
 * so that the group can be momentarily shadowed, and from there Java
 * can detect whether or not the group can be joined.
 * 
 * This variable is protected so that it can be accessed by any 
 * multithreading sub-classes that would later extend this class. These
 * multithread process often involve creating a new connection to the host,
 * and re-joining the group. Since these processes all work together for 
 * one user, then don't broadcast the presence of this connection, but
 * rely on the primary connection to have already done that.
 *
 * @access     protected
 * @return     void
 * @since      v0.1 Dev
 */
	
	protected void silentJoin() {
		try {
			this.msConn.joinGroup(this.netAddr);
		} catch (IOException e) {
			//This error shouldn't occur since it would caught by the connect() method during initial connection to the host
		}
	}

/**
 * Silently leave a multicast group, without broadcasting your lack of
 * presence, so that the group can be momentarily shadowed, and from
 * there Java can detect whether or not the group can be joined, and 
 * will leave if the group is full.
 * 
 * This variable is protected so that it can be accessed by any 
 * multithreading sub-classes that would later extend this class. These
 * multithread process often involve creating a new connection to the host,
 * and re-joining the group. Since these processes all work together for 
 * one user, then don't broadcast when this connection is closed, but
 * rely on the primary connection to have done that.
 *
 * @access     protected
 * @return     void
 * @since      v0.1 Dev
 */
	
	protected void silentLeave() {
		try {
			this.msConn.leaveGroup(this.netAddr);
		} catch (IOException e) {
			//This error shouldn't occur since it would caught by the connect() method during initial connection to the host
		}
	}
	
	public String recieveData() {
		byte buf[] = new byte[10240];
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		
		try {
			//this.msConn.setSoTimeout(3000);
			this.msConn.receive(pack);
			String out = new String(pack.getData());
			return out.substring(0, pack.getLength());
		} catch (IOException e) {
			return new String("");
		}
	}
	
	public String groupStatus() {
	//Join the group silently, without broadcasting your presence, to check if the group is open
		this.silentJoin();
		
	//Receive data for a given duration
		String returnVal = this.recieveData();
		
	//Leave the group silently, without broadcasting a left notification
		this.silentLeave();
		
		return returnVal;
	}
	
	public void closeAccess() {
		KeepGroupClosed close = new KeepGroupClosed(this.PAppletRef, this.IPAddress);
		Thread closeAndGo = new Thread(close);
		closeAndGo.start();
	}
	
	private void joinGroup() {	
		try {
			this.msConn.joinGroup(this.netAddr);
		} catch (IOException e) {
			//This error shouldn't occur since it would caught by the connect() method during initial connection to the host
		}
	}
	
/**
 * Create a connection to a multicast host and port, and alert the user
 * of any potential issues.
 *
 * @access     public
 * @return     void
 * @throws     MulticasterInitException
 * @since      v0.1 Dev
 */
	
	public void connect() throws MulticasterInitException {
	//Try to create a multicast connection on the given IP address and port
		try {
			try {
			//Create a multicast connection on a given port, throws UnknownHostException
				this.msConn = new MulticastSocket(this.port);
				
			//If all goes well, then create a connection to a given IP address using the above port number, throws IOException and SecurityException
				this.netAddr = InetAddress.getByName(this.IPAddress);
			
			//Ensure that the group is empty before joining
				
		//Java cannot connect to the given host
			} catch (UnknownHostException e) {
				throw new MulticasterInitIPException("Java could not connect to the multicast host. Please check your network connections.", e, this.PAppletRef);
		//Java could connect to the given host, but cannot connect to the bind to the specified port
			} catch (IOException e) {
				throw new MulticasterInitPortException("Java could not bind to the server port. Please try another port.", e, this.PAppletRef);
		//Report a security violation
			} catch (SecurityException e) {
				throw new MulticasterInitSecurityException("A security violation has taken place. Try a different port, or another host.", e, this.PAppletRef);
			}
	//A general catch block to catch all of the above thrown exceptions
		} catch (Exception e) {
			throw new MulticasterInitException(e.getMessage(), this.PAppletRef);
		}
	}

	

	
	public void sendData(String data) throws MulticasterSendException {
		DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, this.netAddr, this.port);
		
		try {
			this.msConn.send(packet);
		} catch (IOException e) {
			throw new MulticasterSendException("Input/output exception thrown while sending a data packet to the multicast group", e, this.PAppletRef);
		}
	}
	
	
	public void close() {
		try {
			this.msConn.leaveGroup(this.netAddr);
		} catch (IOException e) {
			
		}
			
		this.msConn.close();
	}
}