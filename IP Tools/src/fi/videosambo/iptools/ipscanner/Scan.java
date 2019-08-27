package fi.videosambo.iptools.ipscanner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Scan {
	
	private String address;
	private Socket socket = new Socket();
	private int timeout;
	private InetAddress ipv;
	
	public Scan(String address, int timeout) {
		this.address = address;
		this.timeout = timeout;
	}
	
	public String scanPort(int port) {
		if (scanPortBoolean(port)) {
			return "Port " + port + " is open\n";
		}
		return "";
	}
	
	public boolean scanPortBoolean(int port) {
		try {
			socket.connect(new InetSocketAddress(address, port), timeout);
			socket.close();
			return true;
		} catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
