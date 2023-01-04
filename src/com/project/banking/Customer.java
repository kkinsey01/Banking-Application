package com.project.banking;

import java.util.Scanner;
import java.sql.*;

/*
    Kyler Kinsey
 */
public class Customer {
    private int customerID;
    private String firstName;
    private String lastName;
    private boolean checking;
    private boolean savings;
    private int numOfLoans;
    Scanner sc = new Scanner(System.in);

    public Customer(String fname, String lname, boolean checking, boolean savings, int numOfLoans) {
        this.firstName = fname;
        this.lastName = lname;
        this.customerID = getCustomerID();
        if (checking) {
            System.out.println("Enter initial balance for the checking account");
            double checkBalance = sc.nextDouble();
            createChecking(checkBalance);
        }
        if (savings) {
            System.out.println("Enter initial balance for the savings account");
            double savingsBalance = sc.nextDouble();
            createSavings(savingsBalance);
        }
        this.numOfLoans = numOfLoans;
        addToDatabase();
    }
    public boolean createChecking(double balance) {
        if (!isChecking()) {
            CheckingAccount newAccount = new CheckingAccount(customerID, firstName, lastName, balance);
            this.checking = true;
            return true;
        }
        else {
            return false;
        }
    }
    public boolean createSavings(double balance) {
        if (!isSavings()) {
            SavingsAccount newAccount = new SavingsAccount(customerID, firstName, lastName, balance);
            this.savings = true;
            return true;
        }
        else {
            return false;
        }
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public boolean isChecking() {
        return checking;
    }
    public boolean isSavings() {
        return savings;
    }
    public int getNumOfLoans() {
        return numOfLoans;
    }
    private void addToDatabase() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingapp", "root", "MyFirstSQL0");

            String query = "insert into customer ( CustomerID, FirstName, LastName, NumberOfLoans, CheckingAccount, SavingsAccount)"
                    + " values (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt (1, customerID);
            preparedStmt.setString (2, firstName);
            preparedStmt.setString (3, lastName);
            preparedStmt.setInt (4, numOfLoans);
            preparedStmt.setBoolean (5, checking);
            preparedStmt.setBoolean(6, savings);

            preparedStmt.execute();


            con.close();


        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    private int getCustomerID() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingapp", "root", "MyFirstSQL0");
            Statement stmt = con.createStatement();
            String query = "Select CustomerID From customer";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
               customerID = rs.getInt("CustomerID");
            }
            customerID++;
            con.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return customerID;
    }
}
