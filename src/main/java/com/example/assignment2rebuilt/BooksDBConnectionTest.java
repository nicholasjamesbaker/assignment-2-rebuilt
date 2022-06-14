package com.example.assignment2rebuilt;

import java.sql.*;

/**
 * BooksDBConnectionTest - Test class for connecting to the database
 * @author Nick
 */
public class BooksDBConnectionTest {

    //Set up the Database Parameters
    static final String DATABASE_URL = "jdbc:mariadb://localhost:3304/books";
    static final String USER = "root";
    static final String PASS = "Password1";

    public static void main(String[] args) {
        // Open a connection
        try(
                Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASS);
                Statement statement = connection.createStatement();
        ){
            String sqlQuery = "SELECT * from titles";          //The Query

            //Execute the query and get the result set
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            System.out.print("ISBN \t\t\t Title");
            while (resultSet.next()) {
                System.out.printf("\n%s \t\t\t %s ",
                        resultSet.getString("isbn"), resultSet.getString("title"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}