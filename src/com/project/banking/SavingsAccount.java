package com.project.banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/*
    Kyler Kinsey
 */
public class SavingsAccount {
    private String firstName;
    private String lastName;
    private double balance;
    private int accountNumber;
    private int customerID;
    private final double INTEREST_RATE = .0315;

    public SavingsAccount(int customerID, String fname, String lname, double initialBalance) {
        this.customerID = customerID;
        this.firstName = fname;
        this.lastName = lname;
        this.balance = initialBalance;
        this.accountNumber = createAccountNumber();
        addToDatabase();
    }
    public double addInterestToBalance() {
        balance = balance + (balance * INTEREST_RATE);
        return balance;
    }
    public void depositMoney(double money) {
        balance += money;
    }
    public boolean withdrawMoney(double money) {
        if (balance - money < 0) {
            return false;
        }
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

            String query = " insert into savings ( CustomerID, AccountNumber, FirstName, LastName, CurrentBalance, InterestRate)"
                    + " values (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, customerID);
            preparedStmt.setInt(2,accountNumber);
            preparedStmt.setString(3, firstName);
            preparedStmt.setString(4, lastName);
            preparedStmt.setDouble(5, balance);
            preparedStmt.setDouble(6, INTEREST_RATE);

            preparedStmt.execute();

            con.close();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
