package BankProc;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Person implements java.io.Serializable, Observer {
	private static final long serialVersionUID = 1L;
	public int id;
	public String name;
	public String email;
	public String phone;	
	private Bank bank;
	public String notificari = new String();
	public boolean notify = false;
	
	public Person(String email) {
		this.email = email;
	}
	
	public Person(String email, Bank bank) {
		this.email = email;
		this.bank = bank;
	}
	
	public Person(ArrayList<String> data) {
		id = mkID();
		name = data.get(0); 
		email = data.get(1);
		phone = data.get(2);
		Serializable.serialize(this, "persoana"+id);
	}
	
	public Person(ArrayList<String> data, int oldID) {
		id = oldID;
		name = data.get(0); 
		email = data.get(1);
		phone = data.get(2);
		Serializable.serialize(this, "persoana"+id);
	}
	
	 public void update(Observable obs, Object obj) {
		 this.notify = true;
	     System.out.println("update(" + obs + "," + obj + ");");
	 }
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }
	
	@Override
    public int hashCode() {
        int result = 0;
        result = (int) (email.hashCode() / 11);
        return result;
    }
	 public static int hashCode(String email) {
	        int result = 0;
	        result = (int) (email.hashCode() / 11);
	        return result;
	    }
	
	private int mkID() {
		return (int) (Math.random()*10000);
	}
} 