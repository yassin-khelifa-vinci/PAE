package be.vinci.pae.dal.enterprise;

import be.vinci.pae.business.DomainFactory;
import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.dal.DALBackServices;
import be.vinci.pae.dal.utils.Util;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.FatalException;
import be.vinci.pae.exception.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The EnterpriseDAOImpl class implements the EnterpriseDAO interface. It provides methods for
 * fetching an enterprise by its ID from the database. This class is responsible for interacting
 * with the database to retrieve enterprise data. It uses the DomainFactory and DALBackServices to
 * prepare and execute SQL queries.
 */
public class EnterpriseDAOImpl implements EnterpriseDAO {

  // Instance of DALBackServices for performing common DAL operations
  @Inject
  private DALBackServices dalBackServices;

  // Instance of DomainFactory for creating domain objects
  @Inject
  private DomainFactory domainFactory;

  /**
   * Fetches an enterprise from the database by its ID. This method prepares a SQL statement,
   * executes it, and maps the result to an EnterpriseDTO object. If no enterprise is found, it
   * returns null.
   *
   * @param id The ID of the enterprise to fetch.
   * @return An EnterpriseDTO object if an enterprise is found, null otherwise.
   * @throws RuntimeException if a SQLException occurs.
   */
  @Override
  public EnterpriseDTO getOneById(int id) {
    try (var ps = dalBackServices.getPreparedStatement(
        "SELECT * FROM pae.enterprises WHERE idEnterprise = ?")) {
      ps.setInt(1, id);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          EnterpriseDTO enterprise = domainFactory.getEnterprise();
          return (EnterpriseDTO) Util.fillInfos(rs, enterprise);
        }
        return null;
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  /**
   * Retrieves a list of all companies from the database.
   *
   * @return A list of EnterpriseDTO objects representing all companies in the database.
   */
  @Override
  public List<EnterpriseDTO> getAll() {
    List<EnterpriseDTO> companyList = new ArrayList<>();

    try (PreparedStatement companies =
        dalBackServices.getPreparedStatement("SELECT * FROM pae.enterprises")) {
      try (ResultSet rs = companies.executeQuery()) {
        while (rs.next()) {
          EnterpriseDTO company = domainFactory.getEnterprise();
          companyList.add((EnterpriseDTO) Util.fillInfos(rs, company));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return companyList;
  }

  /**
   * This method fetches an enterprise from the database by its email. It prepares a SQL statement,
   * executes it, and maps the result to an EnterpriseDTO object. If no enterprise is found, it
   * returns null.
   *
   * @param email The email of the enterprise to fetch.
   * @return An EnterpriseDTO object if an enterprise is found, null otherwise.
   * @throws FatalException if a SQLException occurs.
   */
  @Override
  public EnterpriseDTO getOneByEmail(String email) {
    try (PreparedStatement ps =
        dalBackServices.getPreparedStatement(
            "SELECT * FROM pae.enterprises WHERE LOWER(email) = LOWER(?)")) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return (EnterpriseDTO) Util.fillInfos(rs, domainFactory.getEnterprise());
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  /**
   * This method fetches an enterprise from the database by its trade name and designation. It
   * prepares a SQL statement, executes it, and maps the result to an EnterpriseDTO object. If no
   * enterprise is found, it returns null.
   *
   * @param tradeName   The trade name of the enterprise to fetch.
   * @param designation The designation of the enterprise to fetch.
   * @return An EnterpriseDTO object if an enterprise is found, null otherwise.
   * @throws FatalException if a SQLException occurs.
   */
  @Override
  public EnterpriseDTO getOneByTradeNameAndDesignation(String tradeName, String designation) {
    try (PreparedStatement companies =
        dalBackServices.getPreparedStatement(
            "SELECT * FROM pae.enterprises WHERE LOWER(tradeName) = LOWER(?) AND "
                + "(LOWER(designation) = LOWER(?) OR designation IS NULL AND ? IS NULL)")) {
      companies.setString(1, tradeName);
      companies.setString(2, designation);
      companies.setString(3, designation);
      try (ResultSet rs = companies.executeQuery()) {
        if (rs.next()) {
          return (EnterpriseDTO) Util.fillInfos(rs, domainFactory.getEnterprise());
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return null;
  }

  /**
   * This method creates a new enterprise in the database. It prepares a SQL statement, executes it,
   * and sets the ID of the newly created enterprise to the EnterpriseDTO object. If the enterprise
   * is not created successfully, it returns null.
   *
   * @param enterprise The EnterpriseDTO object containing the data of the new enterprise.
   * @return The EnterpriseDTO with the ID of the created enterprise if successful, null otherwise.
   * @throws FatalException if a SQLException occurs.
   */
  @Override
  public EnterpriseDTO createOne(EnterpriseDTO enterprise) {
    try (PreparedStatement ps = dalBackServices.getPreparedStatement(
        "INSERT INTO pae.enterprises (tradeName, designation, postalCode, city, email, "
            + "phoneNumber, street, streetNumber, country, versionNumber) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING idEnterprise")) {
      ps.setString(1, enterprise.getTradeName());
      ps.setString(2, enterprise.getDesignation());
      ps.setString(3, enterprise.getPostalCode());
      ps.setString(4, enterprise.getCity());
      ps.setString(5, enterprise.getEmail());
      ps.setString(6, enterprise.getPhoneNumber());
      ps.setString(7, enterprise.getStreet());
      ps.setString(8, enterprise.getStreetNumber());
      ps.setString(9, enterprise.getCountry());
      ps.setInt(10, enterprise.getVersionNumber());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          enterprise.setIdEnterprise(rs.getInt(1));
          return enterprise;
        }
      }
    } catch (SQLException ex) {
      throw new FatalException(ex);
    }
    return null;
  }

  /**
   * Blacklists an enterprise in the database.
   *
   * @param enterprise The enterprise to blacklist.
   * @return true if the enterprise was successfully blacklisted, false otherwise.
   */
  @Override
  public boolean updateOne(EnterpriseDTO enterprise) {
    String query = "UPDATE pae.enterprises SET tradename = ?, designation = ?, street = ?, "
        + "streetnumber = ?, postalcode = ?, city = ?, country = ?, phonenumber = ?, email = ?, "
        + "isblacklisted = ?, blacklistedreason = ?, versionnumber=? WHERE identerprise = ? "
        + "AND versionnumber = ?";

    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setString(1, enterprise.getTradeName());
      ps.setString(2, enterprise.getDesignation());
      ps.setString(3, enterprise.getStreet());
      ps.setString(4, enterprise.getStreetNumber());
      ps.setString(5, enterprise.getPostalCode());
      ps.setString(6, enterprise.getCity());
      ps.setString(7, enterprise.getCountry());
      ps.setString(8, enterprise.getPhoneNumber());
      ps.setString(9, enterprise.getEmail());
      ps.setBoolean(10, enterprise.isBlacklisted());
      ps.setString(11, enterprise.getBlacklistedReason());
      ps.setInt(12, enterprise.getVersionNumber());
      ps.setInt(13, enterprise.getIdEnterprise());
      ps.setInt(14, enterprise.getVersionNumber() - 1);
      boolean result = ps.executeUpdate() == 1;
      if (!result) {
        if (getOneById(enterprise.getIdEnterprise()) == null) {
          throw new ObjectNotFoundException("The enterprise does not exist");
        } else {
          throw new ConflictException("version number not updated");
        }
      }
      return true;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * This method retrieves statistical data related to enterprises from the database. The statistics
   * are represented as a Map where the key is an Integer representing the enterprise ID and the
   * value is another Map. The inner Map's key is a String representing the school year and the
   * value is an Integer representing the count of contacts for that enterprise in that school year.
   * The method prepares a SQL statement, executes it, and maps the result to a Map.
   *
   * @return a Map containing the statistics related to enterprises. The outer Map's key is the
   *     enterprise ID, and the value is another Map. The inner Map's key is the school year, and
   *     the value is the count of contacts for that enterprise in that school year.
   * @throws FatalException if a SQLException occurs.
   */
  @Override
  public Map<Integer, Map<String, Integer>> getStats() {
    Map<Integer, Map<String, Integer>> stats = new HashMap<>();
    String query = "SELECT e.idEnterprise, c.schoolYear, COUNT(c.*) FROM pae.enterprises e "
        + "JOIN pae.contacts c ON e.idEnterprise = c.enterprise WHERE c.contactStatus = 'ACCEPTED' "
        + "GROUP BY e.idEnterprise, c.schoolYear";
    try (var ps = dalBackServices.getPreparedStatement(query)) {
      try (var rs = ps.executeQuery()) {
        while (rs.next()) {
          if (!stats.containsKey(rs.getInt(1))) {
            stats.put(rs.getInt(1), new HashMap<>());
          }
          stats.get(rs.getInt(1)).put(rs.getString(2), rs.getInt(3));
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return stats;
  }
}