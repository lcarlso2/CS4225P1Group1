package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * The serial version id
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public Message(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

	public byte[] getSerializedMessage() {
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteArray);
			out.writeObject(this);

			out.flush();
			byteArray.close();
			return byteArray.toByteArray();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			return null;
		}
	}

	public static Message getUnserializedMessage(byte[] byteArray) {
		try {
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteArray);
			ObjectInputStream inStream = new ObjectInputStream(byteIn);
			return (Message) inStream.readObject();

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			return null;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return null;
		}
	}

}
