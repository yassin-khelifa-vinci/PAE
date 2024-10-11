package be.vinci.pae.business.entreprise;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * EnterpriseDTO interface. This interface represents an Enterprise in the system. It provides
 * methods for getting and setting the enterprise's id, trade name, designation, street, street
 * number, postal code, city, country, phone number, email, blacklisted status, and blacklisted
 * reason.
 */
@JsonDeserialize(as = EnterpriseImpl.class)
public interface EnterpriseDTO {

  /**
   * Get the enterprise id.
   *
   * @return the enterprise id
   */
  int getIdEnterprise();

  /**
   * Set the enterprise id.
   *
   * @param idEnterprise the enterprise id
   */
  void setIdEnterprise(int idEnterprise);

  /**
   * Get the trade name.
   *
   * @return the trade name
   */
  String getTradeName();

  /**
   * Set the trade name.
   *
   * @param tradeName the trade name
   */
  void setTradeName(String tradeName);

  /**
   * Get the designation.
   *
   * @return the designation
   */
  String getDesignation();

  /**
   * Set the designation.
   *
   * @param designation the designation
   */
  void setDesignation(String designation);

  /**
   * Get the street.
   *
   * @return the street
   */
  String getStreet();

  /**
   * Set the street.
   *
   * @param street the street
   */
  void setStreet(String street);

  /**
   * Get the street number.
   *
   * @return the street number
   */
  String getStreetNumber();

  /**
   * Set the street number.
   *
   * @param streetNumber the street number
   */
  void setStreetNumber(String streetNumber);

  /**
   * Get the postal code.
   *
   * @return the postal code
   */
  String getPostalCode();

  /**
   * Set the postal code.
   *
   * @param postalCode the postal code
   */
  void setPostalCode(String postalCode);

  /**
   * Get the city.
   *
   * @return the city
   */
  String getCity();

  /**
   * Set the city.
   *
   * @param city the city
   */
  void setCity(String city);

  /**
   * Get the country.
   *
   * @return the country
   */
  String getCountry();

  /**
   * Set the country.
   *
   * @param country the country
   */
  void setCountry(String country);

  /**
   * Get the phone number.
   *
   * @return the phone number
   */
  String getPhoneNumber();

  /**
   * Set the phone number.
   *
   * @param phoneNumber the phone number
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Get the email.
   *
   * @return the email
   */
  String getEmail();

  /**
   * Set the email.
   *
   * @param email the email
   */
  void setEmail(String email);

  /**
   * Get the blacklisted status.
   *
   * @return the blacklisted status
   */
  boolean isBlacklisted();

  /**
   * Set the blacklisted status.
   *
   * @param isBlacklisted the blacklisted status
   */
  void setBlacklisted(boolean isBlacklisted);

  /**
   * Get the blacklisted reason.
   *
   * @return the blacklisted reason
   */
  String getBlacklistedReason();

  /**
   * Set the blacklisted reason.
   *
   * @param blacklistedReason the blacklisted reason
   */
  void setBlacklistedReason(String blacklistedReason);

  /**
   * Get the version number.
   *
   * @return the version number
   */
  int getVersionNumber();

  /**
   * Set the version number.
   *
   * @param versionNumber the version number
   */
  void setVersionNumber(int versionNumber);
}
