package edu.gcc.processing.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

//import processing.core.*;

import edu.gcc.processing.develop.Message;
import edu.gcc.processing.exceptions.multicaster.MulticasterGroupClosedException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitIPException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitIPFatalException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitPortException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitSecurityException;
import edu.gcc.processing.exceptions.multicaster.MulticasterSendException;

/**
 * Send a printed message to the console. For development use only.
 *
 * @category   Networking/Multicasting
 * @package    edu.gcc.processing.net
 * @access     public
 * @since      v0.1 Dev
 */

public class Multicaster{
	public String[] IPList = {"224.0.0.0", "224.0.0.37", "224.0.0.159"};
	public int[] port = {41793, 3376, 4201};
	
	private MulticastSocket msConn;
	private InetAddress netAddr;
	private int internalIPRef = 0;
	private int internalPortRef = 0;
	private boolean triedNewIP = false;
	private boolean triedNewPort = false;
	private boolean resetListRef = false;
	
	public Multicaster() {		
		
	}
	
	/*private void createDialog() {
		super.stroke(255, 255, 255);
		super.fill(0, 0, 0);
		super.rect(-1, 30, super.width + 2, super.height - 60);
		super.textAlign(super.CENTER);
		super.fill(255, 255, 255);
		super.text("Hi!", 0, 40, super.width, 40);
		super.translate(50, 50);
		super.ellipse(10, 50, 5, 5);
		
	}*/
	
	private void joinGroup() throws MulticasterGroupClosedException {	
		try {
			this.msConn.joinGroup(this.netAddr);
			
			for (int i = 0; i <= 2; i ++) {
				if (this.recieveData() == "Active") {
					throw new MulticasterGroupClosedException("The group your are trying to join already has a game in progress. Try another group.");
				}
			}
		} catch (IOException e) {
			//This error shouldn't occur since it would caught by the setupCaller() method during initial connection to the host
		}
	}
	
/**
 * Create a connection to a multicast host and port, and resolve any
 * potential issues that don't require repeated attempts. These issues
 * are resolved by the connect() method firing this function over and
 * over.
 *
 * @access     private
 * @return     void
 * @throws     MulticasterInitIPException, MulticasterInitIPFatalException, MulticasterInitPortException, MulticasterInitSecurityException
 * @since      v0.1 Dev
 */
	
	private void setupCaller() throws MulticasterInitIPException, MulticasterInitIPFatalException, MulticasterInitPortException, MulticasterInitSecurityException {
	//Try to create a multicast connection on the given IP address and port
		try {
		//Create a multicast connection on a given port, throws UnknownHostException
			this.msConn = new MulticastSocket(this.port[this.internalPortRef]);
			
		//If all goes well, then create a connection to a given IP address using the above port number, throws IOException and SecurityException
			this.netAddr = InetAddress.getByName(this.IPList[this.internalIPRef]);
			
		//Try joining the group and see if it is still open for new members
			try {
				this.joinGroup();
			} catch (MulticasterGroupClosedException e) {
				
			}
	//Try to resolve an error when connecting to the IP address
		} catch (UnknownHostException e) {
		//If there are more IP addresses to choose from, then throw an exception for appropriate kind of help
			if (this.IPList.length - 1 > this.internalIPRef) {
				throw new MulticasterInitIPException("The IP address (" + this.internalIPRef + ") of the given host could not be determined. Try another IP address.", e);
			} else {
			//The connect() method is currently trying all available IP addresses and ports, don't handle this yourself
				if (this.resetListRef == true) {
					throw new MulticasterInitIPFatalException("The IP address (" + this.internalIPRef + ") of the given host could not be determined. The list of avaliable IP addresses have been exhausted.", e);
				} else {
					throw new MulticasterInitIPException("The IP address (" + this.internalIPRef + ") of the given host could not be determined. Try another IP address.", e);
				}
			}
	//Try to resolve an error when binding to the specific port
		} catch (IOException e) {
		//If there are more ports to choose from, then throw an exception for appropriate kind of help
			if (this.port.length - 1 > this.internalPortRef) {
				throw new MulticasterInitPortException("A connection to the IP address (" + this.internalPortRef + ") was established, but the application could not bind to the given port. Try another port.", e);
			} else {
			//The connect() method is currently trying all available IP addresses and ports, don't handle this yourself
				//if (this.resetListRef == true) {
				//	throw new MulticasterInitPortFatalException("A connection to the IP address (" + this.internalPortRef + ") was established, but the application could not bind to the given port. The list of avaliable ports have been exhausted.", e);
				//} else {
					throw new MulticasterInitPortException("A connection to the IP address (" + this.internalPortRef + ") was established, but the application could not bind to the given port. Try another port.", e);
				//}
			}
	//Try to resolve a security violation
		} catch (SecurityException e) {
		//Try a different IP address and see if the problem still persists
			if (this.triedNewIP = false) {
				this.triedNewIP = true;
				throw new MulticasterInitIPException("A security violation has taken place. Try another IP address and see if this is still the case.", e);
		//Try a different port and see if the problem still persists
			} else if (this.triedNewPort = false) {
				this.triedNewPort = true;
				throw new MulticasterInitPortException("A security violation has taken place. Try another port and see if this is still the case.", e);
		//None of the above options worked, so throw a security violation exception
			} else {
				throw new MulticasterInitSecurityException("A security violation has taken place, and all possible solutions have been exhausted.", e);
			}
		}
	}
	
/**
 * Create a connection to a multicast host and port, by firing the setupCaller().
 * This method attempts to resolve any connection issues by altering the
 * connection settings, and calling setupCaller() over and over until the issue
 * is resolved or deemed unresolvable.
 *
 * @access     public
 * @return     void
 * @throws     MulticasterInitException
 * @since      v0.1 Dev
 */
	
	public void connect() throws MulticasterInitException {
	//Use a loop to run through all of the possible IP addresses and ports, should connecting to one or more of them fail
		for (int i = 0; i <= this.IPList.length - 1; i ++) {
		//Making a connection using the given IP address (this.internalIPRef) and port (this.internalPortRef)
			try {
				this.setupCaller();
				break;
		//If a connection to a specific IP address fails, try another one
			} catch (MulticasterInitIPException e) {
				new Message("Now trying the next IP address in the list, " + this.IPList[this.internalIPRef + 1] + ".");
				this.internalIPRef ++;
		//No connections could be made, this is probably a networking issue
			} catch (MulticasterInitIPFatalException e) {
				throw new MulticasterInitException("The application could not connect to the desired host. Please check your network connection.", e);
		//A connection to a host was made, but we could not bind to any port. Try all of the available hosts and ports until one works
			} catch (MulticasterInitPortException e) {
			//If this is the first run, reset the IP list and port list pointer back to 0
				if (this.resetListRef = false) {
					this.resetListRef = true;
					this.internalIPRef = 0;
					this.internalPortRef = 0;
			//If this is not the first run, then iteration through both lists is a bit more detailed
				} else {
				//If we have run through all available ports...
					if (this.port.length - 1 == this.internalPortRef) {
					//... and more IP addresses are available
						if (this.IPList.length - 1 != this.internalIPRef + 1) {
							this.internalIPRef ++;
					//... but more IP addresses are not available
						} else {
							throw new MulticasterInitException("The application could not connect to a port on any host. Please check your network connection.", e);
						}
						
					//Reset the port list pointer
						this.internalPortRef = 0;
				//If we have not run though all available ports...
					} else if (this.port.length - 1 > this.internalPortRef) {
						this.internalPortRef ++;
					}
				}
				
			//Run through the testing cycle again
				new Message("Now trying " + this.IPList[this.internalIPRef] + ":" + this.port[this.internalPortRef] + ".");
			//} catch (MulticasterInitPortFatalException e) {
			//	throw new MulticasterInitException("The application could not connect to the desired host. Please check your network connection.", e);
			} catch (MulticasterInitSecurityException e) {
				throw new MulticasterInitException("An unresolvable security violation has taken place. No further details are avaliable.", e);
			}
		}
	}
	
	public void sendData(String data) throws MulticasterSendException {
		/*try {
			InetAddress netAddr = InetAddress.getByName(this.IPList[0]);
			DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, netAddr, this.port);
			
			try {
				this.msConn.send(packet);
			} catch (IOException e) {
				throw new MulticasterSendException("Input/output exception thrown while sending a data packet to the multicast group", e);
			}
		} catch (UnknownHostException e) {
			//throw new MulticasterSendException("Unknown host exception thrown while joining a multicast group", e);
		}*/
	}
	
	public String recieveData() {
		byte buf[] = new byte[1024];
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		
		try {
			this.msConn.receive(pack);
			String out = new String(pack.getData());
			return out.substring(0, pack.getLength());
		} catch (IOException e) {
			return new String();
		}
	}
	
	public void close() {
		try {
			this.msConn.leaveGroup(InetAddress.getByName(this.IPList[0]));
		} catch (IOException e) {
			
		}
			
		this.msConn.close();
	}
}