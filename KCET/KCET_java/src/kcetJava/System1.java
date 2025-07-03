package kcetJava;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class System1 {

	ArrayList<CandidateRegistration> CandidateList = new ArrayList<CandidateRegistration>();
	ArrayList<CollegeRegistration> CollegeList = new ArrayList<CollegeRegistration>();
	ArrayList<KeaAdminRegistration> KeaAdminList = new ArrayList<KeaAdminRegistration>();
	HashMap<Integer, String> seatAllotment = new HashMap<Integer, String>();
	HashMap<Integer,String>loginDetails = new HashMap<Integer,String>();



	Comparator<CandidateRegistration>comp=new Comparator<CandidateRegistration>()
	{

	@Override public int compare(CandidateRegistration c1,CandidateRegistration c2){
	// TODO Auto-generated method stub

	if(c1.getCetMarks()>c2.getCetMarks())
		return-1;

	else if(c1.getCetMarks()<c2.getCetMarks())
	{
		return 1;
		
	}
	else

	{
		return(c1.getFirstName().compareTo(c2.getFirstName()));
	}

	}

	};

void storeDetails()
{
	for(
	CandidateRegistration each:CandidateList)
	{
		loginDetails.put(each.getStudentRegId(), each.getStudentPassword());
	}

	for(
	CollegeRegistration each1:CollegeList)
	{
		loginDetails.put(each1.getCollegeRegId(), each1.getCollegePassword());
	}

	for(
	KeaAdminRegistration each2:KeaAdminList)
	{
		loginDetails.put(each2.getKeaAdminID(),each2.getKeaAdminPassword());
	}
}

	void login(int ID, String password) {

		int flag = 0;
		for (int each3 : loginDetails.keySet()) {
			if ((loginDetails.get(each3).equals(password)) && (each3 == ID)) {
				System.out.println("login successfull");
				flag = 1;
				break;
			}
		}
		if (flag == 0) {
			System.out.println("Invalid credentials");
		}
	}

	void rankDeclaration() {
		CandidateList.sort(comp);
		int i = 1;
		for (CandidateRegistration each : CandidateList) {
			System.out.println("Rank: " + i + " Name: " + each.getFirstName() + " CET marks: " + each.getCetMarks());
			i++;
		}
	}

	void rankDeclaration(int studentId) {
		CandidateList.sort(comp);
		int i = 1;
		for (CandidateRegistration each : CandidateList) {
			if (each.getStudentRegId() == studentId) {
				System.out
						.println("Rank: " + i + " Name: " + each.getFirstName() + " CET marks: " + each.getCetMarks());
				break;
			}
			i++;
		}
	}

	void seatAllotment() {
		// seat matrix in all colleges

		HashMap<String, Integer> bms = new HashMap<String, Integer>();
		bms.put("CSE", 1);
		bms.put("ISE", 1);
		bms.put("ECE", 1);
		HashMap<String, Integer> rv = new HashMap<String, Integer>();
		rv.put("CSE", 1);
		rv.put("ISE", 1);
		rv.put("ECE", 1);
		HashMap<String, Integer> pes = new HashMap<String, Integer>();
		pes.put("CSE", 1);
		pes.put("ISE", 1);
		pes.put("ECE", 1);
		HashMap<String, Integer> msrit = new HashMap<String, Integer>();
		msrit.put("CSE", 1);
		msrit.put("ISE", 1);
		msrit.put("ECE", 1);
		HashMap<String, Integer> nmit = new HashMap<String, Integer>();
		nmit.put("CSE", 1);
		nmit.put("ISE", 1);
		nmit.put("ECE", 1);

		System.out.println("SEAT ALLOTMENT");
		int n;
		for (CandidateRegistration each : CandidateList) {
			for (Preference p : each.CandidatePreferences) {
				if (p.getCollegeName().equals("BMS College of Engineering")) {
					n = bms.get(p.getBranch());
					if (n > 0) {
						System.out.println("Alloted seat to is " + each.getFirstName() + " BMS College of Engineering "
								+ p.getBranch());
						n--;
						if (n < 0)
							n = 0;
						bms.put(p.getBranch(), n);
						seatAllotment.put(each.getStudentRegId(), "Alloted seat to is " + each.getFirstName()
								+ " BMS College of Engineering " + p.getBranch());
						break;
					}

				} else if (p.getCollegeName().equals("PES University")) {
					n = pes.get(p.getBranch());
					if (n > 0) {
						System.out.println(
								"Alloted seat to is " + each.getFirstName() + " PES University " + p.getBranch());
						n--;
						if (n < 0)
							n = 0;
						pes.put(p.getBranch(), n);
						seatAllotment.put(each.getStudentRegId(),
								"Alloted seat to is " + each.getFirstName() + " PES University " + p.getBranch());
						break;
					}
				} else if (p.getCollegeName().equals("RV College of Engineering")) {
					n = rv.get(p.getBranch());
					if (n > 0) {
						System.out.println("Alloted seat to is " + each.getFirstName() + " RV College of Engineering "
								+ p.getBranch());
						n--;
						if (n < 0)
							n = 0;
						rv.put(p.getBranch(), n);
						seatAllotment.put(each.getStudentRegId(), "Alloted seat to is " + each.getFirstName()
								+ " RV College of Engineering " + p.getBranch());

						break;
					}
				} else if (p.getCollegeName().equals("MS Ramaiah Institute of Technology")) {
					n = msrit.get(p.getBranch());
					if (n > 0) {
						System.out.println("Alloted seat to is " + each.getFirstName()
								+ " MS Ramaiah Institute of Technology " + p.getBranch());
						n--;
						if (n < 0)
							n = 0;
						msrit.put(p.getBranch(), n);
						seatAllotment.put(each.getStudentRegId(), "Alloted seat to is " + each.getFirstName()
								+ " MS Ramaiah Institute of Technology " + p.getBranch());

						break;
					}
				} else {
					n = nmit.get(p.getBranch());
					if (n > 0) {
						System.out.println("Alloted seat to is " + each.getFirstName() + " NMIT " + p.getBranch());
						n--;
						if (n < 0)
							n = 0;
						nmit.put(p.getBranch(), n);
						seatAllotment.put(each.getStudentRegId(),
								"Alloted seat to is " + each.getFirstName() + " NMIT  " + p.getBranch());

						break;
					}
				}
			}
		}

	}

	void seatAllotmentByID(int ID) {
		for (int each : seatAllotment.keySet()) {
			if (each == ID) {
				System.out.println(seatAllotment.get(each));
			}
		}
	}
	
	HashMap<Integer,Integer>setA = new HashMap<Integer,Integer>();
	HashMap<Integer,Integer>setB = new HashMap<Integer,Integer>();
	HashMap<Integer,Integer>setC = new HashMap<Integer,Integer>();
	
	void answerKey(String set)
	{
		setA.put(1, 4);
		setA.put(2, 3);
		setA.put(3, 1);
		setA.put(4, 2);
		setA.put(5, 4);
		
		setB.put(1, 4);
		setB.put(2, 3);
		setB.put(3, 1);
		setB.put(4, 2);
		setB.put(5, 4);
		
		setC.put(1, 4);
		setC.put(2, 3);
		setC.put(3, 1);
		setC.put(4, 2);
		setC.put(5, 4);
		
		switch(set)
		{
		
		case "A":
		{
			File file = new File("answerKeyA.text");
			
			try (BufferedWriter output = new BufferedWriter(new FileWriter(file))) {
			
				output.write("QuestionNumber	CorrectOption");
				output.newLine();
				for(int each:setA.keySet())
				{
					output.write(String.valueOf(each +" 	"));
					output.write(String.valueOf(setA.get(each)));
					output.newLine();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		case "B":
		{
			File file = new File("answerKeyB.text");
			
			try (BufferedWriter output = new BufferedWriter(new FileWriter(file))) {
				output.write("QuestionNumber	CorrectOption");
				output.newLine();
				for(int each:setB.keySet())
				{
					output.write(String.valueOf(each+ " 	"));
					output.write(String.valueOf(setB.get(each)));
					output.newLine();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		case "C":
		{
			File file = new File("answerKeyC.text");
			
			try (BufferedWriter output = new BufferedWriter(new FileWriter(file))) {
				output.write("QuestionNumber	CorrectOption");
				output.newLine();
				for(int each:setC.keySet())
				{
					output.write(String.valueOf(each+ "		"));
					output.write(String.valueOf(setC.get(each)));
					output.newLine();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		}
		
	}

}
