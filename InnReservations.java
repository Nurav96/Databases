/*
	Varun Iyengar	Shannon Fong
	CSC 365 Lab 6
	Professor Von Dollen
*/

import javax.sql.*;
import java.sql.*;
import java.io.*;
import java.sql.Date;
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
				//makeReservation();
				break;

			case "Detailed Reservation Information":
			case "I":
				System.out.println("Detailed Reservation Information\n");
				displayReservations();
				break;

			case "Revenue":
			case "R":
				System.out.println("Revenue\n");
				displayRevenue();
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

	public static void displayReservations() {
        String jdbcURL = System.getenv("APP_JDBC_URL");
        String dbUsername = System.getenv("APP_JDBC_USER");
        String dbPassword = System.getenv("APP_JDBC_PW");
        Connection conn = null;
        PreparedStatement ps = null;

        Scanner sc = new Scanner(System.in);

        try {
            conn = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
            ps = conn.prepareStatement("SELECT * FROM lab6_reservations WHERE FirstName LIKE ? " +
                    "AND LastName LIKE ? ?" +
                    "AND Room LIKE ? AND CODE LIKE ?");
            System.out.println("Enter Information:");
            System.out.print("First name: ");
            ps.setString(1, "%"+sc.nextLine()+"%");
            System.out.print("Last name: ");
            ps.setString(2, "%"+sc.nextLine()+"%");
            System.out.print("Start date(YYYY-MM-DD): ");
            String start = sc.nextLine();
            System.out.print("End date(YYYY-MM-DD: ");
            String end = sc.nextLine();
            ps.setString(3, "AND (CheckIn BETWEEN DATE(%s) AND DATE(%s)) ".format(start, end));
            System.out.print("Room code: ");
            ps.setString(4, "%"+sc.nextLine()+"%");
            System.out.print("Reservation code: ");
            ps.setString(5, "%"+sc.nextLine()+"%");



            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.format("\nCode: %s\nRoom: %s\nCheckIn: %s\nCheckOut: %s\nRate: %s\n" +
                        "Last, First: %s, %s\nAdults/Kids: %s, %s\n\n",
                        rs.getString("Code"),
                        rs.getString("Room"),
                        rs.getString("CheckIn"),
                        rs.getString("CheckOut"),
                        rs.getString("Rate"),
                        rs.getString("LastName"),
                        rs.getString("FirstName"),
                        rs.getString("Adults"),
                        rs.getString("Kids"));
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

	public static void makeReservation(){
		String jdbcURL = System.getenv("APP_JDBC_URL");
		String dbUsername = System.getenv("APP_JDBC_USER");
		String dbPassword = System.getenv("APP_JDBC_PW");
		Connection conn = null;
		PreparedStatement ps = null;
		String input[] = new String[8];

		Scanner sc = new Scanner(System.in);

		System.out.println("Reservation Form:\n")
		System.out.print("First Name: ");
		input[0] = sc.nextLine().trim();
		System.out.print("Last Name: ");
		input[1] = sc.nextLine().trim();
		System.out.print("Room Desired (Room Code) ");
		input[2] = sc.nextLine().trim();
		System.out.print("Bed Type: ");
		input[3] = sc.nextLine().trim();
		System.out.print("Begin Date: ");
		input[4] = sc.nextLine().trim();
		System.out.print("End Date: ");
		input[5] = sc.nextLine().trim();
		System.out.print("Number of children: ");
		input[6] = sc.nextLine().trim();
		System.out.print("Number of adults: ");
		input[7] = sc.nextLine().trim();

		try {
			conn = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			ps = conn.prepareStatement("");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){

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
		sc.close();
	}

    public static void displayRevenue(){
        String jdbcURL = System.getenv("APP_JDBC_URL");
        String dbUsername = System.getenv("APP_JDBC_USER");
        String dbPassword = System.getenv("APP_JDBC_PW");
        Connection conn = null;
        PreparedStatement ps = null;
        Calendar checkIn = Calendar.getInstance();
        Calendar checkOut = Calendar.getInstance();
        int[] dateStats;

        try {
            conn = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
            ps = conn.prepareStatement("SELECT * FROM lab6_reservations");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int roomCode = rs.getInt("CODE");
                checkIn.setTime(rs.getDate("CheckIn"));
                checkOut.setTime(rs.getDate("Checkout"));
                float rate = rs.getFloat("Rate");
                if ((dateStats = getDateStats(checkIn, checkOut))[0] + dateStats[1] > 0) {
                    System.out.format("%s, %s\n", rs.getDate("CheckIn"), rs.getDate("Checkout"));
                    System.out.format("%d %d %d %.2f %.2f\n", roomCode, dateStats[0], dateStats[1], rate, costOfStay(dateStats, rate));
                }
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

    public static int[] getDateStats(Calendar start, Calendar end) {
        int weekDays = 0;
	    int weekendDays = 0;
        int[] stayStats = new int[2];

        while (start.compareTo(end) < 0 ) {
            if (start.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY ||
                    start.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
                weekendDays++;
            } else {
                weekDays++;
            }
            start.add(Calendar.HOUR, 24);
        }

        stayStats[0] = weekDays;
        stayStats[1] = weekendDays;
        return stayStats;
    }

    public static double costOfStay(int[] stayStats, float rate) {
	    return (stayStats[0] + stayStats[1] * 1.1) * rate * 1.18;
    }
}