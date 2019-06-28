package nl.utwente.dao;

import nl.utwente.authentication.BasicSecurityContext;
import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.model.User;
import org.apache.poi.util.Internal;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static nl.utwente.authentication.AuthenticationHandler.checkByteArrays;
import static nl.utwente.authentication.AuthenticationHandler.hashPassword;
import static nl.utwente.db.DatabaseConnectionFactory.*;

public class UserDao {
    public static List<User> getAllUsers() {
        Connection connection = DatabaseConnectionFactory.getConnection();
        List<User> users = new ArrayList<>();

        try {
            String query = "SELECT * FROM sqills.users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                User user = new User();
                user.setAdministrator(resultSet.getBoolean("administrator"));
                user.setEmail(resultSet.getString("email"));
                user.setName(resultSet.getString("name"));
                user.setUserid(resultSet.getInt("user_id"));
                users.add(user);
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

        return users;
    }

    // TODO error handlign
    public static User getUser(int userID) {
        Connection connection = DatabaseConnectionFactory.getConnection();
        User user = null;
        try {
            String query = "SELECT * FROM sqills.users WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUserid(resultSet.getInt("user_id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setAdministrator(resultSet.getBoolean("administrator"));
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

    public static boolean isValidUserID(int userID) throws DAOException {
        Connection connection = DatabaseConnectionFactory.conn;

        @Internal
        static boolean isValidUserID ( int userID, Connection connection) throws DAOException {

            boolean isValid = false;

            try {
                String query = "SELECT * FROM sqills.users WHERE user_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, userID);

                ResultSet resultSet = preparedStatement.executeQuery();
                isValid = resultSet.next();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException(e.getMessage());
            }
            return isValid;
        }

        public static User getUserFromEmail (String email) throws
        InvalidEmailException, DAOException {
            if (!isValidEmail(email)) {
                throw new InvalidEmailException(email);
            }

            boolean isValid = false;
            User user = null;
            Connection connection = null;
            try {
                connection = DatabaseConnectionFactory.getConnection();
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
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return user;
        }

        public static String getEmail (String incompleteEmail) throws DAOException {
            int count = 0;
            String email = null;

            Connection connection = null;
            try {
                connection = DatabaseConnectionFactory.getConnection();
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

        @Deprecated
        public static byte[] getSalt (String email) throws InvalidEmailException, DAOException {
            if (!isValidEmail(email)) {
                throw new InvalidEmailException(email);
            }

            int userCount = 1;
            byte[] salt = null;
            byte[] saltbyte = null;
            Connection connection = null;
            try {
                connection = DatabaseConnectionFactory.getConnection();
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
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public static void insertUser (String name, String email,boolean admin) throws DAOException
        {
            Connection connection = null;
            try {
                connection = DatabaseConnectionFactory.getConnection();
                String query = "INSERT INTO sqills.users (\"name\", email, administrator) VALUES (?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setBoolean(3, admin);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                throw new DAOException(sqle.getMessage());
            } finally {
                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        @Deprecated
        public static byte[] getHash (String email) throws InvalidEmailException, DAOException {
            if (!isValidEmail(email)) {
                throw new InvalidEmailException(email);
            }

            int count = 0;
            Connection connection = null;
            try {
                connection = DatabaseConnectionFactory.getConnection();
                String query = "SELECT hash FROM sqills.users WHERE email LIKE ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                //          statement.setBytes(2, userPassword);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    return resultSet.getBytes("hash");
                }
            } catch (SQLException sqle) {
                System.err.println(sqle);
            } finally {
                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Deprecated
        public static boolean checkCredentials (String email, String password) throws DAOException {
            try {
                return checkByteArrays(getHash(email), hashPassword(password, getSalt(email)));
            } catch (InvalidEmailException e) {
                e.printStackTrace();
            }
            return false;
        }

        public static boolean isValidEmail (String email) throws DAOException {
            if (email.contains("@") && email.contains(".")) {
                return getEmail(email) != null;
            }
            return false;
        }

        public static String getEmailFromUsername (String username){
            String result = null;
            Connection connection = DatabaseConnectionFactory.getConnection();

            public static User getUserFromContext (ContainerRequestContext context){
                User user = null;
                try {
                    user = (User) context.getProperty("user");
                } catch (NullPointerException ignored) {
                }
                if (user == null) {
                    try {
                        user = ((BasicSecurityContext) context.getSecurityContext()).getUser();
                    } catch (ClassCastException | NullPointerException ignored) {
                    }
                }
                return user;
            }

            public static void main (String[]args){
                String name = "Platon Frolov";
                String email = "p.m.frolov@student.utwente.nl";
                boolean admin = true;
                try {
                    String query = "SELECT email FROM sqills.users WHERE name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        result = resultSet.getString(1);
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

                return result;
            }

            public static void deleteUser ( int userID){
                Connection connection = DatabaseConnectionFactory.getConnection();

                try {
                    String query = "DELETE FROM sqills.users WHERE user_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, userID);

                    preparedStatement.execute();
                    connection.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            public static User updateUser (User user){
                Connection connection = DatabaseConnectionFactory.getConnection();

                try {
                    String query = "UPDATE sqills.users " +
                        "SET name = ?," +
                        "email = ?," +
                        "administrator = ?" +
                        "WHERE user_id = ?";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, user.getName());
                    preparedStatement.setString(2, user.getEmail());
                    preparedStatement.setBoolean(3, user.isAdministrator());
                    preparedStatement.setInt(4, user.getUserid());

                    preparedStatement.execute();
                    connection.commit();
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

            public static int createUser (User user){
                Connection connection = DatabaseConnectionFactory.getConnection();
                int userID = -1;

                try {
                    String query = "INSERT INTO sqills.users (name, email, administrator)" +
                        " VALUES (?, ?, ?) RETURNING user_id";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, user.getName());
                    preparedStatement.setString(2, user.getEmail());
                    preparedStatement.setBoolean(3, user.isAdministrator());

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        userID = resultSet.getInt(1);
                    }
                    connection.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                return userID;
            }
        }
    }
}