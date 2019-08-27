package fi.videosambo.iptools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import fi.videosambo.iptools.booter.TCPPacketSender;
import fi.videosambo.iptools.booter.UDPPacketSender;
import fi.videosambo.iptools.ipscanner.Scan;
import fi.videosambo.iptools.iptool.Converter;
import fi.videosambo.iptools.whois.GetAddressXMLData;
import fi.videosambo.iptools.whois.GetDNSRecordXMLData;
import net.boplicity.xmleditor.XmlEditorKit;

public class GUI extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField booterAddressField;
	private JTextField booterPortField;
	private JTextField whoisSearchField;

	private GetAddressXMLData xmldata = new GetAddressXMLData();
	private Converter coverter = new Converter();
	private GetDNSRecordXMLData dnsdata = new GetDNSRecordXMLData();
	private TCPPacketSender tcpSender;
	private UDPPacketSender udpSender;
	
	private JTextField dnsSearchField;
	private JTextField dtipDomainField;
	private JTextField dtipIpField;
	private JTextField iptdIpField;
	private JTextField iptdDomainFIeld;
	
	JEditorPane consoleLog;
	
	private XmlToTree xmlToTree;
	
	private Thread t;
	private JTextField ipscanAddress;
	private Scan scan;
	
	private boolean scanRunning = false;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private static JTextField settingsWHOISApiKey;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		settingsWHOISApiKey = new JTextField();
		consoleLog = new JEditorPane();
		settingsWHOISApiKey.setText("at_J7TntBlVBoWiwPElcLFbpdo9I30kC");
		setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/img/iptoolsicon.png")));
		setTitle("IP Tools");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 477);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel booterPanel = new JPanel();
		tabbedPane.addTab("Booter", null, booterPanel, null);
		
		JLayeredPane protocolPanel = new JLayeredPane();
		protocolPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Protocol", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JLayeredPane targetInfoPanel = new JLayeredPane();
		targetInfoPanel.setBorder(new TitledBorder(null, "Target Infromation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLayeredPane packetPanel = new JLayeredPane();
		packetPanel.setBorder(new TitledBorder(null, "Packet Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLayeredPane packetCountPanel = new JLayeredPane();
		packetCountPanel.setBorder(new TitledBorder(null, "Packet Count", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JSeparator booterSeparator = new JSeparator();
		
		JButton booterLaunch = new JButton("Attack");
		booterLaunch.setToolTipText("Start sending packets");
		
		JProgressBar booterProgbar = new JProgressBar();
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBorder(null);
		
		JButton booterSTOP = new JButton("STOP");
		GroupLayout gl_booterPanel = new GroupLayout(booterPanel);
		gl_booterPanel.setHorizontalGroup(
			gl_booterPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_booterPanel.createSequentialGroup()
					.addGap(35)
					.addGroup(gl_booterPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_booterPanel.createSequentialGroup()
							.addComponent(packetPanel, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(packetCountPanel, GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
						.addGroup(gl_booterPanel.createSequentialGroup()
							.addComponent(protocolPanel, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(targetInfoPanel, GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)))
					.addGap(26))
				.addGroup(gl_booterPanel.createSequentialGroup()
					.addGap(25)
					.addComponent(booterLaunch, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(booterProgbar, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(booterSTOP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_booterPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(booterSeparator, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(layeredPane, GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
		);
		gl_booterPanel.setVerticalGroup(
			gl_booterPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_booterPanel.createSequentialGroup()
					.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_booterPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(targetInfoPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
						.addComponent(protocolPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_booterPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(packetCountPanel, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
						.addGroup(gl_booterPanel.createSequentialGroup()
							.addGap(2)
							.addComponent(packetPanel, GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(booterSeparator, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_booterPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(booterLaunch, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(booterProgbar, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(booterSTOP, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		booterSTOP.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (t == null || !t.isAlive())
					return;
				t.interrupt();
				booterProgbar.setIndeterminate(false);
				booterProgbar.setStringPainted(false);
				booterProgbar.setValue(0);
				booterProgbar.setMaximum(100);
				logConsole("Thread interrupted!");
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		
		JSpinner booterPacketCount = new JSpinner();
		
		JEditorPane packetContent = new JEditorPane();
		
		
		JMenuItem booterSave = new JMenuItem("Save");
		booterSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//address::port::packetcount::packetcontent
				String saveContent = booterAddressField.getText() + "::" + booterPortField.getText() + "::" + booterPacketCount.getValue() + "::" + packetContent.getText();
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Specify a file to save"); 
				chooser.setApproveButtonText("Save");
				chooser.setFileFilter(new FileNameExtensionFilter("Text Document", "txt"));
				int selected = chooser.showSaveDialog(new JFrame("Save file"));
				if (selected == JFileChooser.APPROVE_OPTION) {
					try {
						FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".txt");
						fw.write(saveContent);
						fw.close();
					} catch (Exception e2) {
			            e2.printStackTrace();
			        }
				}
			}
		});
		mnFile.add(booterSave);
		
		JMenuItem booterLoad = new JMenuItem("Load");
		booterLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Specify a file to load");
				chooser.setApproveButtonText("Load");
				int selected = chooser.showOpenDialog(new JFrame("Load file"));
				if (selected == JFileChooser.APPROVE_OPTION) {
					File selectedFile = chooser.getSelectedFile();
					if (selectedFile.canRead()) {
						try {
							FileReader reader = new FileReader(selectedFile);
							BufferedReader buff = new BufferedReader(reader);
							String loadContent = buff.readLine();
							buff.close();
							if (loadContent != null) {
								String[] fields = loadContent.split("::");
								booterAddressField.setText(fields[0]);
								booterPortField.setText(fields[1]);
								booterPacketCount.setValue(Integer.parseInt(fields[2]));
								packetContent.setText(fields[3]);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
							logConsole("ERROR: Cannot read file!");
							return;
						}
					} else {
						logConsole("ERROR: Cannot read file!");
						return;
					}
				}
			}
		});
		mnFile.add(booterLoad);
		GroupLayout gl_layeredPane = new GroupLayout(layeredPane);
		gl_layeredPane.setHorizontalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addComponent(menuBar, GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
		);
		gl_layeredPane.setVerticalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane.createSequentialGroup()
					.addComponent(menuBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(1))
		);
		layeredPane.setLayout(gl_layeredPane);
		booterPacketCount.setToolTipText("You can adjust packet count here");
		
		
		JSlider booterSlider = new JSlider();
		booterSlider.setToolTipText("Packet count to send, do not use 0 or it will not work");
		booterSlider.setValue(1);
		booterSlider.setMinimum(-1);
		booterSlider.setMinorTickSpacing(50000);
		booterSlider.setPaintTicks(true);
		booterSlider.setMaximum(1000000);
		GroupLayout gl_packetCountPanel = new GroupLayout(packetCountPanel);
		gl_packetCountPanel.setHorizontalGroup(
			gl_packetCountPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(booterSlider, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
				.addGroup(gl_packetCountPanel.createSequentialGroup()
					.addGap(12)
					.addComponent(booterPacketCount, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
					.addGap(12))
		);
		gl_packetCountPanel.setVerticalGroup(
			gl_packetCountPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_packetCountPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(booterPacketCount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
					.addComponent(booterSlider, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
		);
		packetCountPanel.setLayout(gl_packetCountPanel);
		packetPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		booterSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				booterPacketCount.setValue(booterSlider.getValue());
			}
		});
		booterPacketCount.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if ((int)booterPacketCount.getValue() < -1)
					booterPacketCount.setValue(-1);
				if ((int)booterPacketCount.getValue() > 1000000)
					booterPacketCount.setValue(1000000);
				booterSlider.setValue((int) booterPacketCount.getValue());
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		packetPanel.add(scrollPane);
		packetContent.setFont(new Font("Monospaced", Font.PLAIN, 16));
		packetContent.setToolTipText("Type here content of your packet");
		scrollPane.setViewportView(packetContent);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		
		booterAddressField = new JTextField();
		booterAddressField.setToolTipText("Here you can enter ip or domain name");
		booterAddressField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		
		booterPortField = new JTextField();
		booterPortField.setToolTipText("port of ip");
		booterPortField.setColumns(10);
		GroupLayout gl_targetInfoPanel = new GroupLayout(targetInfoPanel);
		gl_targetInfoPanel.setHorizontalGroup(
			gl_targetInfoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_targetInfoPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_targetInfoPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_targetInfoPanel.createSequentialGroup()
							.addComponent(lblIpAddress)
							.addGap(18)
							.addComponent(booterAddressField, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
						.addGroup(gl_targetInfoPanel.createSequentialGroup()
							.addComponent(lblPort)
							.addGap(18)
							.addComponent(booterPortField, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_targetInfoPanel.setVerticalGroup(
			gl_targetInfoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_targetInfoPanel.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_targetInfoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(booterAddressField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblIpAddress))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_targetInfoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort)
						.addComponent(booterPortField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		targetInfoPanel.setLayout(gl_targetInfoPanel);
		
		JRadioButton radioTCP = new JRadioButton("Transmission Control Protocol");
		radioTCP.setToolTipText("TCP");
		radioTCP.setSelected(true);
		buttonGroup.add(radioTCP);
		
		JRadioButton radioUDP = new JRadioButton("User Datagram Protocol");
		radioUDP.setToolTipText("UDP");
		buttonGroup.add(radioUDP);
		GroupLayout gl_protocolPanel = new GroupLayout(protocolPanel);
		gl_protocolPanel.setHorizontalGroup(
			gl_protocolPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(radioTCP, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
				.addComponent(radioUDP, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
		);
		gl_protocolPanel.setVerticalGroup(
			gl_protocolPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_protocolPanel.createSequentialGroup()
					.addComponent(radioTCP, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addComponent(radioUDP, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
		);
		protocolPanel.setLayout(gl_protocolPanel);
		booterPanel.setLayout(gl_booterPanel);
		
		booterLaunch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String content = packetContent.getText();
				String address = booterAddressField.getText();
				int port = Integer.parseInt(booterPortField.getText());
				int packetCount = booterSlider.getValue();
				if (content.isEmpty() || content == null) {
					logConsole("ERROR: Packet content is empty");
					return;
				}
				if (address.isEmpty() || address == null) {
					logConsole("ERROR: Address field is empty");
					return;
				}
				if (port == 0) {
					logConsole("ERROR: Port cannot be 0");
					return;
				}
				if (packetCount == 0) {
					logConsole("ERROR: Packet count cannot be 0");
					return;
				}
				if (radioTCP.isSelected()) {
					tcpSender = new TCPPacketSender(address, port);
					if (packetCount == -1) {
						if (isRunning())
							t.interrupt();
						logConsole("Sending TCP packets!");
						t = new Thread(new Runnable() {

							@Override
							public void run() {
								booterProgbar.setIndeterminate(true);
								while (true) {
									tcpSender.sendStringPacket(content);
								}
							}
						});
						t.setDaemon(true);
						t.start();
					} else {
						tcpSender = new TCPPacketSender(address, port);
						if (isRunning())
							t.interrupt();
						logConsole("Starting sending TCP packets...");
						t = new Thread(new Runnable() {
							@Override
							public void run() {
								booterProgbar.setStringPainted(true);
								booterProgbar.setMaximum(packetCount+1);
								for (int i = 0; i < packetCount; i++) {
									booterProgbar.setValue(i);
									tcpSender.sendStringPacket(content);
								}
								booterProgbar.setStringPainted(false);
								tcpSender.closeSocket();
								logConsole("All TCP packets sended!");
							}
						});
						t.setDaemon(true);
						t.start();
					}
				} else if (radioUDP.isSelected()) {
					udpSender = new UDPPacketSender(address, port);
					if (packetCount == -1) {
						if (isRunning())
							t.interrupt();
						logConsole("Sending UDP packets!");
						t = new Thread(new Runnable() {

							@Override
							public void run() {
								booterProgbar.setIndeterminate(true);
								while (true) {
									udpSender.sendStringPacket(content);
								}
							}
						});
						t.setDaemon(true);
						t.start();
					} else {
						udpSender = new UDPPacketSender(address, port);
						logConsole("Starting sending UDP packets...");
						t = new Thread(new Runnable() {
							@Override
							public void run() {
								booterProgbar.setStringPainted(true);
								booterProgbar.setMaximum(packetCount+1);
								for (int i = 0; i < packetCount + 1; i++) {
									booterProgbar.setValue(i);
									udpSender.sendStringPacket(content);
								}
								booterProgbar.setStringPainted(false);
								udpSender.closeSocket();
								logConsole("All UDP packets sended!");
							}
						});
						t.setDaemon(true);
						t.start();
					}
				} else {
					logConsole("ERROR: Invalid state");
				}
			}
		});
		
		JPanel whoIsPanel = new JPanel();
		tabbedPane.addTab("WHOIS", null, whoIsPanel, null);
		
		JLayeredPane whoisSearchPanel = new JLayeredPane();
		whoisSearchPanel.setToolTipText("You can search ip's and domain names");
		whoisSearchPanel.setBorder(new TitledBorder(null, "Search WhoIs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLayeredPane whoisEditorPanel = new JLayeredPane();
		whoisEditorPanel.setBorder(new TitledBorder(null, "Editor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_whoIsPanel = new GroupLayout(whoIsPanel);
		gl_whoIsPanel.setHorizontalGroup(
			gl_whoIsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_whoIsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_whoIsPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(whoisEditorPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
						.addComponent(whoisSearchPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_whoIsPanel.setVerticalGroup(
			gl_whoIsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_whoIsPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(whoisSearchPanel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(whoisEditorPanel, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
					.addContainerGap())
		);
		whoisEditorPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		whoisEditorPanel.add(tabbedPane_1);
		
		JPanel panel_2 = new JPanel();
		tabbedPane_1.addTab("Editor", null, panel_2, null);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1);
		
		JEditorPane whoisEditor = new JEditorPane();
		scrollPane_1.setViewportView(whoisEditor);
		whoisEditor.setFont(new Font("Consolas", Font.PLAIN, 22));
		
		JPanel whoisTreePane = new JPanel();
		tabbedPane_1.addTab("Tree View", null, whoisTreePane, null);
		whoisTreePane.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane_5 = new JScrollPane();
		whoisTreePane.add(scrollPane_5);
		
		whoisSearchField = new JTextField();
		whoisSearchField.setColumns(10);
		
		JButton whoisSearchButton = new JButton("Search");
		GroupLayout gl_whoisSearchPanel = new GroupLayout(whoisSearchPanel);
		gl_whoisSearchPanel.setHorizontalGroup(
			gl_whoisSearchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_whoisSearchPanel.createSequentialGroup()
					.addGap(12)
					.addComponent(whoisSearchField, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
					.addGap(12)
					.addComponent(whoisSearchButton, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
					.addGap(6))
		);
		
		whoisSearchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logConsole("Searching domain/ip information of " + whoisSearchField.getText());
				whoisEditor.setEditorKitForContentType("text/xml", new XmlEditorKit());
		        whoisEditor.setContentType("text/xml");
				whoisEditor.setText(xmldata.getXMLContentAsString(whoisSearchField.getText()));
				//TODO
				xmlToTree = new XmlToTree(xmldata.getXMLContentAsDocument(whoisSearchField.getText()));
				if (xmlToTree.show() == null) {
					logConsole("ERROR: Tree not defined!");
					return;
				}
				JTree whoisXMLTree = xmlToTree.show();
				scrollPane_5.setViewportView(whoisXMLTree);
				whoisXMLTree.setVisible(true);
				xmlToTree.expandAllNodes();
				whoisXMLTree = xmlToTree.show();
				//TODO
				logConsole("Searching completed!");
			}
		});
		
		
		gl_whoisSearchPanel.setVerticalGroup(
			gl_whoisSearchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_whoisSearchPanel.createSequentialGroup()
					.addGap(7)
					.addGroup(gl_whoisSearchPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(whoisSearchField, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
						.addGroup(gl_whoisSearchPanel.createSequentialGroup()
							.addComponent(whoisSearchButton, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
							.addGap(1)))
					.addGap(6))
		);
		whoisSearchPanel.setLayout(gl_whoisSearchPanel);
		whoIsPanel.setLayout(gl_whoIsPanel);
		
		JPanel dnslookupPanel = new JPanel();
		tabbedPane.addTab("DNS Lookup", null, dnslookupPanel, null);
		
		JLayeredPane dnslookupSearchPanel = new JLayeredPane();
		dnslookupSearchPanel.setToolTipText("You can search ip's and domain names");
		dnslookupSearchPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Search DNS Records", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		dnsSearchField = new JTextField();
		dnsSearchField.setColumns(10);
		
		JButton dnsSearchButton = new JButton("Search");
		GroupLayout gl_dnslookupSearchPanel = new GroupLayout(dnslookupSearchPanel);
		gl_dnslookupSearchPanel.setHorizontalGroup(
			gl_dnslookupSearchPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 619, Short.MAX_VALUE)
				.addGroup(gl_dnslookupSearchPanel.createSequentialGroup()
					.addGap(12)
					.addComponent(dnsSearchField, GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
					.addGap(12)
					.addComponent(dnsSearchButton, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
					.addGap(6))
		);
		
		
		
		gl_dnslookupSearchPanel.setVerticalGroup(
			gl_dnslookupSearchPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 66, Short.MAX_VALUE)
				.addGroup(gl_dnslookupSearchPanel.createSequentialGroup()
					.addGap(7)
					.addGroup(gl_dnslookupSearchPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(dnsSearchField, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
						.addGroup(gl_dnslookupSearchPanel.createSequentialGroup()
							.addComponent(dnsSearchButton, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
							.addGap(1)))
					.addGap(6))
		);
		dnslookupSearchPanel.setLayout(gl_dnslookupSearchPanel);
		
		JLayeredPane dnsEditorPanel = new JLayeredPane();
		dnsEditorPanel.setBorder(new TitledBorder(null, "Editor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		dnsEditorPanel.setLayout(new GridLayout(1, 0, 0, 0));
		GroupLayout gl_dnslookupPanel = new GroupLayout(dnslookupPanel);
		gl_dnslookupPanel.setHorizontalGroup(
			gl_dnslookupPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_dnslookupPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_dnslookupPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(dnslookupSearchPanel, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
						.addComponent(dnsEditorPanel, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_dnslookupPanel.setVerticalGroup(
			gl_dnslookupPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_dnslookupPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(dnslookupSearchPanel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addComponent(dnsEditorPanel, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		dnsEditorPanel.add(tabbedPane_2);
		
		JPanel panel_4 = new JPanel();
		tabbedPane_2.addTab("Editor", null, panel_4, null);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane_3 = new JScrollPane();
		panel_4.add(scrollPane_3);
		
		JEditorPane dnsEditor = new JEditorPane();
		dnsEditor.setFont(new Font("Consolas", Font.PLAIN, 22));
		scrollPane_3.setViewportView(dnsEditor);
		
		JPanel dnsTreePanel = new JPanel();
		tabbedPane_2.addTab("Tree View", null, dnsTreePanel, null);
		dnsTreePanel.setLayout(new GridLayout(0, 1, 0, 0));
		dnslookupPanel.setLayout(gl_dnslookupPanel);
		
		JScrollPane scrollPane_6 = new JScrollPane();
		dnsTreePanel.add(scrollPane_6);
		
		dnsSearchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logConsole("Starting searching dns information of " + dnsSearchField.getText());
				dnsEditor.setEditorKitForContentType("text/xml", new XmlEditorKit());
				dnsEditor.setContentType("text/xml");
				dnsEditor.setText(dnsdata.getXMLContentAsString(dnsSearchField.getText()));
				//TODO
				xmlToTree = new XmlToTree(dnsdata.getXMLContentAsDocument(dnsSearchField.getText()));
				if (xmlToTree.show() == null) {
					logConsole("ERROR: Tree not defined!");
					return;
				}
				JTree dnsXMLTree = xmlToTree.show();
				scrollPane_6.setViewportView(dnsXMLTree);
				dnsXMLTree.setVisible(true);
				xmlToTree.expandAllNodes();
				dnsXMLTree = xmlToTree.show();
				dnsTreePanel.add(scrollPane_6);
				logConsole("Searching completed!");
			}
		});
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Converter", null, panel_1, "Here you can convert ip to domain and domain to ip");
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Domain to IP converter", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JLayeredPane layeredPane_2 = new JLayeredPane();
		layeredPane_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "IP to Domain coverter", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		iptdIpField = new JTextField();
		iptdIpField.setColumns(10);
		
		JButton iptdButton = new JButton("->");
		iptdButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (iptdIpField.getText().isEmpty()) {
					logConsole("ERROR: IP Field is empty!");
					return;
				}
				iptdDomainFIeld.setText(coverter.getDomain(iptdIpField.getText()));
				logConsole("Converted " + iptdIpField.getText() + " to " + coverter.getDomain(iptdIpField.getText()));
			}});
		
		iptdDomainFIeld = new JTextField();
		iptdDomainFIeld.setColumns(10);
		GroupLayout gl_layeredPane_2 = new GroupLayout(layeredPane_2);
		gl_layeredPane_2.setHorizontalGroup(
			gl_layeredPane_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(iptdIpField, GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(iptdButton, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(iptdDomainFIeld, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_layeredPane_2.setVerticalGroup(
			gl_layeredPane_2.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_layeredPane_2.createSequentialGroup()
					.addGap(25)
					.addGroup(gl_layeredPane_2.createParallelGroup(Alignment.BASELINE))
					.addGap(25))
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_layeredPane_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(iptdIpField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(iptdDomainFIeld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(iptdButton))
					.addContainerGap(40, Short.MAX_VALUE))
		);
		layeredPane_2.setLayout(gl_layeredPane_2);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addGap(79)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(layeredPane_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
						.addComponent(layeredPane_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE))
					.addGap(71))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(65)
					.addComponent(layeredPane_1, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(layeredPane_2, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(158, Short.MAX_VALUE))
		);
		
		dtipDomainField = new JTextField();
		dtipDomainField.setColumns(10);
		
		JButton dtipButton = new JButton("->");
		dtipButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (dtipDomainField.getText().isEmpty()) {
					logConsole("ERROR: Domain field is empty!");
					return;
				}
				dtipIpField.setText(coverter.getIP(dtipDomainField.getText()));
				logConsole("Converted " + dtipDomainField.getText() + " to " + coverter.getIP(dtipDomainField.getText()));
			}});
		
		dtipIpField = new JTextField();
		dtipIpField.setColumns(10);
		GroupLayout gl_layeredPane_1 = new GroupLayout(layeredPane_1);
		gl_layeredPane_1.setHorizontalGroup(
			gl_layeredPane_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(dtipDomainField, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
					.addGap(12)
					.addComponent(dtipButton, GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(dtipIpField, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_layeredPane_1.setVerticalGroup(
			gl_layeredPane_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_layeredPane_1.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(dtipDomainField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(dtipButton)
						.addComponent(dtipIpField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(40))
		);
		layeredPane_1.setLayout(gl_layeredPane_1);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("IP Scanner", null, panel_3, null);
		
		JLayeredPane ipscanPorts = new JLayeredPane();
		ipscanPorts.setBorder(new TitledBorder(null, "Port range", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLayeredPane ipscanControl = new JLayeredPane();
		ipscanControl.setBorder(new TitledBorder(null, "Control Panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLayeredPane ipscanLogPanel = new JLayeredPane();
		ipscanLogPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(ipscanPorts, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
						.addComponent(ipscanControl, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(ipscanLogPanel, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(40)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(ipscanLogPanel, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(ipscanPorts, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(ipscanControl, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)))
					.addContainerGap())
		);
		ipscanLogPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_4 = new JScrollPane();
		ipscanLogPanel.add(scrollPane_4);
		
		JEditorPane ipscanLog = new JEditorPane();
		scrollPane_4.setViewportView(ipscanLog);
		ipscanLog.setEditable(false);
		ipscanLog.setForeground(Color.GREEN);
		ipscanLog.setBackground(Color.BLACK);
		
		JButton ipscanButton = new JButton("Start Scanning");
		
		JCheckBox ipscanOpenPort = new JCheckBox("Open port");
		
		JCheckBox ipscanOSDetect = new JCheckBox("OS Detect");
		ipscanOSDetect.setEnabled(false);
		
		JLabel ipscanAddresLabel = new JLabel("Address");
		
		ipscanAddress = new JTextField();
		ipscanAddress.setColumns(10);
		
		JProgressBar ipscanProgbar = new JProgressBar();
		
		JButton ipscanCancel = new JButton("Cancel");
		GroupLayout gl_ipscanControl = new GroupLayout(ipscanControl);
		gl_ipscanControl.setHorizontalGroup(
			gl_ipscanControl.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_ipscanControl.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_ipscanControl.createParallelGroup(Alignment.LEADING)
						.addComponent(ipscanOpenPort, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
						.addComponent(ipscanOSDetect, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE))
					.addGap(8)
					.addGroup(gl_ipscanControl.createParallelGroup(Alignment.LEADING)
						.addComponent(ipscanProgbar, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
						.addComponent(ipscanButton, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
					.addGap(6))
				.addGroup(gl_ipscanControl.createSequentialGroup()
					.addContainerGap()
					.addComponent(ipscanAddress, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_ipscanControl.createSequentialGroup()
					.addContainerGap()
					.addComponent(ipscanAddresLabel, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(205, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, gl_ipscanControl.createSequentialGroup()
					.addGap(190)
					.addComponent(ipscanCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_ipscanControl.setVerticalGroup(
			gl_ipscanControl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ipscanControl.createSequentialGroup()
					.addGroup(gl_ipscanControl.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_ipscanControl.createSequentialGroup()
							.addGap(12)
							.addComponent(ipscanOpenPort)
							.addGap(5)
							.addComponent(ipscanOSDetect)
							.addPreferredGap(ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
							.addComponent(ipscanAddresLabel))
						.addGroup(gl_ipscanControl.createSequentialGroup()
							.addComponent(ipscanButton, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ipscanProgbar, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ipscanCancel, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ipscanAddress, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		ipscanControl.setLayout(gl_ipscanControl);
		
		ipscanCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				scanRunning = false;
			}
		});
		
		JLabel lblFromPort = new JLabel("From port");
		
		JLabel lblToPort = new JLabel("To port");
		
		JSpinner ipscanPort1 = new JSpinner();
		
		JSpinner ipscanPort2 = new JSpinner();
		GroupLayout gl_ipscanPorts = new GroupLayout(ipscanPorts);
		gl_ipscanPorts.setHorizontalGroup(
			gl_ipscanPorts.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ipscanPorts.createSequentialGroup()
					.addGap(33)
					.addGroup(gl_ipscanPorts.createParallelGroup(Alignment.TRAILING)
						.addComponent(ipscanPort1, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
						.addGroup(gl_ipscanPorts.createSequentialGroup()
							.addGap(35)
							.addComponent(lblFromPort, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addGap(7)
					.addGroup(gl_ipscanPorts.createParallelGroup(Alignment.TRAILING)
						.addComponent(ipscanPort2, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
						.addGroup(gl_ipscanPorts.createSequentialGroup()
							.addGap(50)
							.addComponent(lblToPort, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addGap(49))
		);
		gl_ipscanPorts.setVerticalGroup(
			gl_ipscanPorts.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ipscanPorts.createSequentialGroup()
					.addGroup(gl_ipscanPorts.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblToPort)
						.addComponent(lblFromPort, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_ipscanPorts.createParallelGroup(Alignment.BASELINE)
						.addComponent(ipscanPort1)
						.addComponent(ipscanPort2))
					.addGap(10))
		);
		ipscanPorts.setLayout(gl_ipscanPorts);
		panel_3.setLayout(gl_panel_3);
		
		ipscanButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ipscanAddress.getText().isEmpty() || ipscanAddress.getText() == null) {
					logConsole("ERROR: IP address not defined!");
					return;
				}
				if ((int)ipscanPort1.getValue() == 0 || (int)ipscanPort2.getValue() == 0) {
					logConsole("ERROR: Port cannot be 0");
					return;
				}
				if ((int)ipscanPort1.getValue() > (int)ipscanPort2.getValue()) {
					logConsole("ERROR: Port range cannot be negative!");
					return;
				}
				ipscanLog.setText(ipscanLog.getText()+"Startig scanning" + ipscanAddress.getText() + "\n");
				logConsole("Startig scanning" + ipscanAddress.getText());
				if (ipscanOpenPort.isSelected()) {
					if ((int)ipscanPort1.getValue() == (int)ipscanPort2.getValue()) {
						ipscanProgbar.setIndeterminate(true);
						scan = new Scan(ipscanAddress.getText(), 200);
						ipscanLog.setText(ipscanLog.getText()+scan.scanPort((int)ipscanPort1.getValue()));
						ipscanProgbar.setIndeterminate(false);
						scan.closeSocket();
					} else {
						ipscanLog.setText(ipscanLog.getText()+"Startig scanning" + ipscanAddress.getText() + "\n");
						new Thread(new Runnable() {
							@Override
							public void run() {
								scanRunning = true;
								ipscanProgbar.setStringPainted(true);
								ipscanProgbar.setMinimum((int)ipscanPort1.getValue());
								ipscanProgbar.setMaximum((int)ipscanPort2.getValue());
								for (int i = (int)ipscanPort1.getValue(); i < (int)ipscanPort2.getValue() + 1; i ++) {
									ipscanProgbar.setValue(i);
									scan = new Scan(ipscanAddress.getText(), 200);
									ipscanLog.setText(ipscanLog.getText()+scan.scanPort(i));
									if (!scanRunning)
										break;
								}
								ipscanProgbar.setStringPainted(false);
								ipscanProgbar.setMinimum(0);
								ipscanProgbar.setMaximum(100);
								ipscanProgbar.setValue(0);
								scan.closeSocket();
							}
						}).start();
					}
				}
				logConsole("Scan completed!");
				ipscanLog.setText(ipscanLog.getText()+"Scan completed!\n\n");
			}
		});
		
		JPanel aboutPanel = new JPanel();
		tabbedPane.addTab("About", null, aboutPanel, null);
		aboutPanel.setLayout(new BorderLayout(0, 0));
		
		Label label = new Label("About");
		label.setAlignment(Label.CENTER);
		label.setFont(new Font("Dialog", Font.PLAIN, 30));
		aboutPanel.add(label, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		aboutPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel.add(scrollPane_2);
		
		JTextArea txtrIpTools = new JTextArea();
		scrollPane_2.setViewportView(txtrIpTools);
		txtrIpTools.setEditable(false);
		txtrIpTools.setFont(new Font("Monospaced", Font.PLAIN, 20));
		txtrIpTools.setBackground(UIManager.getColor("Button.background"));
		txtrIpTools.setLineWrap(true);
		txtrIpTools.setText("IP Tools.\r\nVersion Beta 1.1\r\nGreat program to check usefull information of ip and it's domain. You can check ports, os information and dns records of domain with it. You can also test penerate ip's.\r\nEducation purposes only. I take no responsibility for any abuse or wrong use.\r\nThere is still plenty of features that are not avaible yet, but they will be soon!\r\nMade by videosambo\r\nMIT licensed");
		
		
		JPanel settingsPanel = new JPanel();
		tabbedPane.addTab("Settings", null, settingsPanel, null);
		
		JLayeredPane settingsLanguagePanel = new JLayeredPane();
		settingsLanguagePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Languages", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JLayeredPane settingsPacketSettingsPanel = new JLayeredPane();
		settingsPacketSettingsPanel.setBorder(new TitledBorder(null, "PacketSettings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLayeredPane settingsThemesPanel = new JLayeredPane();
		settingsThemesPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Themes", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JLayeredPane layeredPane_3 = new JLayeredPane();
		layeredPane_3.setBorder(new TitledBorder(null, "API Key", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_settingsPanel = new GroupLayout(settingsPanel);
		gl_settingsPanel.setHorizontalGroup(
			gl_settingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_settingsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(settingsPacketSettingsPanel, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_settingsPanel.createSequentialGroup()
							.addComponent(settingsLanguagePanel, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(settingsThemesPanel, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(layeredPane_3, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_settingsPanel.setVerticalGroup(
			gl_settingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_settingsPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(layeredPane_3, Alignment.LEADING)
						.addComponent(settingsLanguagePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
						.addComponent(settingsThemesPanel, Alignment.LEADING))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(settingsPacketSettingsPanel, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		settingsWHOISApiKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xmldata.setAPIKEY(settingsWHOISApiKey.getText());
				dnsdata.setAPIKEY(settingsWHOISApiKey.getText());
				logConsole("Using APIKEY '" + settingsWHOISApiKey.getText() + "'");
			}
		});
		logConsole("Using APIKEY '" + settingsWHOISApiKey.getText() + "'");
		settingsWHOISApiKey.setToolTipText("Enter whois api key here if you want to use whois and dns lookput");
		settingsWHOISApiKey.setColumns(10);
		
		JLabel lblWhoisApiKey = new JLabel("WHOIS API Key");
		lblWhoisApiKey.setToolTipText("My api key can run out of uses so you can enter your own");
		GroupLayout gl_layeredPane_3 = new GroupLayout(layeredPane_3);
		gl_layeredPane_3.setHorizontalGroup(
			gl_layeredPane_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_layeredPane_3.createParallelGroup(Alignment.LEADING)
						.addComponent(settingsWHOISApiKey, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
						.addComponent(lblWhoisApiKey))
					.addContainerGap())
		);
		gl_layeredPane_3.setVerticalGroup(
			gl_layeredPane_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblWhoisApiKey)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(settingsWHOISApiKey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(29, Short.MAX_VALUE))
		);
		layeredPane_3.setLayout(gl_layeredPane_3);
		settingsThemesPanel.setLayout(null);
		
		JMenuBar menuBar_2 = new JMenuBar();
		menuBar_2.setBounds(28, 43, 119, 26);
		settingsThemesPanel.add(menuBar_2);
		
		JMenu mnThemes = new JMenu("Themes");
		menuBar_2.add(mnThemes);
		
		JRadioButtonMenuItem rdbtnmntmLightTheme = new JRadioButtonMenuItem("Light Theme");
		buttonGroup_2.add(rdbtnmntmLightTheme);
		rdbtnmntmLightTheme.setSelected(true);
		mnThemes.add(rdbtnmntmLightTheme);
		
		JRadioButtonMenuItem rdbtnmntmDarkTheme = new JRadioButtonMenuItem("Dark Theme");
		rdbtnmntmDarkTheme.setEnabled(false);
		buttonGroup_2.add(rdbtnmntmDarkTheme);
		mnThemes.add(rdbtnmntmDarkTheme);
		settingsLanguagePanel.setLayout(null);
		
		JMenuBar menuBar_1 = new JMenuBar();
		menuBar_1.setBounds(23, 43, 119, 26);
		settingsLanguagePanel.add(menuBar_1);
		
		JMenu mnLanguage = new JMenu("Language");
		menuBar_1.add(mnLanguage);
		
		JRadioButtonMenuItem rdbtnmntmEnglish = new JRadioButtonMenuItem("English");
		buttonGroup_1.add(rdbtnmntmEnglish);
		rdbtnmntmEnglish.setSelected(true);
		mnLanguage.add(rdbtnmntmEnglish);
		
		JRadioButtonMenuItem rdbtnmntmFinnish = new JRadioButtonMenuItem("Finnish");
		buttonGroup_1.add(rdbtnmntmFinnish);
		rdbtnmntmFinnish.setEnabled(false);
		mnLanguage.add(rdbtnmntmFinnish);
		
		JSpinner settingsMaxPacketCount = new JSpinner();
		settingsMaxPacketCount.setEnabled(false);
		settingsMaxPacketCount.setModel(new SpinnerNumberModel(new Integer(1000000), null, null, new Integer(1)));
		
		JLabel settingsMaxPackets = new JLabel("Max Packet Count");
		GroupLayout gl_settingsPacketSettingsPanel = new GroupLayout(settingsPacketSettingsPanel);
		gl_settingsPacketSettingsPanel.setHorizontalGroup(
			gl_settingsPacketSettingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsPacketSettingsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_settingsPacketSettingsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(settingsMaxPackets, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
						.addComponent(settingsMaxPacketCount, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_settingsPacketSettingsPanel.setVerticalGroup(
			gl_settingsPacketSettingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsPacketSettingsPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(settingsMaxPackets)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(settingsMaxPacketCount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(163, Short.MAX_VALUE))
		);
		settingsPacketSettingsPanel.setLayout(gl_settingsPacketSettingsPanel);
		settingsPanel.setLayout(gl_settingsPanel);
		
		JPanel consolePanel = new JPanel();
		consolePanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Console", null, consolePanel, null);
		consolePanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		consoleLog.setEditable(false);
		consoleLog.setBackground(Color.BLACK);
		consoleLog.setForeground(Color.GREEN);
		consolePanel.add(consoleLog);
		
		
		
		logConsole("IP Tools Started!");
	}
	
	public static String getSettingsWHOISApiKey() {
		if (settingsWHOISApiKey != null) {
			if (settingsWHOISApiKey.getText() != null) {
				return settingsWHOISApiKey.getText();
			}
		}
		
		return "at_J7TntBlVBoWiwPElcLFbpdo9I30kC";
	}

	private boolean isRunning() {
		if (t != null)
			return true; 
		return false;
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
	}

	public JTextField getBooterAddressField() {
		return booterAddressField;
	}

	public JTextField getBooterPortField() {
		return booterPortField;
	}

	public JTextField getWhoisSearchField() {
		return whoisSearchField;
	}
	
	public GUI getInstance() {
		return this;
	}
	
	public void logConsole(String msg) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");  
		LocalDateTime now = LocalDateTime.now();
		String time = "["+dtf.format(now)+"] ";
		consoleLog.setText(consoleLog.getText() + "\n" + time + msg);
	}
}
