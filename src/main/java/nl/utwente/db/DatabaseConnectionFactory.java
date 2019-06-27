package nl.utwente.db;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The DatabaseConnectionFactory is responsible for returning connections to our SQL database.
 */
public class DatabaseConnectionFactory {

    static DataSource datasource;
    static PoolProperties poolProperties;
    static final String host = "castle.ewi.utwente.nl";
    static final String dbName = "di125";
    static final String url = "jdbc:postgresql://" + host + ":5432/" + dbName;
    static final String username = "di125";
    static final String password = "E9+gNMnM";

    public DatabaseConnectionFactory() {
        // We actually use a connection pool of 100 instead of randomly assigning connections
        // (which can lead to problems when there are many users (or a DDOS attack or such)
        // But this is better than using just one connection
        poolProperties = new PoolProperties();
        poolProperties.setUrl(url);
        poolProperties.setDriverClassName("org.postgresql.Driver");
        poolProperties.setUsername(username);
        poolProperties.setPassword(password);
        poolProperties.setJmxEnabled(true);
        poolProperties.setTestWhileIdle(false);
        poolProperties.setTestOnBorrow(true);
        poolProperties.setValidationQuery("SELECT 1");
        poolProperties.setTestOnReturn(false);
        poolProperties.setValidationInterval(30000);
        poolProperties.setTimeBetweenEvictionRunsMillis(30000);
        poolProperties.setMaxActive(100);
        poolProperties.setInitialSize(10);
        poolProperties.setMaxWait(10000);
        poolProperties.setRemoveAbandonedTimeout(60);
        poolProperties.setMinEvictableIdleTimeMillis(30000);
        poolProperties.setMinIdle(10);
        poolProperties.setLogAbandoned(true);
        poolProperties.setRemoveAbandoned(true);
        poolProperties.setDefaultAutoCommit(false);
        poolProperties.setJdbcInterceptors(
            "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        datasource = new DataSource();
        datasource.setPoolProperties(poolProperties);
    }

    /**
     * Returns a connection to the SQL database.
     * @return connection object to the database
     *         null if no connection could be established
     */
    public static Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }

}
