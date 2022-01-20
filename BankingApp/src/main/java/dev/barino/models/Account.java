package dev.barino.models;

import java.text.DecimalFormat;

public class Account {

    private int accountID;
    private String pw;
    private String firstName;
    private String lastName;
    private double balance = 0.0;

    //No-arg constructor
    public Account() {
    }

    //Full-argument constructor
//    public Account(int accountID, String pw, String firstName, String lastName, double balance) {
//        this.accountID = accountID;
//        this.pw = pw;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.balance = balance;
//    }

    //ID-less constructor
    public Account(String pw, String firstName, String lastName, double balance) {
        this.pw = pw;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    //Getters and setters
    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getBalance() {
        return this.balance;
    }

    public String displayBalance() {
        DecimalFormat f = new DecimalFormat("###,###,##0.00");
        return f.format(this.balance);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountID=" + accountID +
                ", pw='" + pw + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", balance=" + balance +
                '}';
    }

}
