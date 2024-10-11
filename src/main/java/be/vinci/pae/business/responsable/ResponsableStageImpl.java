package be.vinci.pae.business.responsable;

import be.vinci.pae.business.entreprise.EnterpriseDTO;

/**
 * ResponsableStageDTO interface. This interface represents an internship supervisor in the system.
 * It provides methods for getting and setting the unique identifier, last name, first name, email,
 * phone number, enterprise identifier, and enterprise associated with the internship supervisor.
 */
public class ResponsableStageImpl implements ResponsableStageDTO {

  /**
   * The unique identifier for the internship supervisor.
   */
  private int responsableId;

  /**
   * The last name of the internship supervisor.
   */
  private String lastName;

  /**
   * The first name of the internship supervisor.
   */
  private String firstName;

  /**
   * The email of the internship supervisor.
   */
  private String email;

  /**
   * The phone number of the internship supervisor.
   */
  private String phoneNumber;

  /**
   * The enterprise identifier of the internship supervisor.
   */
  private int enterprise;

  /**
   * The enterprise of the internship supervisor.
   */
  private EnterpriseDTO enterpriseDTO;

  /**
   * Returns the unique identifier of the internship supervisor.
   *
   * @return The unique identifier of the internship supervisor.
   */
  @Override
  public int getResponsableId() {
    return responsableId;
  }

  /**
   * Sets the unique identifier of the internship supervisor.
   *
   * @param responsableId The unique identifier of the internship supervisor.
   */
  @Override
  public void setResponsableId(int responsableId) {
    this.responsableId = responsableId;
  }

  /**
   * Returns the last name of the internship supervisor.
   *
   * @return The last name of the internship supervisor.
   */
  @Override
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of the internship supervisor.
   *
   * @param lastName The last name of the internship supervisor.
   */
  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Returns the first name of the internship supervisor.
   *
   * @return The first name of the internship supervisor.
   */
  @Override
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of the internship supervisor.
   *
   * @param firstName The first name of the internship supervisor.
   */
  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Returns the email of the internship supervisor.
   *
   * @return The email of the internship supervisor.
   */
  @Override
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email of the internship supervisor.
   *
   * @param email The email of the internship supervisor.
   */
  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the phone number of the internship supervisor.
   *
   * @return The phone number of the internship supervisor.
   */
  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Sets the phone number of the internship supervisor.
   *
   * @param phoneNumber The phone number of the internship supervisor.
   */
  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Returns the enterprise identifier of the internship supervisor.
   *
   * @return The enterprise identifier of the internship supervisor.
   */
  @Override
  public int getEnterprise() {
    return enterprise;
  }

  /**
   * Sets the enterprise identifier of the internship supervisor.
   *
   * @param enterprise The enterprise identifier of the internship supervisor.
   */
  @Override
  public void setEnterprise(int enterprise) {
    this.enterprise = enterprise;
  }

  /**
   * Returns the enterprise of the internship supervisor.
   *
   * @return The enterprise of the internship supervisor.
   */
  @Override
  public EnterpriseDTO getEnterpriseDTO() {
    return enterpriseDTO;
  }

  /**
   * Sets the enterprise of the internship supervisor.
   *
   * @param enterpriseDTO The enterprise of the internship supervisor.
   */
  @Override
  public void setEnterpriseDTO(EnterpriseDTO enterpriseDTO) {
    this.enterpriseDTO = enterpriseDTO;
  }
}
