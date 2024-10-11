package be.vinci.pae.business.stage;

import java.util.Map;

/**
 * The StageUCC interface provides the structure for a Use Case Controller (UCC) that handles the
 * business operations related to Stage entities. It defines methods to get a stage by its id.
 */
public interface StageUCC {

  /**
   * This method is used to retrieve a stage by its id from the database.
   *
   * @param id The id of the stage to retrieve.
   * @return A Stage with the given id. If no stage is found, it returns null.
   */
  StageDTO getOne(int id);

  /**
   * This method is used to create a stage in the database.
   *
   * @param stageDTO The StageDTO object representing the stage to create.
   * @return A StageDTO object representing the created stage.
   */
  StageDTO createStage(StageDTO stageDTO);

  /**
   * Changes the internship project for a given user.
   *
   * @param idUser     The ID of the user whose internship project is to be changed.
   * @param text       The new text for the internship project.
   * @param numVersion The version number of the internship project.
   * @return The modified StageDTO object after changing the internship project.
   */
  StageDTO changeInternshipProject(int idUser, String text, int numVersion);

  /**
   * Retrieves a list of school years without a stage. This method is responsible for fetching the
   * list of school years without a stage from the database.
   *
   * @return A list of school years without a stage.
   */
  Map<String, Map<String, Integer>> getStatsStages();
}
