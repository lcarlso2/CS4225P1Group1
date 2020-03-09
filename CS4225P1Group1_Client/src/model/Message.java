package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
/**
 * The class responsible for keeping track of the message object that implements Serializable 
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 *
 */
public class Message implements Serializable  {

	/**
	 * The serial version id
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	/**
	 * Creates a new message with the specified message
	 * @precondition none
	 * @postcondition the message is created
	 * @param message the message
	 */
	public Message(String message) {
		this.message = message;
	}
	
	/**
	 * Gets the message
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * Strips the message of the type 
	 * @precondition none
	 * @postcondition the message is stripped of the type
	 */
	public void stripMessageOfType() {
		this.message = this.message.split("---")[1];
	}

	/**
	 * Gets the serialized message in an array of bytes
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the serialized message
	 */
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

	/**
	 * Gets the unserialized message 
	 * @param byteArray the byte array the message is being unserialized from
	 * @return the message
	 */
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
