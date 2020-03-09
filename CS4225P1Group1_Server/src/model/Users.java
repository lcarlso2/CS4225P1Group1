package model;

import java.util.ArrayList;
/**
 * The class responsible for keeping track of the users
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 *
 */
public class Users {
	
	private ArrayList<User> users;
	
	/**
	 * Creates a new users object
	 * @param users the users 
	 * @precondition none
	 * @postcondition the new object is created
	 */
	public Users(ArrayList<User> users) {
		this.users = users;
	}
	
	/**
	 * Validates the login credentials. Returns true if they are valid, otherwise false
	 * @param user the user name
	 * @param pass the password 
	 * @return Returns true if they are valid, otherwise false
	 */
	public boolean validateCredentials(String user, String pass) {
		for (var current : this.users) {
			if (current.validate(user, pass)) {
				return true;
			}
		}
		return false;
	}

}
