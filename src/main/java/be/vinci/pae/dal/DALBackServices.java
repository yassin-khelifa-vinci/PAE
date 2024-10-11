package be.vinci.pae.dal;

import java.sql.PreparedStatement;

/**
 * The DALBackServices interface provides a method for preparing SQL queries. Classes implementing
 * this interface should provide a concrete implementation of these methods. The methods in this
 * interface are used to prepare SQL queries for execution. The prepared statements can then be used
 * to execute SQL commands against a database.
 */
public interface DALBackServices {

  /**
   * Prepares a SQL query for execution. This method takes a SQL query as a string and returns a
   * PreparedStatement object that represents the prepared query.
   *
   * @param query The SQL query to prepare.
   * @return A PreparedStatement object that represents the prepared query.
   */
  PreparedStatement getPreparedStatement(String query);

}