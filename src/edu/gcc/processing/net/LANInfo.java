package edu.gcc.processing.net;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

/**
 * Seek for all available computers on the LAN.
 *
 * @category   Networking
 * @package    edu.gcc.processing.net
 * @access     public
 * @since      v0.1 Dev
 */

public class LANInfo {
/**
 * Empty constructor method
 *
 * @access     public
 * @since      v0.1 Dev
 */
	public LANInfo() {
	//Nothing to do!
	}
	
/**
 * Obtain the LAN IPv4 address of the host, while avoiding any virtual adapters
 *
 * @access     protected
 * @return     String
 * @since      v0.1 Dev
 */
	protected String getIP() {
		try {
		//Create a loop iteration counter
			int iterator = 1;
			
		//Loop over the network interface data
			for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
			//Parse the data down to the LAN IPv4 address
				if (iterator == 13) {
					String networkInfo = networkInterface.getInterfaceAddresses().toString();
					String[] IPAddressPrep = networkInfo.split("[,]");
					String[] IPAddress = IPAddressPrep[3].split("[/]");
					
				//Return the generated IP address
					return IPAddress[1];
				}
				
			//Increment the iterator
				iterator++;
			}
			
		//IP address could not be found
			return "0.0.0.0";
		} catch (SocketException e) {
		//IP address could not be found
			return "0.0.0.0";
		}
	}
	
/**
 * Parse the IP address it's gateway address
 *
 * @access     protected
 * @return     String
 * @since      v0.1 Dev
 */
	protected String gateway() {
	//Get the IP address of the machine
		String IP = this.getIP();
		
	//Split the string at the each period
		String[] tokens = IP.split("[.]");
		
	//Rebuild the gateway IP address
		String returnVal;
		returnVal = tokens[0] + ".";
		returnVal += tokens[1] + ".";
		returnVal += tokens[2] + ".";
		returnVal += tokens[3].charAt(0);
		
		return returnVal;
	}
}