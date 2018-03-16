/*
	Varun Iyengar	Shannon Fong
	CSC 365 Lab 6
	Professor Von Dollen
*/

import javax.sql.*;
import java.sql.*;
import java.io.*;
import java.util.*;



public class InnReservations {
	public static void main(String args[]){
		String command = "";
		System.out.println("Welcome to the Inn Reservations Main Menu:");
		prompt();
		Scanner sc = new Scanner(System.in);
		while(runCommand(sc.nextLine()));
		sc.close();
	}

	public static boolean runCommand(String command){
		boolean status = true;
		switch(command){
			case "Rooms and Rates":
			case "RR":
			case "rr":
				System.out.println("Rooms and Rates\n");
				break;

			case "Reservations":
			case "RV":
			case "rv":
				System.out.println("Reservations\n");
				break;

			case "Detailed Reservation Information":
			case "I":
				System.out.println("Detailed Reservation Information\n");
				break;

			case "Revenue":
			case "R":
				System.out.println("Revenue\n");
				break;

			case "Display Tables":
			case "D":
				System.out.println("Display Tables\n");
				displayTables();
				break;

			case "Exit":
			case "E":
				System.out.println("Exiting...\n");
				status = false;
				break;

			default:
				System.out.println("Command not recognized, enter another:");
				prompt();
				break;
		}
		return status;
	}

	public static void prompt() {
		System.out.println("Select a command:\n");
		System.out.println("\tRooms and Rates [RR|rr]\n\tReservations [RV|rv]");
		System.out.println("\tDetailed Reservation Information [I]");
		System.out.println("\tRevenue [R]");
		System.out.println("\tExit [E]\n");
	}

	public static void displayTables(){
		String jdbcURL = System.getenv("APP_JDBC_URL");
		String dbUsername = System.getenv("APP_JDBC_USER");
		String dbPassword = System.getenv("APP_JDBC_PW");
		Connection conn = null;
		PreparedStatement ps = null;
	
		try {
			conn = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			ps = conn.prepareStatement("SELECT * FROM lab6_rooms");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String roomCode = rs.getString("RoomCode");
				String roomName = rs.getString("RoomName");
				int beds = rs.getInt("Beds");
				String bedType = rs.getString("bedType");
				int maxOcc = rs.getInt("maxOcc");
				double basePrice = rs.getDouble("basePrice");
				String decor = rs.getString("decor");
				System.out.format("%s %s %d %s %d %f %s\n", roomCode, roomName, beds, bedType, maxOcc, basePrice, decor);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if(ps != null){
				try{
					ps.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			if(conn != null) {
				try{
					conn.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}


	public static void makeConnection(){
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