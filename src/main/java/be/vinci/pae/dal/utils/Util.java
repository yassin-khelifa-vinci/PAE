package be.vinci.pae.dal.utils;

import be.vinci.pae.exception.FatalException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for handling database operations.
 */
public class Util {

  /**
   * Fills an object with data from a ResultSet.
   *
   * @param rs  the ResultSet to retrieve data from
   * @param obj the object to fill with data
   * @return the filled object
   * @throws SQLException if a database access error occurs
   */
  public static Object fillInfos(ResultSet rs, Object obj) throws SQLException {
    try {
      Class<?> clazz = obj.getClass();

      // Iterate over each column in the ResultSet
      for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
        String columnName = rs.getMetaData().getColumnName(i);

        Object value = rs.getObject(columnName);
        if (value == null) {
          continue;
        }

        // Iterate over each field in the object
        for (Field field : clazz.getDeclaredFields()) {
          // If the field name matches the column name
          if (field.getName().equalsIgnoreCase(columnName)) {
            // Make the field accessible if it is not already
            if (!field.canAccess(obj)) {
              field.setAccessible(true);
            }
            // If the field is an enum, convert the value to an enum
            if (field.getType().isEnum()) {
              Class<?> enumType = field.getType();
              Method valueOf = enumType.getMethod("valueOf", String.class);
              value = valueOf.invoke(null, (String) value);
            } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
              value = Boolean.parseBoolean(value.toString());
            }
            field.set(obj, value);
            break;
          }
        }
      }
      return obj;
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }
}