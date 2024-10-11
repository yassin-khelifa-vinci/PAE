package be.vinci.pae.business.entreprise;

import be.vinci.pae.exception.ConflictException;
import java.util.List;
import java.util.Map;

/**
 * EnterpriseUCC interface.
 */
public interface EnterpriseUCC {

  /**
   * Get the enterprise information.
   *
   * @param id The id of the enterprise
   * @return EnterpriseDTO The enterprise information
   */
  EnterpriseDTO getEnterpriseInfo(int id);

  /**
   * Retrieves a list of all companies.
   *
   * @return A list of all enterprises.
   */
  List<EnterpriseDTO> getAll();

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
  EnterpriseDTO addEnterprise(EnterpriseDTO enterprise);

  /**
   * Blacklist an enterprise.
   *
   * @param idEnterprise    the id of the enterprise
   * @param blacklistReason the reason for blacklisting the enterprise
   * @param versionNumber   the version number of the enterprise
   * @return the enterprise information
   */
  EnterpriseDTO blacklist(int idEnterprise, String blacklistReason, int versionNumber);

  /**
   * Get the statistics related to enterprises.
   *
   * @return a Map containing the statistics related to enterprises
   */
  Map<Integer, Map<String, Integer>> getStats();
}
