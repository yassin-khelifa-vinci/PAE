package be.vinci.pae.utils;

// Importing the Role enum from UserDTO class

import java.time.LocalDate;

/**
 * This is a utility class that provides static methods for common operations.
 */
public class Util {

  /**
   * This method checks if a given string is null or blank.
   *
   * @param str The string to be checked.
   * @return boolean Returns true if the string is null or blank, false otherwise.
   */
  public static boolean checkEmptyString(String str) {
    // Check if the string is null or blank
    return str == null || str.isBlank();
  }


  /**
   * Returns the current school year as a string in the format "YYYY/YYYY", based on the current
   * date. The school year starts on September 14th and ends on September 13th of the next year. If
   * the current date is between these two dates, the method returns the current year and the next
   * year separated by a slash. If the current date is not between these two dates, the method
   * returns the previous year and the current year separated by a slash.
   *
   * @return The current school year as a string in the format "YYYY/YYYY".
   */
  public static String getCurrentSchoolYear() {
    // Get the current date
    LocalDate currentDate = LocalDate.now();

    // Get the current year and the next year
    int year = currentDate.getYear();
    int nextYear = year + 1;

    // Define the start and end date of the current school year
    LocalDate currentStartDate = LocalDate.of(year, 9, 14);
    LocalDate currentEndDate = LocalDate.of(nextYear, 9, 13);

    // Check if the current date is within the current school year
    if (currentDate.isAfter(currentStartDate) && currentDate.isBefore(currentEndDate)) {
      // If it is, return the current year and the next year
      return year + "-" + nextYear;
    } else {
      // If it's not, return the previous year and the current year
      return (year - 1) + "-" + year;
    }
  }

}