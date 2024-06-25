package com.example.to_do_list;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.Class.forName;

public class DBConnection {

    private static DBConnection dbconnection;
    private Connection connection;
    private DBConnection()
    {
        try {
        forName("com.mysql.cj.jdbc.Driver");
       connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","");
    } catch (ClassNotFoundException | SQLException e) {
        throw new RuntimeException(e);
    }
    }

    public static DBConnection getInstance()
    {
        return(dbconnection == null) ? dbconnection = new DBConnection() :dbconnection;
    }

    public Connection getConnection()
    {
        return connection;
    }

}
