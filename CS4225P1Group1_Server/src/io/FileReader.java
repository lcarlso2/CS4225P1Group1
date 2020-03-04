package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import model.User;
import model.Users;

public class FileReader {
	
	public Users GetUsers(String fileName) {
		var file = new File(fileName);
		var users = this.readFile(file);
		return new Users(users);
		
	}
	
	private ArrayList<User> readFile(File file) {
		var users = new ArrayList<User>();
		try {
			var scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				var line = scanner.nextLine();
				var lineSplit = line.split(" ");
				var username = lineSplit[0].split(":")[1];
				var password = lineSplit[1].split(":")[1];
				var user = new User(username, password);
				users.add(user);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return users;
	}

}
