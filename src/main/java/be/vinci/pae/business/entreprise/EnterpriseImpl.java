package be.vinci.pae.business.entreprise;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enterprise interface. This interface represents an Enterprise in the system. It provides methods
 * for getting and setting the enterprise's id, trade name, designation, street, street number,
 * postal code, city, country, phone number, email, blacklisted status, and blacklisted reason.
 */
public class EnterpriseImpl implements Enterprise {

  /**
   * The unique identifier for the enterprise.
   */
  private int idEnterprise;

  /**
   * The trade name of the enterprise.
   */
  private String tradeName;

  /**
   * The designation of the enterprise.
   */
  private String designation;

  /**
   * The street where the enterprise is located.
   */
  private String street;

  /**
   * The street number where the enterprise is located.
   */
  private String streetNumber;

  /**
   * The postal code where the enterprise is located.
   */
  private String postalCode;

  /**
   * The city where the enterprise is located.
   */
  private String city;

  /**
   * The country where the enterprise is located.
   */
  private String country;

  /**
   * The phone number of the enterprise.
   */
  private String phoneNumber;

  /**
   * The email of the enterprise.
   */
  private String email;

  /**
   * The blacklisted status of the enterprise.
   */
  private boolean isBlacklisted;

  /**
   * The reason for blacklisting the enterprise.
   */
  private String blacklistedReason;

  /**
   * The version number of the enterprise.
   */
  private int versionNumber;

  /**
   * Retrieves the unique identifier of the enterprise.
   *
   * @return The unique identifier of the enterprise.
   */
  @Override
  public int getIdEnterprise() {
    return idEnterprise;
  }

  /**
   * Sets the unique identifier of the enterprise.
   *
   * @param idEnterprise The unique identifier to set for the enterprise.
   */
  @Override
  public void setIdEnterprise(int idEnterprise) {
    this.idEnterprise = idEnterprise;
  }

  /**
   * Retrieves the trade name of the enterprise.
   *
   * @return The trade name of the enterprise.
   */
  @Override
  public String getTradeName() {
    return tradeName;
  }

  /**
   * Sets the trade name of the enterprise.
   *
   * @param tradeName The trade name to set for the enterprise.
   */
  @Override
  public void setTradeName(String tradeName) {
    this.tradeName = tradeName;
  }

  /**
   * Retrieves the designation of the enterprise.
   *
   * @return The designation of the enterprise.
   */
  @Override
  public String getDesignation() {
    return designation;
  }

  /**
   * Sets the designation of the enterprise.
   *
   * @param designation The designation to set for the enterprise.
   */
  @Override
  public void setDesignation(String designation) {
    this.designation = designation;
  }

  /**
   * Retrieves the street where the enterprise is located.
   *
   * @return The street where the enterprise is located.
   */
  @Override
  public String getStreet() {
    return street;
  }

  /**
   * Sets the street where the enterprise is located.
   *
   * @param street The street to set for the enterprise.
   */
  @Override
  public void setStreet(String street) {
    this.street = street;
  }

  /**
   * Retrieves the street number where the enterprise is located.
   *
   * @return The street number where the enterprise is located.
   */
  @Override
  public String getStreetNumber() {
    return streetNumber;
  }

  /**
   * Sets the street number where the enterprise is located.
   *
   * @param streetNumber The street number to set for the enterprise.
   */
  @Override
  public void setStreetNumber(String streetNumber) {
    this.streetNumber = streetNumber;
  }

  /**
   * Retrieves the postal code where the enterprise is located.
   *
   * @return The postal code where the enterprise is located.
   */
  @Override
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Sets the postal code where the enterprise is located.
   *
   * @param postalCode The postal code to set for the enterprise.
   */
  @Override
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * Retrieves the city where the enterprise is located.
   *
   * @return The city where the enterprise is located.
   */
  @Override
  public String getCity() {
    return city;
  }

  /**
   * Sets the city where the enterprise is located.
   *
   * @param city The city to set for the enterprise.
   */
  @Override
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Retrieves the country where the enterprise is located.
   *
   * @return The country where the enterprise is located.
   */
  @Override
  public String getCountry() {
    return country;
  }

  /**
   * Sets the country where the enterprise is located.
   *
   * @param country The country to set for the enterprise.
   */
  @Override
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Retrieves the phone number of the enterprise.
   *
   * @return The phone number of the enterprise.
   */
  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Sets the phone number of the enterprise.
   *
   * @param phoneNumber The phone number to set for the enterprise.
   */
  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Retrieves the email of the enterprise.
   *
   * @return The email of the enterprise.
   */
  @Override
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email of the enterprise.
   *
   * @param email The email to set for the enterprise.
   */
  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Retrieves the blacklisted status of the enterprise.
   *
   * @return The blacklisted status of the enterprise.
   */
  @Override
  @JsonProperty("isBlacklisted")
  public boolean isBlacklisted() {
    return isBlacklisted;
  }

  /**
   * Sets the blacklisted status of the enterprise.
   *
   * @param blacklisted The blacklisted status to set for the enterprise.
   */
  @Override
  public void setBlacklisted(boolean blacklisted) {
    this.isBlacklisted = blacklisted;
  }

  /**
   * Retrieves the reason for blacklisting the enterprise.
   *
   * @return The reason for blacklisting the enterprise.
   */
  @Override
  public String getBlacklistedReason() {
    return blacklistedReason;
  }

  /**
   * Sets the reason for blacklisting the enterprise.
   *
   * @param blacklistedReason The reason to set for blacklisting the enterprise.
   */
  @Override
  public void setBlacklistedReason(String blacklistedReason) {
    this.blacklistedReason = blacklistedReason;
  }

  /**
   * Get the version number.
   *
   * @return the version number
   */
  @Override
  public int getVersionNumber() {
    return versionNumber;

  }

  /**
   * Set the version number.
   *
   * @param versionNumber the version number
   */
  @Override
  public void setVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
  }
}

