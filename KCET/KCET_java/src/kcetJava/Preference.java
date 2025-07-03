package kcetJava;

public class Preference {

		private int studentId;
		private String collegeName;
		private String collegeCode;
		private String branch;
		private int preferenceNumber;
		
		Preference(int studentId,String collegeName,String collegeCode,String Branch,int preferenceNumber)
		{
			this.setStudentId(studentId);
			this.setCollegeName(collegeName);
			this.setBranch(Branch);
			this.setPreferenceNumber(preferenceNumber);
		}
		public int getStudentId() {
			return studentId;
		}
		public void setStudentId(int studentId) {
			this.studentId = studentId;
		}
		public String getCollegeName() {
			return collegeName;
		}
		public void setCollegeName(String collegeName) {
			this.collegeName = collegeName;
		}
		public String getCollegeCode() {
			return collegeCode;
		}
		public void setCollegeCode(String collegeCode) {
			this.collegeCode = collegeCode;
		}
		public String getBranch() {
			return branch;
		}
		public void setBranch(String branch) {
			this.branch = branch;
		}
		public int getPreferenceNumber() {
			return preferenceNumber;
		}
		public void setPreferenceNumber(int preferenceNumber) {
			this.preferenceNumber = preferenceNumber;
		}
		
	void display()
	{
		System.out.println("preference number: "+ this.getPreferenceNumber()+ " college name "+ this.getCollegeName()+ " branch "+ this.getBranch());
	}
}
