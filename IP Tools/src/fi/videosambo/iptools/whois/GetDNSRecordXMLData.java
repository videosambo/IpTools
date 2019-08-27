package fi.videosambo.iptools.whois;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import fi.videosambo.iptools.GUI;

public class GetDNSRecordXMLData {
	
	private static String APIKEY = GUI.getSettingsWHOISApiKey();
	
	private static String url = "https://www.whoisxmlapi.com/whoisserver/WhoisService?apiKey=" + APIKEY + "&type=_all&domainName=";
	
	public static void setAPIKEY(String aPIKEY) {
		APIKEY = aPIKEY;
	}

	public String getXMLContentAsString(String address) {
		return convertDocumentToString(getXMLContentAsDocument(address));
	}
	
	public Document getXMLContentAsDocument(String address) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		try {
			doc = builder.parse(new URL(url + address + "").openStream());
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public String convertDocumentToString(Document doc) {
		try {
			StringWriter writer = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			transformer.transform(new DOMSource((Node) doc), new StreamResult(writer));
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
