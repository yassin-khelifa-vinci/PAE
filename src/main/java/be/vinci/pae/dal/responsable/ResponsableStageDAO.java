package be.vinci.pae.dal.responsable;

import be.vinci.pae.business.responsable.ResponsableStageDTO;
import java.util.List;

/**
 * ResponsableStageDAO is an interface that defines the methods that must be implemented by a
 * database access object (DAO) responsible for managing internship supervisors. It provides
 * operations to create, retrieve, and manipulate internship supervisors in the database.
 */
public interface ResponsableStageDAO {

  /**
   * Returns the internship supervisor by its unique identifier. This method prepares a SQL
   * statement, executes it, and maps the result to a ResponsableStageDTO object. If no internship
   * supervisor is found, it returns null.
   *
   * @param id The unique identifier of the internship supervisor.
   * @return A ResponsableStageDTO representing the internship supervisor. If not,returns null.
   */
  ResponsableStageDTO getOne(int id);

  /**
   * Create a new internship supervisor in the database. This method prepares a SQL statement,
   * executes it, and maps the result to a ResponsableStageDTO object.
   *
   * @param responsableStageDTO The internship supervisor to create.
   * @return The created internship supervisor.
   */
  ResponsableStageDTO createResponsableStage(ResponsableStageDTO responsableStageDTO);

  /**
   * Get all the information of an internship supervisor. This method prepares a SQL statement,
   * executes it, and maps the result to a ResponsableStageDTO object.
   *
   * @return The internship supervisor.
   */
  List<ResponsableStageDTO> getAll();
}
