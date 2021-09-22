import java.util.*;
import java.sql.*;

public class Person {
	//customer, manager, architect and engineer attributes.
	private static String customer_name, customer_email, customer_address, 
	engineer_name, engineer_email, engineer_address,  
	manager_name, manager_email, manager_address,
	architect_name, architect_email, architect_address;
	
	private static int engineer_id, manager_id, architect_id, customer_id, inputID,
	customer_telephone, engineer_telephone, manager_telephone, architect_telephone;
	
	static float outstandingFees;
	static ResultSet results;
	static int rowsAffected, choice;
	static boolean error = false;
	static Scanner userInput = new Scanner(System.in);	
	
	Project project = new Project();
	
	/**
	 * This method lets the user choice to either select an existing engineer or add a new one for the project.
	 * @param statement
	 */
	public void assignEngineer(Statement statement) {
		while (!error) {
			System.out.println("Choose the relevent option: \n1- Assign existing Structural Engineers \n2- Assign a new Structural Engineer");
			choice = userInput.nextInt();
			userInput.nextLine();
			
			if (choice == 1) {
				System.out.println("Enter the existing Structural Engineer's ID: ");
				engineer_id = userInput.nextInt();
				userInput.nextLine();
				error = true;
			}
			else if(choice == 2) {
				addEngineer(statement);
				error = true;
			}
			else {
				System.out.println("Invalid entry, Please make sure you enter the correct option.");
			}
		}
	}
 
	/**
	 * This method lets the user choice to either select an existing manager or add a new one for the project.
	 * @param statement
	 */
	public void assignManager(Statement statement) {
		error = false;
		while(!error) {
			System.out.println("Choose the relevent option: \n1- Assign existing Project Manager \n2- Assign a new Project Manager");
			choice = userInput.nextInt();
			userInput.nextLine();
			
			if (choice == 1) {
				System.out.println("Enter the existing Project Manager's ID: ");
				manager_id = userInput.nextInt();
				userInput.nextLine();
				error = true;
			}
			else if(choice == 2) {
				addManager(statement);
				error = true;
			}
			else {
				System.out.println("Invalid entry, Please make sure you enter the correct option.");
			}			
		}
	}	
	/**
	 * This method lets the user choice to either select an existing architect or add a new one for the project.
	 * @param statement
	 */
	public void assignArchitect(Statement statement) {
		error = false;
		while(!error) {
			System.out.println("Choose the relevent option: \n1- Assign existing Architect \n2- Assign a new Architect");
			choice = userInput.nextInt();
			userInput.nextLine();
			
			if (choice == 1) {
				System.out.println("Enter the existing Architect's ID: ");
				architect_id = userInput.nextInt();
				userInput.nextLine();
				error = true;
			}
			else if(choice == 2) {
				addArchitect(statement);
				error = true;
			}
			else {
				System.out.println("Invalid entry, Please make sure you enter the correct option.");
			}			
		}
	}
	
	/**
	 * This method will take in the customer's details and adds them to the table after assigning it to the project
	 * @param statement
	 * @throws SQLExecption
	 * @throws InputMismatchExecption
	 */
	public void addCustomer(Statement statement) {
		System.out.println("-----Add Customer-----\n");
		try {
			results = statement.executeQuery("SELECT MAX(customer_id) FROM customer");
			results.next();
			customer_id = results.getInt("MAX(customer_id)") + 1;
			customer_id =+ 1;
				
			System.out.println("Enter the customer name: ");
			customer_name = userInput.nextLine();
			customer_name = "'"+ customer_name +"'";
			
			System.out.println("Enter the customer's telephone: ");
			customer_telephone = userInput.nextInt();
			userInput.nextLine();
			
			System.out.println("Enter the customer's email address: ");
			customer_email = userInput.nextLine();
			customer_email = "'" + customer_email + "'";
			
			System.out.println("Enter the customer address(10 Example street, City): ");
			customer_address = userInput.nextLine();
			customer_address = "'"+ customer_address +"'";
			
			//after taking in the customers details use the engineer, manager, architects id that exists or newly added then link it to the customer table.
			rowsAffected = statement.executeUpdate("INSERT INTO customer VALUES(" + customer_id +","+ customer_name +","+ customer_telephone +","+ customer_email +","+ customer_address +","+ 
			engineer_id +","+ manager_id +","+ architect_id +")");
			System.out.println("Query complete, " + rowsAffected + "customer added.");
		}
		catch(SQLException e) {
			System.out.println("Failed to add customer to table.");
		}
		catch (InputMismatchException e) {
			System.out.println("Invalid data entry. Please enter the correct data.");
		}
	}
	
	/**
	 * This method adds a new Engineer to the engineer table.
	 * @param statement
	 * @throws SQLExecption
	 * @throws InputMismatchExecption
	 */
	public void addEngineer(Statement statement) {
		System.out.println("-----Add Structural Engineer-----\n");
		try {
			results = statement.executeQuery("SELECT MAX(engineer_id) FROM engineer");
			results.next();
			engineer_id = results.getInt("MAX(engineer_id)") + 1;
			engineer_id =+ 1;
			
			System.out.println("Enter the Engineer's name: ");
			engineer_name = userInput.nextLine();
			engineer_name = "'"+ engineer_name +"'";
			
			System.out.println("Enter the Engineer's telephone: ");
			engineer_telephone = userInput.nextInt();
			userInput.nextLine();
			
			System.out.println("Enter the Engineer's email address: ");
			engineer_email = userInput.nextLine();
			engineer_email = "'" + engineer_email + "'";
			
			System.out.println("Enter the Engineer's address(10 Example street, City): ");
			engineer_address = userInput.nextLine();
			engineer_address = "'"+ engineer_address +"'";
			
			//add to engineer table
			rowsAffected = statement.executeUpdate("INSERT INTO engineer VALUES("+ engineer_id +", "+ engineer_name +", "+ engineer_telephone +", "+ engineer_email +", "+ engineer_address +")" );
			System.out.println("Query complete, " + rowsAffected + " engineer added.");
			
		} 
		catch (SQLException e) {
			System.out.println("Failed to add engineer into table. " + e);
		}
		catch (InputMismatchException e) {
			System.out.println("Invalid data entry. Please enter the correct data.");
		}
	}
	
	/**
	 * This method adds a new Manager to the manager table.
	 * @param statement
	 * @throws SQLExecption
	 * @throws InputMismatchExecption
	 */
	public void addManager(Statement statement) {
		System.out.println("-----Add Project Manager-----\n");
		try {
			results = statement.executeQuery("SELECT MAX(manager_id) FROM manager");
			results.next();
			manager_id = results.getInt("MAX(manager_id)") + 1;
			manager_id =+ 1;
			
			System.out.println("Enter the Manager's name: ");
			manager_name = userInput.nextLine();
			manager_name = "'"+ manager_name +"'";
			
			System.out.println("Enter the Manager's telephone: ");
			manager_telephone = userInput.nextInt();
			userInput.nextLine();
			
			System.out.println("Enter the Manager's email address: ");
			manager_email = userInput.nextLine();
			manager_email = "'" + manager_email + "'";
			
			System.out.println("Enter the Manager's address(10 Example street, City): ");
			manager_address = userInput.nextLine();
			manager_address = "'"+ manager_address +"'";
			
			//add to manager table
			rowsAffected = statement.executeUpdate("INSERT INTO manager VALUES(" + manager_id +", "+ manager_name +", "+ manager_telephone +", "+ manager_email +", "+ manager_address +")");
			System.out.println("Query complete, " + rowsAffected + " manager added.");
		}
		catch (SQLException e) {
			System.out.println("Failed to add project manager into table.");
		}
		catch (InputMismatchException e) {
			System.out.println("Invalid data entry. Please enter the correct data.");
		}
	}
	
	/**
	 * This method adds a new Architect to the architect table.
	 * @param statement
	 * @throws SQLExecption
	 * @throws InputMismatchExecption
	 */
	public void addArchitect(Statement statement) {
		System.out.println("-----Add Architect-----\n");
		try {
			results = statement.executeQuery("SELECT MAX(architect_id) FROM architect");
			results.next();
			architect_id = results.getInt("MAX(architect_id)") + 1;
			architect_id =+ 1;
			
			System.out.println("Enter the Architect's name: ");
			architect_name = userInput.nextLine();
			architect_name = "'"+ architect_name +"'";
			
			System.out.println("Enter the Architect's telephone: ");
			architect_telephone = userInput.nextInt();
			userInput.nextLine();
			
			System.out.println("Enter the Architect's email address: ");
			architect_email = userInput.nextLine();
			architect_email = "'" + architect_email + "'";
			
			System.out.println("Enter the Architect's address(10 Example street, City): ");
			architect_address = userInput.nextLine();
			architect_address = "'"+ architect_address +"'";
			
			//add to manager table
			rowsAffected = statement.executeUpdate("INSERT INTO architect VALUES(" + architect_id + ", "+ architect_name +", "+ architect_telephone +", "+ architect_email +", "+ architect_address +")");
			System.out.println("Query complete, " + rowsAffected + " architect added.");
		}
		catch (SQLException e) {
			System.out.println("Failed to add architect into table.");
		}
		catch (InputMismatchException e) {
			System.out.println("Invalid data entry. Please enter the correct data.");
		}
	}
	/**
	 * returns the customer id to be added to the project
	 * @return customer_id
	 */
	public int getCustomerID() {
		return customer_id;
	}
	/**
	 * returns the engineer id to be added to the project
	 * @return engineer_id
	 */
	public int getEngineerID() {
		return engineer_id;
	}
	/**
	 * returns the manager id to be added to the project
	 * @return manager_id
	 */
	public int getManagerID() {
		return manager_id;
	}
	/**
	 * returns the architect id to be added to the project
	 * @return architect_id
	 */
	public int getArchitect() {
		return architect_id;
	}
	
	/**
	 * displays all engineers in the database.
	 * @param statement
	 */
	public void displayEngineers(Statement statement) {
		System.out.println("-----Display Engineer(s)-----\n");
		try {
			results = statement.executeQuery("SELECT * FROM engineer");
			while(results.next()) {
				System.out.println(" Engineer ID: "+ results.getInt("engineer_id") 
				+ "\n Engineer Name: "+ results.getString("engineer_name")
				+ "\n Engineer Telephone: "+ results.getInt("engineer_telephone")
				+ "\n Engineer Email: "+ results.getString("engineer_email")
				+ "\n Engineer Address: "+ results.getString("engineer_address")
				+ "\n");
			}
		} catch (SQLException e) {
			System.out.println("Failed to read engineer table. " + e);
		}		
	}
	
	/**
	 * displays all project managers in the database.
	 * @param statement
	 */
	public void displayManagers(Statement statement) {
		System.out.println("-----Display Manager(s)-----\n");
		try {
			results = statement.executeQuery("SELECT * FROM manager");
			while(results.next()) {
				System.out.println(" Manager ID: "+ results.getInt("manager_id") 
				+ "\n Manager Name: "+ results.getString("manager_name")
				+ "\n Manager Telephone: "+ results.getInt("manager_telephone")
				+ "\n Manager Email: "+ results.getString("manager_email")
				+ "\n Manager Address: "+ results.getString("manager_address")
				+ "\n");
			}
		} catch (SQLException e) {
			System.out.println("Failed to read manager table. " + e);
		}		
	}
	
	/**
	 * displays all project architect in the database.
	 * @param statement
	 */
	public void displayArchitect(Statement statement) {
		System.out.println("-----Display Architect(s)-----\n");
		try {
			results = statement.executeQuery("SELECT * FROM architect");
			while(results.next()) {
				System.out.println(" Architect ID: "+ results.getInt("architect_id") 
				+ "\n Architect Name: "+ results.getString("architect_name")
				+ "\n Architect Telephone: "+ results.getInt("architect_telephone")
				+ "\n Architect Email: "+ results.getString("architect_email")
				+ "\n Architect Address: "+ results.getString("architect_address")
				+ "\n");
			}
		} catch (SQLException e) {
			System.out.println("Failed to read architect table. " + e);
		}		
	}

	/**
	 * This method lets the user enter the project id for which you want an invoice for, an invoice will be displayed if the is an outstanding amount but if it has been finalised it wont show.
	 * @param statement
	 */
	public void createInvoice(Statement statement) {
		System.out.println("-----Create Invoice-----\n");
		System.out.println("Enter the project ID");
		inputID = userInput.nextInt();
		userInput.nextLine();
		
		try {
			results = statement.executeQuery("SELECT customer.customer_id, customer.customer_name, customer.customer_telephone, customer.customer_email, customer.customer_address, "
					+ "project.project_fee, project.paid_amount, project.payment_status, project.deadline FROM customer, project WHERE project.project_id ="+ inputID);
			results.next();
			if(results.getString("payment_status").equals("Incomplete")) {
				outstandingFees = results.getFloat("project_fee") - results.getFloat("paid_amount");
				
				System.out.println("Customer ID: " + results.getInt("customer_id")
						+ "\nCustomer name: "+ results.getString("customer_name")
						+ "\nCustomer telephone: 0"+ results.getInt("customer_telephone")
						+ "\nCustomer email: "+ results.getString("customer_email")
						+ "\nCustomer address: "+ results.getString("customer_address")
						+ "\nProject Fee: R"+ results.getFloat("project_fee")
						+ "\nOutstanding fees: R"+ outstandingFees
						+ "\nDeadline: " + results.getString("deadline"));
			}
			else {
				System.out.println("Invoice not available, the are no outstanding payments.");
			}
		} 
		catch (SQLException e) {
			System.out.println("Failed to get customer details.");
			e.printStackTrace();
		}
	}
}
