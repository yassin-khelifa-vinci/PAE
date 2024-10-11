package be.vinci.pae.business.entreprise;

import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.enterprise.EnterpriseDAO;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * EnterpriseUCCImpl is a class that implements the EnterpriseUCC interface. It provides the
 * implementation for the method to get the enterprise information. This class is responsible for
 * handling business logic related to Enterprise operations. It interacts with the Data Access Layer
 * (DAL) to retrieve and manipulate Enterprise data.
 */
public class EnterpriseUCCImpl implements EnterpriseUCC {

  /**
   * Instance of EnterpriseDAO for interacting with the database. Injected by the dependency
   * injection framework.
   */
  @Inject
  private EnterpriseDAO myEnterpriseDAO;

  /**
   * Instance of DALServices for performing operations related to the data access layer. Injected by
   * the dependency injection framework.
   */
  @Inject
  private DALServices dalServices;

  /**
   * Get the enterprise information.
   *
   * @param id the id of the enterprise
   * @return the enterprise information
   */
  @Override
  public EnterpriseDTO getEnterpriseInfo(int id) {
    // Call the getOneById method of the EnterpriseDAO to retrieve the enterprise information
    try {
      dalServices.open();
      return myEnterpriseDAO.getOneById(id);
    } finally {
      dalServices.close();
    }
  }

  /**
   * Get all enterprises.
   *
   * @return a list of all enterprises
   */
  @Override
  public List<EnterpriseDTO> getAll() {
    try {
      // Open a new session with the database
      dalServices.open();

      // Retrieve all enterprises from the database
      return myEnterpriseDAO.getAll();
    } finally {
      // Close the session with the database
      dalServices.close();
    }
  }

  /**
   * Adds a new enterprise to the database. This method starts a transaction and checks if the
   * enterprise's email and trade name with designation already exist in the database. If they do,
   * it throws a ConflictException. If the enterprise's email and trade name with designation are
   * unique, it sets the version number of the enterprise to 1, adds the enterprise to the database,
   * and commits the transaction. If an exception occurs during the process, it rolls back the
   * transaction and rethrows the exception.
   *
   * @param enterprise The EnterpriseDTO object containing the data of the new enterprise.
   * @return The EnterpriseDTO object with the ID of the newly created enterprise if successful.
   * @throws ConflictException if the enterprise's email or trade name and designation already exist
   *                           in the database.
   */
  @Override
  public EnterpriseDTO addEnterprise(EnterpriseDTO enterprise) {
    try {
      dalServices.startTransaction();
      if (enterprise.getEmail() != null
          && myEnterpriseDAO.getOneByEmail(enterprise.getEmail()) != null) {
        throw new ConflictException("Email already exists");
      }
      if (myEnterpriseDAO.getOneByTradeNameAndDesignation(enterprise.getTradeName(),
          enterprise.getDesignation()) != null) {
        throw new ConflictException("Trade name and designation already exists");
      }
      enterprise.setVersionNumber(1);
      myEnterpriseDAO.createOne(enterprise);
      dalServices.commit();
      return enterprise;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Blacklist an enterprise.
   *
   * @param idEnterprise    the id of the enterprise
   * @param blacklistReason the reason for blacklisting the enterprise
   * @param versionNumber   the version number of the enterprise
   * @return the enterprise information
   */
  @Override
  public EnterpriseDTO blacklist(int idEnterprise, String blacklistReason, int versionNumber) {
    // Start a transaction
    try {
      dalServices.startTransaction();
      EnterpriseDTO enterpriseTemp = myEnterpriseDAO.getOneById(idEnterprise);

      //Check if the enterprise exists in the database
      if (enterpriseTemp == null) {
        throw new ObjectNotFoundException("The enterprise does not exist");
      }
      //Check if the enterprise is already blacklisted
      if (enterpriseTemp.isBlacklisted()) {
        throw new ConflictException("The enterprise is already blacklisted");
      }

      enterpriseTemp.setBlacklisted(true);
      enterpriseTemp.setBlacklistedReason(blacklistReason);
      enterpriseTemp.setVersionNumber(versionNumber + 1);

      myEnterpriseDAO.updateOne(enterpriseTemp);

      dalServices.commit();
      return enterpriseTemp;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Retrieves statistical data related to enterprises from the database. This method opens a
   * connection to the database, retrieves the statistics, and then closes the connection.
   *
   * @return a Map containing the statistics related to enterprises. The outer Map's key is the
   *     enterprise ID, and the value is another Map. The inner Map's key is the school year, and
   *     the value is the count of contacts for that enterprise in that school year.
   */
  @Override
  public Map<Integer, Map<String, Integer>> getStats() {
    try {
      dalServices.open();
      return myEnterpriseDAO.getStats();
    } finally {
      dalServices.close();
    }
  }

}