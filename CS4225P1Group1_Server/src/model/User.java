package model;

public class User {
	private String username;
	private String password;
	
	public User(String user, String pass) {
		this.username = user;
		this.password = pass;
	}
	
	public boolean validate(String user, String pass) {
		return this.username.equals(user) && this.password.equals(pass);
	}
	
	public String toString() {
		return this.username + " " + this.password;
	}

}
