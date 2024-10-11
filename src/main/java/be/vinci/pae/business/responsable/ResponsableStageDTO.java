package be.vinci.pae.business.responsable;

import be.vinci.pae.business.entreprise.EnterpriseDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * ResponsableStageDTO interface. This interface represents an internship supervisor in the system.
 * It provides methods for getting and setting the unique identifier, last name, first name, email,
 * phone number, enterprise identifier, and enterprise associated with the internship supervisor.
 */
@JsonDeserialize(as = ResponsableStageImpl.class)
public interface ResponsableStageDTO {

  /**
   * Returns the unique identifier of the internship supervisor.
   *
   * @return The unique identifier of the internship supervisor.
   */
  int getResponsableId();

  /**
   * Sets the unique identifier of the internship supervisor.
   *
   * @param responsableId The unique identifier of the internship supervisor.
   */
  void setResponsableId(int responsableId);

  /**
   * Returns the last name of the internship supervisor.
   *
   * @return The last name of the internship supervisor.
   */
  String getLastName();

  /**
   * Sets the last name of the internship supervisor.
   *
   * @param lastName The last name of the internship supervisor.
   */
  void setLastName(String lastName);

  /**
   * Returns the first name of the internship supervisor.
   *
   * @return The first name of the internship supervisor.
   */
  String getFirstName();

  /**
   * Sets the first name of the internship supervisor.
   *
   * @param firstName The first name of the internship supervisor.
   */
  void setFirstName(String firstName);

  /**
   * Returns the email of the internship supervisor.
   *
   * @return The email of the internship supervisor.
   */
  String getEmail();

  /**
   * Sets the email of the internship supervisor.
   *
   * @param email The email of the internship supervisor.
   */
  void setEmail(String email);

  /**
   * Returns the phone number of the internship supervisor.
   *
   * @return The phone number of the internship supervisor.
   */
  String getPhoneNumber();

  /**
   * Sets the phone number of the internship supervisor.
   *
   * @param phoneNumber The phone number of the internship supervisor.
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Returns the enterprise identifier of the internship supervisor.
   *
   * @return The enterprise identifier of the internship supervisor.
   */
  int getEnterprise();

  /**
   * Sets the enterprise identifier of the internship supervisor.
   *
   * @param enterprise The enterprise identifier of the internship supervisor.
   */
  void setEnterprise(int enterprise);

  /**
   * Returns the enterprise of the internship supervisor.
   *
   * @return The enterprise of the internship supervisor.
   */
  EnterpriseDTO getEnterpriseDTO();

  /**
   * Sets the enterprise of the internship supervisor.
   *
   * @param enterpriseDTO The enterprise of the internship supervisor.
   */
  void setEnterpriseDTO(EnterpriseDTO enterpriseDTO);
}
