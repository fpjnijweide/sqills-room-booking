package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;

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
            connection.close();
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

    public static byte[] getSalt(String email){
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

    public static boolean checkByteArrays(byte[] firstArray, byte[] secondArray){
        return Arrays.equals(firstArray, secondArray);
    }

    public static void insertUser(String name, String email, String password, boolean admin){
        Connection connection = DatabaseConnectionFactory.getConnection();
        try{
            byte[] salt = generateSalt();
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
            System.err.println(sqle);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(String password, byte[] salt) {
        byte[] hash = null;
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
        } catch(NoSuchAlgorithmException e){
            System.err.println(e);
        } catch (InvalidKeySpecException e){
            System.err.println(e);
        }
        return hash;
    }

    public static byte[] getHash(String email){
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
        return checkByteArrays(getHash(email), hashPassword(password, getSalt(email)));
    }

    public static boolean isValidEmail(String email) {
        return getEmail(email)!=null;
    }
}
