package com.cognixia.jump.java.finalproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EmployeeManagementSystem{
	
	private static HashMap<Integer, Employee> employeeData;
	private static boolean isCsvImport;
	
	// Static Block - will run first when starting the program
	static {
		// Instantiate a File object with the employee csv file as its source
		File outputFile = new File("res/employees.csv");
		File importFile = outputFile;
		isCsvImport = true;
		
		// If the csv file DNE, this is the first time running the program at all
		if (!importFile.exists()) {
			isCsvImport = false;
			
			// Delete the current file object with the csv path
			importFile = null; 
			
			// Create new File object with the employee text file path
			importFile = new File("res/employees.txt");
			
		}
		
		// From the import data, create Employee obects
		employeeData = importEmployeeData(importFile, isCsvImport);
		
		// not csv import --> create and write to csv
		writeToCsv(outputFile, employeeData);
		
		System.out.println("Employee data SUCCESSFULLY imported.");
	}

	public static void main(String[] args) {
		boolean done = false;
		Scanner sc = new Scanner(System.in);
		int selOption;
		
		// Create a HashMap to store department employee data
		HashMap<Department, ArrayList<Employee>> deptEmployees = new HashMap<Department, ArrayList<Employee>>();
		
		// Declare an array list for each department
		for (Department dept : Department.values()) {
			deptEmployees.put(dept, new ArrayList<Employee>());
		}
		
		// Add each employee to the appropriate department list
		employeeData.values().stream().forEach(emp -> deptEmployees.get(emp.getDepartment()).add(emp));
		
		while (!done) {
			selOption = getValidatedInput(sc, displayMainMenu(), 7);
			
			switch(selOption) {
			case 1:	// View all employees
			case 3:	// View employee information
			case 4: // Update employee information
			case 6: // Delete an employee
				// Display all employees
				printEmployeeList(-1, deptEmployees);
				
				// switch case for options with additional input
				switch(selOption) {
					case 1:	// view all employees
						break;
					case 3:	
					case 4:
					case 6:
						String instruction = "Enter an employee's id to " + ((selOption == 6) ? "delete:" : "view their information:");
						int selEmpId = getValidatedInput(sc, instruction, Employee.getHighestId());
						
						switch(selOption) {
							// View employee information
							case 3:
								System.out.println(employeeData.get(selEmpId).prettifyDetails());
								break;
							case 4:
								updateEmployeeInformation(sc, employeeData.get(selEmpId));
								break;
							case 6:
								System.out.println("Employee with the following information has been deleted.");
								System.out.println(employeeData.get(selEmpId).prettifyDetails());
								employeeData.remove(selEmpId);
								break;
						}
						break;
				}
				
				break;
			case 2:
				// View employees by department
				System.out.println(listDepartments());
				int dept = getValidatedInput(sc, "Select a department:", Department.values().length) - 1;
				printEmployeeList(dept, deptEmployees);
				break;
			
			case 5:
				// Add new employee
				addNewEmployee(sc, deptEmployees);
				break;
			case 7:
				File outputFile = new File("res/employees.csv");
				writeToCsv(outputFile, employeeData);
				
				System.out.println("Have a nice day!");
				done = true;
				break;
			}
		}

	}
	
	public static void addNewEmployee(Scanner sc, HashMap<Department, ArrayList<Employee>> deptEmployees) {
		String firstName, lastName;
		int salary;
		int department;
		
		System.out.println("=== New Employee Input ===");
		System.out.print("Enter the employee's first name: ");
		firstName = sc.nextLine();
		System.out.print("Enter the employee's last name: ");
		lastName = sc.nextLine();
		salary = getValidatedInput(sc, "Enter the employee's salary:", Integer.MAX_VALUE);
		
		System.out.println(listDepartments());
		department = getValidatedInput(sc, "Enter the employee's department:", Department.values().length) - 1;
		
		Employee newEmployee = new Employee(firstName, lastName, salary, Department.values()[department]);
		
		// Add new employee to the data
		employeeData.put(newEmployee.getId(), newEmployee);
		deptEmployees.get(Department.values()[department]).add(newEmployee);
		
		System.out.println("New employee successfully added!");
	}
	
	public static String listDepartments() {
		String deptList = "\n";
		for (int i = 1; i <= Department.values().length; i++) {
			deptList += i + ". " + Department.values()[i-1].name + "\n";
		}
		
		return deptList;
	}
	
	public static void updateEmployeeInformation(Scanner sc, Employee emp) {
		System.out.println(emp.prettifyDetails());
		
		String instruction = "\nInformation you can change:\n1. First name\n2. Last name\n3. Salary\n4. Department\nSelect an option:";
		
		int dataField = getValidatedInput(sc, instruction, 4);
		
		switch(dataField) {
			case 1:
			case 2:
				changeName(sc, emp, dataField);
				emp.updateWorkEmail();
				break;
			case 3:
				int newSalary = getValidatedInput(sc, "Enter a new salary:", Integer.MAX_VALUE);
				emp.setSalary(newSalary);
				System.out.println("Salary successfully changed to " + newSalary);
				break;
			case 4:
				System.out.println(listDepartments());
				
				int newDepartment = getValidatedInput(sc, "Enter a department:", Department.values().length) - 1;
				emp.setDepartment(Department.values()[newDepartment]);
				
				System.out.println("Department successfully changed to " + emp.getDepartment().name);
				break;
			default:
				System.out.println("Field does not exist.");
				break;
				
		}
	}
	
	public static void changeName(Scanner sc, Employee emp, int dataField) {
		String nameType = dataField == 1 ? "First" : "Last";
		
		System.out.print("Enter a new " + nameType + " name:");
		String newName = sc.nextLine();
		
		
		if (dataField == 1) {
			emp.setFirstName(newName);
		} else {
			emp.setLastName(newName);
		}
		
		System.out.println(nameType + " name has successfully been changed to " + newName);
	}
	
	public static void printEmployeeList(int dept, HashMap<Department, ArrayList<Employee>> deptList) {
		ArrayList<Employee> displayList;
		
		if (dept >= 0) {
			// retrieve employee list from hashmap
			displayList = deptList.get(Department.values()[dept]);
			
		// Print all employees
		} else {
			displayList = new ArrayList<Employee>(employeeData.values());
		}
		
		String displayFormat = "%-5s %-15s %-15s %-15s";
		
		String listName = (dept > 0) ? Department.values()[dept].name + " Employee List" : "ALL EMPLOYEES LIST";
		
		System.out.println(listName);
		
		System.out.println(String.format(displayFormat, "Id", "First Name", "Last Name", "Department"));
		displayList.stream()
			.map(emp -> String.format(displayFormat, emp.getId(), emp.getFirstName(), emp.getLastName(), emp.getDepartment().name))
			.forEach(System.out::println);
	}
	
	public static String displayMainMenu() {
		return
				"\n[ ===== MAIN MENU ===== ]\n" +
				"1. View All Employees\n" +
				"2. View Employees by Department\n" +
				"3. View Employee Information\n" +
				"4. Update Employee Information\n" +
				"5. Add New Employee\n" +
				"6. Delete an Employee\n" +
				"7. Exit the Program\n\n" +
				"Please select an option:";
	}
	
	
	// Import employee data from text file
	public static HashMap<Integer, Employee> importEmployeeData(File importFile, boolean isCsvImport) {
		
		HashMap<Integer, Employee> employeeImport = new HashMap<Integer, Employee>();
		
		// File Reader
		FileReader fileReader = null;
		LineNumberReader lineReader = null;
		
		try {
			fileReader = new FileReader(importFile);
			lineReader = new LineNumberReader(fileReader);
			
			// Import employees from the import text file
			String dataEntry;
			
			int numFields = isCsvImport ? 6 : 4;
			
			// While the file has data
			while ((dataEntry = lineReader.readLine()) != null) {
				// Parse the string to get the arguments
				String[] dataFields = dataEntry.split(",");
				
				// If the number of fields don't match
				if (dataFields.length != numFields) {
					throw new DataEntryException(lineReader.getLineNumber(), dataFields.length, isCsvImport);
				}
				
				// Create employee object and append to the array
				try {
					if (isCsvImport) {
						employeeImport.put(Integer.parseInt(dataFields[0]), new Employee(Integer.parseInt(dataFields[0]), dataFields[1], dataFields[2], 
								Integer.parseInt(dataFields[3]), Department.getDepartment(dataFields[5])));
					} 
					else {
						employeeImport.put(Employee.getHighestId() + 1, new Employee(dataFields[0], dataFields[1], 
								Integer.parseInt(dataFields[2]), Department.getDepartment(dataFields[3])));
					}
				} catch (NumberFormatException e) {
					throw new DataEntryException(lineReader.getLineNumber(), e);
				} catch (DataEntryException e) {
					throw new DataEntryException(lineReader.getLineNumber(), e.getMessage());
				}

			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Import file not found!!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DataEntryException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				fileReader.close();
				lineReader.close();
				System.out.println("SUCCESSFULLY imported employee data.");
			} catch (IOException e) {
				System.out.println("ERR: FAILED to close file reader stream");
				e.printStackTrace();
			}
		}
		
		return employeeImport;
	}
	
	public static void writeToCsv(File outputFile, HashMap<Integer, Employee> data) {
		// file write objects
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)))) {
			// use stream to write to csv file
			data.values().stream()
				.map(Employee::detailsToCsvData)
				.forEach(writer::println);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// Validation functions
	public static int getValidatedInput(Scanner sc, String instruction, int maxOption) {
		int input = 0;
		boolean isValid = false;
		
		while (!isValid) {
			System.out.println(instruction);
			
			try {
				input = sc.nextInt();
				
				// Assume that the input is valid at this point
				isValid = true;
				
				// Input is not within the bounds of the option
				if (input < 1 || input > maxOption) {
					isValid = false;
					throw new DataEntryException(1, maxOption, input);
				}
			} catch (InputMismatchException e) {
				System.out.println("ERR: Input must be an integer value.");
			} catch (DataEntryException e) {
				System.out.println(e.getMessage());
			} finally {
				sc.nextLine();
			}
		}
		
		return input;
	}

}

