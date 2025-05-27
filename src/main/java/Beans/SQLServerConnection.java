package Beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLServerConnection {
	public static Connection initializeDatabase() throws SQLException, ClassNotFoundException {
		String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = "LAPTOP-AQL6LS36";
		String dbName = "WEB";
		String dbUserName = "sa";
		String dbPassword = "1234567";

		String connectionURL = "jdbc:sqlserver://" + dbURL + ";databaseName=" + dbName
				+ ";encrypt=true;trustServerCertificate=true";

		Connection conn = null;

		try {
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(connectionURL, dbUserName, dbPassword);
			System.out.println("Kết nối thành công.");
		} catch (SQLException e) {
			System.out.println("Kết nối thất bại.");
			e.printStackTrace();
			throw e; // Ném lại lỗi
		}
		return conn;
	}
}