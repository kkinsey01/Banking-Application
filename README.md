# Banking-Application

Hello, this is my banking application using some basic OOP concepts and my first attempt at anything using MySQL. I learned SQL completely on my own through research and google, so the code may be a little messy.

The CheckingAccount, Customer, SavingsAccount, and Employee classes create the oop objects to add to the databases. The Main class gives all the functionality to the program and lets the user control what they want to do through the menu options. This program does not use a GUI, it is all console/terminal based. I also included screenshots of an example of what the databases would look like.

Important note: In the customer database, if there is a 1 in either the CheckingAccount or SavingsAccount, that means that the customer has the corresponding account. If there is a 0, the customer does not have the corresponding account.

Another important note: Assume that when a customer is creating a new account, that they walk into the bank and ask the teller. The teller then creates the account through the admin menu. There is no option in the customer menu to make themselves a customer, they have to talk to a bank employee. Same thing for creating a checking/savings account. 


The idea is that a bank would use this application to enter any employees and customers and keep track of all their employees, customers and their respective accounts. This way all the data would be organized in a MySQL table. 
