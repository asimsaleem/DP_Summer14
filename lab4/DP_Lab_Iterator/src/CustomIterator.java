import java.util.Iterator;
import java.util.NoSuchElementException;


class Student {
  public double GPA;
  public int age;
  public String name;
  public String school;

  public Student( double GPA, String name, int age, String school) {
    this.GPA = GPA;
    this.name = name;
    this.age = age;
    this.school = school;
  }

  @Override
  public String toString() {
    return name + "," + GPA + "," + age + "," + school;
  }

  public static Student [] createTestArray() {
    Student s0 = new Student( 3.8d, "Adnan Aziz", 21, "UT Austin");
    Student s1 = new Student( 3.6d, "Imran Aziz", 20, "MIT");
    Student s2 = new Student( 3.5d, "Aardvark Smith", 18, "Berkeley");
    Student s3 = new Student( 2.9d, "Thomas Jefferson", 17, "UT Austin");
    Student s4 = new Student( 3.3d, "Matt Biondi", 22, "Berkeley");
    Student [] testarray = {s0, s1, s2, s3, s4};
    return testarray;
  }

}

public class CustomIterator {

  public static class StudentIteratorBySchool implements Iterator<Student> {

    //TODO(DP): implement this iterator as per the lab specification
    // the remove method should  throw an UnsupportedOperationException

	/**** START ****/
	Student[] studentWithSchools;
	int position = 0;
	 
	public StudentIteratorBySchool(Student[] studentWithSchoolNames, String schoolName) {
		//Invoke the getFilteredList Method to get only the list of Students whose School Name matches the one expected
		this.studentWithSchools = getFilteredList(studentWithSchoolNames, schoolName);
	}
	
	private static Student[] getFilteredList(Student[] studentWithSchoolNames, String schoolName){
		int i = 0;
		//Initialize a new Student Array which will be populated only with the Filtered List of Students
		Student[] filteredStudentSchoolNames = new Student[studentWithSchoolNames.length];
		for(Student studentWithSpecifiedSchool : studentWithSchoolNames){
			if(studentWithSpecifiedSchool.school.equalsIgnoreCase(schoolName)){
				filteredStudentSchoolNames[i] = studentWithSpecifiedSchool;
				i = i+1;
			}
		}
		//Return the FilteredList to the Constructor
		return filteredStudentSchoolNames;
	}
	
	@Override
	public boolean hasNext() {
		//Check if the Position has reached the Max Length or if its null to determine if there are more elements or not
		if(position >= studentWithSchools.length || studentWithSchools[position] == null){
			return false;
		}else
			return true;
	}
	
	@Override
	public Student next() {
		//Get the Student element available at the location specified by the variable Position
		Student studentWithSpecifiedSchool = studentWithSchools[position];
		//Increment the Position variable to keep track of where the Iterator is currently pointing
		position = position + 1;
		return studentWithSpecifiedSchool;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove Operation is not supported");
	}	  
	/**** END *****/
  }

  interface StudentPredicate {
    boolean check( Student s );
  }


  public static class StudentIteratorPredicated implements Iterator<Student> {

    //TODO(DP): implement this iterator as per the lab specification
    // the remove method should  throw an UnsupportedOperationException

	/**** START ****/
	Student[] studentsArray;
	int position = 0;

 	public StudentIteratorPredicated(StudentPredicate studentPredicate,
									 Student[] studentsArray) {
		
		//Student Array will now contain a list which is already filtered as per the Search conditions
		this.studentsArray = getFilteredList(studentPredicate,studentsArray);
	}

	private static Student[] getFilteredList(StudentPredicate studentPredicate, Student[] studentsArray){

		//Initialize a new Student Array which will be populated only with the Filtered List of Students
		Student[] filteredStudentSchoolNames = new Student[studentsArray.length];
		int i=0;
		for(Student individualStudent: studentsArray){
			//Check if the Student Predicate returns TRUE before populating the Filtered list
			if(studentPredicate.check(individualStudent)){
				filteredStudentSchoolNames[i] = individualStudent;
				i = i+1;
			}
		}
		//Return the FilteredList to the Constructor
		return filteredStudentSchoolNames;
	}
	
	@Override
	public boolean hasNext() {
		//Check if the Position has reached the Max Length or if its null to determine if there are more elements or not
		if(position >= studentsArray.length || studentsArray[position] == null){
			return false;
		}else
			return true;
	}
	
	@Override
	public Student next() {
		//Get the Student element available at the location specified by the variable Position
		Student student = studentsArray[position];
		//Increment the Position variable to keep track of where the Iterator is currently pointing
		position = position + 1;
		return student;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove Operation is not supported");
	}	  
	/**** END *****/
  }
}