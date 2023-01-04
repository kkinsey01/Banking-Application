package com.project.banking;

import java.sql.*;

/*
    Kyler Kinsey
 */
public class CheckingAccount {
    private int customerID;
    private int accountNumber;
    private String firstName;
    private String lastName;
    private double balance;

    public CheckingAccount(int customerID, String fname, String lname, double initialBalance) {
        this.customerID = customerID;
        this.accountNumber = createAccountNumber();
        this.firstName = fname;
        this.lastName = lname;
        this.balance = initialBalance;
        addToDatabase();
    }

    public String getName() {
        return firstName + " " + lastName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getAccountNumber() {
        return accountNumber;
    }
    public double getBalance() {
        return balance;
    }
    public void depositMoney(double money) {
        balance += money;
    }
    public boolean withdrawMoney(double money) {
        if (balance - money < 0)
            return false;
        else {
            balance = balance - money;
            return true;
        }
    }
    private int createAccountNumber() {
        return (int)(Math.random() * 100000000);
    }
    private void addToDatabase() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingapp", "root", "MyFirstSQL0");

            String query = " insert into checkings ( CustomerID, AccountNumber, FirstName, LastName, CurrentBalance)"
                    + " values (?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, customerID);
            preparedStmt.setInt(2,accountNumber);
            preparedStmt.setString(3, firstName);
            preparedStmt.setString(4, lastName);
            preparedStmt.setDouble(5, balance);

            preparedStmt.execute();

            con.close();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
