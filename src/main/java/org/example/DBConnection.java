package org.example;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private Connection connection;

    public DBConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
        createTable();
    }


    public Account findAccount(String saskaitosNumeris) throws SQLException {

        var statement = connection.prepareStatement("SELECT * FROM accounts WHERE accNum = ?");
        statement.setString(1, saskaitosNumeris);

        var result = statement.executeQuery();
        if(!result.next()) {
            return null;
        }

        return new Account(result.getString(1), result.getString(2), result.getDouble(3), result.getString(4));
    }


    public void upsertAccount(Account acc) throws SQLException {
        var statement = connection.prepareStatement("INSERT INTO accounts (accNum, name, balance, pin) VALUES (?, ?, ?, ?)" +
                "ON CONFLICT(accNum) DO UPDATE SET name = excluded.name, balance = excluded.balance, pin = excluded.pin");
        statement.setString(1, acc.getSaskaitosNumeris());
        statement.setString(2, acc.getPilnasVardas());
        statement.setDouble(3, acc.getLikutis());
        statement.setString(4, acc.getPin());
        statement.executeUpdate();
        statement.close();
    }


    public void createTable() throws SQLException {
        var statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS accounts ( accNum TINYTEXT PRIMARY KEY, name TINYTEXT, balance FLOAT, pin TINYTEXT )");
        statement.close();
    }


    public void closeConnection() throws SQLException {
        connection.close();
    }

}
