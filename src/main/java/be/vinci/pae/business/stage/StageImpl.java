package be.vinci.pae.business.stage;

import be.vinci.pae.business.responsable.ResponsableStageDTO;

/**
 * Stage interface. This interface represents a Stage in the system. It provides methods for getting
 * and setting the unique identifier, internship project, internship supervisor, contact, signature
 * date, and user associated with the stage.
 */
public class StageImpl implements StageDTO {

  /**
   * The unique identifier for the stage.
   */
  private int idStage;

  /**
   * The internship project associated with the stage.
   */
  private String internshipProject;

  /**
   * The internship supervisor associated with the stage.
   */
  private int internshipSupervisor;

  /**
   * The internship supervisor associated with the stage.
   */
  private ResponsableStageDTO internshipSupervisorDTO;

  /**
   * The contact associated with the stage.
   */
  private int contact;

  /**
   * The signature date of the stage.
   */
  private String signatureDate;

  /**
   * The user associated with the stage.
   */
  private int userId;

  private int versionNumber;

  /**
   * Retrieves the unique identifier of the stage.
   *
   * @return The unique identifier of the stage.
   */
  @Override
  public int getIdStage() {
    return idStage;
  }

  /**
   * Sets the unique identifier of the stage.
   *
   * @param idStage The unique identifier of the stage.
   */
  @Override
  public void setIdStage(int idStage) {
    this.idStage = idStage;
  }

  /**
   * Retrieves the internship project associated with the stage.
   *
   * @return The internship project associated with the stage.
   */
  @Override
  public String getInternshipProject() {
    return internshipProject;
  }

  /**
   * Sets the internship project associated with the stage.
   *
   * @param internshipProject The internship project to set for the stage.
   */
  @Override
  public void setInternshipProject(String internshipProject) {
    this.internshipProject = internshipProject;
  }

  /**
   * Retrieves the internship supervisor associated with the stage.
   *
   * @return The internship supervisor associated with the stage.
   */
  @Override
  public int getInternshipSupervisorId() {
    return internshipSupervisor;
  }

  /**
   * Sets the internship supervisor associated with the stage.
   *
   * @param internshipSupervisor The internship supervisor to set for the stage.
   */
  @Override
  public void setInternshipSupervisorId(int internshipSupervisor) {
    this.internshipSupervisor = internshipSupervisor;
  }

  /**
   * Retrieves the internship supervisor associated with the stage.
   *
   * @return The internship supervisor associated with the stage.
   */
  @Override
  public ResponsableStageDTO getInternshipSupervisorDTO() {
    return internshipSupervisorDTO;
  }

  /**
   * Sets the internship supervisor associated with the stage.
   *
   * @param internshipSupervisorDTO The internship supervisor to set for the stage.
   */
  @Override
  public void setInternshipSupervisorDTO(ResponsableStageDTO internshipSupervisorDTO) {
    this.internshipSupervisorDTO = internshipSupervisorDTO;
  }

  /**
   * Retrieves the contact associated with the stage.
   *
   * @return The contact associated with the stage.
   */
  @Override
  public int getContact() {
    return contact;
  }

  /**
   * Sets the contact associated with the stage.
   *
   * @param contact The contact to set for the stage.
   */
  @Override
  public void setContact(int contact) {
    this.contact = contact;
  }

  /**
   * Retrieves the signature date of the stage.
   *
   * @return The signature date of the stage.
   */
  @Override
  public String getSignatureDate() {
    return signatureDate;
  }

  /**
   * Sets the signature date of the stage.
   *
   * @param signatureDate The signature date to set for the stage.
   */
  @Override
  public void setSignatureDate(String signatureDate) {
    this.signatureDate = signatureDate;
  }

  /**
   * Retrieves the user associated with the stage.
   *
   * @return The user associated with the stage.
   */
  @Override
  public int getUserId() {
    return userId;
  }

  /**
   * Sets the user associated with the stage.
   *
   * @param userId The user to set for the stage.
   */
  @Override
  public void setUserId(int userId) {
    this.userId = userId;
  }


  /**
   * Gets the version number of the internship.
   *
   * @return the versionNumber number int.
   */
  @Override
  public int getVersionNumber() {
    return versionNumber;
  }

  /**
   * Sets the version number of the internship.
   *
   * @param versionNumber the new version number.
   */
  @Override
  public void setVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
  }
}
