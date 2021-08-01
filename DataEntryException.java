package com.cognixia.jump.java.finalproject;

public class DataEntryException extends Exception{

	/**
	 * Required SerialVersionUID when extending from the Exception class
	 */
	private static final long serialVersionUID = 3143278563866009356L;
	
	// Number of required data columns for each entry to be valid
	private static int txtColumns = 4;
	private static int csvColumns = 6;
	
	// Incorrect number of data fields for a data entry
	public DataEntryException(int line, int columns, boolean isCsvImport) {
		super("Data Entry Error! A data entry has " + columns + " fields when there should only be " + 
			(isCsvImport ? csvColumns : txtColumns) + " has occured on line " + line + ". Please review and fix your import file.");
	}
	
	// Incorrect data type for integer fields
	public DataEntryException(int line, NumberFormatException e) {
		super("Data Entry Error on line " + line + ". The 3rd data field is not an integer type. Please correct your file.");
	}
	
	// Helper exception that propagates the argument to the final exception
	public DataEntryException(String deptName) {
		super(deptName);
	}
	
	// Details which line throws the error for a non-existent department
	public DataEntryException(int line, String deptName) {
		super("Data Entry Error on line " + line + ". The department '" + deptName + "' does not exist. Please correct your file.");
	}
	
	// Option out of bounds
	public DataEntryException(int start, int end, int input) {
		super("Option Index Out of Bounds! Your input of '" + input + "' is out of range of the possible options [" + start + "-" + end + "].");
	}
}
