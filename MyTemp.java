/*
	Varun Iyengar	Shannon Fong
	CSC 365 Lab 6
	Professor Von Dollen
*/

import javax.sql.*;
import java.sql.*;
import com.jcraft.jsch.*;


public class MyTemp {
	public static void main(String args[]){
		String jdbcUrl = System.getenv("APP_JDBC_URL");
		String dbUsername = System.getenv("APP_JDBC_USER");
		String dbPassword = System.getenv("APP_JDBC_PW");

		try {
			JSch jsch = new JSch();

			Session session = jsch.getSession("viyengar", "unix1.csc.calpoly.edu", 22);
			session.setPassword("V1y3ng@r2");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

		} catch (JSchException e) {
			System.out.println("Failed SSH...");
		}
		
		try {
			Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
		} catch (SQLException e) {
			System.out.println("Failed SQL Connection..." + e.getMessage());
		} finally {
			System.out.println("Varun");
		}
	}
}