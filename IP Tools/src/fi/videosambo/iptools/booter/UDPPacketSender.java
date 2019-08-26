package fi.videosambo.iptools.booter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPPacketSender {

	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	
	private byte[] buf;
	
	public UDPPacketSender(String address, int port) {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			this.address = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
	}
	
	public void sendStringPacket(String msg) {
		buf = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String sendEchoPacket(String msg) {
		buf = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		packet = new DatagramPacket(buf, buf.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String received = new String(packet.getData(), 0, packet.getLength());
		return received;
	}
	
	public void closeSocket() {
		socket.close();
	}
	
}
