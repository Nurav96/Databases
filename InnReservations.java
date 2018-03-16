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
				getRoomsAndRates();
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
				break;
		}

		prompt();
		return status;
	}

	public static void prompt() {
		System.out.println("Select a command:\n");
		System.out.println("\tRooms and Rates [RR|rr]\n\tReservations [RV|rv]");
		System.out.println("\tDetailed Reservation Information [I]");
		System.out.println("\tRevenue [R]");
		System.out.println("\tExit [E]\n");
	}

	public static void getRoomsAndRates(){
		String jdbcURL = System.getenv("APP_JDBC_URL");
		String dbUsername = System.getenv("APP_JDBC_USER");
		String dbPassword = System.getenv("APP_JDBC_PW");
		Connection conn = null;
		PreparedStatement ps = null;
	
		try {
			conn = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			ps = conn.prepareStatement("SELECT RoomCode, RoomName, Beds, bedType, maxOcc, basePrice, decor, ROUND(SUM(DATEDIFF(Checkout, CheckIn))/180, 2) AS Room_Popularity_Score , (SELECT R1.Checkout FROM lab6_reservations R1 WHERE Room = RoomCode AND R1.Checkout >= Now() AND R1.Checkout NOT IN (SELECT CheckIn FROM lab6_reservations R2 WHERE Room = RoomCode AND R1.Checkout >= Now()) LIMIT 1) AS Next_Avail_Date, (SELECT DATEDIFF(Checkout, CheckIn) FROM lab6_reservations WHERE Checkout <= NOW() AND Room = RoomCode ORDER BY Checkout DESC LIMIT 1) AS Last_Duration, (SELECT Checkout FROM lab6_reservations WHERE Checkout <= NOW() AND Room = RoomCode ORDER BY Checkout DESC LIMIT 1) AS Last_Checkout FROM lab6_reservations AS Resv JOIN lab6_rooms AS R ON Resv.Room = R.RoomCode WHERE (CheckIn >= DATE_SUB(NOW(), INTERVAL 180 day) OR CheckOut >= DATE_SUB(NOW(), INTERVAL 180 day)) AND (CheckIn < NOW() OR CheckOut < NOW()) GROUP BY Room ORDER BY Room_Popularity_Score DESC;");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String roomCode = rs.getString("RoomCode");
				String roomName = rs.getString("RoomName");
				int beds = rs.getInt("Beds");
				String bedType = rs.getString("bedType");
				int maxOcc = rs.getInt("maxOcc");
				double basePrice = rs.getDouble("basePrice");
				String decor = rs.getString("decor");
				double rate = rs.getDouble("Room_Popularity_Score");
				java.sql.Date nextAvail = rs.getDate("Next_Avail_Date");
				int lastDuration = rs.getInt("Last_Duration");
				java.sql.Date lastCheckout = rs.getDate("Last_Checkout");
				System.out.format("%s %s %d %s %d %f %s %f %s %d %s\n", roomCode, roomName, beds, bedType, maxOcc, basePrice, decor, rate, nextAvail, lastDuration, lastCheckout);
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