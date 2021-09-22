import java.util.*;
import java.sql.*;
/**
 * The program is for an engineering company which would create construction projects and thus need a management system that will record all activities relating to the company.
 * Therefore the program allows the user to add new projects, edit existing projects whilst also recording the details of those assigned to each project.
 * The details of the customer, structural engineer, project manager, architect and the project itself will be captured into a database.
 * <p>
 * @author Lwandiso
 * @version 1.2
 * @since 2021-08-15
 */
public class Main {
	private static String driver = "jdbc:mysql://localhost:3306/poisedpms?useSSL=false";
	private static String user = "otheruser";
	private static String password = "@timop";
	
	//attributes to control the navigation of the program
	private static char complete = 'n';
	private static int menu ;
	static Scanner userInput = new Scanner(System.in);
	
	//these objects will be used to access every method in the program for full functionality.
	static Person personObject = new Person();
	static Project projectObject = new Project();
	
	//main
	public static void main(String[] args) {	
		System.out.println("**********Welcome to Poised Construction**********\n");
		//try to connect to the database before trying to do anything
		try (Connection connect = DriverManager.getConnection(driver, user, password);
				Statement statement = connect.createStatement();
		){
			do {
				System.out.println("*****************************\n"
				+ "Menu: \n"
				+ "1 - View Projects, Engineers, Manager, Architect \n"
				+ "2 - Add new Project \n"
				+ "3 - Add new Structural Engineer \n"
				+ "4 - Add new Project Manager \n"
				+ "5 - Add new Architect \n"
				+ "6 - Update Project \n"
				+ "7 - Invoice \n"
				+ "8 - View Incomplete Projects \n"
				+ "9 - Overdue Projects \n"					
				+ "0 - Exit Program \n"
				+ "*****************************");
				
				/**
				 * The user is promted to enter the available menu options such as: 
				 * preview projects, add new project, enginners, managers and  architect. Updating projects, preview incomplete and overdue projects, invoice and lastly exit the program.
				 */			
				try {
					menu = userInput.nextInt();
					userInput.nextLine();
					/**
					 * Reads from the database and displays all the available projects, engineers, managers and architect.
					 */
					if (menu == 1) {
						System.out.println("Options: \n"
								+ "1 - Projects \n"
								+ "2 - Structural Engineers \n"
								+ "3 - Project Managers \n"
								+ "4 - Architect \n"
								+ "0 - Back");
						
						try {
							int option = userInput.nextInt();
							userInput.nextLine();
							
							switch(option) {
							case 1:
								projectObject.displayAllProjects(statement); 
								break;
								
							case 2:
								personObject.displayEngineers(statement);
								break;
								
							case 3:
								personObject.displayManagers(statement);
								break;
								
							case 4:
								personObject.displayArchitect(statement);
								break;
								
							case 0:
								break;
								
							default:
								System.out.println("Invalid input, make sure input matches the given options.");
							}
						} 
						catch (InputMismatchException e) {						
							System.out.println("Invalid input, make sure input matches the given options." + e);
							userInput.next();
						} 							
					}
					
					/**
					*Allows the user to add another project, engineer, manager and architect from options 2 - 5.
					*/
					else if (menu == 2) {
						projectObject.addProject(statement);
					}
					
					else if (menu == 3) {
						personObject.addEngineer(statement);
					}
					
					else if (menu == 4) {
						personObject.addManager(statement);
					}
					
					else if (menu == 5) {
						personObject.addArchitect(statement);
					}
					
					/**
					 * Allows the user to select the desired project or project manager details they want to update, by identifying them with the IDs.
					 */
					else if (menu == 6) {								
						projectObject.updateProject(statement);
					}
					
					/**
					 * Gets the invoice for the customer of the specified project which payments aren't complete. 
					 * If payments are made in full no invoice will be generated.
					 */
					else if (menu == 7) {
						personObject.createInvoice(statement);
					}
					
					/**
					 * Displays all the incomplete projects.
					 */
					else if (menu == 8) {
						projectObject.displayIncompleteProjects(statement);
					}
					
					/**
					 * Displays all the projects that are incomplete and are overdue.
					 */
					else if (menu == 9) { 
						projectObject.displayOverdueProjects(statement);				
					}
					
					/**
					 * Ends the program and closes the opened object resources.
					 */
					else if (menu == 0) {
						complete = 'e';
						System.out.println("---Program ended---");
						projectObject.closeResultset();
						statement.close();
						connect.close();						
					}
					
					else {
						System.out.println("Invalid input, make sure input matches the menu options.");
					}
				} 
				catch (InputMismatchException e) {						
					System.out.println("Invalid input, make sure input matches the menu options. " + e);
					userInput.next();
				} 
			} while (Character.toLowerCase(complete) != 'e');			
		} 
		catch (Exception e) {
			System.out.println("Error failed to connect to the database. " + e);
		}
	}
}
