package com.learning.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import java.sql.Statement;

public class StudentDbUtil {
	
	private DataSource dataSource;
	
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	

	public List<Student> getStudent() throws Exception {
		
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			// get a connection
			myConn = dataSource.getConnection();
			
			// create sql statement
			String sql = "SELECT * FROM student ORDER BY last_name";
			
			myStmt = myConn.createStatement();
			
			//execute query
			myRs = myStmt.executeQuery(sql);
			
			//process result set
			while (myRs.next()) {
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// create new student object
				Student tempStudent = new Student (id,firstName, lastName, email);
				
				// add it to the list of students
				students.add(tempStudent);
			}
			
			return students;
		}
		finally {
			// close JDBC objects
			close(myConn, myStmt, myRs);
			
		}
		
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if(myConn != null) {
				myConn.close();
			}
			
		}
		catch (Exception exe) {
			exe.printStackTrace();
		}
		
	}



	public void addStudent(Student theStudent) throws Exception{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
		// get db connection
			myConn = dataSource.getConnection();
			
		// create sql for insert
			String sql = "insert into student" 
						+ "(first_name, last_name, email) "
						+ "values (?,?,?)";
		
			myStmt = myConn.prepareStatement(sql);
			
		// set the param values for the student
			myStmt.setString(1,  theStudent.getFirstName());
			myStmt.setString(2,  theStudent.getLastName());
			myStmt.setString(3,  theStudent.getEmail());
		
		// execute sql insert
			myStmt.execute();
		
		}
		finally {
		// clean up JDBC objects
			close(myConn,myStmt,null);
			
		}
			
		
	}



	public Student getStudent(String theStudentId) throws Exception{
		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			// Convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to ge tselected student
			String sql = "select * from student where id=?";
			
			// created prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, studentId);
			
			// execute statement
			myRs = myStmt.executeQuery();
			
			// retrieve data from result set row
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// use the studentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
				
			}
			else {
				throw new Exception ("Could not find student id: " + studentId);
			}
			return theStudent;
		}
		finally {
			// clean up JDBC object
			close(myConn,myStmt, myRs);
			
			
		}
		
		
	}

	public void updateStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
		// get a connection to db
		myConn = dataSource.getConnection();
		
		// create SQL update statement
		String sql = "update student "
					+ "set first_name=?, last_name=?, email=? "
					+ "where id=?";
		
		// prepare statement
		myStmt = myConn.prepareStatement(sql);
		
		// set parameters
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());
		myStmt.setInt(4, theStudent.getId());
		
		
		// execute SQL statement
		myStmt.execute();
		}
		finally {
			close(myConn,myStmt, null);
		}	
	}



	public void deleteStudent(String theStudentId) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
		// convert a student id to int
			int studentId = Integer.parseInt(theStudentId);
			
		// get a connection to db
			myConn = dataSource.getConnection();
			
		// create SQL delete statement
			String sql = "delete from student where id=?";
					
		// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
		// set parameters
			myStmt.setInt(1, studentId);
			
		// execute SQL statement
			myStmt.execute();
			
		}
		finally {
			close(myConn, myStmt,null);
			
		}
		
	}
}
