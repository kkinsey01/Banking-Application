package com.project.banking;

import java.sql.*;

/*
    Kyler Kinsey
 */
public class Employee {
    private int employeeID;
    private final int INITIAL_EMPLOYEE_ID = 100;
    private double salary;
    private String email;
    private String firstName;
    private String lastName;

    public Employee(double salary, String firstName, String lastName) {
        this.salary = salary;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = generateEmail();
        this.employeeID = getEmployeeID();
        addToDatabase();
    }


    private String generateEmail() {
        String s = firstName + lastName + "@americanbank.com";
        s.toLowerCase();
        return s;
    }
    private void addToDatabase() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingapp", "root", "MyFirstSQL0");
            String query = " insert into employee (EmployeeID, FirstName, LastName, Email, Salary) "
                    + " values (?, ?, ?, ?, ?)";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, employeeID);
            preparedStmt.setString(2, firstName);
            preparedStmt.setString(3, lastName);
            preparedStmt.setString(4, email);
            preparedStmt.setDouble(5, salary);

            preparedStmt.execute();

            con.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    private int getEmployeeID() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingapp", "root", "MyFirstSQL0");
            Statement stmt = con.createStatement();
            String query = "Select EmployeeID From employee";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                employeeID = rs.getInt("EmployeeID");
            }
            employeeID++;
            if (employeeID < 100) {
                employeeID = 100;
            }
            con.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return employeeID;
    }
}
