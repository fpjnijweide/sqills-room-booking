package nl.utwente.model;

import nl.utwente.db.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Room {
    /**
     * Returns whether a roomID belongs to a room.
     * @param roomID The roomID which validity will be checked
     * @return Whether the provided roomID is valid
     */
    public static boolean isValidRoomID(int roomID) {
        try {
            Connection connection = DatabaseConnectionFactory.getConnection();
            String query = "SELECT * FROM sqills.Room WHERE roomID = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);

            ResultSet resultSet = statement.executeQuery();

            boolean result = resultSet.next();
            resultSet.getStatement().getConnection().close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
