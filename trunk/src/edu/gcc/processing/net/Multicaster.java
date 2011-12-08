package edu.gcc.processing.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Timer;

import edu.gcc.processing.develop.Message;
import edu.gcc.processing.exceptions.multicaster.MulticasterGroupClosedException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitIPException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitPortException;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitSecurityException;
import edu.gcc.processing.exceptions.multicaster.MulticasterSendException;
import edu.gcc.processing.exceptions.net.IPAddressNumericException;
import edu.gcc.processing.exceptions.net.IPAddressSyntaxException;

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
	
	private String IPAddress;
	private MulticastSocket msConn;
	private InetAddress netAddr;
	private int internalIPRef = 0;
	private int internalPortRef = 0;
	private boolean triedNewIP = false;
	private boolean triedNewPort = false;
	private boolean resetListRef = false;
	
	public Multicaster(String IPAddress) {		
		this.IPAddress = IPAddress;
	}
	
	public void checkAddress(String address) throws IPAddressNumericException, IPAddressSyntaxException {
	//Split the IP address by the period
		String[] addressSplitter = address.split("[.]");
		
	//Check to see if this is a number		
		try {
			String IPNumbers = address.replace(".", "");
			Long.parseLong(IPNumbers);
		} catch (NumberFormatException e) {
			throw new IPAddressNumericException("The IP address you entered is not numeric. The IP address must be in the format x.x.x.x.", e);
		}
		
	//Check for x.x.x.x
		if (!address.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]).){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")) {
			throw new IPAddressSyntaxException("The IP address you entered is not a valid LAN multicaster IP address. The IP address must be in the format x.x.x.x, and in the range 239.0.0.0 - 239.255.255.255.");
		}
		
	//Check for 239.x.x.x
		if (Integer.parseInt(addressSplitter[0]) != 239) {
			throw new IPAddressSyntaxException("The IP address you entered is not a valid LAN multicaster IP address. The IP address must be in the format x.x.x.x, and in the range 239.0.0.0 - 239.255.255.255.");
		}
		
	//Check for 239.0.x.x - 239.255.x.x
		if (Integer.parseInt(addressSplitter[1]) < 0 && Integer.parseInt(addressSplitter[1]) > 255) {
			throw new IPAddressSyntaxException("The IP address you entered is not a valid LAN multicaster IP address. The IP address must be in the format x.x.x.x, and in the range 239.0.0.0 - 239.255.255.255.");
		}
		
	//Check for 239.0.0.x - 239.255.255.x
		if (Integer.parseInt(addressSplitter[2]) < 0 && Integer.parseInt(addressSplitter[2]) > 255) {
			throw new IPAddressSyntaxException("The IP address you entered is not a valid LAN multicaster IP address. The IP address must be in the format x.x.x.x, and in the range 239.0.0.0 - 239.255.255.255.");
		}
		
	//Check for 239.0.0.0 - 239.255.255.255
		if (Integer.parseInt(addressSplitter[3]) < 0 && Integer.parseInt(addressSplitter[3]) > 255) {
			throw new IPAddressSyntaxException("The IP address you entered is not a valid LAN multicaster IP address. The IP address must be in the format x.x.x.x, and in the range 239.0.0.0 - 239.255.255.255.");
		}
	}
	
	private void silentJoin() {
		try {
			this.msConn.joinGroup(this.netAddr);
		} catch (IOException e) {
			//This error shouldn't occur since it would caught by the connect() method during initial connection to the host
		}
	}
	
	public String recieveData() {
		byte buf[] = new byte[1024];
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		
		try {
			this.msConn.receive(pack);
			String out = new String(pack.getData());
			return out.substring(0, pack.getLength());
		} catch (IOException e) {
			return new String("");
		}
	}
	
	public void groupStatus() {
	//Join the group silently, without broadcasting your presence, to check if the group is open
		this.silentJoin();
		
	//Receive data for a given duration
		for (int i = 0; i <= 2; i ++) {
			String thing = this.recieveData();
			new Message(this.recieveData());
			if (this.recieveData() != "") {
				new Message("gotcha");
				return;
			}
		}
		
		new Message("I've nothing interesting to say...");
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
				throw new MulticasterInitIPException("Java could not connect to the multicast host located at the IP address " + this.IPAddress + ". Please check your network connections or try another host within the range of 239.0.0.0 - 239.255.255.255.", e);
		//Java could connect to the given host, but cannot connect to the bind to the specified port
			} catch (IOException e) {
				throw new MulticasterInitPortException("Java successfully connected to the muliticast host located at " + this.IPAddress + ", however, it could not bind to the server port " + this.port + ". This is most likely an issue with the host, and can be easily resolved by choosing an alternative port. For example, avoid using common ports such as ports 80, 443, 21, or 8080.", e);
		//Report a security violatioin
			} catch (SecurityException e) {
				throw new MulticasterInitSecurityException("A security violation has taken place. Try a different port, or another host altogether.", e);
			}
	//A general catch block to catch all of the above thrown exceptions
		} catch (Exception e) {
			throw new MulticasterInitException(e.getMessage(), e);
		}
	}

	

	/*
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
		}
	}*/
	
	/*
	public void close() {
		try {
			this.msConn.leaveGroup(InetAddress.getByName(this.IPList[0]));
		} catch (IOException e) {
			
		}
			
		this.msConn.close();
	}*/
}