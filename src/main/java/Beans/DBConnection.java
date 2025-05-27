package Beans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class DBConnection {
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		return SQLServerConnection.initializeDatabase();
	}
}
