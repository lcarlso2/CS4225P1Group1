package model;

import java.util.ArrayList;

public class Users {
	
	private ArrayList<User> users;
	
	public Users(ArrayList<User> users) {
		this.users = users;
	}
	
	public boolean ValidateCredentials(String user, String pass) {
		for (var current : this.users) {
			if (current.validate(user, pass)) {
				return true;
			}
		}
		return false;
	}

}
