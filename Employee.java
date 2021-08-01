package com.cognixia.jump.java.finalproject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//For now, just inherit from Person. Will find another way to use inheritance later
//Not completely sure if composition is a form of inheritance because inheritance is
//a child class obtaining properties of its parent class
public class Employee extends Person{

	private static int empCounter = 0;
	private static int highestId = 0;
	
	private int id;
	private int salary;
	private Department department;
	private String workEmail;
	
	// Constructor
	public Employee(String firstName, String lastName, int salary, Department department) {
		super(firstName, lastName);
		this.salary = salary;
		this.department = department;
		this.workEmail = String.format("%s.%s@work.com", firstName, lastName);
		
		if (highestId > empCounter) {
			this.id = ++highestId;
			empCounter++;
		} else {
			this.id = ++empCounter;
			highestId = id;
		}
		
	}
	
	public Employee(int id, String firstName, String lastName, int salary, Department department) {
		super(firstName, lastName);
		this.id = id;
		this.salary = salary;
		this.department = department;
		this.workEmail = String.format("%s.%s@work.com", firstName.toLowerCase(), lastName.toLowerCase()); 
		empCounter++;
		
		// Update the highest id if the current id is greater than the current value
		if (id > highestId) {
			highestId = id;
		}
	}

	// Getters and Setters
	public static int getHighestId() {
		return highestId;
	}
	
	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public int getId() {
		return id;
	}

	public String getWorkEmail() {
		return workEmail;
	}
	
	public void updateWorkEmail() {
		this.workEmail = String.format("%s.%s@work.com", super.getFirstName().toLowerCase(), super.getLastName().toLowerCase());
	}
	
	@Override
	public String toString() {
		return "Employee [id=" + id + ", salary=" + salary + ", department=" + department.name
				+ ", workEmail=" + workEmail + ", getFirstName()=" + getFirstName() + ", getLastName()=" + getLastName()
				+ "]";
	}

	// Return a formatted string that displays employee details in a clean format
	public String prettifyDetails() {
		return
				"Employee ID: " + id + "\n" +
				"Name: " + super.getFirstName() + " " + super.getLastName() + "\n" +
				"Salary: " + salary + "\n" +
				"Work Email: " + workEmail + "\n" +
				"Department: " + department.name + "\n";
				
	}
	
	public String detailsToCsvData() {
		List<String> csvDetails = Arrays.asList(String.valueOf(id), getFirstName(), getLastName(), String.valueOf(salary), workEmail, department.name);
		String csvLine = csvDetails.stream()
				.map(s -> String.valueOf(s))
				.collect(Collectors.joining(","));
		
		return csvLine;
	}
	
	public static void main(String[] args) {
		Employee test = new Employee("FirstName", "LastName",  60000, Department.RND);
		System.out.println(test.getDepartment());
		System.out.println(test.detailsToCsvData());
	}
}
