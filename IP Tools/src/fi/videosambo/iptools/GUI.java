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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import fi.videosambo.iptools.Lang.Language;
import fi.videosambo.iptools.booter.TCPPacketSender;
import fi.videosambo.iptools.booter.UDPPacketSender;
import fi.videosambo.iptools.ipscanner.Scan;
import fi.videosambo.iptools.iptool.Converter;
import fi.videosambo.iptools.whois.GetAddressXMLData;
import fi.videosambo.iptools.whois.GetDNSRecordXMLData;
import net.boplicity.xmleditor.XmlEditorKit;

public class GUI extends JFrame {

	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();

	private JPanel contentPane;

	private Converter coverter = new Converter();

	private GetDNSRecordXMLData dnsdata = new GetDNSRecordXMLData();
	private GetAddressXMLData xmldata = new GetAddressXMLData();

	private TCPPacketSender tcpSender;
	private UDPPacketSender udpSender;

	private JTextField booterAddressField, booterPortField, dnsSearchField, whoisSearchField, dtipDomainField,
			dtipIpField, iptdIpField, iptdDomainFIeld, ipscanAddress;

	JEditorPane packetContent, whoisEditor, dnsEditor, ipscanLog;

	private XmlToTree xmlToTree;
	private Scan scan;
	private Lang lang;

	private boolean scanRunning = false, booterRunning = false;
	private String settingsFile = "./settings.txt";

	private static JTextField settingsWHOISApiKey;

	JEditorPane consoleLog;

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
		
		lang = new Lang(this, Language.EN);

		settingsWHOISApiKey = new JTextField();
		consoleLog = new JEditorPane();
		settingsWHOISApiKey.setText("at_J7TntBlVBoWiwPElcLFbpdo9I30kC");

		setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/img/iptoolsicon.png")));
		setTitle("IP Tools");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 477);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		/*
		 * Creating elements
		 */

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JTabbedPane whoisEditorPanelFrame = new JTabbedPane(JTabbedPane.TOP);
		JTabbedPane dnsEditorPanelFrame = new JTabbedPane(JTabbedPane.TOP);
		JPanel booterPanel = new JPanel();
		JPanel whoIsPanel = new JPanel();
		JPanel dnslookupPanel = new JPanel();
		JPanel whoisTreePane = new JPanel();
		JPanel panel_4 = new JPanel();
		JPanel converterPanel = new JPanel();
		JPanel dnsTreePanel = new JPanel();
		JPanel panel_2 = new JPanel();
		JPanel ipscannerPanel = new JPanel();
		JPanel aboutTextFieldPanel = new JPanel();
		JPanel consolePanel = new JPanel();
		JPanel aboutPanel = new JPanel();
		JPanel settingsPanel = new JPanel();
		JMenuBar menuBar = new JMenuBar();
		JMenuBar menuBar_2 = new JMenuBar();
		JMenuBar menuBar_1 = new JMenuBar();
		JLayeredPane protocolPanel = new JLayeredPane();
		JLayeredPane targetInfoPanel = new JLayeredPane();
		JLayeredPane packetPanel = new JLayeredPane();
		JLayeredPane packetCountPanel = new JLayeredPane();
		JLayeredPane layeredPane = new JLayeredPane();
		JLayeredPane whoisSearchPanel = new JLayeredPane();
		JLayeredPane whoisEditorPanel = new JLayeredPane();
		JLayeredPane dnsEditorPanel = new JLayeredPane();
		JLayeredPane dnslookupSearchPanel = new JLayeredPane();
		JLayeredPane converterDTIPPanel = new JLayeredPane();
		JLayeredPane converterIPTDPanel = new JLayeredPane();
		JLayeredPane settingsLanguagePanel = new JLayeredPane();
		JLayeredPane settingsPacketSettingsPanel = new JLayeredPane();
		JLayeredPane settingsThemesPanel = new JLayeredPane();
		JLayeredPane settingsAPIPanel = new JLayeredPane();
		JLayeredPane ipscanPorts = new JLayeredPane();
		JLayeredPane ipscanControl = new JLayeredPane();
		JLayeredPane ipscanLogPanel = new JLayeredPane();
		packetContent = new JEditorPane();
		whoisEditor = new JEditorPane();
		dnsEditor = new JEditorPane();
		ipscanLog = new JEditorPane();
		JMenu mnFile = new JMenu(lang.getMessage("lang.file"));
		JMenu mnThemes = new JMenu(lang.getMessage("lang.themes"));
		JMenu mnLanguage = new JMenu(lang.getMessage("lang.language"));
		JMenuItem booterSave = new JMenuItem(lang.getMessage("lang.save"));
		JMenuItem booterLoad = new JMenuItem(lang.getMessage("lang.load"));
		JRadioButtonMenuItem rdbtnmntmLightTheme = new JRadioButtonMenuItem(lang.getMessage("lang.theme-light"));
		JRadioButtonMenuItem rdbtnmntmDarkTheme = new JRadioButtonMenuItem(lang.getMessage("lang.theme-dark"));
		JRadioButtonMenuItem rdbtnmntmEnglish = new JRadioButtonMenuItem(lang.getMessage("lang.language-list.english"));
		JRadioButtonMenuItem rdbtnmntmFinnish = new JRadioButtonMenuItem(lang.getMessage("lang.language-list.finnish"));
		JSeparator booterSeparator = new JSeparator();
		JButton booterLaunch = new JButton(lang.getMessage("lang.attack"));
		JButton booterSTOP = new JButton(lang.getMessage("lang.stop"));
		JButton whoisSearchButton = new JButton(lang.getMessage("lang.search"));
		JButton dnsSearchButton = new JButton(lang.getMessage("lang.search"));
		JButton iptdButton = new JButton("->");
		JButton dtipButton = new JButton("->");
		JButton ipscanButton = new JButton(lang.getMessage("lang.start-scan"));
		JButton ipscanCancel = new JButton(lang.getMessage("lang.cancel"));
		JButton btnSave = new JButton(lang.getMessage("lang.save"));
		JProgressBar booterProgbar = new JProgressBar();
		JProgressBar ipscanProgbar = new JProgressBar();
		GroupLayout gl_booterPanel = new GroupLayout(booterPanel);
		GroupLayout gl_layeredPane = new GroupLayout(layeredPane);
		GroupLayout gl_packetCountPanel = new GroupLayout(packetCountPanel);
		GroupLayout gl_targetInfoPanel = new GroupLayout(targetInfoPanel);
		GroupLayout gl_protocolPanel = new GroupLayout(protocolPanel);
		GroupLayout gl_whoIsPanel = new GroupLayout(whoIsPanel);
		GroupLayout gl_whoisSearchPanel = new GroupLayout(whoisSearchPanel);
		GroupLayout gl_dnslookupSearchPanel = new GroupLayout(dnslookupSearchPanel);
		GroupLayout gl_dnslookupPanel = new GroupLayout(dnslookupPanel);
		GroupLayout gl_converterIPTDPanel = new GroupLayout(converterIPTDPanel);
		GroupLayout gl_converterPanel = new GroupLayout(converterPanel);
		GroupLayout gl_converterDTIPPanel = new GroupLayout(converterDTIPPanel);
		GroupLayout gl_settingsPacketSettingsPanel = new GroupLayout(settingsPacketSettingsPanel);
		GroupLayout gl_settingsPanel = new GroupLayout(settingsPanel);
		
		
		gl_settingsPanel.setHorizontalGroup(
			gl_settingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_settingsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_settingsPanel.createSequentialGroup()
							.addComponent(settingsPacketSettingsPanel, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 339, Short.MAX_VALUE)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_settingsPanel.createSequentialGroup()
							.addComponent(settingsLanguagePanel, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(settingsThemesPanel, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(settingsAPIPanel, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_settingsPanel.setVerticalGroup(
			gl_settingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_settingsPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(settingsAPIPanel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(settingsLanguagePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
						.addComponent(settingsThemesPanel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_settingsPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(settingsPacketSettingsPanel, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		GroupLayout gl_settingsAPIPanel = new GroupLayout(settingsAPIPanel);
		GroupLayout gl_ipscannerPanel = new GroupLayout(ipscannerPanel);
		GroupLayout gl_ipscanControl = new GroupLayout(ipscanControl);
		GroupLayout gl_ipscanPorts = new GroupLayout(ipscanPorts);
		JSpinner booterPacketCount = new JSpinner();
		JSpinner ipscanPort1 = new JSpinner();
		JSpinner ipscanPort2 = new JSpinner();
		JSpinner settingsMaxPacketCount = new JSpinner();
		JSlider booterSlider = new JSlider();
		JScrollPane scrollPane = new JScrollPane();
		JScrollPane scrollPane_1 = new JScrollPane();
		JScrollPane whoisTreeview = new JScrollPane();
		JScrollPane scrollPane_3 = new JScrollPane();
		JScrollPane dnsTreeview = new JScrollPane();
		JScrollPane scrollPane_2 = new JScrollPane();
		JScrollPane scrollPane_4 = new JScrollPane();
		JLabel lblIpAddress = new JLabel(lang.getMessage("lang.ip-address"));
		JLabel lblPort = new JLabel(lang.getMessage("lang.port"));
		JLabel settingsMaxPackets = new JLabel(lang.getMessage("lang.max-packet-count"));
		JLabel lblWhoisApiKey = new JLabel(lang.getMessage("lang.whois-api-key"));
		JLabel ipscanAddresLabel = new JLabel(lang.getMessage("lang.address"));
		JLabel lblFromPort = new JLabel(lang.getMessage("lang.from-port"));
		JLabel lblToPort = new JLabel(lang.getMessage("lang.to-port"));
		Label aboutLabel = new Label(lang.getMessage("lang.tabs.about"));
		JRadioButton radioTCP = new JRadioButton(lang.getMessage("lang.tcp-long"));
		JRadioButton radioUDP = new JRadioButton(lang.getMessage("lang.udp-long"));
		JTextArea txtrIpTools = new JTextArea();
		JCheckBox ipscanOpenPort = new JCheckBox(lang.getMessage("lang.open-port"));
		JCheckBox ipscanOSDetect = new JCheckBox(lang.getMessage("lang.os-detect"));

		//lang.getMessage("lang.")
		
		/*
		 * Specifying elements
		 */

		contentPane.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab("Booter", null, booterPanel, null);

		protocolPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), lang.getMessage("lang.layer-panel.protocol"),
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		targetInfoPanel.setBorder(
				new TitledBorder(null, lang.getMessage("lang.layer-panel.target-info"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

		packetPanel.setBorder(
				new TitledBorder(null, lang.getMessage("lang.layer-panel.packet-info"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

		packetCountPanel
				.setBorder(new TitledBorder(null, lang.getMessage("lang.layer-panel.packet-count"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

		booterLaunch.setToolTipText(lang.getMessage("lang.tooltips.send-packets"));

		layeredPane.setBorder(null);

		gl_booterPanel.setHorizontalGroup(gl_booterPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_booterPanel.createSequentialGroup().addGap(35)
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
				.addGroup(
						gl_booterPanel.createSequentialGroup().addGap(25)
								.addComponent(booterLaunch, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
								.addGap(18).addComponent(booterProgbar, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(booterSTOP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addContainerGap())
				.addGroup(gl_booterPanel.createSequentialGroup().addContainerGap()
						.addComponent(booterSeparator, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
						.addContainerGap())
				.addComponent(layeredPane, GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE));
		gl_booterPanel.setVerticalGroup(gl_booterPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_booterPanel
				.createSequentialGroup()
				.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_booterPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(targetInfoPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 100,
								Short.MAX_VALUE)
						.addComponent(protocolPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_booterPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(packetCountPanel, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
						.addGroup(gl_booterPanel.createSequentialGroup().addGap(2).addComponent(packetPanel,
								GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(booterSeparator, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE).addGap(18)
				.addGroup(gl_booterPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(booterLaunch, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(booterProgbar, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 35,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(booterSTOP, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 35,
								GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));

		menuBar.add(mnFile);

		mnFile.add(booterSave);

		mnFile.add(booterLoad);
		gl_layeredPane.setHorizontalGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING).addComponent(menuBar,
				GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE));
		gl_layeredPane.setVerticalGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane.createSequentialGroup()
						.addComponent(menuBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(1)));
		layeredPane.setLayout(gl_layeredPane);
		booterPacketCount.setToolTipText(lang.getMessage("lang.tooltips.adjust-count"));

		booterSlider.setToolTipText(lang.getMessage("lang.tooltips.zero-packet-count"));
		booterSlider.setValue(1);
		booterSlider.setMinimum(-1);
		booterSlider.setMinorTickSpacing(50000);
		booterSlider.setPaintTicks(true);
		booterSlider.setMaximum(1000000);
		gl_packetCountPanel.setHorizontalGroup(gl_packetCountPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(booterSlider, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
				.addGroup(gl_packetCountPanel.createSequentialGroup().addGap(12)
						.addComponent(booterPacketCount, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE).addGap(12)));
		gl_packetCountPanel.setVerticalGroup(gl_packetCountPanel.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_packetCountPanel.createSequentialGroup().addContainerGap()
						.addComponent(booterPacketCount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
						.addComponent(booterSlider, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)));
		packetCountPanel.setLayout(gl_packetCountPanel);
		packetPanel.setLayout(new GridLayout(1, 0, 0, 0));

		packetPanel.add(scrollPane);
		packetContent.setFont(new Font("Monospaced", Font.PLAIN, 16));
		packetContent.setToolTipText(lang.getMessage("lang.tooltips.packet-content"));
		scrollPane.setViewportView(packetContent);

		booterAddressField = new JTextField();
		booterAddressField.setToolTipText(lang.getMessage("lang.tooltips.booter-address-field"));
		booterAddressField.setColumns(10);

		booterPortField = new JTextField();
		booterPortField.setToolTipText(lang.getMessage("lang.tooltips.booter-port"));
		booterPortField.setColumns(10);
		gl_targetInfoPanel.setHorizontalGroup(gl_targetInfoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_targetInfoPanel.createSequentialGroup().addContainerGap().addGroup(gl_targetInfoPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_targetInfoPanel.createSequentialGroup().addComponent(lblIpAddress).addGap(18)
								.addComponent(booterAddressField, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
						.addGroup(gl_targetInfoPanel.createSequentialGroup().addComponent(lblPort).addGap(18)
								.addComponent(booterPortField, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_targetInfoPanel.setVerticalGroup(gl_targetInfoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_targetInfoPanel.createSequentialGroup().addGap(5)
						.addGroup(gl_targetInfoPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(booterAddressField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblIpAddress))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_targetInfoPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblPort)
								.addComponent(booterPortField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		targetInfoPanel.setLayout(gl_targetInfoPanel);

		radioTCP.setToolTipText(lang.getMessage("lang.tooltips.tcp"));
		radioTCP.setSelected(true);
		buttonGroup.add(radioTCP);

		radioUDP.setToolTipText(lang.getMessage("lang.tooltips.udp"));
		buttonGroup.add(radioUDP);
		gl_protocolPanel.setHorizontalGroup(gl_protocolPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(radioTCP, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
				.addComponent(radioUDP, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE));
		gl_protocolPanel.setVerticalGroup(gl_protocolPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_protocolPanel.createSequentialGroup()
						.addComponent(radioTCP, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addComponent(radioUDP, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)));
		protocolPanel.setLayout(gl_protocolPanel);
		booterPanel.setLayout(gl_booterPanel);

		tabbedPane.addTab(lang.getMessage("lang.tabs.whois"), null, whoIsPanel, null);

		whoisSearchPanel.setToolTipText(lang.getMessage("lang.tooltips.whois-address"));
		whoisSearchPanel
				.setBorder(new TitledBorder(null, lang.getMessage("lang.layer-panel.search-whois"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		whoisEditorPanel
				.setBorder(new TitledBorder(null, lang.getMessage("lang.editor"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		gl_whoIsPanel.setHorizontalGroup(gl_whoIsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_whoIsPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_whoIsPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(whoisEditorPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 619,
										Short.MAX_VALUE)
								.addComponent(whoisSearchPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 619,
										Short.MAX_VALUE))
						.addContainerGap()));
		gl_whoIsPanel.setVerticalGroup(gl_whoIsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_whoIsPanel.createSequentialGroup().addContainerGap()
						.addComponent(whoisSearchPanel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(whoisEditorPanel, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
						.addContainerGap()));
		whoisEditorPanel.setLayout(new GridLayout(1, 0, 0, 0));

		whoisEditorPanel.add(whoisEditorPanelFrame);

		whoisEditorPanelFrame.addTab(lang.getMessage("lang.editor"), null, panel_2, null);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));

		panel_2.add(scrollPane_1);

		scrollPane_1.setViewportView(whoisEditor);
		whoisEditor.setFont(new Font("Consolas", Font.PLAIN, 22));

		whoisEditorPanelFrame.addTab(lang.getMessage("lang.tree-view"), null, whoisTreePane, null);
		whoisTreePane.setLayout(new GridLayout(0, 1, 0, 0));

		whoisTreePane.add(whoisTreeview);

		whoisSearchField = new JTextField();
		whoisSearchField.setColumns(10);

		gl_whoisSearchPanel.setHorizontalGroup(gl_whoisSearchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_whoisSearchPanel.createSequentialGroup().addGap(12)
						.addComponent(whoisSearchField, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE).addGap(12)
						.addComponent(whoisSearchButton, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
						.addGap(6)));

		gl_whoisSearchPanel.setVerticalGroup(gl_whoisSearchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_whoisSearchPanel.createSequentialGroup().addGap(7)
						.addGroup(gl_whoisSearchPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(whoisSearchField, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
								.addGroup(gl_whoisSearchPanel.createSequentialGroup()
										.addComponent(whoisSearchButton, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
										.addGap(1)))
						.addGap(6)));
		whoisSearchPanel.setLayout(gl_whoisSearchPanel);
		whoIsPanel.setLayout(gl_whoIsPanel);

		tabbedPane.addTab(lang.getMessage("lang.tabs.dns-lookup"), null, dnslookupPanel, null);

		dnslookupSearchPanel.setToolTipText(lang.getMessage("lang.tooltips.whois-address"));
		dnslookupSearchPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				lang.getMessage("lang.layer-panel.search-dns"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		dnsSearchField = new JTextField();
		dnsSearchField.setColumns(10);

		gl_dnslookupSearchPanel.setHorizontalGroup(gl_dnslookupSearchPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 619, Short.MAX_VALUE)
				.addGroup(gl_dnslookupSearchPanel.createSequentialGroup().addGap(12)
						.addComponent(dnsSearchField, GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE).addGap(12)
						.addComponent(dnsSearchButton, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
						.addGap(6)));

		gl_dnslookupSearchPanel.setVerticalGroup(gl_dnslookupSearchPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 66, Short.MAX_VALUE)
				.addGroup(gl_dnslookupSearchPanel.createSequentialGroup().addGap(7)
						.addGroup(gl_dnslookupSearchPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(dnsSearchField, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
								.addGroup(gl_dnslookupSearchPanel.createSequentialGroup()
										.addComponent(dnsSearchButton, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
										.addGap(1)))
						.addGap(6)));
		dnslookupSearchPanel.setLayout(gl_dnslookupSearchPanel);

		dnsEditorPanel.setBorder(new TitledBorder(null, lang.getMessage("lang.editor"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		dnsEditorPanel.setLayout(new GridLayout(1, 0, 0, 0));
		gl_dnslookupPanel.setHorizontalGroup(gl_dnslookupPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_dnslookupPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_dnslookupPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(dnslookupSearchPanel, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
								.addComponent(dnsEditorPanel, GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE))
						.addContainerGap()));
		gl_dnslookupPanel.setVerticalGroup(gl_dnslookupPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_dnslookupPanel.createSequentialGroup().addContainerGap()
						.addComponent(dnslookupSearchPanel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
						.addGap(7).addComponent(dnsEditorPanel, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
						.addContainerGap()));

		dnsEditorPanel.add(dnsEditorPanelFrame);

		dnsEditorPanelFrame.addTab(lang.getMessage("lang.editor"), null, panel_4, null);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));

		panel_4.add(scrollPane_3);

		dnsEditor.setFont(new Font("Consolas", Font.PLAIN, 22));
		scrollPane_3.setViewportView(dnsEditor);

		dnsEditorPanelFrame.addTab(lang.getMessage("lang.tree-view"), null, dnsTreePanel, null);
		dnsTreePanel.setLayout(new GridLayout(0, 1, 0, 0));
		dnslookupPanel.setLayout(gl_dnslookupPanel);

		dnsTreePanel.add(dnsTreeview);

		tabbedPane.addTab(lang.getMessage("lang.tabs.converter"), null, converterPanel, lang.getMessage("lang.tooltips.converter"));

		converterDTIPPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				lang.getMessage("lang.layer-panel.dtip"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		converterIPTDPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				lang.getMessage("lang.layer-panel.iptd"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		iptdIpField = new JTextField();
		iptdIpField.setColumns(10);

		iptdDomainFIeld = new JTextField();
		iptdDomainFIeld.setColumns(10);
		gl_converterIPTDPanel.setHorizontalGroup(gl_converterIPTDPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_converterIPTDPanel.createSequentialGroup().addContainerGap()
						.addComponent(iptdIpField, GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(iptdButton, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(iptdDomainFIeld, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
						.addContainerGap()));
		gl_converterIPTDPanel.setVerticalGroup(gl_converterIPTDPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING,
						gl_converterIPTDPanel.createSequentialGroup().addGap(25)
								.addGroup(gl_converterIPTDPanel.createParallelGroup(Alignment.BASELINE)).addGap(25))
				.addGroup(gl_converterIPTDPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_converterIPTDPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(iptdIpField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(iptdDomainFIeld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(iptdButton))
						.addContainerGap(40, Short.MAX_VALUE)));
		converterIPTDPanel.setLayout(gl_converterIPTDPanel);
		gl_converterPanel.setHorizontalGroup(gl_converterPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_converterPanel.createSequentialGroup().addGap(79)
						.addGroup(gl_converterPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(converterIPTDPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 493,
										Short.MAX_VALUE)
								.addComponent(converterDTIPPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 359,
										Short.MAX_VALUE))
						.addGap(71)));
		gl_converterPanel.setVerticalGroup(gl_converterPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_converterPanel.createSequentialGroup().addGap(65)
						.addComponent(converterDTIPPanel, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(converterIPTDPanel, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(158, Short.MAX_VALUE)));

		dtipDomainField = new JTextField();
		dtipDomainField.setColumns(10);

		dtipIpField = new JTextField();
		dtipIpField.setColumns(10);
		gl_converterDTIPPanel.setHorizontalGroup(gl_converterDTIPPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_converterDTIPPanel.createSequentialGroup().addContainerGap()
						.addComponent(dtipDomainField, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE).addGap(12)
						.addComponent(dtipButton, GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(dtipIpField, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE).addContainerGap()));
		gl_converterDTIPPanel.setVerticalGroup(gl_converterDTIPPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_converterDTIPPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_converterDTIPPanel.createParallelGroup(Alignment.BASELINE, false)
								.addComponent(dtipDomainField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(dtipButton).addComponent(dtipIpField, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(40)));
		converterDTIPPanel.setLayout(gl_converterDTIPPanel);
		converterPanel.setLayout(gl_converterPanel);

		tabbedPane.addTab(lang.getMessage("lang.tabs.ip-scanner"), null, ipscannerPanel, null);

		ipscanPorts.setBorder(new TitledBorder(null, lang.getMessage("lang.layer-panel.port-range"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		ipscanControl
				.setBorder(new TitledBorder(null, lang.getMessage("lang.layer-panel.control-panel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

		ipscanLogPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));

		gl_ipscannerPanel
				.setHorizontalGroup(gl_ipscannerPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_ipscannerPanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_ipscannerPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(ipscanPorts, GroupLayout.PREFERRED_SIZE, 285,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(ipscanControl, GroupLayout.PREFERRED_SIZE, 285,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(ipscanLogPanel, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
								.addContainerGap()));
		gl_ipscannerPanel.setVerticalGroup(gl_ipscannerPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ipscannerPanel.createSequentialGroup().addGap(40).addGroup(gl_ipscannerPanel
						.createParallelGroup(Alignment.LEADING)
						.addComponent(ipscanLogPanel, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
						.addGroup(gl_ipscannerPanel.createSequentialGroup()
								.addComponent(ipscanPorts, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(ipscanControl, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)))
						.addContainerGap()));
		ipscanLogPanel.setLayout(new GridLayout(1, 0, 0, 0));

		ipscanLogPanel.add(scrollPane_4);

		scrollPane_4.setViewportView(ipscanLog);
		ipscanLog.setEditable(false);
		ipscanLog.setForeground(Color.GREEN);
		ipscanLog.setBackground(Color.BLACK);

		ipscanOSDetect.setEnabled(false);

		ipscanAddress = new JTextField();
		ipscanAddress.setColumns(10);

		gl_ipscanControl
				.setHorizontalGroup(gl_ipscanControl.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_ipscanControl.createSequentialGroup().addGap(6)
								.addGroup(gl_ipscanControl.createParallelGroup(Alignment.LEADING)
										.addComponent(ipscanOpenPort, GroupLayout.PREFERRED_SIZE, 113,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(ipscanOSDetect, GroupLayout.PREFERRED_SIZE, 113,
												GroupLayout.PREFERRED_SIZE))
								.addGap(8)
								.addGroup(gl_ipscanControl.createParallelGroup(Alignment.LEADING)
										.addComponent(ipscanProgbar, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
										.addComponent(ipscanButton, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
								.addGap(6))
						.addGroup(gl_ipscanControl.createSequentialGroup().addContainerGap()
								.addComponent(ipscanAddress, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
								.addContainerGap())
						.addGroup(gl_ipscanControl.createSequentialGroup().addContainerGap()
								.addComponent(ipscanAddresLabel, GroupLayout.PREFERRED_SIZE, 56,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(205, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING,
								gl_ipscanControl
										.createSequentialGroup().addGap(190).addComponent(ipscanCancel,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addContainerGap()));
		gl_ipscanControl.setVerticalGroup(gl_ipscanControl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ipscanControl.createSequentialGroup()
						.addGroup(gl_ipscanControl.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_ipscanControl.createSequentialGroup().addGap(12)
										.addComponent(ipscanOpenPort).addGap(5).addComponent(ipscanOSDetect)
										.addPreferredGap(ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
										.addComponent(ipscanAddresLabel))
								.addGroup(
										gl_ipscanControl.createSequentialGroup()
												.addComponent(ipscanButton, GroupLayout.PREFERRED_SIZE, 49,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(ipscanProgbar, GroupLayout.PREFERRED_SIZE, 31,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(ipscanCancel,
														GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(ipscanAddress, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		ipscanControl.setLayout(gl_ipscanControl);

		gl_ipscanPorts.setHorizontalGroup(gl_ipscanPorts.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ipscanPorts.createSequentialGroup().addGap(33)
						.addGroup(gl_ipscanPorts.createParallelGroup(Alignment.TRAILING)
								.addComponent(ipscanPort1, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
								.addGroup(gl_ipscanPorts.createSequentialGroup().addGap(35).addComponent(lblFromPort,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGap(7)
						.addGroup(gl_ipscanPorts.createParallelGroup(Alignment.TRAILING)
								.addComponent(ipscanPort2, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
								.addGroup(gl_ipscanPorts.createSequentialGroup().addGap(50).addComponent(lblToPort,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGap(49)));
		gl_ipscanPorts.setVerticalGroup(gl_ipscanPorts.createParallelGroup(Alignment.LEADING).addGroup(gl_ipscanPorts
				.createSequentialGroup()
				.addGroup(gl_ipscanPorts.createParallelGroup(Alignment.BASELINE).addComponent(lblToPort)
						.addComponent(lblFromPort, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_ipscanPorts
						.createParallelGroup(Alignment.BASELINE).addComponent(ipscanPort1).addComponent(ipscanPort2))
				.addGap(10)));
		ipscanPorts.setLayout(gl_ipscanPorts);
		ipscannerPanel.setLayout(gl_ipscannerPanel);

		tabbedPane.addTab(lang.getMessage("lang.tabs.about"), null, aboutPanel, null);
		aboutPanel.setLayout(new BorderLayout(0, 0));

		aboutLabel.setAlignment(Label.CENTER);
		aboutLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		aboutPanel.add(aboutLabel, BorderLayout.NORTH);

		aboutPanel.add(aboutTextFieldPanel, BorderLayout.CENTER);
		aboutTextFieldPanel.setLayout(new GridLayout(0, 1, 0, 0));

		aboutTextFieldPanel.add(scrollPane_2);

		scrollPane_2.setViewportView(txtrIpTools);
		txtrIpTools.setEditable(false);
		txtrIpTools.setFont(new Font("Monospaced", Font.PLAIN, 20));
		txtrIpTools.setBackground(UIManager.getColor("Button.background"));
		txtrIpTools.setLineWrap(true);
		txtrIpTools.setText(
				"IP Tools.\r\nVersion Beta 1.2\r\nGreat program to check usefull information of ip and it's domain. You can check ports, os information and dns records of domain with it. You can also test penerate ip's.\r\nEducation purposes only. I take no responsibility for any abuse or wrong use.\r\nThere is still plenty of features that are not avaible yet, but they will be soon!\r\nMade by videosambo\r\nMIT licensed");

		tabbedPane.addTab(lang.getMessage("lang.tabs.settings"), null, settingsPanel, null);

		settingsLanguagePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), lang.getMessage("lang.languages"),
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		settingsPacketSettingsPanel.setBorder(
				new TitledBorder(null, lang.getMessage("lang.layer-panel.packet-settings"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

		settingsThemesPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), lang.getMessage("lang.themes"),
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		settingsAPIPanel
				.setBorder(new TitledBorder(null, lang.getMessage("lang.layer-panel.api-key"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

		logConsole("Using APIKEY '" + settingsWHOISApiKey.getText() + "'");
		settingsWHOISApiKey.setToolTipText(lang.getMessage("lang.tooltips.apikey"));
		settingsWHOISApiKey.setColumns(10);

		lblWhoisApiKey.setToolTipText(lang.getMessage("lang.tooltips.apikey-label"));
		gl_settingsAPIPanel
				.setHorizontalGroup(
						gl_settingsAPIPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_settingsAPIPanel.createSequentialGroup().addContainerGap()
										.addGroup(gl_settingsAPIPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(settingsWHOISApiKey, GroupLayout.DEFAULT_SIZE, 214,
														Short.MAX_VALUE)
												.addComponent(lblWhoisApiKey))
										.addContainerGap()));
		gl_settingsAPIPanel.setVerticalGroup(gl_settingsAPIPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsAPIPanel.createSequentialGroup().addContainerGap().addComponent(lblWhoisApiKey)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(settingsWHOISApiKey,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(29, Short.MAX_VALUE)));
		settingsAPIPanel.setLayout(gl_settingsAPIPanel);
		settingsThemesPanel.setLayout(null);

		menuBar_2.setBounds(28, 43, 119, 26);
		settingsThemesPanel.add(menuBar_2);

		menuBar_2.add(mnThemes);

		buttonGroup_2.add(rdbtnmntmLightTheme);
		rdbtnmntmLightTheme.setSelected(true);
		mnThemes.add(rdbtnmntmLightTheme);

		rdbtnmntmDarkTheme.setEnabled(false);
		buttonGroup_2.add(rdbtnmntmDarkTheme);
		mnThemes.add(rdbtnmntmDarkTheme);
		settingsLanguagePanel.setLayout(null);

		menuBar_1.setBounds(23, 43, 119, 26);
		settingsLanguagePanel.add(menuBar_1);

		menuBar_1.add(mnLanguage);

		buttonGroup_1.add(rdbtnmntmEnglish);
		rdbtnmntmEnglish.setSelected(true);
		mnLanguage.add(rdbtnmntmEnglish);

		buttonGroup_1.add(rdbtnmntmFinnish);
		rdbtnmntmFinnish.setEnabled(false);
		mnLanguage.add(rdbtnmntmFinnish);

		settingsMaxPacketCount.setEnabled(false);
		settingsMaxPacketCount.setModel(new SpinnerNumberModel(new Integer(1000000), null, null, new Integer(1)));

		gl_settingsPacketSettingsPanel.setHorizontalGroup(gl_settingsPacketSettingsPanel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsPacketSettingsPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_settingsPacketSettingsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(settingsMaxPackets, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 144,
										Short.MAX_VALUE)
								.addComponent(settingsMaxPacketCount, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
						.addContainerGap()));
		gl_settingsPacketSettingsPanel
				.setVerticalGroup(gl_settingsPacketSettingsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_settingsPacketSettingsPanel.createSequentialGroup().addContainerGap()
								.addComponent(settingsMaxPackets).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(settingsMaxPacketCount, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(163, Short.MAX_VALUE)));
		settingsPacketSettingsPanel.setLayout(gl_settingsPacketSettingsPanel);
		settingsPanel.setLayout(gl_settingsPanel);

		consolePanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Console", null, consolePanel, null);
		consolePanel.setLayout(new GridLayout(0, 1, 0, 0));

		consoleLog.setEditable(false);
		consoleLog.setBackground(Color.BLACK);
		consoleLog.setForeground(Color.GREEN);
		consolePanel.add(consoleLog);

		/*
		 * Events
		 */
		
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				try {
//					FileInputStream fis = new FileInputStream(settingsFile);
//					BufferedReader in = new BufferedReader(new InputStreamReader(fis));
//					String input = in.readLine(); in.close();
//					String[] settings = input.split("::");
//				} catch (IOException er) {
//					er.printStackTrace();
//				}
			}
		});

		booterSTOP.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				booterRunning = false;
				booterProgbar.setIndeterminate(false);
				booterProgbar.setStringPainted(false);
				booterProgbar.setValue(0);
				booterProgbar.setMaximum(100);
				logConsole(lang.getMessage("lang.console-log.booter-stopped"));
			}
		});

		booterSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// address::port::packetcount::packetcontent
				String saveContent = booterAddressField.getText() + "::" + booterPortField.getText() + "::"
						+ booterPacketCount.getValue() + "::" + packetContent.getText();
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Specify a file to save");
				chooser.setApproveButtonText(lang.getMessage("lang.save"));
				chooser.setFileFilter(new FileNameExtensionFilter("Text Document", "txt"));
				int selected = chooser.showSaveDialog(new JFrame(lang.getMessage("lang.save-file")));
				if (selected == JFileChooser.APPROVE_OPTION) {
					try {
						FileWriter fw;
						if (!chooser.getSelectedFile().toString().endsWith(".txt")) {
							fw = new FileWriter(chooser.getSelectedFile() + ".txt");
						} else {
							fw = new FileWriter(chooser.getSelectedFile());
						}
						fw.write(saveContent);
						fw.close();
					} catch (Exception e2) {
						e2.printStackTrace();
						logConsole(e2.toString());
					}
				}
			}
		});

		booterLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Document", "txt", "text");
				chooser.setFileFilter(filter);
				chooser.setDialogTitle("Specify a file to load");
				chooser.setApproveButtonText(lang.getMessage("lang.load"));
				int selected = chooser.showOpenDialog(new JFrame(lang.getMessage("lang.load-file")));
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
							logConsole(lang.getMessage("lang.console-log.error.cannot-read-file"));
							return;
						}
					} else {
						logConsole(lang.getMessage("lang.console-log.error.cannot-read-file"));
						return;
					}
				}
			}
		});

		booterSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				booterPacketCount.setValue(booterSlider.getValue());
			}
		});
		booterPacketCount.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if ((int) booterPacketCount.getValue() < -1)
					booterPacketCount.setValue(-1);
				if ((int) booterPacketCount.getValue() > 1000000)
					booterPacketCount.setValue(1000000);
				booterSlider.setValue((int) booterPacketCount.getValue());
			}
		});

		dnsSearchButton.addActionListener(new ActionListener() {
//lang.getMessage("lang.")
			@Override
			public void actionPerformed(ActionEvent e) {
				logConsole(lang.getMessage("lang.console-log.searching-dns").replaceAll("%1", dnsSearchField.getText()));
				dnsEditor.setEditorKitForContentType("text/xml", new XmlEditorKit());
				dnsEditor.setContentType("text/xml");
				dnsEditor.setText(dnsdata.getXMLContentAsString(dnsSearchField.getText()));
				xmlToTree = new XmlToTree(dnsdata.getXMLContentAsDocument(dnsSearchField.getText()));
				if (xmlToTree.show() == null) {
					logConsole(lang.getMessage("lang.console-log.error.tree-not-defined"));
					return;
				}
				JTree dnsXMLTree = xmlToTree.show();
				dnsTreeview.setViewportView(dnsXMLTree);
				dnsXMLTree.setVisible(true);
				xmlToTree.expandAllNodes();
				dnsXMLTree = xmlToTree.show();
				dnsTreePanel.add(dnsTreeview);
				logConsole(lang.getMessage("lang.console-log.search-complete"));
			}
		});

		booterLaunch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String content = packetContent.getText();
				String address = booterAddressField.getText();
				int port = Integer.parseInt(booterPortField.getText());
				int packetCount = booterSlider.getValue();
				if (content.isEmpty() || content == null) {
					logConsole(lang.getMessage("lang.console-log.error.empty-packet"));
					return;
				}
				if (address.isEmpty() || address == null) {
					logConsole(lang.getMessage("lang.console-log.error.empty-address-field"));
					return;
				}
				if (port == 0) {
					logConsole(lang.getMessage("lang.console-log.error.zero-port"));
					return;
				}
				if (packetCount == 0) {
					logConsole(lang.getMessage("lang.console-log.error.zero-packet"));
					return;
				}
				booterRunning = true;
				if (radioTCP.isSelected()) {
					tcpSender = new TCPPacketSender(address, port);
					if (packetCount == -1) {
						logConsole(lang.getMessage("lang.console-log.loop-tcp"));
						new Thread(new Runnable() {

							@Override
							public void run() {
								booterProgbar.setIndeterminate(true);
								while (booterRunning) {
									tcpSender.sendStringPacket(content);
								}
							}
						}).start();
					} else {
						tcpSender = new TCPPacketSender(address, port);
						logConsole(lang.getMessage("lang.console-log.sending-tcp"));
						new Thread(new Runnable() {
							@Override
							public void run() {
								booterProgbar.setStringPainted(true);
								booterProgbar.setMaximum(packetCount + 1);
								for (int i = 0; i < packetCount; i++) {
									booterProgbar.setValue(i);
									tcpSender.sendStringPacket(content);
									if (!booterRunning)
										break;
								}
								booterProgbar.setStringPainted(false);
								tcpSender.closeSocket();
								logConsole(lang.getMessage("lang.console-log.tcp-done"));
							}
						}).start();
					}
				} else if (radioUDP.isSelected()) {
					udpSender = new UDPPacketSender(address, port);
					if (packetCount == -1) {
						logConsole(lang.getMessage("lang.console-log.loop-udp"));
						new Thread(new Runnable() {

							@Override
							public void run() {
								booterProgbar.setIndeterminate(true);
								while (booterRunning) {
									udpSender.sendStringPacket(content);
								}
							}
						}).start();
					} else {
						udpSender = new UDPPacketSender(address, port);
						logConsole(lang.getMessage("lang.console-log.sending-udp"));
						new Thread(new Runnable() {
							@Override
							public void run() {
								booterProgbar.setStringPainted(true);
								booterProgbar.setMaximum(packetCount + 1);
								for (int i = 0; i < packetCount + 1; i++) {
									booterProgbar.setValue(i);
									udpSender.sendStringPacket(content);
									if (!booterRunning)
										break;
								}
								booterProgbar.setStringPainted(false);
								udpSender.closeSocket();
								logConsole(lang.getMessage("lang.console-log.udp-done"));
							}
						}).start();
					}
				} else {
					logConsole(lang.getMessage("lang.console-log.error.invalid-state"));
				}
			}
		});

		whoisSearchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				logConsole(lang.getMessage("lang.console-log.searching-whois").replaceAll("%1", whoisSearchField.getText()));
				whoisEditor.setEditorKitForContentType("text/xml", new XmlEditorKit());
				whoisEditor.setContentType("text/xml");
				whoisEditor.setText(xmldata.getXMLContentAsString(whoisSearchField.getText()));
				xmlToTree = new XmlToTree(xmldata.getXMLContentAsDocument(whoisSearchField.getText()));
				if (xmlToTree.show() == null) {
					logConsole(lang.getMessage("lang.console-log.error.tree-not-defined"));
					return;
				}
				JTree whoisXMLTree = xmlToTree.show();
				whoisTreeview.setViewportView(whoisXMLTree);
				whoisXMLTree.setVisible(true);
				xmlToTree.expandAllNodes();
				whoisXMLTree = xmlToTree.show();
				logConsole(lang.getMessage("lang.console-log.search-complete"));
			}
		});

		iptdButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (iptdIpField.getText().isEmpty()) {
					logConsole(lang.getMessage("lang.console-log.error.empty-ip"));
					return;
				}
				iptdDomainFIeld.setText(coverter.getDomain(iptdIpField.getText()));
				logConsole(lang.getMessage("lang.console-log.convert").replaceAll("%1", iptdIpField.getText()).replaceAll("%2", coverter.getDomain(iptdIpField.getText())));
			}
		});

		dtipButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (dtipDomainField.getText().isEmpty()) {
					logConsole(lang.getMessage("lang.console-log.error.empty-domain"));
					return;
				}
				dtipIpField.setText(coverter.getIP(dtipDomainField.getText()));
				logConsole(lang.getMessage("lang.console-log.convert").replaceAll("%1", dtipDomainField.getText()).replaceAll("%2", coverter.getIP(dtipDomainField.getText())));
			}
		});

		ipscanCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				scanRunning = false;
			}
		});

		ipscanButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ipscanAddress.getText().isEmpty() || ipscanAddress.getText() == null) {
					logConsole(lang.getMessage("lang.console-log.error.empty-ip"));
					return;
				}
				if ((int) ipscanPort1.getValue() == 0 || (int) ipscanPort2.getValue() == 0) {
					logConsole(lang.getMessage("lang.console-log.error.zero-port"));
					return;
				}
				if ((int) ipscanPort1.getValue() > (int) ipscanPort2.getValue()) {
					logConsole(lang.getMessage("lang.console-log.error.negative-range"));
					return;
				}//lang.getMessage("lang.")
				ipscanLog.setText(ipscanLog.getText() + lang.getMessage("lang.console-log.scanning").replaceAll("%1", ipscanAddress.getText()) + "\n");
				logConsole(lang.getMessage("lang.console-log.scanning").replaceAll("%1", ipscanAddress.getText()));
				if (ipscanOpenPort.isSelected()) {
					if ((int) ipscanPort1.getValue() == (int) ipscanPort2.getValue()) {
						ipscanProgbar.setIndeterminate(true);
						scan = new Scan(ipscanAddress.getText(), 200);
						ipscanLog.setText(ipscanLog.getText() + scan.scanPort((int) ipscanPort1.getValue()));
						ipscanProgbar.setIndeterminate(false);
						scan.closeSocket();
					} else {
						ipscanLog.setText(ipscanLog.getText() + lang.getMessage("lang.console-log.scanning").replaceAll("%1", ipscanAddress.getText()) + "\n");
						new Thread(new Runnable() {
							@Override
							public void run() {
								scanRunning = true;
								ipscanProgbar.setStringPainted(true);
								ipscanProgbar.setMinimum((int) ipscanPort1.getValue());
								ipscanProgbar.setMaximum((int) ipscanPort2.getValue());
								for (int i = (int) ipscanPort1.getValue(); i < (int) ipscanPort2.getValue() + 1; i++) {
									ipscanProgbar.setValue(i);
									scan = new Scan(ipscanAddress.getText(), 200);
									ipscanLog.setText(ipscanLog.getText() + scan.scanPort(i));
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
				logConsole(lang.getMessage("lang.console-log.scan-completed"));
				ipscanLog.setText(ipscanLog.getText() + lang.getMessage("lang.console-log.scan-completed")+"\n\n");
			}
		});

		settingsWHOISApiKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xmldata.setAPIKEY(settingsWHOISApiKey.getText());
				dnsdata.setAPIKEY(settingsWHOISApiKey.getText());
				logConsole(lang.getMessage("lang.console-log.use-apikey").replaceAll("%1", settingsWHOISApiKey.getText()));
			}
		});

		logConsole(lang.getMessage("lang.iptools-started"));
	}

	public void logConsole(String msg) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String time = "[" + dtf.format(now) + "] ";
		consoleLog.setText(consoleLog.getText() + "\n" + time + msg);
	}
	
	/*
	 * Getters and Setters
	 */

	public static String getSettingsWHOISApiKey() {
		if (settingsWHOISApiKey != null) {
			if (settingsWHOISApiKey.getText() != null) {
				return settingsWHOISApiKey.getText();
			}
		}

		return "at_J7TntBlVBoWiwPElcLFbpdo9I30kC";
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
}
