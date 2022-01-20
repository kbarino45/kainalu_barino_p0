package dev.barino.util;

//Imports from java.sql -> JDBC
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnection {

    private static Connection connection = null;

    /**
     * Establishes a connection if none exists. Returns the current connection otherwise.
     * @return Connection
     */
    public static Connection getConnection() {

        if (connection == null) {

            Properties props = new Properties();
            try {

                props.load(JDBCConnection.class.getClassLoader().getResourceAsStream("connection.properties"));
                //props.load(new FileReader("src/main/resources/connection.properties"));

                String endpoint = props.getProperty("endpoint");
                String url = "jdbc:postgresql://" + endpoint + "/postgres";
                String username = props.getProperty("username");
                String password = props.getProperty("password");

                connection = DriverManager.getConnection(url, username, password);

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } //end if

        return connection;

    }

    //TESTER - SUCCESSFUL
//     public static void main(String[] args) {
//         Connection conn1 = getConnection();
//         Connection conn2 = getConnection();
//         System.out.println(conn1);
//         System.out.println(conn2);
//     }

}
