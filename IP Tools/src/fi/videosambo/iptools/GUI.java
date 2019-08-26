package fi.videosambo.iptools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JEditorPane;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import fi.videosambo.iptools.whois.GetXMLData;

public class GUI extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField booterAddressField;
	private JTextField booterPortField;
	private JTextField whoisSearchField;

	private GetXMLData xmldata = new GetXMLData();
	private JTextField dnsSearchField;
	private JTextField dtipDomainField;
	private JTextField dtipIpField;
	private JTextField iptdIpField;
	private JTextField iptdDomainFIeld;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		GroupLayout gl_booterPanel = new GroupLayout(booterPanel);
		gl_booterPanel.setHorizontalGroup(
			gl_booterPanel.createParallelGroup(Alignment.TRAILING)
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
					.addComponent(booterProgbar, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
					.addGap(29))
				.addGroup(gl_booterPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(booterSeparator, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(layeredPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
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
					.addGroup(gl_booterPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_booterPanel.createSequentialGroup()
							.addComponent(booterProgbar, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
							.addGap(18))
						.addGroup(gl_booterPanel.createSequentialGroup()
							.addComponent(booterLaunch, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		mnFile.add(mntmLoad);
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
		
		JSpinner spinner = new JSpinner();
		spinner.setToolTipText("You can adjust packet count here");
		
		JSlider slider = new JSlider();
		slider.setToolTipText("Packet count to send, do not use 0 or it will not work");
		slider.setValue(1);
		slider.setMinimum(-1);
		slider.setMinorTickSpacing(50000);
		slider.setPaintTicks(true);
		slider.setMaximum(1000000);
		GroupLayout gl_packetCountPanel = new GroupLayout(packetCountPanel);
		gl_packetCountPanel.setHorizontalGroup(
			gl_packetCountPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(slider, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
				.addGroup(gl_packetCountPanel.createSequentialGroup()
					.addGap(12)
					.addComponent(spinner, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
					.addGap(12))
		);
		gl_packetCountPanel.setVerticalGroup(
			gl_packetCountPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_packetCountPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
					.addComponent(slider, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
		);
		packetCountPanel.setLayout(gl_packetCountPanel);
		packetPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		packetPanel.add(scrollPane);
		
		JEditorPane packetContent = new JEditorPane();
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
		
		JScrollPane scrollPane_1 = new JScrollPane();
		whoisEditorPanel.add(scrollPane_1);
		
		JEditorPane whoisEditor = new JEditorPane();
		whoisEditor.setFont(new Font("Monospaced", Font.PLAIN, 25));
		scrollPane_1.setViewportView(whoisEditor);
		
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
				whoisEditor.setText(xmldata.getXMLContentAsString(whoisSearchField.getText()));
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
		
		JScrollPane scrollPane_3 = new JScrollPane();
		dnsEditorPanel.add(scrollPane_3);
		
		JEditorPane dnsEditor = new JEditorPane();
		scrollPane_3.setViewportView(dnsEditor);
		dnslookupPanel.setLayout(gl_dnslookupPanel);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Domain to IP", null, panel_1, null);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Domain to IP converter", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JLayeredPane layeredPane_2 = new JLayeredPane();
		layeredPane_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "IP to Domain coverter", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		iptdIpField = new JTextField();
		iptdIpField.setColumns(10);
		
		JButton iptdButton = new JButton("->");
		
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
		
		JPanel settingsPanel = new JPanel();
		tabbedPane.addTab("Settings", null, settingsPanel, null);
		
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
		txtrIpTools.setText("IP Tools.\r\nGreat program to check usefull information of ip and it's domain. You can check ports, os information and dns records of domain with it. You can also test penerate ip's.\r\nEducation purposes only. I take no responsibility for any abuse or wrong use.\r\nMade by videosambo\r\nMIT licensed");
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Console", null, panel_2, null);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JEditorPane dtrpnDawwdnajosd = new JEditorPane();
		dtrpnDawwdnajosd.setEditable(false);
		dtrpnDawwdnajosd.setForeground(Color.GREEN);
		dtrpnDawwdnajosd.setBackground(Color.BLACK);
		panel_2.add(dtrpnDawwdnajosd);
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
}
