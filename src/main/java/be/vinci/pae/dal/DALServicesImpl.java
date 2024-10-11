package be.vinci.pae.dal;

import be.vinci.pae.exception.FatalException;
import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * The DALServicesImpl class implements the DALBackServices and DALServices interfaces. It provides
 * methods for database connection and query preparation. This class is responsible for handling the
 * database connection and executing SQL queries. It uses a ThreadLocal object to hold the
 * Connection object for each thread. It also uses a BasicDataSource object to manage the database
 * connection pool.
 */
public class DALServicesImpl implements DALBackServices, DALServices {

  // ThreadLocal object to hold the Connection object for each thread
  private final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

  // BasicDataSource object to manage the database connection pool
  private final BasicDataSource dataSource = new BasicDataSource();

  /**
   * The constructor for the DALServicesImpl class. It establishes a connection to the database
   * using the BasicDataSource object and the database credentials from the Config class.
   */
  public DALServicesImpl() {
    this.dataSource.setUrl(Config.getProperty("dbURL"));
    this.dataSource.setUsername(Config.getProperty("dbUser"));
    this.dataSource.setPassword(Config.getProperty("dbPassword"));
  }

  /**
   * Prepares a SQL query for execution.
   *
   * @param query The SQL query to prepare.
   * @return A PreparedStatement object that represents the prepared query.
   * @throws RuntimeException if a SQLException is thrown when preparing the statement.
   */
  @Override
  public PreparedStatement getPreparedStatement(String query) {
    try {
      return threadLocalConnection.get().prepareStatement(query);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Opens a new database connection and sets it in the ThreadLocal. This method checks if a
   * connection already exists in the ThreadLocal. If it does, the method returns without doing
   * anything. If it doesn't, the method retrieves a new connection from the dataSource and sets it
   * in the ThreadLocal. If a SQLException is thrown during this process, a FatalException is
   * thrown.
   *
   * @throws FatalException if a SQLException is thrown when retrieving a connection from the
   *                        dataSource
   */
  public void open() {
    // Check if a connection already exists in the ThreadLocal
    if (threadLocalConnection.get() != null) {
      return;
    }
    try {
      // Retrieve a new connection from the dataSource
      Connection conn = dataSource.getConnection();
      // Set the new connection in the ThreadLocal
      threadLocalConnection.set(conn);
    } catch (SQLException e) {
      // Throw a FatalException if a SQLException is thrown
      throw new FatalException(e);
    }
  }

  /**
   * Starts a database transaction by setting auto-commit to false.
   *
   * @throws RuntimeException if a SQLException is thrown when setting auto-commit.
   */
  @Override
  public void startTransaction() {
    try {
      open();
      threadLocalConnection.get().setAutoCommit(false);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Commits the current database transaction and closes the connection.
   *
   * @throws RuntimeException if a SQLException is thrown when committing the transaction or closing
   *                          the connection.
   */
  @Override
  public void commit() {
    Connection conn = threadLocalConnection.get();
    try (conn) {
      threadLocalConnection.remove();
      conn.commit();
      conn.setAutoCommit(true);
    } catch (SQLException e) {
      try {
        conn.setAutoCommit(true);
      } catch (SQLException ignored) {
        throw new FatalException(e);
      }
      throw new FatalException(e);
    }
  }

  /**
   * Rolls back the current database transaction and closes the connection.
   *
   * @throws RuntimeException if a SQLException is thrown when rolling back the transaction or
   *                          closing the connection.
   */
  @Override
  public void rollback() {
    Connection conn = threadLocalConnection.get();
    try (conn) {
      threadLocalConnection.remove();
      conn.rollback();
      conn.setAutoCommit(true);
    } catch (SQLException e) {
      try {
        conn.setAutoCommit(true);
      } catch (SQLException ignored) {
        throw new FatalException(e);
      }
      throw new FatalException(e);
    }
  }

  /**
   * Closes the current database connection and removes it from the ThreadLocal. This method
   * retrieves the Connection object from the ThreadLocal and attempts to close it. The Connection
   * object is then removed from the ThreadLocal. If a SQLException is thrown during the process, a
   * RuntimeException is thrown.
   *
   * @throws RuntimeException if a SQLException is thrown when closing the connection.
   */
  @Override
  public void close() {
    try {
      Connection conn = threadLocalConnection.get();
      threadLocalConnection.remove();
      conn.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }
}