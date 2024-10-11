package be.vinci.pae.business.stage;

import be.vinci.pae.business.responsable.ResponsableStageDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * StageDTO interface. This interface represents a Stage in the system. It provides methods for
 * getting and setting the unique identifier, internship project, internship supervisor, contact,
 * signature date, and user associated with the stage.
 */
@JsonDeserialize(as = StageImpl.class)
public interface StageDTO {

  /**
   * Retrieves the unique identifier of the stage.
   *
   * @return The unique identifier of the stage.
   */
  int getIdStage();

  /**
   * Sets the unique identifier of the stage.
   *
   * @param idStage The unique identifier of the stage.
   */
  void setIdStage(int idStage);

  /**
   * Retrieves the internship project associated with the stage.
   *
   * @return The internship project associated with the stage.
   */
  String getInternshipProject();

  /**
   * Sets the internship project associated with the stage.
   *
   * @param internshipProject The internship project to set for the stage.
   */
  void setInternshipProject(String internshipProject);

  /**
   * Retrieves the internship supervisor associated with the stage.
   *
   * @return The internship supervisor associated with the stage.
   */
  int getInternshipSupervisorId();

  /**
   * Sets the internship supervisor associated with the stage.
   *
   * @param internshipSupervisor The internship supervisor to set for the stage.
   */
  void setInternshipSupervisorId(int internshipSupervisor);

  /**
   * Retrieves the internship supervisor associated with the stage.
   *
   * @return The internship supervisor associated with the stage.
   */
  ResponsableStageDTO getInternshipSupervisorDTO();

  /**
   * Sets the internship supervisor associated with the stage.
   *
   * @param internshipSupervisorDTO The internship supervisor to set for the stage.
   */
  void setInternshipSupervisorDTO(ResponsableStageDTO internshipSupervisorDTO);

  /**
   * Retrieves the internship start date associated with the stage.
   *
   * @return The internship start date associated with the stage.
   */
  int getContact();

  /**
   * Sets the internship start date associated with the stage.
   *
   * @param contact The internship start date to set for the stage.
   */
  void setContact(int contact);

  /**
   * Retrieves the internship end date associated with the stage.
   *
   * @return The internship end date associated with the stage.
   */
  String getSignatureDate();

  /**
   * Sets the internship end date associated with the stage.
   *
   * @param signatureDate The internship end date to set for the stage.
   */
  void setSignatureDate(String signatureDate);

  /**
   * Retrieves the user associated with the stage.
   *
   * @return The user associated with the stage.
   */
  int getUserId();

  /**
   * Sets the user associated with the stage.
   *
   * @param userId The user to set for the stage.
   */
  void setUserId(int userId);

  /**
   * Gets the version number of the internship.
   *
   * @return the version number as int.
   */
  int getVersionNumber();

  /**
   * Sets the version number of the internship.
   *
   * @param versionNumber the new version number.
   */
  void setVersionNumber(int versionNumber);
}
