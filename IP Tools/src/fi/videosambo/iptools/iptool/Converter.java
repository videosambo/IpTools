package fi.videosambo.iptools.iptool;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Converter {

	public String getDomain(String ip) {
		try {
			return InetAddress.getByName(ip).getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "Error 404 host not found";
	}
	
	public String getIP(String domain) {
		try {
			return InetAddress.getByName(domain).getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "Error 404 host not found";
	}
	
}
