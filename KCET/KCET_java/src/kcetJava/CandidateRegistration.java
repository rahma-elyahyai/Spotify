package kcetJava;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class CandidateRegistration implements CetRegistration,CandidateCollegePreference,Grievance,ResultDeclaration,
CandidateCollegeAllotment,AdmitCard
{
	private int studentRegId;
	private String firstName;
	private String lastName;
	private String userName;
	private String studentPassword;
	private String city;
	private String state;
	private String pincode;
	private String college;
	private String branch;
	private String yearOfPassing;
	private int cetMarks;
	ArrayList<Preference>CandidatePreferences = new ArrayList<Preference>();	
	CandidateRegistration(int studentRegId, String firstname,String lastname,String userName,String studentPassword,String city,String state,
			String pincode,String college,String branch,String yearOfPassing)
	{
		this.setStudentRegId(studentRegId);
		this.setBranch(branch);
		this.setFirstName(firstname);
		this.setLastName(lastname);
		this.setUserName(userName);
		this.setStudentPassword(studentPassword);
		this.setCity(city);
		this.setState(state);
		this.setPincode(pincode);
		this.setCollege(college);
		this.setBranch(branch);
		this.setYearOfPassing(yearOfPassing);
		
	}
	
	public int getStudentRegId() {
		return studentRegId;
	}


	public void setStudentRegId(int studentRegId) {
		this.studentRegId = studentRegId;
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


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getStudentPassword() {
		return studentPassword;
	}


	public void setStudentPassword(String studentPassword) {
		this.studentPassword = studentPassword;
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


	public String getPincode() {
		return pincode;
	}


	public void setPincode(String pincode) {
		this.pincode = pincode;
	}


	public String getCollege() {
		return college;
	}


	public void setCollege(String college) {
		this.college = college;
	}


	public String getBranch() {
		return branch;
	}


	public void setBranch(String branch) {
		this.branch = branch;
	}


	public String getYearOfPassing() {
		return yearOfPassing;
	}


	public void setYearOfPassing(String yearOfPassing2) {
		this.yearOfPassing = yearOfPassing2;
	}

	@Override
	public void studentCetRegistration() {
		// TODO Auto-generated method stub
	
		
		int studentId;
		studentId=this.getStudentRegId();
		String firstname;
		String lastname;
		String email;
		String address;
		String currentAddress;
		String city;
		String state;
		String zip;
		String phoneNumber;
		String category;
		String aadharNumber;
		boolean governmentId;
		boolean passportPhoto;
		boolean marksCard10th;
		boolean marksCard12th;
		int physicsMarks;
		int chemistryMarks;
		int mathMarks;  
		
		System.out.println(this.getFirstName()+ " has successfully registered for CET");
		}

	@Override
	public void collegePrefernce() {
		// TODO Auto-generated method stub

	System.out.println("college preferences for "+ this.getFirstName());
	for(Preference each : CandidatePreferences)
	{
		each.display();
	}
		
	}

	@Override
	public void candidateGrievance() {
		try (// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in)) {
			String grievance;
			
			System.out.println("State your grievance:");
			grievance = input.next();
			
			
			System.out.println("Grievance:" + grievance);
		}
		
	}



	public int getCetMarks() {
		return cetMarks;
	}

	public void setCetMarks(int cetMarks) {
		this.cetMarks = cetMarks;
	}

	@Override
	public void resultDeclaration(System1 system1) {
		// TODO Auto-generated method stub
		
		
	system1.rankDeclaration(getStudentRegId());
		
		
	}

	@Override
	public void collegeAllotment(System1 system) {
		// TODO Auto-generated method stub
		
		system.seatAllotmentByID(studentRegId);
		
	}

	@Override
	public void admitCard() {
		// TODO Auto-generated method stub
		
		String text =String.valueOf( this.getStudentRegId());
		text=text+"_"+this.getFirstName();
		text = text+".txt";
		File file = new File(text);
		
		try (BufferedWriter output = new BufferedWriter(new FileWriter(file))) {
		
			output.write("ADMIT CARD");
			output.newLine();
			output.write(this.getFirstName()+" ");
			output.write(this.getLastName());
			output.newLine();
			output.write(String.valueOf(this.getStudentRegId()));
			output.newLine();
			output.write("Test Centre : Galaxe Solutions");
			output.newLine();
			output.write("Test timings : 2.00 pm");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}




		
}




