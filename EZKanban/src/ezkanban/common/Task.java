package ezkanban.common;

import java.sql.Date;

public class Task {
	
	private enum Priority {
		VERYHIGH, HIGH, MEDIUM, LOW
	}
	
	private enum Status {
		NOTSTARTED, INPROGRESS, DONE, CANCELLED
	}
	
	private int taskID;
	private String description;	
	private Priority priority;
	private int userID;
	private Date added;
	private Date completed;	
	private Status status;
	private int scrumUnits;
	
	public Task (String allParams){
		//Tokenizer goes here; 
	}
	

}
