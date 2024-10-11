package be.vinci.pae.dal.responsable;

import be.vinci.pae.business.DomainFactory;
import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.business.responsable.ResponsableStageDTO;
import be.vinci.pae.dal.DALBackServices;
import be.vinci.pae.dal.utils.Util;
import be.vinci.pae.exception.FatalException;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * ResponsableStageDAOImpl is a class that implements the ResponsableStageDAO interface. It provides
 * the implementation for the methods to get an internship supervisor by its unique identifier,
 * create a new internship supervisor, and get all the information of an internship supervisor. This
 * class is responsible for handling database operations related to internship supervisors.
 */
public class ResponsableStageDAOImpl implements ResponsableStageDAO {

  // Instance of DALBackServices for performing common DAL operations
  @Inject
  private DALBackServices dalBackServices;

  // Instance of DomainFactory for creating domain objects
  @Inject
  private DomainFactory domainFactory;


  /**
   * Returns the internship supervisor by its unique identifier. This method prepares a SQL
   * statement, executes it, and maps the result to a ResponsableStageDTO object. If no internship
   * supervisor is found, it returns null.
   *
   * @param id The unique identifier of the internship supervisor.
   * @return A ResponsableStageDTO representing the internship supervisor. If not, returns null.
   */
  @Override
  public ResponsableStageDTO getOne(int id) {
    String query =
        "SELECT * FROM pae.responsables_stages WHERE responsableid = ?;";
    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setInt(1, id);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          return (ResponsableStageDTO) Util.fillInfos(rs, domainFactory.getResponsableStage());
        }
        return null;
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  /**
   * Create a new internship supervisor in the database. This method prepares a SQL statement,
   * executes it, and maps the result to a ResponsableStageDTO object.
   *
   * @param responsableStageDTO The internship supervisor to create.
   * @return The created internship supervisor.
   */
  @Override
  public ResponsableStageDTO createResponsableStage(ResponsableStageDTO responsableStageDTO) {
    String query =
        "INSERT INTO pae.responsables_stages (lastname, firstname, phonenumber, email, enterprise) "
            + "VALUES (?, ?, ?, ?, ?) RETURNING responsableid;";
    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setString(1, responsableStageDTO.getLastName());
      ps.setString(2, responsableStageDTO.getFirstName());
      ps.setString(3, responsableStageDTO.getPhoneNumber());
      ps.setString(4, responsableStageDTO.getEmail());
      ps.setInt(5, responsableStageDTO.getEnterprise());
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          responsableStageDTO.setResponsableId(rs.getInt(1));
          return responsableStageDTO;
        }
        return null;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get all the information of an internship supervisor. This method prepares a SQL statement,
   * executes it, and maps the result to a ResponsableStageDTO object.
   *
   * @return The internship supervisor.
   */
  @Override
  public List<ResponsableStageDTO> getAll() {
    List<ResponsableStageDTO> responsableStageList = new ArrayList<>();
    String query = "SELECT RE.*, EN.* FROM pae.responsables_stages RE, pae.enterprises EN "
        + "WHERE RE.enterprise = EN.identerprise;";
    try (var ps = dalBackServices.getPreparedStatement(query)) {
      try (var rs = ps.executeQuery()) {
        while (rs.next()) {
          ResponsableStageDTO myResponsable = (ResponsableStageDTO) Util.fillInfos(rs,
              domainFactory.getResponsableStage());
          EnterpriseDTO myEnterprise = (EnterpriseDTO) Util.fillInfos(rs,
              domainFactory.getEnterprise());

          myResponsable.setEnterpriseDTO(myEnterprise);

          responsableStageList.add(myResponsable);
        }
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
    return responsableStageList;
  }

}
