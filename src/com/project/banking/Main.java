package com.project.banking;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Scanner;

/*
    Made by Kyler Kinsey, from 1/1/22 to
    1/4/23. First time working with SQL Databases
    Some OOP concepts in here as well
 */

/* ASSUME THAT WHEN A CUSTOMER IS CREATING A NEW ACCOUNT,
   THAT THEY WALK INTO THE BANK AND ASK THE TELLER. THE TELLER THEN CREATES
   THE ACCOUNT THROUGH THE ADMIN MENU. THERE IS NO OPTION IN THE CUSTOMER MENU
   TO MAKE THEMSELVES A CUSTOMER, THEY HAVE TO TALK TO A BANK EMPLOYEE. SAME THING
   FOR CREATING A CHECKING/ SAVINGS ACCOUNT
 */

public class Main {
    static Scanner sc = new Scanner(System.in);
    private static final String url = "jdbc:mysql://localhost/bankingapp";
    private static final String user = "root";
    private static final String password = "MyFirstSQL0";

    public static void main(String[] args) throws SQLException {
        menu();
    }

    public static void menu() throws SQLException {
        int input = 100;
        do {
            System.out.println("----WELCOME MENU----");
            System.out.println("Who is using our system? Input only the number corresponding to the option.");
            System.out.println("0) Exit");
            System.out.println("1) Admin");
            System.out.println("2) Customer");
            System.out.println("3) System Menu");
            input = sc.nextInt();
            if (input == 1) {
                adminMenu();
            }
            else if(input == 2) {
                customerMenu();
            }
            else if (input == 3) {
                systemMenu();
            }
        }  while (input != 0);
    }
    public static void adminMenu() throws SQLException {
        int input = 100;
        do {
            System.out.println("----ADMIN MENU----");
            System.out.println("What would you like to do? Input only the number corresponding to the option.");
            System.out.println("0) Exit");
            System.out.println("1) Add Customer");
            System.out.println("2) Add Employee");
            System.out.println("3) Create a checking account for existing customer");
            System.out.println("4) Create a savings account for an existing customer");
            System.out.println("5) Remove Customer");
            System.out.println("6) Remove Employee");
            System.out.println("7) Update Employee Salary");
            input = sc.nextInt();
            if (input == 1) {
                addCustomer();
            }
            else if (input == 2) {
                addEmployee();
            }
            else if (input == 3) {
                createChecking();
            }
            else if (input == 4) {
                createSavings();
            }
            else if (input == 5) {
                deleteCustomer();
            }
            else if (input == 6) {
                deleteEmployee();
            }
            else if (input == 7) {
                updateEmployeeSalary();
            }
        } while (input != 0);
    }
    public static void customerMenu() throws SQLException{
        int input = 100;
        sc.nextLine();
        System.out.println("----CUSTOMER MENU----");
        System.out.println("Enter your last name associated with the account: ");
        String lastName = sc.nextLine();
        if (checkAccountExists(lastName)) {
            do {
                System.out.println("\n" + "What would you like to do? Input only the number corresponding to the option.");
                System.out.println("0) Exit");
                System.out.println("1) View checking balance");
                System.out.println("2) View savings balance");
                System.out.println("3) Withdraw money from checking");
                System.out.println("4) Deposit money to checking");
                System.out.println("5) Withdraw money from savings");
                System.out.println("6) Deposit money to savings");
                input = sc.nextInt();
                if (input == 1) {
                    viewCheckingBalance(lastName);
                } else if (input == 2) {
                    viewSavingsBalance(lastName);
                }
                else if (input == 3) {
                    withdrawChecking(lastName);
                }
                else if (input == 4) {
                    depositChecking(lastName);
                }
                else if (input == 5) {
                    withdrawSavings(lastName);
                }
                else if (input == 6) {
                    depositSavings(lastName);
                }
            } while (input != 0);
        }
        else {
            customerMenu();
        }
    }
    public static void systemMenu() throws SQLException {
        int input = 100;
        sc.nextLine();
        do {
            System.out.println("\n----SYSTEM MENU----");
            System.out.println("----CAUTION----");
            System.out.println("0) Exit");
            System.out.println("1) Delete Customer Database");
            System.out.println("2) Delete Employee Database");
            System.out.println("3) Delete Checkings Database");
            System.out.println("4) Delete Savings Database");
            System.out.println("5) Accumulate All Savings Accounts");
            input = sc.nextInt();
            if (input == 1)
                deleteCustomerDatabase();
            else if (input == 2)
                deleteEmployeeDatabase();
            else if (input == 3)
                deleteCheckingsDatabase();
            else if (input == 4)
                deleteSavingsDatabase();
            else if (input == 5)
                accumulateSavings();
        } while (input != 0);
    }

    // Admin menu option methods
    public static void addCustomer() {
        boolean checking = false;
        boolean savings = false;
        sc.nextLine();
        System.out.println("----ADD CUSTOMER----");
        System.out.println("Enter customer first name: ");
        String firstName = sc.nextLine();
        System.out.println("Enter customer last name: ");
        String lastName = sc.nextLine();
        System.out.println("Create a checking account (Y or N): ");
        char ch = sc.nextLine().charAt(0);
        if (ch == 'Y') {
            checking = true;
        }
        System.out.println("Create a savings account (Y or N): ");
        ch = sc.nextLine().charAt(0);
        if (ch == 'Y') {
            savings = true;
        }
        System.out.println("Number of loans? ");
        int numOfLoans = sc.nextInt();
        Customer newCustomer = new Customer(firstName, lastName, checking, savings, numOfLoans);
    }

    public static void addEmployee() {
        sc.nextLine();
        System.out.println("----ADD EMPLOYEE----");
        System.out.println("Enter employee first name: ");
        String firstName = sc.nextLine();
        System.out.println("Enter employee last name: ");
        String lastName = sc.nextLine();
        System.out.println("Enter employee salary: ");
        double salary = sc.nextDouble();
        Employee newEmployee = new Employee(salary, firstName, lastName);
    }

    public static boolean createChecking() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from customer");
        sc.nextLine();
        System.out.println("Enter last name of the customer for which you want to create an account for");
        String lastName = sc.nextLine().toLowerCase();
        while(rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(lastName)) {
                if (rs.getInt("CheckingAccount") == 0) {
                    System.out.println("Enter initial balance for the checking account: ");
                    double balance = sc.nextDouble();
                    int customerID = rs.getInt("CustomerID");
                    String firstName = rs.getString("FirstName");
                    String lName = rs.getString("LastName");
                    CheckingAccount newAccount = new CheckingAccount(customerID, firstName, lName, balance);
                    String sql = "UPDATE CUSTOMER "
                            + "SET CheckingAccount = 1  WHERE CustomerID=" + customerID;
                    stmt.execute(sql);
                    con.close();
                    return true;
                }
                else {
                    System.out.println("An account already exists for " + rs.getString("FirstName") + " " + lastName);
                    break;
                }
            }
        }
        con.close();
        return false;
    }

    public static boolean createSavings() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from customer");
        sc.nextLine();
        System.out.println("Enter last name of the customer for which you want to create an account for");
        String lastName = sc.nextLine().toLowerCase();
        while(rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(lastName)) {
                if (rs.getInt("SavingsAccount") == 0) {
                    System.out.println("Enter initial balance for the savings account: ");
                    double balance = sc.nextDouble();
                    int customerID = rs.getInt("CustomerID");
                    String firstName = rs.getString("FirstName");
                    String lName = rs.getString("LastName");
                    SavingsAccount newAccount = new SavingsAccount(customerID, firstName, lName, balance);
                    String sql = "UPDATE CUSTOMER "
                            + "SET SavingsAccount = 1  WHERE CustomerID=" + customerID;
                    stmt.execute(sql);
                    con.close();
                    return true;
                }
                else {
                    System.out.println("An account already exists for " + rs.getString("FirstName") + " " + lastName);
                    break;
                }
            }
        }
        con.close();
        return false;
    }

    public static boolean deleteCustomer() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from customer");
        sc.nextLine();
        System.out.println("Enter last name of the customer you are removing from the database");
        String lastName = sc.nextLine().toLowerCase();
        while(rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(lastName)) {
                int customerID = rs.getInt("CustomerID");
                String sql = "DELETE FROM CUSTOMER WHERE CustomerID=" + customerID;
                if (rs.getInt("CheckingAccount") == 1)
                    deleteChecking(lastName);
                if (rs.getInt("SavingsAccount") == 1)
                    deleteSavings(lastName);
                stmt.execute(sql);
                con.close();
                return true;
            }
        }
        System.out.println("No account exists for " + rs.getString("FirstName") + " " + lastName);
        con.close();
        return false;
    }

    public static boolean deleteChecking(String lastName) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from checkings");
        while(rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(lastName)) {
                int customerID = rs.getInt("CustomerID");
                String sql = "DELETE FROM CHECKINGS WHERE CustomerID=" + customerID;
                stmt.execute(sql);
                con.close();
                return true;
            }
        }
        con.close();
        return false;
    }
    public static boolean deleteSavings(String lastName) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from savings");
        while(rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(lastName)) {
                int customerID = rs.getInt("CustomerID");
                String sql = "DELETE FROM SAVINGS WHERE CustomerID=" + customerID;
                stmt.execute(sql);
                con.close();
                return true;
            }
        }
        con.close();
        return false;
    }
    public static boolean deleteEmployee() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from employee");
        sc.nextLine();
        System.out.println("Enter the last name of the employee you would like to remove: ");
        String lastName = sc.nextLine();
        String s = lastName.toLowerCase();
        while(rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(s)) {
                int employeeID = rs.getInt("EmployeeID");
                String sql = "DELETE FROM EMPLOYEE WHERE EmployeeID=" + employeeID;
                stmt.execute(sql);
                con.close();
                return true;
            }
        }
        System.out.println("Employee with last name " + lastName + " does not exist in the database");
        con.close();
        return false;
    }
    public static boolean updateEmployeeSalary() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from employee");
        sc.nextLine();
        System.out.println("Enter the last name of the employee whose salary you would like to update: ");
        String lastName = sc.nextLine();
        String s = lastName.toLowerCase();
        while(rs.next()) {
            if(rs.getString("LastName").toLowerCase().equals(s)) {
                int employeeID = rs.getInt("EmployeeID");
                double oldSalary = rs.getDouble("Salary");
                System.out.println(lastName + "'s old salary is " + oldSalary);
                System.out.println("Enter their new salary: ");
                double newSalary = sc.nextDouble();
                String sql = "UPDATE EMPLOYEE "
                        + "SET SALARY = " + newSalary + " WHERE EmployeeID=" + employeeID;
                stmt.execute(sql);
                con.close();
                return true;
            }
        }
        System.out.println("No such employee exists in the database");
        con.close();
        return false;
    }


    // Customer menu method options
    public static boolean viewCheckingBalance(String lastName) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from checkings");
        String s = lastName.toLowerCase();
        while(rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(s)) {
                System.out.println(lastName + ", your balance is " + rs.getInt("CurrentBalance"));
                con.close();
                return true;
            }
        }
        System.out.println("No checking account exists with the last name " + lastName);
        con.close();
        return false;
    }

    public static boolean viewSavingsBalance(String lastName) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from savings");
        String s = lastName.toLowerCase();
        while(rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(s)) {
                System.out.println(lastName + ", your balance is " + rs.getInt("CurrentBalance"));
                con.close();
                return true;
            }
        }
        System.out.println("No savings account exists with the last name " + lastName);
        con.close();
        return false;
    }

    public static boolean withdrawChecking(String lastName) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from checkings");
        String s = lastName.toLowerCase();
        while(rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(s)) {
                sc.nextLine();
                System.out.println("Input value to withdraw");
                double amount = sc.nextDouble();
                int customerID = rs.getInt("CustomerID");
                double origBalance = rs.getDouble("CurrentBalance");
                double newBalance = origBalance - amount;
                if (newBalance < 0) {
                    System.out.println("A withdraw of amount $" + amount + " is invalid due to a negative balance!");
                    con.close();
                    return false;
                }
                String sql = "UPDATE CHECKINGS "
                        + "SET CurrentBalance = " + newBalance + " WHERE CustomerID=" + customerID;
                stmt.execute(sql);
                con.close();
                return true;
            }
        }
        System.out.println("No checking account exists under " + lastName);
        con.close();
        return false;
    }

    public static boolean depositChecking(String lastName) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from checkings");
        String s = lastName.toLowerCase();
        while (rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(s)) {
                sc.nextLine();
                System.out.println("Input value to deposit");
                double amount = sc.nextDouble();
                int customerID = rs.getInt("CustomerID");
                double origBalance = rs.getDouble("CurrentBalance");
                double newBalance = origBalance + amount;
                String sql = "UPDATE CHECKINGS "
                        + "SET CurrentBalance = " + newBalance + " WHERE CustomerID=" + customerID;
                stmt.execute(sql);
                con.close();
                return true;
            }
        }
        System.out.println("No checking account exists under " + lastName);
        con.close();
        return false;
    }

    public static boolean withdrawSavings(String lastName) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from savings");
        String s = lastName.toLowerCase();
        while(rs.next()) {
            if (rs.getString("LastName").toLowerCase().equals(s)) {
                sc.nextLine();
                System.out.println("Input value to withdraw");
                double amount = sc.nextDouble();
                int customerID = rs.getInt("CustomerID");
                double origBalance = rs.getDouble("CurrentBalance");
                double newBalance = origBalance - amount;
                if (newBalance < 0) {
                    System.out.println("A withdraw of amount $" + amount + " is invalid due to a negative balance!");
                    con.close();
                    return false;
                }
                String sql = "UPDATE savings "
                        + "SET CurrentBalance = " + newBalance + " WHERE CustomerID=" + customerID;
                stmt.execute(sql);
                con.close();
                return true;
            }
        }
        System.out.println("No savings account exists under " + lastName);
        con.close();
        return false;
    }

    public static boolean depositSavings(String lastName) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from savings");
        String s = lastName.toLowerCase();
        while(rs.next()) {
            if(rs.getString("LastName").toLowerCase().equals(s)) {
                sc.nextLine();
                System.out.println("Input value to deposit");
                double amount = sc.nextDouble();
                int customerID = rs.getInt("CustomerID");
                double origBalance = rs.getDouble("CurrentBalance");
                double newBalance = origBalance + amount;
                String sql = "UPDATE SAVINGS "
                        + "SET CurrentBalance = " + newBalance +" WHERE CustomerID=" +customerID;
                stmt.execute(sql);
                con.close();
                return true;
            }
        }
        System.out.println("No savings account exists under " + lastName);
        con.close();
        return false;
    }


    // System menu options

    public static boolean checkAccountExists(String lastName) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from customer");
        String lname = lastName.toLowerCase();
        while(rs.next()) {
            if(rs.getString("LastName").toLowerCase().equals(lname)) {
                con.close();
                return true;
            }
        }
        con.close();
        return false;
    }
    public static void deleteCustomerDatabase() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        String s = "DELETE FROM CUSTOMER";
        stmt.execute(s);
        con.close();
    }
    public static void deleteCheckingsDatabase() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        String s = "DELETE FROM CHECKINGS";
        stmt.execute(s);
        con.close();
    }
    public static void deleteSavingsDatabase() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        String s = "DELETE FROM SAVINGS";
        stmt.execute(s);
        con.close();
    }
    public static void deleteEmployeeDatabase() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        String s = "DELETE FROM EMPLOYEE";
        stmt.execute(s);
        con.close();
    }
    public static void accumulateSavings() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        Statement executeStmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from savings");
        while(rs.next()) {
            int customerID = rs.getInt("CustomerID");
            double origBalance = rs.getDouble("CurrentBalance");
            double interest = rs.getDouble("InterestRate");
            double newBalance = origBalance + (origBalance * interest);
            String sql = " UPDATE SAVINGS "
                    + "SET CurrentBalance = " + newBalance + " WHERE CustomerID=" + customerID;
            executeStmt.execute(sql);
        }
        con.close();
    }
}
