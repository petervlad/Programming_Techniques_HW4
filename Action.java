package BankProc;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

class Action implements ActionListener {
	private BankProc bankproc;
	private Person person;
	private String perString;
	
	public Action(BankProc bankproc) {
		this.bankproc = bankproc;
	}
	
	public Action(BankProc bankproc, Person person) {
		this.bankproc = bankproc;
		this.person = person;
		System.out.println("Email-ul la person noastra" + person.email + "\n");
	}
	
	public Action(BankProc bankproc, String perString) {
		this.bankproc = bankproc;
		this.perString = perString;
		System.out.println("String-ul pus la person noastra " + perString + "\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * String-ul command contine cuvinte trimise din TabbedPane de catre ascultatori 
		 */
		String command = e.getActionCommand();
		switch(command) {
			case "adaugaClienti":
				mkClient();
				break;
			case "editareClienti":
				editareClient();
				break;
			case "stergereClienti":
				delClient("person");
				break;
			case "refreshPerson":
				mkRefresh("person");
				break;
			case "contNou": 
				contNou();
				break;
			case "optiuniGenerale":
				optiuniGenerale();
				break;
			case "alegeAccount":
				detailAccount();
				break;
			default:
				break;
		}
	}
	
	private void optiuniGenerale() {
		JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setPreferredSize(new Dimension(200, 140));
        panel.add(new JLabel("Setare taxe retrage: "));
        panel.add(field1);
        panel.add(new JLabel("Setare rata dobanda: "));
        panel.add(field2);
        int result = JOptionPane.showConfirmDialog(null, panel, "Adauga un client",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
        	ArrayList<String> data = new ArrayList<String>();
        	data.add(field1.getText());
        	data.add(field2.getText());
        	// Trebuie sa actualizam aici Banca
        	bankproc.bank.setTaxaRetragere(Double.parseDouble(data.get(0)));
        	bankproc.bank.setDobanda(Double.parseDouble(data.get(1)));
        } else {
            System.out.println("Cancelled");
        }
	}
	
	private void detailAccount() {
		bankproc.accountContainer.removeAll();
		String email = bankproc.listUpdate.getSelectedItem().toString();
		Person checkGuy = new Person(email);
		if(bankproc.bank.map.isEmpty()) {
			System.out.println("Banca nici macar nu e initializata cu ceva");
		} else {
			try {
				List<Account> conturi = bankproc.bank.map.get(checkGuy);
				System.out.println("Are " + conturi.size() + " conturi");
				if(conturi.size() == 0)
					bankproc.accountContainer.add(new JLabel("<html><p>Nu exista conturi asociate acestei persoane!</p></html>"));	
				else {
					JPanel griddy = new JPanel();
					GridBagLayout layout = new GridBagLayout();
			        griddy.setLayout(layout);        
			        GridBagConstraints gbc = new GridBagConstraints();
			        
					JLabel headerInfoAccount = new JLabel("<html><p style='font-size: 14px;color:#498583';>Informatii despre conturi:</p><br><p style='color:red'><b> NumeAccount____________Suma Bani_____________TipAccount</b></p></html>");
					gbc.fill = GridBagConstraints.HORIZONTAL;
			        gbc.gridx = 0;
			        gbc.weightx = 0.5;
			        gbc.gridy = 0;
					griddy.add(headerInfoAccount, gbc);
					
					JLabel label = new JLabel();

					Set<Person> setul = bankproc.bank.map.keySet();
					String notificari = new String();
					for(Person pers: setul) {
						if(pers.equals(checkGuy)) {
							if(pers.notify) {
								label.setText("<html><p style='color:red'>Accountul a fost anuntat de catre bank ca au aparut modificari la rata dobanzilor!</p></html>");
							} else {
								System.out.println("NIMIC!\n");
							}
						}
					}
					gbc.fill = GridBagConstraints.HORIZONTAL;
			        gbc.gridx = 0;
			        gbc.weightx = 0.5;
			        gbc.gridy = 1;
					bankproc.accountContainer.add(label, gbc);

					// inseamna ca exista conturi si vrem sa le afisam
			        int i = 2;
					for(Account cont: conturi) {
						String numeleAccountului = cont.getName();
						Double bani = cont.getMoney();
						boolean tipAccount = cont.getTipAccount();
						
						gbc.fill = GridBagConstraints.HORIZONTAL;
				        gbc.gridx = 0;
				        gbc.weightx = 0.5;
				        gbc.gridy = i++;
						
				        JPanel line = new JPanel();
						JLabel informatiiAccount  =  new  JLabel("<html>" + numeleAccountului + "________________" 
							+ bani + "_________________" 
							+ ((tipAccount == Account.contCheltuieli) ? "Cheltuieli" : "Economii") + "<br></html>");
						line.add(informatiiAccount);
						JButton retrage = new JButton("Retrage");
						retrage.setActionCommand("retrageBani");
						retrage.addActionListener(this);
						JButton depune = new JButton("Depune");
						depune.setActionCommand("depuneBani");
						depune.addActionListener(this);
						line.add(retrage);
						line.add(depune);
						griddy.add(line, gbc);
					}
					bankproc.accountContainer.add(griddy);
				} 
			} catch(Exception e) {
				System.out.println("Am prins exceptia ... nu exista omul cu cheia asta in bank");
			}			
		}
		bankproc.tabs.revalidate();
	}
	
	private void contNou() {
		String personString = this.bankproc.listUpdate.getSelectedItem().toString();
		JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");
        ArrayList<String> alegeTipulAccount = new ArrayList<>();	
        alegeTipulAccount.add("Economii");
        alegeTipulAccount.add("Cheltuieli");
        JComboBox combo = new JComboBox<String>(alegeTipulAccount.toArray(new String[0]));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setPreferredSize(new Dimension(200, 160));
        panel.add(new JLabel("Nume cont: "));
        panel.add(field1);
        panel.add(new JLabel("Suma initiala: "));
        panel.add(field2);
        panel.add(new JLabel("Alege tipul cont:"));
        panel.add(combo);
        int result = JOptionPane.showConfirmDialog(null, panel, "Adauga un client",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
        	ArrayList<String> data = new ArrayList<String>();
        	data.add(field1.getText());
        	data.add(field2.getText());
        	data.add(combo.getSelectedItem().toString());
        	// Trebuie sa adaugam aici conturi noi
        	Account cont = new Account(data, bankproc.bank);
        	
        	// Actualizare bank
        	Person pers = new Person(personString);
        	try {
        		if(this.bankproc.bank.map.containsKey(pers)) {
        			List<Account> conturi = this.bankproc.bank.map.get(pers);
        			System.out.println("Am gasit person in hash si are " + conturi.size() + "conturi\n");
        			this.bankproc.bank.map.get(pers).add(cont);
                	this.bankproc.bank.serializeBank();
        		}
        	} catch(Exception e) {
        		e.printStackTrace();
        		// chiar nu exista cont
        		System.out.println("Am ajuns in exceptie fiindca nu existau conturi inca\n");
        		List<Account> conturi = new ArrayList<Account>();
        		conturi.add(cont);
        		bankproc.bank.map.put(pers, conturi);
            	this.bankproc.bank.serializeBank();
        		System.out.println(bankproc.bank.map.get(pers).get(0).toString());
        	}
        } else {
            System.out.println("Cancelled");
        }
	}
	
	private void mkRefresh(String whatTo) {
		bankproc.refresh(whatTo);
	}
	
	private void mkClient() {
		JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");
        JTextField field3 = new JTextField("");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setPreferredSize(new Dimension(400, 200));
        panel.add(new JLabel("Nume: "));
        panel.add(field1);
        panel.add(new JLabel("Email: "));
        panel.add(field2);
        panel.add(new JLabel("Telefon: "));
        panel.add(field3);
        int result = JOptionPane.showConfirmDialog(null, panel, "Adauga un client",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
        	ArrayList<String> data = new ArrayList<String>();
        	data.add(field1.getText());
        	data.add(field2.getText());
        	data.add(field3.getText());
        	
        	Person nouClient = new Person(data);
        	bankproc.bank.map.put(nouClient, new ArrayList<Account>());
        	bankproc.bank.serializeBank();
            System.out.println("Un nou client a fost adaugat! \n");
        } else {
            System.out.println("Cancelled");
        }
	}
	
	/**
	 * Metoda editareClient() se uita prin model si cauta valori de la indexul corespunzator
	 * */
	private void editareClient() {
		DefaultTableModel model = Serializable.generator("person", "tmp/");
		ArrayList<String> items = new ArrayList<String>();
		int rowCount = model.getRowCount();
		System.out.println("Ajung pe aici si val. este " + rowCount +"\n");
		for(int i=0;i<rowCount;i++) {
			model.getValueAt(i, 2);
			items.add(model.getValueAt(i, 1) + ". Nume client: " + model.getValueAt(i, 2));
		}
		// Urmatoarele 3 linii de cod creeaza un array de string-uri
		String[] itemsArr = new String[items.size()];
		itemsArr = items.toArray(itemsArr);
        JComboBox<String> combo = new JComboBox<String>(itemsArr);
        
        JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");
        JTextField field3 = new JTextField("");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setPreferredSize(new Dimension(400, 200));
        panel.add(new JLabel("Alege client"));
        panel.add(combo);
        panel.add(new JLabel("Nume:"));
        panel.add(field1);
        panel.add(new JLabel("Email:"));
        panel.add(field2);
        panel.add(new JLabel("Telefon:"));
        panel.add(field3);
        int result = JOptionPane.showConfirmDialog(null, panel, "Editeaza un client",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
        	ArrayList<String> data = new ArrayList<String>();
        	String buffer = combo.getSelectedItem().toString();
        	String temp[] = buffer.split("\\.");
        	if(!temp[0].isEmpty()) {
        		data.add(field1.getText());
            	data.add(field2.getText());
            	data.add(field3.getText());
            	int id = Integer.parseInt(temp[0]);
            	/* Serializarea reprezinta doar crearea unui nou obiect si salvarea acestuia
            	 * cu numele actualului fisier. In felul acesta se suprascriu si gata.*/
            	Person nouClient = new Person(data, id);
                System.out.println("Clientul a fost editat. \n");
        	}
        } else {
            System.out.println("Cancelled");
        }
	}
	
	private static void delClient(String fileType) {
		DefaultTableModel model = Serializable.generator("person", "tmp/");
		ArrayList<String> items = new ArrayList<String>();
		int rowCount = model.getRowCount();
		for(int i=0;i<rowCount;i++) {
			model.getValueAt(i, 2);
			items.add(model.getValueAt(i, 1) + ". Nume client: " + model.getValueAt(i, 2));
		}
		// Urmatoarele 3 linii de cod creeaza un array de string-uri
		String[] itemsArr = new String[items.size()];
		itemsArr = items.toArray(itemsArr);
        JComboBox<String> combo = new JComboBox<String>(itemsArr);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setPreferredSize(new Dimension(400, 100));
        panel.add(new JLabel("Alege client"));
        panel.add(combo);
        panel.add(new JLabel("Esti absolut sigur ca vrei sa faci stergerea?"));
        int result = JOptionPane.showConfirmDialog(null, panel, "Sterge un client",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
        	String buffer = combo.getSelectedItem().toString();
        	String temp[] = buffer.split("\\.");
        	if(!temp[0].isEmpty()) {
            	int id = Integer.parseInt(temp[0]);
            	System.out.println(id);
            	try{
            		File file = new File("tmp/"+fileType+id+".ser"); 
            		if(file.delete()){
            			System.out.println(file.getName() + " is deleted!");
            		}else{
            			System.out.println("Delete operation is failed.");
            		}
            	}catch(Exception e){
            		e.printStackTrace();
            	}
                System.out.println("Clientul a fost sters definitiv. \n");
        	}
        } else {
            System.out.println("Cancelled");
        }
	}
	
}