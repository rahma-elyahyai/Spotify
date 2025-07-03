package kcetJava;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System1 newSystem = new System1();

		CandidateRegistration[] candidates = new CandidateRegistration[10];

		// college registration

		CollegeRegistration college1 = new CollegeRegistration(1001, "RV College of Engineering", "RVCE001",
				"securePassword123", "Karnataka");

		CollegeRegistration college2 = new CollegeRegistration(1002, "BMS College of Engineering", "BMSCE002",
				"securePassword456", "Karnataka");

		// candidates register

		candidates[0] = new CandidateRegistration(1, "Aarav", "Reddy", "aarav.reddy", "pass123", "Bangalore",
				"Karnataka", "560001", "CMR", "PCMB", "2024-06-30");
		candidates[1] = new CandidateRegistration(2, "Vivaan", "Nayak", "vivaan.nayak", "pass234", "Mysore",
				"Karnataka", "570001", "RCIS", "PCMC", "2024-06-30");
		candidates[2] = new CandidateRegistration(3, "Aditya", "Sharma", "aditya.sharma", "pass345", "Mangalore",
				"Karnataka", "575001", "MES", "PCME", "2024-06-30");
		candidates[3] = new CandidateRegistration(4, "Ananya", "Kumar", "ananya.kumar", "pass456", "Hubli", "Karnataka",
				"580001", "JSS", "PCMB", "2024-06-30");
		candidates[4] = new CandidateRegistration(5, "Sai", "Shetty", "sai.shetty", "pass567", "Belgaum", "Karnataka",
				"590001", "CMR", "PCMB", "2024-06-30");
		candidates[5] = new CandidateRegistration(6, "Diya", "Bhat", "diya.bhat", "pass678", "Davanagere", "Karnataka",
				"577001", "Presidency", "PCME", "2024-06-30");
		candidates[6] = new CandidateRegistration(7, "Krishna", "Hegde", "krishna.hegde", "pass789", "Tumkur",
				"Karnataka", "572101", "CMR", "PCMC", "2024-06-30");
		candidates[7] = new CandidateRegistration(8, "Sneha", "Acharya", "sneha.acharya", "pass890", "Raichur",
				"Karnataka", "584101", "RCIS", "PCMB", "2024-06-30");
		candidates[8] = new CandidateRegistration(9, "Rohan", "Kale", "rohan.kale", "pass901", "Karwar", "Karnataka",
				"581301", "MES", "PCMB", "2024-06-30");
		candidates[9] = new CandidateRegistration(10, "Pooja", "Desai", "pooja.desai", "pass012", "Bangalore",
				"Karnataka", "560001", "Narayana", "PCME", "2024-06-30");

		//kea admin registration
		KeaAdminRegistration admin1 = new KeaAdminRegistration(101, "Vijay Prasad", "vijayprasad@gmail.com",
				"password123", "Bangalore", "Karnataka");

		// registered candidates,kea Admin, colleges are added to the system database 
		for (int i = 0; i < 10; i++)
			newSystem.CandidateList.add(candidates[i]);
		
		newSystem.CollegeList.add(college1);
		newSystem.CollegeList.add(college2);
		
		newSystem.KeaAdminList.add(admin1);

		newSystem.storeDetails();
		
		//candidates,college,kea admin can login to the website
		
		newSystem.login(2, "pass234");
		newSystem.login(1001,"securePassword123");
		newSystem.login(101,"34");
		
		// registered candidates on KCET website apply for CET exam by providing all the
		// necessary details and documents
		for (int i = 0; i < 10; i++)
			candidates[i].studentCetRegistration();

		// candidates can download admit card
		System.out.println("divya's admit card");
		candidates[5].admitCard();

		// After the exam is conducted the KEA board gives the CET marks to students
		// (max CET marks is 180)

		candidates[0].setCetMarks(120);
		candidates[1].setCetMarks(101);
		candidates[2].setCetMarks(89);
		candidates[3].setCetMarks(56);
		candidates[4].setCetMarks(140);
		candidates[5].setCetMarks(89);
		candidates[6].setCetMarks(156);
		candidates[7].setCetMarks(45);
		candidates[8].setCetMarks(100);
		candidates[9].setCetMarks(34);

		//candidates can download answer key from website
		newSystem.answerKey("A");
		
		newSystem.rankDeclaration();

		// candidates can also check their rank by giving their ID
		System.out.println("divya's rank");
		candidates[5].resultDeclaration(newSystem);

		// councelling

		// students add college preferences

		// Adding preferences for all candidates using the specified format

		// Adding preferences for all candidates with restricted branches
		candidates[0].CandidatePreferences.add(new Preference(1, "RV College of Engineering", "RVCE001", "CSE", 1));
		candidates[0].CandidatePreferences.add(new Preference(1, "BMS College of Engineering", "BMSCE002", "CSE", 2));
		candidates[0].CandidatePreferences.add(new Preference(1, "BMS Institute of Technology", "BMSIT003", "ISE", 3));
		candidates[0].CandidatePreferences
				.add(new Preference(1, "MS Ramaiah Institute of Technology", "MSRIT004", "CSE", 4));
		candidates[0].CandidatePreferences.add(new Preference(1, "PES University", "PES005", "CSE", 5));

		candidates[1].CandidatePreferences.add(new Preference(2, "BMS College of Engineering", "BMSCE001", "ISE", 1));
		candidates[1].CandidatePreferences.add(new Preference(2, "RV College of Engineering", "RVCE002", "ISE", 2));
		candidates[1].CandidatePreferences.add(new Preference(2, "PES University", "PES003", "CSE", 3));
		candidates[1].CandidatePreferences.add(new Preference(2, "NMIT", "NMIT004", "ISE", 4));
		candidates[1].CandidatePreferences
				.add(new Preference(2, "MS Ramaiah Institute of Technology", "MSRIT005", "CSE", 5));

		candidates[2].CandidatePreferences.add(new Preference(3, "RV College of Engineering", "RVCE001", "ECE", 1));
		candidates[2].CandidatePreferences.add(new Preference(3, "BMS College of Engineering", "BMSCE002", "ECE", 2));
		candidates[2].CandidatePreferences.add(new Preference(3, "BMS Institute of Technology", "BMSIT003", "ECE", 3));
		candidates[2].CandidatePreferences
				.add(new Preference(3, "MS Ramaiah Institute of Technology", "MSRIT004", "CSE", 4));
		candidates[2].CandidatePreferences.add(new Preference(3, "PES University", "PES005", "CSE", 5));

		candidates[3].CandidatePreferences.add(new Preference(4, "BMS College of Engineering", "BMSCE001", "CSE", 1));
		candidates[3].CandidatePreferences.add(new Preference(4, "RV College of Engineering", "RVCE002", "CSE", 2));
		candidates[3].CandidatePreferences.add(new Preference(4, "PES University", "PES003", "ISE", 3));
		candidates[3].CandidatePreferences.add(new Preference(4, "NMIT", "NMIT004", "CSE", 4));
		candidates[3].CandidatePreferences
				.add(new Preference(4, "MS Ramaiah Institute of Technology", "MSRIT005", "ECE", 5));

		candidates[4].CandidatePreferences.add(new Preference(5, "RV College of Engineering", "RVCE001", "CSE", 1));
		candidates[4].CandidatePreferences.add(new Preference(5, "BMS College of Engineering", "BMSCE002", "ISE", 2));
		candidates[4].CandidatePreferences
				.add(new Preference(5, "MS Ramaiah Institute of Technology", "MSRIT003", "ECE", 3));
		candidates[4].CandidatePreferences.add(new Preference(5, "NMIT", "NMIT004", "CSE", 4));
		candidates[4].CandidatePreferences.add(new Preference(5, "PES University", "PES005", "ISE", 5));

		candidates[5].CandidatePreferences.add(new Preference(6, "BMS College of Engineering", "BMSCE001", "ECE", 1));
		candidates[5].CandidatePreferences.add(new Preference(6, "RV College of Engineering", "RVCE002", "CSE", 2));
		candidates[5].CandidatePreferences
				.add(new Preference(6, "MS Ramaiah Institute of Technology", "MSRIT003", "ECE", 3));
		candidates[5].CandidatePreferences.add(new Preference(6, "NMIT", "NMIT004", "CSE", 4));
		candidates[5].CandidatePreferences.add(new Preference(6, "PES University", "PES005", "ISE", 5));

		candidates[6].CandidatePreferences.add(new Preference(7, "RV College of Engineering", "RVCE001", "CSE", 1));
		candidates[6].CandidatePreferences.add(new Preference(7, "BMS College of Engineering", "BMSCE002", "ISE", 2));
		candidates[6].CandidatePreferences.add(new Preference(7, "BMS Institute of Technology", "BMSIT003", "CSE", 3));
		candidates[6].CandidatePreferences
				.add(new Preference(7, "MS Ramaiah Institute of Technology", "MSRIT004", "CSE", 4));
		candidates[6].CandidatePreferences.add(new Preference(7, "PES University", "PES005", "ECE", 5));

		candidates[7].CandidatePreferences.add(new Preference(8, "BMS College of Engineering", "BMSCE001", "ISE", 1));
		candidates[7].CandidatePreferences.add(new Preference(8, "RV College of Engineering", "RVCE002", "CSE", 2));
		candidates[7].CandidatePreferences.add(new Preference(8, "PES University", "PES003", "CSE", 3));
		candidates[7].CandidatePreferences.add(new Preference(8, "NMIT", "NMIT004", "ISE", 4));
		candidates[7].CandidatePreferences
				.add(new Preference(8, "MS Ramaiah Institute of Technology", "MSRIT005", "CSE", 5));

		candidates[8].CandidatePreferences.add(new Preference(9, "RV College of Engineering", "RVCE001", "ECE", 1));
		candidates[8].CandidatePreferences.add(new Preference(9, "BMS College of Engineering", "BMSCE002", "CSE", 2));
		candidates[8].CandidatePreferences.add(new Preference(9, "BMS Institute of Technology", "BMSIT003", "ECE", 3));
		candidates[8].CandidatePreferences
				.add(new Preference(9, "MS Ramaiah Institute of Technology", "MSRIT004", "CSE", 4));
		candidates[8].CandidatePreferences.add(new Preference(9, "PES University", "PES005", "ECE", 5));

		candidates[9].CandidatePreferences.add(new Preference(10, "BMS College of Engineering", "BMSCE001", "CSE", 1));
		candidates[9].CandidatePreferences.add(new Preference(10, "RV College of Engineering", "RVCE002", "CSE", 2));
		candidates[9].CandidatePreferences
				.add(new Preference(10, "MS Ramaiah Institute of Technology", "MSRIT003", "ECE", 3));
		candidates[9].CandidatePreferences.add(new Preference(10, "NMIT", "NMIT004", "CSE", 4));
		candidates[9].CandidatePreferences.add(new Preference(10, "PES University", "PES005", "ISE", 5));

		candidates[0].collegePrefernce();
		candidates[1].collegePrefernce();
		candidates[2].collegePrefernce();
		candidates[3].collegePrefernce();
		candidates[4].collegePrefernce();
		candidates[5].collegePrefernce();
		candidates[6].collegePrefernce();
		candidates[7].collegePrefernce();
		candidates[8].collegePrefernce();
		candidates[9].collegePrefernce();

		newSystem.seatAllotment();

		// candidates can also check their seat allotment by giving their ID
		System.out.println("divya's seat allotment");
		candidates[5].collegeAllotment(newSystem);

		System.out.println("divya's grievance ");
		candidates[5].candidateGrievance();
	}

}
