package BankProc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

class BankProc extends JFrame {
	// Variabila globala in care se salveaza directorul
	public static final String DIRNAME = "tmp/";
	public JPanel mainPanel;
	public JScrollPane scrollPane;
	public JTable table;
	public JTabbedPane tabs;
	public JPanel accountContainer;
	JLabel taxeSiDobanda;
	public JComboBox<String> listUpdate;
	public Bank bank;
	
	
	public BankProc() {
		bank = new Bank();
		this.prepareBankProc();
	}
	
	public void prepareBankProc() {
		this.setTitle("Bank");
		this.setSize(1000, 600);
		this.setLayout(new GridLayout(1,1));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		mainPanel = new JPanel();
		this.modelPanel(mainPanel);
		this.add(mainPanel);
	}
	
	public void modelPanel(JPanel panel) {
		Border empty = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);        
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        JLabel label1 = new JLabel();
        label1.setBorder(empty);
        label1.setText("<html><p style='font-size:32px;text-align:center;font-family:Old English Text MT;font-weight:400'>Bank Proc</p></html>");        
        panel.add(label1, gbc);
        
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.weightx = 0.9;
   
        File imageCheck = new File("imagini/refresh-icon.png");

        if(imageCheck.exists()) 
            System.out.println("Am gasit refresh!");
        else 
            System.out.println("Image file not found!");
        
        JButton refresh = new JButton();
        refresh.setBorderPainted(false);
        refresh.setFocusPainted(false);
        refresh.setContentAreaFilled(false);
        try {
        	Image img = ImageIO.read(new FileInputStream("imagini/refresh-icon.png"));
        	refresh.setIcon(new ImageIcon(img));
        } catch(IOException e) {}
        refresh.setActionCommand("refreshPerson");
        refresh.addActionListener(new Action(this));
        panel.add(refresh, gbc);
        
        // Butonul de adauga comenzi
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipady = 5;
        gbc.ipadx = 20;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        JPanel fluid = new JPanel(new FlowLayout());
        JButton adaugaProduse = BankProc.butonClasic("<html><p style='font-size:30px'>+</p></html>");
        try {
        	BufferedImage img = ImageIO.read(new FileInputStream("imagini/add.png"));
        	adaugaProduse.setIcon(new ImageIcon(img));
            adaugaProduse.setPreferredSize(new Dimension(img.getWidth()+2, img.getHeight()+2));
        } catch(IOException e) {}
        adaugaProduse.setActionCommand("adaugaClienti");
        adaugaProduse.addActionListener(new Action(this));
        fluid.add(adaugaProduse, gbc);
        
        JButton editareProduse = BankProc.butonClasic("<html><p style='font-size:30px;line-height: 0;padding:0;'>&curren;</p></html>");
        try {
        	BufferedImage img = ImageIO.read(new FileInputStream("imagini/edit.png"));
        	editareProduse.setIcon(new ImageIcon(img));
            editareProduse.setPreferredSize(new Dimension(img.getWidth()+2, img.getHeight()+2));
        } catch(IOException e) {}
        editareProduse.setActionCommand("editareClienti");
        editareProduse.addActionListener(new Action(this));
        fluid.add(editareProduse);
        
        JButton stergereClienti = BankProc.butonClasic("<html><p style='font-weight:600;font-size:20px;line-height: 0;padding:0;'>x</p></html>");
        try {
        	BufferedImage img = ImageIO.read(new FileInputStream("imagini/delete.png"));
        	stergereClienti.setIcon(new ImageIcon(img));
            stergereClienti.setPreferredSize(new Dimension(img.getWidth()+2, img.getHeight()+2));
        } catch(IOException e) {}
        stergereClienti.setActionCommand("stergereClienti");
        stergereClienti.addActionListener(new Action(this));
        fluid.add(stergereClienti);
        
        panel.add(fluid, gbc);
        
        // Generatorul tabelei de clienti
        DefaultTableModel model = new DefaultTableModel();
        model = Serializable.generator("persoana", DIRNAME);
        table = new JTable(model);
        
        // Pentru actulizare model
        int sizeRow = model.getRowCount();
        int sizeCol = model.getColumnCount();
        String[] allCelule = new String[sizeCol];
        for(int i=0;i<sizeRow;i++) {
        	for(int j=0;j<sizeCol;j++) {
        	   allCelule[i] = (String) table.getModel().getValueAt(i, j);
        	}
        	//copac.put(allCelule, i);
        }
        
 		// Adaugarea tabelului la un Scrooling Pane
        tabs = new JTabbedPane();
        tabs.setBorder(empty);
        scrollPane = new JScrollPane( table );
 		table.setPreferredSize(new Dimension(500, table.getRowHeight()*15+1));
 		scrollPane.setPreferredSize(table.getPreferredSize());
 		 
 		ArrayList<String> conturi = new ArrayList<>();	
		Vector<Vector<Object>> allData = model.getDataVector();
		for(Vector data: allData) 
			conturi.add(data.elementAt(2).toString());
		
		// Definim zona pentur tab-ul 2 unde se pot adauga noi informarii legate de 
		// account-uri si legaturile acestora cu conturile
 		JPanel conturiContainer = new JPanel(new GridBagLayout());
 		conturiContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
 		JLabel conturiExistente = new JLabel("1. Selecteaza persoana: ");
 		gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipady = 5;
        gbc.ipadx = 20;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0;
 		conturiContainer.add(conturiExistente, gbc);
 		conturiContainer.setPreferredSize(new Dimension(500, 20));
 		if(conturi.isEmpty()) {
 			conturi.add("");
 		}
 		listUpdate = new JComboBox<String>(conturi.toArray(new String[0]));
 		listUpdate.setPreferredSize(new Dimension(500, 20));
 		listUpdate.setSelectedIndex(0);
 		listUpdate.setActionCommand("alegeCont");
 		String value = listUpdate.getSelectedItem().toString();
 		Person emptyPers = new Person(value); 		
 		Set<Person> persSet = this.bank.map.keySet();
 		for(Person pers: persSet) {
 			if(pers.equals(emptyPers)) {
 				//System.out.println("Am gasit o egalitate!!\n");
 			} else {
 				//System.out.println("Nu s-a gasit nimic \n");
 			}
 		}
 		listUpdate.addActionListener(new Action(this));
 		gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipady = 5;
        gbc.ipadx = 20;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0;
 		conturiContainer.add(listUpdate, gbc);
 		this.accountContainer = new JPanel();
		gbc.fill = GridBagConstraints.NONE;
	    gbc.anchor = GridBagConstraints.WEST;
	    gbc.ipady = 5;
	    gbc.ipadx = 20;
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gbc.weighty = 0;
 		conturiContainer.add(this.accountContainer, gbc);
 		
 		gbc.fill = GridBagConstraints.NONE;
	    gbc.anchor = GridBagConstraints.WEST;
	    gbc.ipady = 5;
	    gbc.ipadx = 20;
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gbc.weighty = 0;
	    JButton adaugaCont = new JButton("Adauga cont");
	    adaugaCont.setActionCommand("contNou");
	    adaugaCont.addActionListener(new Action(this));
	    conturiContainer.add(adaugaCont, gbc);
 		
 		// Mini Hack for making the grid bag layout proper display
 		gbc.fill = GridBagConstraints.NONE;
	    gbc.anchor = GridBagConstraints.WEST;
	    gbc.ipady = 5;
	    gbc.ipadx = 20;
	    gbc.gridx = 0;
	    gbc.gridy = 4;
	    gbc.weighty = 1;
	    JLabel emptyLabelHack = new JLabel("");
	    conturiContainer.add(emptyLabelHack, gbc);
 		
	 	tabs.addTab("Clienti", scrollPane);
	 	tabs.addTab("Conturi", conturiContainer);
	 	gbc.fill = GridBagConstraints.HORIZONTAL;
 	    gbc.ipady = 0;   
 	    gbc.gridx = 0;
 	    gbc.gridy = 4;
 	    gbc.weighty = 0;
 		panel.add( tabs, gbc);
 		
 		gbc.fill = GridBagConstraints.HORIZONTAL;
 	    gbc.ipady = 0;   
 	    gbc.gridx = 0;
 	    gbc.gridy = 5;
 	    gbc.weighty = 0;
 	   
 	    JPanel dateGeneraleBank = new JPanel(new GridLayout(2, 1));
 	    dateGeneraleBank.setBorder(empty);
 	    JLabel dateGenerale = new JLabel("<html><em>Date generale bank</em></html>");
 	    taxeSiDobanda = new JLabel("TAXA RETRAGERE: " + this.bank.TAXA_RETRAGERE * 100 + "%    DOBANDA: " + this.bank.DOBANDA * 100 +"%");
 	    dateGeneraleBank.add(dateGenerale);
 	    dateGeneraleBank.add(taxeSiDobanda);
 	    panel.add(dateGeneraleBank, gbc);
 		
 		gbc.fill = GridBagConstraints.HORIZONTAL;
 	    gbc.ipady = 0;   
 	    gbc.gridx = 0;
 	    gbc.gridy = 6;
 	    gbc.weighty = 0;
 	    JPanel optiuniBank = new JPanel(new GridLayout(1, 3));
 	    JPanel optiuni1 = new JPanel();
 	    JLabel optiuniLabel = new JLabel("Setare taxe generale: ");
 	    optiuni1.add(optiuniLabel);
 	    JButton optiuniSuplimentare = new JButton("General");
 	    optiuniSuplimentare.setActionCommand("optiuniGenerale");
 	    optiuniSuplimentare.addActionListener(new Action(this));
 	    optiuni1.add(optiuniSuplimentare);
 	    optiuniBank.add(optiuni1);
 	    
 	    JPanel optiuni2 = new JPanel();
 	    optiuniBank.add(optiuni2);
 	    JPanel optiuni3 = new JPanel();
 	    optiuniBank.add(optiuni3);
 	    panel.add(optiuniBank, gbc);
 		
 		GridBagConstraints c = new GridBagConstraints();
 		c.gridx = 0;
 		c.gridy = 7;
 		c.fill = GridBagConstraints.BOTH;
 		c.weightx=1;
 		c.weighty=1;
 		c.gridwidth = 2;
 		panel.add(new JLabel(" "),c);
	}
	
	private static JButton butonClasic(String text) {
		JButton button = new JButton(text);
	    button.setForeground(Color.GRAY);
	    button.setBackground(Color.WHITE);
	    button.setOpaque(false);
	    //button.setContentAreaFilled(false);
	    button.setBorderPainted(false);
	    LineBorder line = new LineBorder(Color.WHITE);
	    EmptyBorder margin = new EmptyBorder(5, 15, 5, 15);
	    CompoundBorder compound = new CompoundBorder(line, margin);
	    button.setBorder(compound);
	    return button;
	}
	/**
	 * O metoda personala foarte generica ce cauta automat componentele in structa cu 
	 * TabbedPane. Odata gasite ele sunt revalidate si redesenate. In acest fel se 
	 * gaseste exact ceea ce trebuie modificat fara a reface intreg panoul
	 * */
	public void refresh(String fileType) {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = layout.getConstraints(mainPanel);
		gbc.fill = GridBagConstraints.HORIZONTAL;
 	    gbc.ipady = 0;   
 	    gbc.gridx = 0;
 	    gbc.gridy = 2;
 	    gbc.weightx = 1;
 	    
 	    // Genereaza un model actualizat pe baza locului in care te afli
 		AbstractTableModel resultsModel = Serializable.generator(fileType, DIRNAME);
 	    
		// Se va seta noul model si cateva dimensiuni standard pe table + scrollPane
		table.setModel(resultsModel);
		int index = 4;
		//table.remove(0);
		//tabs.add(scrollPane);
		table.revalidate();
		table.repaint();
		
		// Refresh la lista conturi
		listUpdate.revalidate();
		listUpdate.repaint();
		
		//taxe si dobanda
		taxeSiDobanda.setText("TAXA RETRAGERE: " + this.bank.TAXA_RETRAGERE * 100 + "%    DOBANDA: " + this.bank.DOBANDA * 100 +"%");
		
		tabs.revalidate();
		tabs.repaint();
 			
	}
	
	
	public DefaultTableModel furaModelul() {
		return (DefaultTableModel)table.getModel();
	}
	
	private int getIndexOfComp(JPanel panel, String compName) {
		Component[] components = panel.getComponents();
		int j = -1;
		for(int i =0; i<components.length; i++) {
			Component comp = components[i];
			if((comp.getClass().getSimpleName()).equals(compName)) {
				j = i;
				return j;
			}
		}
		return j;
	}
	
	public static final int getComponentIndex(Component component) {
	    if (component != null && component.getParent() != null) {
	      Container c = component.getParent();
	      for (int i = 0; i < c.getComponentCount(); i++) {
	        if (c.getComponent(i) == component)
	          return i;
	      }
	    }

	    return -1;
	  }
	public void showPanel(){ 
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		BankProc gui = new BankProc();
		gui.showPanel();
	}
}