package com.comp3004.educationmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class Server extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
// 		Connection con = null;
//		Statement stmt = null;
//		int result = 0;
//		ResultSet results = null;
//
//		try {
//			Class.forName("org.hsqldb.jdbc.JDBCDriver");
//			con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/testdb", "SA", "");
//			stmt = con.createStatement();
//
//			result = stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (userid int, username varchar(20));");
//			result = stmt.executeUpdate("INSERT INTO users VALUES (0, 'cameronrolfe');");
//			result = stmt.executeUpdate("INSERT INTO users VALUES (1, 'camerondickie');");
//			result = stmt.executeUpdate("INSERT INTO users VALUES (2, 'benwilliams');");
//			result = stmt.executeUpdate("INSERT INTO users VALUES (3, 'jaxsonhood');");
//
//
//
//			results = stmt.executeQuery("select * from users;");
//
//			while(results.next()){
//				System.out.println(results.getInt("userid") + " | " +  results.getString("username"));
//
//			}
//
//		}  catch (Exception e) {
//			e.printStackTrace(System.out);
//		}
//		System.out.println("Table created successfully");
	}
}
