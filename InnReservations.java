/*
	Varun Iyengar	Shannon Fong
	CSC 365 Lab 6
	Professor Von Dollen
*/

import javax.sql.*;
import java.sql.*;


public class InnReservations {
	public static void main(String args[]){
		String jdbcURL = System.getenv("APP_JDBC_URL");
		String dbUsername = System.getenv("APP_JDBC_USER");
		String dbPassword = System.getenv("APP_JDBC_PW");
	
		try {
			Connection conn = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
		} catch (SQLException e) {
			System.out.println("Failed SQL Connection..." + e.getMessage());
		} finally {
			System.out.println("Varun");
		}
	}
}