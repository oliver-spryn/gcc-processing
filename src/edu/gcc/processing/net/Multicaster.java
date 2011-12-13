package edu.gcc.processing.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import edu.gcc.processing.develop.Message;
import edu.gcc.processing.events.IPacketRecievedListener;
import edu.gcc.processing.events.PacketRecieved;
import edu.gcc.processing.events.PacketRecievedHandler;
import edu.gcc.processing.exceptions.multicaster.MulticasterJoinException;
import edu.gcc.processing.exceptions.multicaster.MulticasterJoinedPreviouslyException;
import edu.gcc.processing.exceptions.multicaster.MulticasterNotJoinedException;
import edu.gcc.processing.gui.TabAlerts;
import edu.gcc.processing.multithreading.IterateReception;

import processing.core.*;


/**
 * This class is used to largely simplify the process of sending,
 * receiving, and getting information about other users on a network
 * multicast. In reality, this is only a pseudo-multicasting class, 
 * and it does not use any multicasting standards or feature at all.
 * Instead, this class connects to a back-end PHP/MySQL server that is
 * specially designed to communicate with this class, and from there
 * mimics the behavior a multicast.
 *
 * @category   Networking/Multicasting
 * @package    edu.gcc.processing.net
 * @access     public
 * @since      v0.1 Dev
 */

public class Multicaster {
/**
 * The domain name of the host which will process the pseudo-
 * multicasting information
 *
 * @access     public
 * @var        String
 */
	public String host = "204.93.159.80";
	
/**
 * The path to the script which will process the pseudo-multicasting
 * information
 *
 * @access     public
 * @var        String
 */
	public String processorURL = "~pavcsbel/comp155/multicaster.php";
	
/**
 * Whether or not Java's network connections will go over a proxy
 *
 * @access     public
 * @var        boolean
 */
	public boolean proxy = true;
	
/**
 * The domain name of the proxy host
 *
 * @access     public
 * @var        String
 */
	public String proxyURL = "proxy.gcc.edu";
	
/**
 * The proxy port over which a network communication will be established
 *
 * @access     public
 * @var        int
 */
	public int proxyPort = 8080;
	
/**
 * The maximum number of users that a room may hold at one time
 *
 * @access     public
 * @var        int
 */
	public int groupMax = 2;
	
/**
 * A reference to the receiver class which will dispatch events
 *
 * @access     public
 * @var        IterateReception
 */
	public IterateReception reciever;
	
/**
 * A reference to the object which extends the PApplet class
 *
 * @access     protected
 * @var        PApplet
 */
	protected PApplet PAppletRef;
	
/**
 * A container for the last data packet that was received
 *
 * @access     protected
 * @var        ArrayList
 */
	protected ArrayList lastPacket;
	
/**
 * If this class was instantiated via the sub-class IterateReception,
 * then we don't want to go in a loop of endless self-instantiation
 * (see the joinRoom() method). Set whether or not the IterateReception
 * method should be called, or has it been called already?
 *
 * @access     protected
 * @var        boolean
 */
	protected boolean instantiate = true;
	
/**
 * The unique ID of this user
 *
 * @access     protected
 * @var        string
 */
	protected String uniqueID;
	
/**
 * The name of the room which the user has currently joined
 *
 * @access     protected
 * @var        String
 */
	protected String room = null;
	
/**
 * A reference to the multithreading class
 *
 * @access     private
 * @var        Thread
 */
	private Thread thread;

/**
 * The constructor method, which is responsible for assigning the
 * constructor parameters to protected instance variables for global
 * use, as well as generating an unique ID for the user in order to 
 * distinguish between different users within a room.
 *
 * @access     public
 * @param      PApplet     applet      A reference to the object which extends the PApplet class
 * @return     void
 * @since      v0.1 Dev
 */
	
	public Multicaster(PApplet applet) {
		this.PAppletRef = applet;
		this.uniqueID = System.getProperty("user.name");
	}
	
/**
 * This is the main vehicle through which communication to the outside
 * world will be made. All specialized data packets will be ultimately
 * sent via this method to the PHP server for server-side execution.
 *
 * @access     private
 * @param      String      table       The name of the MySQL table on which PHP will execute this command
 * @param      String      command     The unique GET parameter to which PHP will respond and process
 * @param      String      data        Any additional pieces of information to send along with the request. Individual pieces can be separated by semicolons.
 * @return     String
 * @since      v0.1 Dev
 */
	
	private String transmit(String table, String command, String data) {
		String returnVal;
		
		try {
		//Build the GET URL parameters
			String parameters = URLEncoder.encode(table, "UTF-8") + "=value";
			parameters += "&" + URLEncoder.encode(command, "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8");
			
		//Define the URL to which we will be connecting
			URL link = new URL("http://" + this.host + "/" + this.processorURL + "?" + parameters);
			
		//Open a connection to the server, either directly or through a proxy
			URLConnection connection;
			
			if (this.proxy && this.host != "localhost" && this.host != "127.0.0.1") {
				SocketAddress address = new InetSocketAddress(this.proxyURL, this.proxyPort);
				connection = link.openConnection(new Proxy(Proxy.Type.HTTP, address));
			} else {
				connection = link.openConnection();
			}
			
			connection.setDoOutput(true);
			
		//Receive data back from the server
			BufferedReader recieveData = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			returnVal = recieveData.readLine();
			
		//Close the connection reader stream
			recieveData.close();
			
		//Return the data
			return returnVal;
		} catch(UnsupportedEncodingException e) {
			//Not a problem! UTF-8 has nearly universal support :)
		} catch(MalformedURLException e) {
			//This only thrown if the protocol is missing or unsupported. The HTTP protocol will be just fine :)
		} catch (IOException e) {
		//Input/output exceptions can be tough. Let the user know about this one...
			TabAlerts tab = new TabAlerts(this.PAppletRef);
			tab.borderColor = 0xFFFF0000;
			tab.fillColor = 0xFFFFCCEE;
			
			if (this.proxy && this.host != "localhost" && this.host != "127.0.0.1") {
				tab.message = "Communication to the server could not be made. Check your proxy connection.";
			} else {
				tab.message = "Communication to the server could not be made. Check your network connection.";
			}
			
			tab.build();
		}
	
	//If something fails, then return an empty string
		return new String();
	}
	
/**
 * Return a listing of all of the rooms registered on the database server,
 * in alphabetical order.
 *
 * @access     public
 * @return     ArrayList
 * @since      v0.1 Dev
 */
	
	public ArrayList roomNames() {
	//Parse the JSON array into an ArrayList
		Object JSON = JSONValue.parse(this.transmit("room", "names", ""));
		ArrayList returnVal = (ArrayList) JSON;
		
	//Return the result
		return returnVal;
	}
	
/**
 * Return a listing the total number of users currently within a room, not
 * to be confused with roomUsers();
 *
 * @access     public
 * @return     int
 * @since      v0.1 Dev
 */
	
	public int roomTotal(String room) {
	//Return the result from the server as an int
		return Integer.parseInt(this.transmit("room", "number_single", room));
	}
	
/**
 * Return a listing the total number of users currently within each room
 * that is registered on the database server. The order of the listing 
 * follows the alphabetical order of each of the room's names.
 *
 * @access     public
 * @return     ArrayList
 * @since      v0.1 Dev
 */
	
	public ArrayList roomTotals() {
	//Parse the JSON array into an ArrayList
		Object JSON = JSONValue.parse(this.transmit("room", "number_all", ""));
		ArrayList returnVal = (ArrayList) JSON;
		
	//Return the result
		return returnVal;
	}
	
/**
 * Join a room and keep track of the name of the room that the user joined
 *
 * @access     public
 * @return     void
 * @throws     MulticasterJoinException, MulticasterJoinedPreviouslyException
 * @since      v0.1 Dev
 */
	
	public void joinRoom(String room) throws MulticasterJoinException, MulticasterJoinedPreviouslyException {
	//Make sure that the user has not already joined a previous room
		if (this.room == null) {
		//The user has now joined a room
			this.room = room;
			
		//Join the room
			String statusCheck = this.transmit("room", "join", room + ";" + this.groupMax + ";" + this.uniqueID);
			
		//Ensure that the server gave a "success" message in reply, otherwise the room is too full
			if (!"success".equals(statusCheck)) {
				throw new MulticasterJoinException(statusCheck, this.PAppletRef);
			}
			
		//Start the receiving cycle
			if (this.instantiate) {
				this.reciever = new IterateReception(this.PAppletRef, this.room, this.uniqueID, this.host, this.processorURL, this.groupMax);
				this.thread = new Thread(this.reciever);
				this.thread.start();
			}
	//If the user has joined a previous room...
		} else {
			throw new MulticasterJoinedPreviouslyException("You have already joined a room. You cannot join a second one.", this.PAppletRef);
		}
	}
	
/**
 * Return a listing of the unique IDs of all of the users within the current
 * room, not to be confused with roomTotal()
 *
 * @access     public
 * @return     ArrayList
 * @throws     MulticasterNotJoinedException
 * @since      v0.1 Dev
 */
	
	public ArrayList roomUsers() throws MulticasterNotJoinedException {
	//See if the user has already joined a room
		if (this.room != null) {
		//Parse the JSON array into an ArrayList
			Object JSON = JSONValue.parse(this.transmit("room", "users", this.room));
			ArrayList returnVal = (ArrayList) JSON;
			
		//Return the result
			return returnVal;
	//If the user has not joined a room...
		} else {
			throw new MulticasterNotJoinedException("You have not yet joined a room. Please join a room.", this.PAppletRef);
		}
	}
	
/**
 * Leave a room and remove the name of the room from memory
 *
 * @access     public
 * @return     void
 * @throws     MulticasterNotJoinedException
 * @since      v0.1 Dev
 */
	
	public void leaveRoom() throws MulticasterNotJoinedException {
	//See if the user has already joined a room
		if (this.room != null) {
		//Remove the name of this room from memory
			this.room = null;
		
		//Leave the room
			this.transmit("room", "leave", room + ";" + this.uniqueID);
			
		//Turn off the receiver
			this.thread.interrupt();
	//If the user has not joined a room...
		} else {
			throw new MulticasterNotJoinedException("You have not yet joined a room. Please join a room.", this.PAppletRef);
		}
	}
	
/**
 * Leave a room and remove all other members from the room, as well
 *
 * @access     public
 * @return     void
 * @throws     MulticasterNotJoinedException
 * @since      v0.1 Dev
 */
	
	public void closeRoom() throws MulticasterNotJoinedException {
	//See if the user has already joined a room
		if (this.room != null) {
		//Remove the name of this room from memory
			this.room = null;
		
		//Leave the room
			this.transmit("room", "close", room + ";" + this.uniqueID);
			
		//Turn off the receiver
			this.thread.interrupt();
	//If the user has not joined a room...
		} else {
			throw new MulticasterNotJoinedException("You have not yet joined a room. Please join a room.", this.PAppletRef);
		}
	}
	
/**
 * Send a packet of data
 *
 * @access     public
 * @param      String      data        The string of data to send
 * @return     void
 * @throws     MulticasterNotJoinedException
 * @since      v0.1 Dev
 */
	
	public void sendData(String data) throws MulticasterNotJoinedException {
	//See if the user has already joined a room
		if (this.room != null) {
			this.transmit("data", "send", data + ";" + this.room + ";" + this.uniqueID);
	//If the user has not joined a room...
		} else {
			throw new MulticasterNotJoinedException("You have not yet joined a room. Please join a room.", this.PAppletRef);
		}
	}
	
/**
 * Receive a packet of data
 *
 * @access     public
 * @return     void
 * @throws     MulticasterNotJoinedException
 * @since      v0.1 Dev
 */
	
	public ArrayList recieve() throws MulticasterNotJoinedException {
	//See if the user has already joined a room
		if (this.room != null) {
		//Parse the JSON array into an ArrayList
			Object JSON = JSONValue.parse(this.transmit("data", "recieve", this.room + ";" + this.uniqueID));
			ArrayList returnVal = (ArrayList) JSON;
			
		//Return the result
			return returnVal;
	//If the user has not joined a room...
		} else {
			throw new MulticasterNotJoinedException("You have not yet joined a room. Please join a room.", this.PAppletRef);
		}
	}
	
/**
 * Get the unique ID of the user
 *
 * @access     public
 * @return     String
 * @since      v0.1 Dev
 */
	
	public String uniqueID() {
		return this.uniqueID;
	}
	
/**
 * Check to see if this user is the primary user in current room. The primary
 * user is determined by who enters the room first.
 *
 * @access     public
 * @return     boolean
 * @throws     MulticasterNotJoinedException
 * @since      v0.1 Dev
 */
	
	public boolean isPrimary() throws MulticasterNotJoinedException {
	//See if the user has already joined a room
		if (this.room != null) {
		//Get the list of users and see if the first unique ID in the list matches this user's unique ID
			ArrayList users = this.roomUsers();
			
		//Return the result
			return users.get(0).equals(this.uniqueID) ? true : false; 
	//If the user has not joined a room...
		} else {
			throw new MulticasterNotJoinedException("You have not yet joined a room. Please join a room.", this.PAppletRef);
		}
	}
}