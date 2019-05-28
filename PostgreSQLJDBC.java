import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class PostgreSQLJDBC {
	public static void main(String args[]) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/labb2", "postgres", ""/*password here*/);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			String sql;


			//CREATE TABLE TEAM

			sql = "CREATE TABLE TEAM " + "(ID INT PRIMARY KEY NOT NULL, " + " NAME TEXT NOT NULL);";
			stmt.executeUpdate(sql);

			System.out.println("Created TEAM successfully");

			//INSERT INTO TEAM

			sql = "INSERT INTO TEAM (ID,NAME)" + "VALUES (1, 'BestTeam');";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO TEAM (ID,NAME)" + "VALUES (2, 'WORSTTeam');";
			stmt.executeUpdate(sql);

			System.out.println("Inserted TEAM successfully");

			//CREATE TABLE PERSON

			sql = "CREATE TABLE PERSON " + "(ID INT PRIMARY KEY NOT NULL, "
					+ " NAME TEXT NOT NULL, " + " POSITION TEXT NOT NULL, "
					+ " STATUS CHAR(50) NOT NULL, " + " teamname TEXT , " + "REPRESENT CHAR(50));";
			stmt.executeUpdate(sql);

			System.out.println("Created PERSON successfully");

			//INERT INTO PERSON

			sql = "INSERT INTO PERSON (ID,NAME,POSITION,STATUS,teamname,REPRESENT)"
					+ "VALUES (1, 'Paul','Manager','Employee','BestTeam', NULL);";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO PERSON (ID,NAME,POSITION,STATUS,teamname,REPRESENT)"
					+ "VALUES (2, 'Allen', 'Accountant','Partner', NULL , 'Sony');";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO PERSON (ID,NAME,POSITION,STATUS,teamname,REPRESENT)"
					+ "VALUES (3, 'Teddy','Accountant','Employee', 'WORSTTeam', NULL);";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO PERSON (ID,NAME,POSITION,STATUS,teamname,REPRESENT)"
					+ "VALUES (4, 'Mark', 'VD','Partner', NULL ,'XXL');";
			stmt.executeUpdate(sql);

			System.out.println("Inserted PERSON successfully");

			//CREATE TABLE ROOM

			sql = "CREATE TABLE ROOM " + "(ID INT PRIMARY KEY NOT NULL," + "NAME TEXT NOT NULL);";
			stmt.executeUpdate(sql);

			System.out.println("Created ROOM successfully");

			//INSERT INTO ROOM

			sql = "INSERT INTO ROOM (ID,NAME)" + "VALUES (1, 'Main Room');";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO ROOM (ID,NAME)" + "VALUES (2, 'Sonic');";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO ROOM (ID,NAME)" + "VALUES (3, 'Mario');";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO ROOM (ID,NAME)" + "VALUES (4, 'Megaman');";
			stmt.executeUpdate(sql);

			System.out.println("Inserted ROOM successfully");

			//CREATE TABLE MEETING

			sql = "CREATE TABLE MEETING " + "(ID INT PRIMARY KEY NOT NULL, " + " roomID INT REFERENCES ROOM(ID) NOT NULL, "
					+"teamID INT NOT NULL, "+"startTime TIME NOT NULL, "+" endTime TIME NOT NULL ,"+"date DATE NOT NULL ,"
					+"bookedby INT REFERENCES person(id) NOT NULL"+")";
			stmt.executeUpdate(sql);

			System.out.println("Created MEETING successfully");



			//CREATE TABLE COST

			sql = "CREATE TABLE COST " + "(teamname TEXT NOT NULL, " + " AMOUNT INT NOT NULL, "+" MEETINGID INT REFERENCES MEETING(ID) NOT NULL); ";
			stmt.executeUpdate(sql);

			System.out.println("Created COST successfully");


			//CREATE TABLE FACILITY

			sql = "CREATE TABLE FACILITY " + "(ID INT PRIMARY KEY NOT NULL," + " TYPE TEXT NOT NULL);";
			stmt.executeUpdate(sql);

			System.out.println("Created FACILITY successfully");

			//INSERT INTO FACILITY

			sql = "INSERT INTO FACILITY (ID,TYPE)" + "VALUES (1, 'TV');";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO FACILITY (ID,TYPE)" + "VALUES (2, 'Whiteboard');";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO FACILITY (ID,TYPE)" + "VALUES (3, 'Table');";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO FACILITY (ID,TYPE)" + "VALUES (4, 'Sofa');";
			stmt.executeUpdate(sql);

			System.out.println("Inserted FACILITY successfully");







			//CREATE TABLE ATTENDS

			sql = "CREATE TABLE ATTENDS " + "(meetingID INT REFERENCES MEETING(ID) NOT NULL," + " personID INT REFERENCES PERSON(ID) NOT NULL); ";
			stmt.executeUpdate(sql);

			System.out.println("Created ATTENDS successfully");

			//CREATE TABLE ROOMFACILITIES

			sql = "CREATE TABLE ROOMFACILITIES" + "(ROOMID INT REFERENCES ROOM(ID) NOT NULL," + " FACILITYID INT REFERENCES FACILITY(ID) NOT NULL); ";
			stmt.executeUpdate(sql);

			//INSERT INTO ROOMFACILITIES

			sql = "INSERT INTO ROOMFACILITIES (ROOMID,FACILITYID)" + "VALUES (1,3);";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO ROOMFACILITIES (ROOMID,FACILITYID)" + "VALUES (1,2);";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO ROOMFACILITIES (ROOMID,FACILITYID)" + "VALUES (2,1);";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO ROOMFACILITIES (ROOMID,FACILITYID)" + "VALUES (3,3);";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO ROOMFACILITIES (ROOMID,FACILITYID)" + "VALUES (3,1);";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO ROOMFACILITIES (ROOMID,FACILITYID)" + "VALUES (3,4);";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO ROOMFACILITIES (ROOMID,FACILITYID)" + "VALUES (4,4);";
			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}
}
