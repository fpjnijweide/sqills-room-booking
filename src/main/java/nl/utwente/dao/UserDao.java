package nl.utwente.dao;

import nl.utwente.authentication.AuthenticationHandler;
import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.model.User;

import java.sql.*;

import static nl.utwente.authentication.AuthenticationHandler.checkByteArrays;
import static nl.utwente.authentication.AuthenticationHandler.hashPassword;

public class UserDao {
    public static boolean isValidUserID(int userID) throws DAOException {
        Connection connection = DatabaseConnectionFactory.conn;
        boolean isValid = false;

        try {
            String query = "SELECT * FROM sqills.users WHERE userid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            isValid = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isValid;
    }

    public static User getUserFromEmail(String email) throws InvalidEmailException, DAOException {
        if (!isValidEmail(email)) {
            throw new InvalidEmailException(email);
        }
        Connection connection = DatabaseConnectionFactory.conn;
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
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    public static String getEmail(String incompleteEmail) throws DAOException {
        int count = 0;
        String email = null;
        Connection connection = DatabaseConnectionFactory.conn;
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
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (count == 1) {
            return email;
        }
        return null;
    }

    @Deprecated
    public static byte[] getSalt(String email) throws InvalidEmailException, DAOException {
        if (!isValidEmail(email)){
            throw new InvalidEmailException(email);
        }
        Connection connection = DatabaseConnectionFactory.conn;
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
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Deprecated
    public static void insertUser(String name, String email, String password, boolean admin) throws DAOException {
        // TODO input validation (XSS and such?)
        Connection connection = DatabaseConnectionFactory.conn;
        try{
            byte[] salt = AuthenticationHandler.generateSalt();
            String query = "INSERT INTO sqills.users(name, email, hash, administrator, salt) " +
                    "VALUES(?,?,?,?,?);";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, email);
            statement.setString(2, email);
            statement.setBytes(3, hashPassword(password, salt));
            statement.setBoolean(4, admin);
            statement.setBytes(5, salt);
            int affectedRows = statement.executeUpdate();
            System.out.println(affectedRows);
        } catch (SQLException sqle){
            throw new DAOException(sqle.getMessage());
        } finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    public static byte[] getHash(String email) throws InvalidEmailException, DAOException {
        if (!isValidEmail(email)){
            throw new InvalidEmailException(email);
        }
        Connection connection = DatabaseConnectionFactory.conn;
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
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Deprecated
    public static boolean checkCredentials(String email, String password) throws DAOException {
        try {
            return checkByteArrays(getHash(email), hashPassword(password, getSalt(email)));
        } catch (InvalidEmailException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public static boolean isValidEmail(String email) throws DAOException {
        if (email.contains("@") && email.contains(".")){
            return getEmail(email) != null;
        }
        return false;
    }

//    public static boolean loggedIn(SecurityContext securityContext) {
//        return securityContext.getUserPrincipal().
//    }
}
