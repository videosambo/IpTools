package fi.videosambo.iptools;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JLayeredPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JSplitPane;
import java.awt.CardLayout;
import javax.swing.ButtonGroup;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JEditorPane;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import javax.swing.JTextPane;
import java.awt.Label;
import java.awt.TextArea;
import javax.swing.JTree;

public class GUI extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField booterAddressField;
	private JTextField booterPortField;
	private JTextField whoisSearchField;

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
		txtrIpTools.setText("IP Tools.\r\nGreat program to check usefull information of ip and it's domain. You can check ports, os information and dns records of domain with it. You can also test penerate ip's.\r\nEducation purposes only. I take no responsibility for any abuse or wrong use.\r\nMade by videosambo\r\nGUI licensed");
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
	}
}
