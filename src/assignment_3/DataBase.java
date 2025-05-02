/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_3;

/**
 *
 * @author Ayxan
 */
import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private Connection connection;

    public DataBase() throws SQLException {
        String dbURL = "jdbc:mysql://localhost:3306/highscore?serverTimezone=UTC";
        String user = "root";
        String password = "12345678ayxanpaukXX";
        connection = DriverManager.getConnection(dbURL, user, password);
    }

    public void addPlayerScore(String name, int collectedBaskets) throws SQLException {
        String query = "INSERT INTO leaderboard (timestamp, name, collected_baskets) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setString(2, name);
            statement.setInt(3, collectedBaskets);
            statement.executeUpdate();
        }
    }

    public ArrayList<LeaderBoardEnt> getLeaderboard() throws SQLException {
        String query = "SELECT name, collected_baskets FROM leaderboard ORDER BY collected_baskets DESC, timestamp ASC";
        ArrayList<LeaderBoardEnt> leaderboard = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(query)) {
            while (results.next()) {
                String name = results.getString("name");
                int collectedBaskets = results.getInt("collected_baskets");
                leaderboard.add(new LeaderBoardEnt(name, collectedBaskets));
            }
        }

        return leaderboard;
    }
}