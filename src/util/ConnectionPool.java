package util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionPool {
	private static DataSource dataSource = null;

	public static Connection get() throws NamingException, SQLException {
		if (dataSource == null) {
			dataSource = (DataSource) (new InitialContext()).lookup("java:comp/env/jdbc/mysns");
		}
		return dataSource.getConnection();
	}
}
