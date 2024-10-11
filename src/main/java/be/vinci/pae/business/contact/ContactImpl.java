package be.vinci.pae.business.contact;

import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.business.user.UserDTO;

/**
 * Contact interface. This interface represents a Contact in the system. It provides methods for
 * getting and setting the unique identifier, user identifier, enterprise identifier, status,
 * location of the contact meeting, reason for refusing the contact, and academic year associated
 * with the contact.
 */
public class ContactImpl implements Contact {

  /**
   * The enterprise associated with the contact.
   */
  EnterpriseDTO enterpriseDTO;
  /**
   * The unique identifier for the contact.
   */
  private int idContact;
  /**
   * The user identifier associated with the contact.
   */
  private int userId;

  private UserDTO userDTO;
  /**
   * The enterprise identifier associated with the contact.
   */
  private int enterprise;
  /**
   * The status of the contact (e.g., accepted, pending, rejected).
   */
  private Status contactStatus;
  /**
   * The location where the contact meeting took place.
   */
  private String meetingPlace;
  /**
   * The reason for refusing the contact, if applicable.
   */
  private String reasonForRefusal;
  /**
   * The academic year associated with the contact.
   */
  private String schoolYear;

  /**
   * The version number of the contact.
   */
  private int versionNumber;

  /**
   * Retrieves the unique identifier of the contact.
   *
   * @return The unique identifier of the contact.
   */
  @Override
  public int getIdContact() {
    return idContact;
  }

  /**
   * Sets the unique identifier of the contact.
   *
   * @param idContact The unique identifier to set for the contact.
   */
  @Override
  public void setIdContact(int idContact) {
    this.idContact = idContact;
  }

  /**
   * Retrieves the user identifier associated with the contact.
   *
   * @return The user identifier associated with the contact.
   */
  @Override
  public int getUserId() {
    return userId;
  }

  /**
   * Sets the user identifier associated with the contact.
   *
   * @param userId The user identifier to set for the contact.
   */
  @Override
  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * Retrieves the enterprise identifier associated with the contact.
   *
   * @return The enterprise identifier associated with the contact.
   */
  @Override
  public int getEnterprise() {
    return enterprise;
  }

  /**
   * Sets the enterprise identifier associated with the contact.
   *
   * @param enterprise The enterprise identifier to set for the contact.
   */
  @Override
  public void setEnterprise(int enterprise) {
    this.enterprise = enterprise;
  }

  /**
   * Retrieves the version number of the contact.
   *
   * @return The version number of the contact.
   */
  @Override
  public int getVersionNumber() {
    return versionNumber;
  }

  /**
   * Sets the version number of the contact.
   *
   * @param versionNumber The version number to set for the contact.
   */
  @Override
  public void setVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
  }

  /**
   * Retrieves the status of the contact.
   *
   * @return The status of the contact (e.g., accepted, pending, rejected).
   */
  @Override
  public Status getContactStatus() {
    return contactStatus;
  }

  /**
   * Sets the status of the contact.
   *
   * @param contactStatus The status to set for the contact.
   */
  @Override
  public void setContactStatus(String contactStatus) {
    this.contactStatus = Status.valueOf(contactStatus);
  }

  /**
   * Retrieves the location where the contact meeting took place.
   *
   * @return The location where the contact meeting took place.
   */
  @Override
  public String getMeetingPlace() {
    return meetingPlace;
  }

  /**
   * Sets the location where the contact meeting took place.
   *
   * @param meetingPlace The location to set for the contact meeting.
   */
  @Override
  public void setMeetingPlace(String meetingPlace) {
    this.meetingPlace = meetingPlace;
  }

  /**
   * Retrieves the reason for refusing the contact, if applicable.
   *
   * @return The reason for refusing the contact.
   */
  @Override
  public String getReasonForRefusal() {
    return reasonForRefusal;
  }

  /**
   * Sets the reason for refusing the contact.
   *
   * @param reasonForRefusal The reason to set for refusing the contact.
   */
  @Override
  public void setReasonForRefusal(String reasonForRefusal) {
    this.reasonForRefusal = reasonForRefusal;
  }

  /**
   * Retrieves the academic year associated with the contact.
   *
   * @return The academic year associated with the contact.
   */
  @Override
  public String getSchoolYear() {
    return schoolYear;
  }

  /**
   * Sets the academic year associated with the contact.
   *
   * @param schoolYear The academic year to set for the contact.
   */
  @Override
  public void setSchoolYear(String schoolYear) {
    this.schoolYear = schoolYear;
  }

  /**
   * Retrieves the enterprise associated with the contact.
   *
   * @return The enterprise associated with the contact.
   */
  @Override
  public EnterpriseDTO getEnterpriseDTO() {
    return enterpriseDTO;
  }

  /**
   * Sets the enterprise associated with the contact.
   *
   * @param enterpriseDTO The enterprise to set for the contact.
   */
  @Override
  public void setEnterpriseDTO(EnterpriseDTO enterpriseDTO) {
    this.enterpriseDTO = enterpriseDTO;
  }

  /**
   * Retrieves the user associated with the contact.
   *
   * @return The user associated with the contact.
   */
  @Override
  public UserDTO getUserDTO() {
    return userDTO;
  }

  /**
   * Sets the user associated with the contact.
   *
   * @param userDTO The user to set for the contact.
   */
  @Override
  public void setUserDTO(UserDTO userDTO) {
    this.userDTO = userDTO;
  }

  /**
   * This method checks if the status change is valid.
   *
   * @param newStatus the new status
   * @param idUser    the id of the user
   * @return true if the status change is valid, false otherwise
   */
  public boolean verification(Status newStatus, int idUser) {

    if (this.getUserId() != idUser) {
      return false;
    }

    if (!this.checkStatus(newStatus)) {
      return false;
    }
    return true;
  }


  /**
   * This method checks if the status change is valid.
   *
   * @param newStatus the new status
   * @return true if the status change is valid, false otherwise
   */
  private boolean checkStatus(Status newStatus) {
    if (newStatus.equals(Status.ADMITTED) && this.contactStatus.equals(Status.STARTED)) {
      return true;
    }
    if (newStatus.equals(Status.UNSUPERVISED) && this.contactStatus.equals(Status.STARTED)) {
      return true;
    }
    if (newStatus.equals(Status.UNSUPERVISED) && this.contactStatus.equals(Status.ADMITTED)) {
      return true;
    }
    if (newStatus.equals(Status.TURNED_DOWN) && this.contactStatus.equals(Status.ADMITTED)) {
      return true;
    }
    if (newStatus.equals(Status.ACCEPTED) && this.contactStatus.equals(Status.ADMITTED)) {
      return true;
    }
    return false;
  }

}
