package model;

/**
 * The user class responsible for keeping track of the user
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 *
 */
public class User {
	private String username;
	private String password;
	
	/**
	 * Creates a new user with the given username and password
	 * @param user the username
	 * @param pass the password
	 * @precondition none
	 * @postcondition the user is created
	 */
	public User(String user, String pass) {
		this.username = user;
		this.password = pass;
	}
	
	/**
	 * Validates the given username and password against the actual username and password
	 * @param user the username
	 * @param pass the password 
	 * @return true if they match otherwise false
	 */
	public boolean validate(String user, String pass) {
		return this.username.equals(user) && this.password.equals(pass);
	}
	
	/**
	 * Gets the tostring representation of the user object
	 * @precondition none
	 * @postcondition none
	 * @return the toString representation of the user
	 */
	public String toString() {
		return this.username + " " + this.password;
	}

}
