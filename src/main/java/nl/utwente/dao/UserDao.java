package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static nl.utwente.authentication.AuthenticationHandler.checkByteArrays;
import static nl.utwente.authentication.AuthenticationHandler.hashPassword;

public class UserDao {
    public static boolean isValidUserID(int userID) {
        Connection connection = DatabaseConnectionFactory.getConnection();
        boolean isValid = false;

        try {
            String query = "SELECT * FROM sqills.users WHERE userid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            isValid = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isValid;
    }

    public static User getUserFromEmail(String email) throws InvalidEmailException {
        if (!isValidEmail(email)) {
            throw new InvalidEmailException(email);
        }
        Connection connection = DatabaseConnectionFactory.getConnection();
        boolean isValid = false;
        User user = null;
        try {
            String query = "SELECT * FROM sqills.users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int id = resultSet.getInt("user_id");
                boolean admin = resultSet.getBoolean("administrator");
                user = new User(id, name, email, admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    public static String getEmail(String incompleteEmail) {
        int count = 0;
        String email = null;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT email FROM sqills.users WHERE email LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, incompleteEmail + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                count++;
                email = resultSet.getString("email");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (count == 1) {
            return email;
        }
        return null;
    }

    public static byte[] getSalt(String email) throws InvalidEmailException {
        if (!isValidEmail(email)){
            throw new InvalidEmailException(email);
        }
        Connection connection = DatabaseConnectionFactory.getConnection();
        int userCount = 1;
        byte[] salt = null;
        byte[] saltbyte = null;
        try {
            String query = "SELECT salt FROM sqills.users WHERE email LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getBytes("salt");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void insertUser(String name, String email, boolean admin){
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "INSERT INTO sqills.users (\"name\", email, administrator) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setBoolean(3, admin);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException sqle){
            sqle.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] getHash(String email) throws InvalidEmailException {
        if (!isValidEmail(email)){
            throw new InvalidEmailException(email);
        }
        Connection connection = DatabaseConnectionFactory.getConnection();
        int count = 0;
        try {
            String query = "SELECT hash FROM sqills.users WHERE email LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            //          statement.setBytes(2, userPassword);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                return resultSet.getBytes("hash");
            }
        } catch (SQLException sqle){
            System.err.println(sqle);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static boolean checkCredentials(String email, String password){
        try {
            return checkByteArrays(getHash(email), hashPassword(password, getSalt(email)));
        } catch (InvalidEmailException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public static boolean isValidEmail(String email) {
        if (email.contains("@") && email.contains(".")){
            return getEmail(email) != null;
        }
        return false;
    }

//    public static boolean loggedIn(SecurityContext securityContext) {
//        return securityContext.getUserPrincipal().
//    }

    public static void main(String[] args) {
        String name = "Platon Frolov";
        String email = "p.m.frolov@student.utwente.nl";
        boolean admin = true;
        insertUser(name, email, admin);
    }
}
