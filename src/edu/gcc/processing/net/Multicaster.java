package edu.gcc.processing.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import javax.swing.event.EventListenerList;

import edu.gcc.processing.develop.Message;
import edu.gcc.processing.exceptions.multicaster.MulticasterInitException;
import edu.gcc.processing.exceptions.multicaster.MulticasterJoinException;
import edu.gcc.processing.exceptions.multicaster.MulticasterSendException;

public class Multicaster extends EventListenerList {
	public String multicastIP = "224.0.0.0";
	public int port = 5000;
	public int ttl = 1;
	private MulticastSocket msConn;
	
	public Multicaster() {
		
	}
	
	public void setup() throws MulticasterInitException {
		try {
			this.msConn = new MulticastSocket(this.port);
		} catch (IOException e) {
			throw new MulticasterInitException("Input/output exception thrown while establishing a connection to the multicast group", e);
		} catch (SecurityException e) {
			throw new MulticasterInitException("Security exception thrown while establishing a connection to the multicast group", e);
		}
	}
	
	public void joinGroup() throws MulticasterJoinException {
		InetAddress netAddr;
		
		try {
			netAddr = InetAddress.getByName(this.multicastIP);
		} catch (UnknownHostException e) {
			throw new MulticasterJoinException("Unknown host exception thrown while joining a multicast group", e);
		}
		
		try {
			this.msConn.joinGroup(netAddr);
		} catch (IOException e) {
			throw new MulticasterJoinException("Input/output exception thrown while joining a multicast group", e);
		}
	}
	
	public void sendData(String data) throws MulticasterSendException {
		try {
			InetAddress netAddr = InetAddress.getByName(this.multicastIP);
			DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, netAddr, this.port);
			
			try {
				this.msConn.send(packet);
			} catch (IOException e) {
				throw new MulticasterSendException("Input/output exception thrown while sending a data packet to the multicast group", e);
			}
		} catch (UnknownHostException e) {
			//throw new MulticasterSendException("Unknown host exception thrown while joining a multicast group", e);
		}
	}
	
	public void recieveData() {
		byte buf[] = new byte[1024];
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		
		try {
			this.msConn.receive(pack);
			String out = new String(pack.getData());
			new Message(out.substring(0, pack.getLength()));
		} catch (IOException e) {
			//TO DO
		}
	}
	
	public void close() {
		try {
			this.msConn.leaveGroup(InetAddress.getByName(this.multicastIP));
		} catch (IOException e) {
			
		}
			
		this.msConn.close();
	}
}
