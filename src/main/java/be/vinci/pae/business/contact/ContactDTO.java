package be.vinci.pae.business.contact;

import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.business.user.UserDTO;

/**
 * This interface represents a Contact in the system. It provides methods for getting and setting
 * the unique identifier, user identifier, enterprise identifier, status, location of the contact
 * meeting, reason for refusing the contact, and academic year associated with the contact.
 */
public interface ContactDTO {

  /**
   * Retrieves the unique identifier of the contact.
   *
   * @return The unique identifier of the contact.
   */
  int getIdContact();

  /**
   * Sets the unique identifier of the contact.
   *
   * @param idContact The unique identifier to set for the contact.
   */
  void setIdContact(int idContact);

  /**
   * Retrieves the user identifier associated with the contact.
   *
   * @return The user identifier associated with the contact.
   */
  int getUserId();

  /**
   * Sets the user identifier associated with the contact.
   *
   * @param userId The user identifier to set for the contact.
   */
  void setUserId(int userId);

  /**
   * Retrieves the enterprise identifier associated with the contact.
   *
   * @return The enterprise identifier associated with the contact.
   */
  int getEnterprise();

  /**
   * Sets the enterprise identifier associated with the contact.
   *
   * @param enterprise The enterprise identifier to set for the contact.
   */
  void setEnterprise(int enterprise);

  /**
   * Retrieves the status of the contact.
   *
   * @return The status of the contact (e.g., accepted, pending, rejected).
   */
  Status getContactStatus();

  /**
   * Sets the status of the contact.
   *
   * @param contactStatus The status to set for the contact.
   */
  void setContactStatus(String contactStatus);

  /**
   * Retrieves the location where the contact meeting took place.
   *
   * @return The location where the contact meeting took place.
   */
  String getMeetingPlace();

  /**
   * Sets the location where the contact meeting took place.
   *
   * @param meetingPlace The location to set for the contact meeting.
   */
  void setMeetingPlace(String meetingPlace);

  /**
   * Retrieves the reason for refusing the contact, if applicable.
   *
   * @return The reason for refusing the contact.
   */
  String getReasonForRefusal();

  /**
   * Sets the reason for refusing the contact.
   *
   * @param reasonForRefusal The reason to set for refusing the contact.
   */
  void setReasonForRefusal(String reasonForRefusal);

  /**
   * Retrieves the academic year associated with the contact.
   *
   * @return The academic year associated with the contact.
   */
  String getSchoolYear();

  /**
   * Sets the academic year associated with the contact.
   *
   * @param schoolYear The academic year to set for the contact.
   */
  void setSchoolYear(String schoolYear);

  /**
   * Retrieves the enterprise associated with the contact.
   *
   * @return The enterprise associated with the contact.
   */
  EnterpriseDTO getEnterpriseDTO();

  /**
   * Sets the enterprise associated with the contact.
   *
   * @param enterpriseDTO The enterprise to set for the contact.
   */
  void setEnterpriseDTO(EnterpriseDTO enterpriseDTO);

  /**
   * Retrieves the user associated with the contact.
   *
   * @return The user associated with the contact.
   */
  UserDTO getUserDTO();

  /**
   * Sets the user associated with the contact.
   *
   * @param userDTO The user to set for the contact.
   */
  void setUserDTO(UserDTO userDTO);

  /**
   * Retrieves the version number of the contact.
   *
   * @return The version number of the contact.
   */
  int getVersionNumber();

  /**
   * Sets the version number of the contact.
   *
   * @param versionNumber The version number to set for the contact.
   */
  void setVersionNumber(int versionNumber);

  /**
   * Status enum. This enum represents the possible statuses of a contact.
   */
  enum Status {

    /**
     * The contact has been started.
     */
    STARTED,
    /**
     * The contact has been admitted.
     */
    ADMITTED,
    /**
     * The contact has been suspended.
     */
    UNSUPERVISED,
    /**
     * The contact has been turned down.
     */
    TURNED_DOWN,
    /**
     * The contact has been accepted.
     */
    ACCEPTED,
    /**
     * The contact has been put on hold.
     */
    ON_HOLD;

  }

}