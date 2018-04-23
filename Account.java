package BankProc;

import java.util.ArrayList;

public class Account implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public static final boolean contEconomii = true;
	public static final boolean contCheltuieli = false;
	private double fund;
	private String numeAccount;
	private Bank bank;
	private boolean tipAccount;
	
	public Account(double bani, String numeAccount, Bank bank) {
		this.fund = bani;
		this.numeAccount = numeAccount;
		this.bank = bank;
	}
	
	public Account(ArrayList<String> data, Bank bank) {
		this.bank = bank;
		this.numeAccount = data.get(0);
		this.fund = Double.parseDouble(data.get(1));
		this.tipAccount = (data.get(2) == "Economii") ? Account.contEconomii : Account.contCheltuieli;
	}
	

	
	
	public void SavingAccount() {
		this.tipAccount = Account.contEconomii;
	}
	
	public void SpendingAccount() {
		this.tipAccount = Account.contCheltuieli;
	}
	
	public boolean getTipAccount() {
		return tipAccount;
	}
	public void addFund(double amount) {
		assert amount > 0;
		this.fund += amount;
	}
	/**
	* returns true if and only if the list is empty
	* @pre true
	* @post @result <=> getSize() > 0
	* @post @nochange
	*/
	// Scoatere de bani din portofel
	public double withrawalMoney(int amount) {
		double amountReala = amount + amount * bank.getTaxa();
		if(amountReala > this.fund) 
			return -1;
		else {
			this.fund -= amountReala;
		}
		return amount;
	}
	
	/**
	* returneaza ok daca exista bani in cont
	* @pre true
	* @post @result <=> getSize() > 0
	* @post @nochange
	*/
	public double getMoney() {
		return this.fund;
	}
	
	public String getName() {
		return this.numeAccount;
	}
} 