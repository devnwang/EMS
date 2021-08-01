package com.cognixia.jump.java.finalproject;

public enum Department {
	HR("HUMAN RESOURCES"), RND("R&D"), IT("IT"), FINANCE("FINANCE"), MARKETING("MARKETING");
	
	public final String name;
	
	private Department(String name) {
		this.name = name;
	}
	
	public static Department getDepartment(String name) throws DataEntryException {
		switch(name.toUpperCase()) {
			case "HUMAN RESOURCES":
				return Department.HR;
			case "R&D":
				return Department.RND;
			case "IT":
				return Department.IT;
			case "FINANCE":
				return Department.FINANCE;
			case "MARKETING":
				return Department.MARKETING;
			default:
				throw new DataEntryException(name);
		}
	}
}