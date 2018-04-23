package BankProc;

import java.io.EOFException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class Bank extends Observable implements java.io.Serializable {
	public Map<Person, List<Account>> map;
	public double TAXA_RETRAGERE = 0.01;
	public double DOBANDA = 0.01;
	
	public Bank() {
		this.map =  new HashMap<Person, List<Account>>();
		this.deserializeBank();
		addObservers();
	}
	
	private void addObservers() {
		Set<Person> set = this.map.keySet();
		for(Person pers: set) {
			this.addObserver(pers);
		}
	}
	
	public void setTaxaRetragere(double taxa) {
        this.TAXA_RETRAGERE = taxa;
        setChanged();
        notifyObservers();
	}
	public double getTaxa() {
		return TAXA_RETRAGERE;
	}
	
	public void setDobanda(double d) {
		this.DOBANDA = d;
		setChanged();
		notifyObservers();
	}
	public double getDobanda() {
		return DOBANDA;
	}
	
	public void initBank() {
		DefaultTableModel model = Serializable.generator("persoana", BankProc.DIRNAME);
		Vector<Vector<Object>> allData = model.getDataVector();
		for(Vector data: allData) 
			for(Object obj: data) {
				Person cont = (Person)obj;
				System.out.println("Am reusit sa ajungem la persoana cu numele: \n" + cont.name);
			}
	}
	
	public void serializeBank() {
		Serializable.serialize(map, "banca");
	}
	
	private void deserializeBank() {
		try {
			map = (HashMap)Serializable.deserialize(map, "banca.ser");
		} catch(Exception e) {
			System.out.println("Am prins exceptia!");
		}
		if(map == null) {
			System.out.println("Chiar nu este nimic\n");
			map = new HashMap<Person, List<Account>>();
		}
	}
} 