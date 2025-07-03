package kcetJava;

public class KeaAdminRegistration {
	private int keaAdminID;
	private String userName;
	private String userID;
	private String keaAdminPassword;
	private String city;
	private String state;

	KeaAdminRegistration(int keaAdminID,String userName,String userID,String keaAdminPassword,String city,String state)
	{
		this.setKeaAdminID(keaAdminID);
		this.setUserName(userName);
		this.setUserID(userID);
		this.setKeaAdminPassword(keaAdminPassword);
		this.setCity(city);
		this.setState(state);
	}

public int getKeaAdminID() {
		return keaAdminID;
	}
	public void setKeaAdminID(int keaAdminID) {
		this.keaAdminID = keaAdminID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getKeaAdminPassword() {
		return keaAdminPassword;
	}
	public void setKeaAdminPassword(String keaAdminPassword) {
		this.keaAdminPassword = keaAdminPassword;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}


}
