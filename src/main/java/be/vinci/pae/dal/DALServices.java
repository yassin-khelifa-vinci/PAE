package be.vinci.pae.dal;

/**
 * The DALServices interface provides the contract for the Data Access Layer services. It declares
 * methods for starting, committing, and rolling back a database transaction. This interface is
 * implemented by classes that provide concrete implementations for these methods. The methods in
 * this interface are used to manage database transactions.
 */
public interface DALServices {

  /**
   * Opens a new database connection. This method is responsible for establishing a new connection
   * to the database. The exact details of how this connection is established will depend on the
   * specific implementation. Typically, this involves using a JDBC driver and a specific URL for
   * the database.
   */
  void open();

  /**
   * Starts a database transaction. Implementations should ensure that this method sets the
   * auto-commit mode of the database connection to false.
   */
  void startTransaction();

  /**
   * Commits the current database transaction. Implementations should ensure that this method
   * commits the current transaction and closes the database connection.
   */
  void commit();

  /**
   * Rolls back the current database transaction. Implementations should ensure that this method
   * rolls back the current transaction and closes the database connection.
   */
  void rollback();

  /**
   * Closes the current database connection and removes it from the ThreadLocal. This method
   * retrieves the Connection object from the ThreadLocal and attempts to close it. The Connection
   * object is then removed from the ThreadLocal. If a SQLException is thrown during the process, a
   * RuntimeException is thrown.
   *
   * @throws RuntimeException if a SQLException is thrown when closing the connection.
   */
  void close();
}