package kcetJava;

public class CollegeRegistration {
	
	private int collegeRegId;
	private String collegeName;
	private String collegeId;
	private String collegePassword;
	private String state;
	
	CollegeRegistration(int collegeRegId,String collegeName,String collegeId,String collegePassword,String state)
	{
		this.setCollegeRegId(collegeRegId);
		this.setCollegeName(collegeName);
		this.setCollegeId(collegeId);
		this.setCollegePassword(collegePassword);
		this.setState(state);
	}
	
	public int getCollegeRegId() {
		return collegeRegId;
	}
	public void setCollegeRegId(int collegeRegId) {
		this.collegeRegId = collegeRegId;
	}
	public String getCollegeName() {
		return collegeName;
	}
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	public String getCollegeId() {
		return collegeId;
	}
	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}
	public String getCollegePassword() {
		return collegePassword;
	}
	public void setCollegePassword(String collegePassword) {
		this.collegePassword = collegePassword;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	

}
