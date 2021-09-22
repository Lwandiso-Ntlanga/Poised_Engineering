import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * This class handles the capturing of project data and storing it in database whilst also allowing for manipulation of that data.
 * @author Lwandiso
 */
public class Project {
	//project attributes
	private static int project_id, ERF, inputID, manager_telephone;
	private static String project_name, project_address, building_type, deadline, manager_email;	
	private static double project_fee, paid_amount, payment, payment_due;
	
	//navigation attributes
	static char choice;
	static boolean error = false;  
	
	static int rowsAffected;
	static ResultSet results;
	static Person personObject = new Person();
	static Date  currentDate = new Date();
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	static Scanner userInput = new Scanner(System.in);	
	/**
	 * The method displays all the records of the project table in the PoisedPMS database.
	 * @param statement
	 * @throws SQLException 
	 */
	public void displayAllProjects(Statement statement) {
		System.out.println("-----Display Project(s)-----\n");
		try {
			results = statement.executeQuery("SELECT * FROM project");
			while(results.next()) {
				System.out.print(" Project ID: " + results.getInt("project_id") + 
						"\n Project Name: " + results.getString("project_name") + 
						"\n Project Address: " + results.getString("project_address") + 
						"\n Building Type: " + results.getString("building_type") + 
						"\n ERF Number: " + results.getInt("ERF") + 
						"\n Project Fee: R" + results.getDouble("project_fee") + 
						"\n Paid Amount: R" + results.getDouble("paid_amount") + 
						"\n Deadline: " + results.getDate("deadline"));
				
				if(results.getString("payment_status").equals("Finalised")) {
					System.out.print("\n Payment Status: " +results.getString("payment_status") + 
							"\n Finalised Date: " + results.getString("finalised_date"));
				}
				else {
					System.out.print("\n Payment Status: " +results.getString("payment_status"));
				}		
				System.out.print("\n Engineer ID: " + results.getInt("engineer_id") + 
						"\n Manager ID: " + results.getInt("manager_id") + 
						"\n Architect ID: " + results.getInt("architect_id") + 
						"\n Customer ID: " + results.getInt("customer_id") +
						"\n\n");						
			}
		}		
		catch (SQLException e) {
			System.out.println("Failed to view project table \n" + e);
		}
	}
	
	/**
	 * This method lets the user add a new project into the database.
	 * @param statement
	 * @throws SQLExecption
	 */
	public void addProject(Statement statement) {
		System.out.println("-----Add Project-----\n");
		try {
			results = statement.executeQuery("SELECT MAX(project_id) FROM project");
			results.next();
			project_id = results.getInt("MAX(project_id)") ;
			project_id =+1;

			System.out.println("Enter the project name: ");
			project_name = userInput.nextLine();
			project_name = "'" + project_name + "'";
			
			System.out.println("Enter the project address: ");
			project_address = userInput.nextLine();
			project_address = "'"+ project_address +"'";
			
			System.out.println("Enter the building type: ");
			building_type = userInput.nextLine();
			building_type = "'"+ building_type +"'";
			
			System.out.println("Enter the ERF number: ");
			ERF = userInput.nextInt();
		
			System.out.println("Enter the project fee: ");
			project_fee = userInput.nextDouble();
			
			System.out.println("Enter the initial payment: ");
			paid_amount = userInput.nextDouble();
			userInput.nextLine();
			
			System.out.println("Enter the deadline (yyyy-mm-dd): ");
			deadline = userInput.nextLine();
			deadline = "'" + deadline +"'";
			
			//these assign the respective engineer, project manager and architect to the project being created, which will directly be assigned to the customer.
			personObject.assignEngineer(statement);
			
			personObject.assignManager(statement);
			
			personObject.assignArchitect(statement);
			
			//this method will assign a new customer id whilst also taking in the customer's details.
			personObject.addCustomer(statement);
			
			rowsAffected = statement.executeUpdate("INSERT INTO project VALUES("+ project_id +","+ project_name +","+ project_address +","+ building_type +","+ 
			ERF +","+ project_fee +","+ paid_amount +","+ deadline +","+ personObject.getEngineerID() +","+ personObject.getManagerID() +","+ personObject.getArchitect() +","+ personObject.getCustomerID()+")");
			System.out.println("Query complete, " + rowsAffected + " project added.");
			
		}
		catch(SQLException e) {
			System.out.println("Failed to add project to project table \n" + e);
		}
		catch (InputMismatchException e) {
			System.out.println("Invalid data entry. Please enter the correct data.");
		}
	}
	/**
	 * The method allows the user to update the chosen project's deadline, payment and contact details.
	 * @param statement
	 * @throws Exception
	 */
	public void updateProject(Statement statement) throws Exception {
		System.out.println("-----Update Project-----\n");
		try {
			System.out.println("Enter the project ID");
			inputID = userInput.nextInt();
			userInput.nextLine();
			error = false;
			
			results = statement.executeQuery("SELECT project.project_id, project.deadline, project.project_fee, project.paid_amount, manager.manager_telephone, manager.manager_email "
					+ "FROM project, manager WHERE project.project_id =" + inputID);
			results.next();
			project_id = results.getInt("project_id");
			
			if(project_id == inputID) {
				while(!error) {
					System.out.println("\nEnter what you would like to update(d- Deadline, p- Payment, m- Project Manager's contacts, b- Back):");
					char choice = userInput.next().charAt(0);
					userInput.nextLine();
					
					switch(Character.toLowerCase(choice)) {
					case 'd':
						updateDeadline(statement);
						break;
						
					case 'p':
						updatePayment(statement, inputID);
						break;
						
					case 'm':
						updateContactDetails(statement);
						break;
						
					case 'b':
						error = true;
						break;
						
					default:
						System.out.println("Invalid entry. Please enter the correct data.");
					}					
				}
			}			
		}
		catch(SQLException e) {
			System.out.println("Failed to update project." + e);
		}
		catch (InputMismatchException e) {
			System.out.println("Invalid entry. Please enter the correct data." + e);
			
		}
	}
	/**
	 * This method lets the user update the contact details for the project manager. An update to the telephone and email address.
	 * @param statement
	 * @throws SQLException
	 */
	public void updateContactDetails(Statement statement) throws SQLException {
		System.out.println("-----Update Contact Details-----\n");
		System.out.println("Enter the new telephone number: ");
		manager_telephone = userInput.nextInt();
		userInput.nextLine();
		
		System.out.println("Enter the new email address: ");
		manager_email = userInput.nextLine();
		manager_email = "'"+ manager_email +"'";
		
		rowsAffected = statement.executeUpdate("UPDATE manager SET manager_email =" + manager_email + ", manager_telephone =" + manager_telephone);
		System.out.println("Query complete, " + rowsAffected + " manager updated.");
		error = true;
	}
	/**
	 * Method updates the payments made, lets the user make payments to cover the project fee then once done update the database of the date of finalisation and payment for the project specified.
	 * @param statement
	 * @param inputID
	 * @throws SQLException
	 */
	public void updatePayment(Statement statement, int inputID) throws SQLException {
		System.out.println("-----Update Project Payment-----\n");
		//calculates the payment due from the total project fee.
		paid_amount = results.getDouble("paid_amount");
		project_fee = results.getDouble("project_fee");
		payment_due = project_fee - paid_amount;
		
		//this determines if the is still an outstanding amount to be paid or if it's fully paid off before providing options that follow afterwards..
		if (!(payment_due <= 0)) {
			System.out.println("Outstanding amount: R" + payment_due);
			System.out.println("Enter payment amount: ");
			payment = userInput.nextDouble();
			
			//checks if the user over paid
			if (!(payment > payment_due)) {
				paid_amount = payment + paid_amount;
				deadline = dateFormat.format(currentDate) ;
				
				if (paid_amount == project_fee) {
					rowsAffected = statement.executeUpdate("UPDATE project SET paid_amount =" + paid_amount + ", payment_status = 'Finalised' , finalised_date = '"+ deadline +"' WHERE project_id =" + inputID);
					System.out.println("Query complete, " + rowsAffected + " project updated.");
				}
				else {
					rowsAffected = statement.executeUpdate("UPDATE project SET paid_amount =" + paid_amount + "WHERE project_id =" + inputID);
					System.out.println("Query complete, " + rowsAffected + " project updated.");
				}
				error = true;
			} 
			else {
				System.out.println("Error, the payment exceeds the due payment. Please input correct payment.");
			}
		}
		else {
			System.out.println("The is no outstanding payment.");	
			error = true;
		}
	}

	/**
	 * Method allows the user to update the deadline to a different date then updates the database
	 * @param statement
	 * @throws SQLException
	 */
	public void updateDeadline(Statement statement) throws SQLException {
		System.out.println("-----Update Deadline-----\n");
		System.out.println("Enter the new deadline(yyyy-mm-dd): ");
		deadline = userInput.nextLine();//date input changed to string in the database so that it can accept the deadline change/update.
		//deadline = "'"+ deadline +"'";
		
		rowsAffected = statement.executeUpdate("UPDATE project SET deadline ='" + deadline +"' WHERE project_id =" + project_id );
		System.out.println("Query complete, " + rowsAffected + " project updated.");
		error = true;
	}

	/**
	 * This method displays all projects that are incomplete.
	 * @param statement
	 * @throws SQLException
	 */
	public void displayIncompleteProjects(Statement statement) throws SQLException {
		System.out.println("-----Display Incomplete Project(s)-----\n");
		try {
			results = statement.executeQuery("SELECT * FROM project WHERE payment_status = 'Incomplete'");
			while(results.next()) {
				System.out.println(" Project ID: " + results.getInt("project_id") + 
						"\n Project Name: " + results.getString("project_name") + 
						"\n Project Address: " + results.getString("project_address") + 
						"\n Building Type: " + results.getString("building_type") + 
						"\n ERF Number: " + results.getInt("ERF") + 
						"\n Project Fee: R" + results.getDouble("project_fee") + 
						"\n Paid Amount: R" + results.getDouble("paid_amount") + 
						"\n Payment Status: " + results.getString("payment_status") +
						"\n Deadline: " + results.getDate("deadline") + 
						"\n Engineer ID: " + results.getInt("engineer_id") + 
						"\n Manager ID: " + results.getInt("manager_id") + 
						"\n Architect ID: " + results.getInt("architect_id") + 
						"\n Customer ID: " + results.getInt("customer_id") +
						"\n");
			}			
		} 
		catch (SQLException e) {
			System.out.println("Failed to view project table.");
		}
	}
	
	/**
	 * This method displays all the projects that are incomplete and are over due
	 * @param statement
	 */
	public void displayOverdueProjects(Statement statement) {
		System.out.println("-----Display Overdue Project(s)-----\n");
		try {
			results = statement.executeQuery("SELECT * FROM project WHERE payment_status = 'Incomplete' AND deadline < CURRENT_DATE() ");
			while(results.next()) {
			System.out.println(" Project ID: " + results.getInt("project_id") + 
					"\n Project Name: " + results.getString("project_name") + 
					"\n Project Address: " + results.getString("project_address") + 
					"\n Building Type: " + results.getString("building_type") + 
					"\n ERF Number: " + results.getInt("ERF") + 
					"\n Project Fee: R" + results.getDouble("project_fee") + 
					"\n Paid Amount: R" + results.getDouble("paid_amount") + 
					"\n Payment Status: " + results.getString("payment_status") +
					"\n Deadline: " + results.getDate("deadline") + 
					"\n Engineer ID: " + results.getInt("engineer_id") + 
					"\n Manager ID: " + results.getInt("manager_id") + 
					"\n Architect ID: " + results.getInt("architect_id") + 
					"\n Customer ID: " + results.getInt("customer_id") +
					"\n");
			}					
		} 
		catch (SQLException e) {
			System.out.println("Failed to view project table." + e);
		}
	}

	//closes the result set once done with the program.
	public void closeResultset() {
		try {
			results.close();
		} catch (SQLException e) {
			System.out.println("failed to close results set.");
		}
	}
}
