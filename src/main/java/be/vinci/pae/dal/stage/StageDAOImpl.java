package be.vinci.pae.dal.stage;

import be.vinci.pae.business.DomainFactory;
import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.business.responsable.ResponsableStageDTO;
import be.vinci.pae.business.stage.StageDTO;
import be.vinci.pae.dal.DALBackServices;
import be.vinci.pae.dal.utils.Util;
import be.vinci.pae.exception.FatalException;
import be.vinci.pae.exception.ObjectNotFoundException;
import be.vinci.pae.exception.WrongBodyDataException;
import jakarta.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The StageDAOImpl class implements the StageDAO interface. It provides methods for fetching a
 * stage by request ID from the database. This class is responsible for interacting with the
 * database to retrieve stage data. It uses the DomainFactory and DALBackServices to prepare and
 * execute SQL queries.
 */
public class StageDAOImpl implements StageDAO {

  // Instance of DALBackServices for performing common DAL operations
  @Inject
  private DALBackServices dalBackServices;

  // Instance of DomainFactory for creating domain objects
  @Inject
  private DomainFactory domainFactory;

  /**
   * Returns the stage of a request. This method prepares a SQL statement, executes it, and maps the
   * result to a StageDTO object. If no stage is found, it returns null.
   *
   * @param id The ID of the request.
   * @return A StageDTO object representing the stage of the request. If not, it returns null.
   * @throws FatalException if a SQLException occurs.
   */
  @Override
  public StageDTO getOne(int id) {
    String query =
        "SELECT S.*, R.*, E.* "
            + "FROM pae.stages S "
            + "JOIN pae.responsables_stages R ON R.responsableid = S.internshipSupervisor "
            + "JOIN pae.enterprises E ON E.identerprise = R.enterprise "
            + "WHERE S.userId = ?";
    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          StageDTO stage = (StageDTO) Util.fillInfos(rs, domainFactory.getStage());

          ResponsableStageDTO myIntershipSupervisor = (ResponsableStageDTO) Util.fillInfos(rs,
              domainFactory.getResponsableStage());

          EnterpriseDTO enterprise = (EnterpriseDTO) Util.fillInfos(rs,
              domainFactory.getEnterprise());
          myIntershipSupervisor.setEnterpriseDTO(enterprise);

          stage.setInternshipSupervisorDTO(myIntershipSupervisor);
          return stage;
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new FatalException(e);
    }
    return null;
  }

  /**
   * Creates a new stage.
   *
   * @param stage the stage to create
   * @return the created stage
   */
  @Override
  public StageDTO createStage(StageDTO stage) {
    String query =
        "INSERT INTO pae.stages (userid, internshipproject, internshipsupervisor, "
            + "contact, signaturedate, versionnumber)"
            + " VALUES (?, ?, ?, ?, ?, ?) RETURNING idstage;";
    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setInt(1, stage.getUserId());
      ps.setString(2, stage.getInternshipProject());
      ps.setInt(3, stage.getInternshipSupervisorId());
      ps.setInt(4, stage.getContact());
      ps.setString(5, stage.getSignatureDate());
      ps.setInt(6, 1);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          stage.setIdStage(rs.getInt("idstage"));
          return stage;
        }
        return null;
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  /**
   * Updates a single StageDTO object in the database.
   *
   * @param stageTemp The StageDTO object to be updated.
   * @return True if the update operation was successful, false otherwise.
   */
  @Override
  public boolean updateOne(StageDTO stageTemp) {
    String query = "UPDATE pae.stages SET userId = ?,"
        + " internshipProject = ?, internshipSupervisor = ?,"
        + " contact = ?, signatureDate = ?, versionNumber = ?"
        + " WHERE idstage = ? AND versionNumber = ?";

    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setInt(1, stageTemp.getUserId());
      ps.setString(2, stageTemp.getInternshipProject());
      ps.setInt(3, stageTemp.getInternshipSupervisorId());
      ps.setInt(4, stageTemp.getContact());
      ps.setString(5, stageTemp.getSignatureDate());
      ps.setInt(6, stageTemp.getVersionNumber());
      ps.setInt(7, stageTemp.getIdStage());
      ps.setInt(8, stageTemp.getVersionNumber() - 1);
      boolean result = ps.executeUpdate() == 1;
      if (!result) {
        if (getOne(stageTemp.getIdStage()) == null) {
          throw new ObjectNotFoundException("Internship does not exist");
        } else {
          throw new WrongBodyDataException("Wrong version number");
        }
      }
      return true;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Retrieves a list of students who do not have a stage. This method prepares and executes a SQL
   * query to select the school year of students who do not have a stage from the 'users' table. It
   * then iterates over the result set and for each row, it adds the school year to the
   * yearsWithoutStage list. If a database access error occurs, it throws a FatalException.
   *
   * @return A list of school years of students without a stage.
   * @throws FatalException if a database access error occurs.
   */
  public List<String> getStudentsWithoutStage() {
    List<String> yearsWithoutStage = new ArrayList<>();
    String query =
        "SELECT u.schoolYear, (SELECT MAX(c.schoolYear) FROM pae.contacts c "
            + "WHERE c.userId = u.idUser AND c.contactStatus = 'ACCEPTED') FROM pae.users u "
            + "WHERE u.role = 'STUDENT' "
            + "AND NOT EXISTS (SELECT 1 FROM pae.contacts c WHERE c.userId = u.idUser "
            + "AND c.contactStatus = 'ACCEPTED' AND c.schoolYear = u.schoolYear)";
    try (var ps = dalBackServices.getPreparedStatement(query)) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          yearsWithoutStage.add(rs.getString(1) + " " + rs.getString(2));
        }
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
    return yearsWithoutStage;
  }

  /**
   * Retrieves the number of stages per school year. This method prepares and executes a SQL query
   * to select the school year and the count of contacts with the status 'ACCEPTED' from the
   * 'contacts' table, grouped by the school year. It then iterates over the result set and for each
   * row, it adds a new entry to the stats map with the school year as the key and the count as the
   * value. If a database access error occurs, it throws a FatalException.
   *
   * @return A map with the school year as the key and the count of stages as the value.
   * @throws FatalException if a database access error occurs.
   */
  public Map<String, Integer> getNumberStagesBySchoolYear() {
    Map<String, Integer> stats = new HashMap<>();
    String query = "SELECT schoolYear, COUNT(*) FROM pae.contacts WHERE contactStatus = 'ACCEPTED' "
        + "GROUP BY schoolYear";
    try (var ps = dalBackServices.getPreparedStatement(query)) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          stats.put(rs.getString(1), rs.getInt(2));
        }
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
    return stats;
  }
}