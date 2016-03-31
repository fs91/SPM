package edu.purdue.spm.entity;

public class Contact {

	
	
	String user_ID;
	String firstName;
	String lastName;
	String phone;
	String address;
	
	


	public Contact(String user_ID, String firstName, String lastName,
			String phone, String address) {
		super();
		this.user_ID = user_ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.address = address;
	}




	public String getUser_ID() {
		return user_ID;
	}




	public void setUser_ID(String user_ID) {
		this.user_ID = user_ID;
	}




	public String getFirstName() {
		return firstName;
	}




	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}




	public String getLastName() {
		return lastName;
	}




	public void setLastName(String lastName) {
		this.lastName = lastName;
	}




	public String getPhone() {
		return phone;
	}




	public void setPhone(String phone) {
		this.phone = phone;
	}




	public String getAddress() {
		return address;
	}




	public void setAddress(String address) {
		this.address = address;
	}

	
}
