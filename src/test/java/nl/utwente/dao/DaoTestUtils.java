package nl.utwente.dao;

import org.postgresql.core.SqlCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.TestCase.fail;

public class DaoTestUtils {
    public static Connection connection;
    public static int roomID = -100;
    public static String roomName = "test_room";
    public static String bookingTitle = "Test booking title";
    public static String email = "sqills_tablet@gmail.com";

    static Time currentTimePlusMinutes(int offset) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE,offset);
        return new Time(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),0);
    }

    public static void createRoom(int id, String name) {
        String query = "INSERT INTO sqills.room (room_id, room_name) VALUES (" + id + ", '" + name + "')";
        try {
            connection.createStatement().execute(query);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public static void deleteRoom(int id) throws SQLException {
        String query = "DELETE FROM sqills.room WHERE room_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
        connection.commit();
    }
}
