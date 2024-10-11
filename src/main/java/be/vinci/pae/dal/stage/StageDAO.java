package be.vinci.pae.dal.stage;

import be.vinci.pae.business.stage.StageDTO;
import java.util.List;
import java.util.Map;

/**
 * This interface provides the methods for the data access logic of the stage of a request.
 */
public interface StageDAO {

  /**
   * Returns the stage of a request.
   *
   * @param id the id of the request
   * @return the last stage of the request
   */
  StageDTO getOne(int id);

  /**
   * Creates a new stage.
   *
   * @param stage the stage to create
   * @return the created stage
   */
  StageDTO createStage(StageDTO stage);

  /**
   * Updates a single StageDTO object in the database.
   *
   * @param stageTemp  The StageDTO object to be updated.
   * @return True if the update operation was successful, false otherwise.
   */
  boolean updateOne(StageDTO stageTemp);


  /**
   * Retrieves the list of school years without a stage. This method is responsible for fetching the
   * list of school years without a stage from the database.
   *
   * @return A list of school years without a stage.
   */
  List<String> getStudentsWithoutStage();

  /**
   * Retrieves the number of stages per school year. This method is responsible for fetching the
   * count of stages for each school year from the database.
   *
   * @return A map with the school year as the key and the count of stages as the value.
   */
  Map<String, Integer> getNumberStagesBySchoolYear();
}
