package be.vinci.pae.dal.enterprise;

import be.vinci.pae.business.entreprise.EnterpriseDTO;
import java.util.List;
import java.util.Map;

/**
 * The EnterpriseDAO interface provides the structure for a Data Access Object (DAO) that handles
 * the data operations related to Enterprise entities. It defines methods to retrieve the enterprise
 * information.
 */
public interface EnterpriseDAO {

  /**
   * Get the enterprise information.
   *
   * @param id the id of the enterprise
   * @return the enterprise information
   */
  EnterpriseDTO getOneById(int id);

  /**
   * Retrieves a list of all companies from the database.
   *
   * @return A list of EnterpriseDTO objects representing all companies in the database.
   */
  List<EnterpriseDTO> getAll();

  /**
   * Fetches an enterprise from the database by its email.
   *
   * @param email The email of the enterprise to fetch.
   * @return An EnterpriseDTO object if an enterprise is found, null otherwise.
   */
  EnterpriseDTO getOneByEmail(String email);

  /**
   * Fetches an enterprise from the database by its trade name and designation.
   *
   * @param tradeName   The trade name of the enterprise to fetch.
   * @param designation The designation of the enterprise to fetch.
   * @return An EnterpriseDTO object if an enterprise is found, null otherwise.
   */
  EnterpriseDTO getOneByTradeNameAndDesignation(String tradeName, String designation);

  /**
   * Creates a new enterprise in the database.
   *
   * @param enterprise The EnterpriseDTO object containing the data of the new enterprise.
   * @return The EnterpriseDTO object with the ID of the newly created enterprise if successful,
   *     null otherwise.
   */
  EnterpriseDTO createOne(EnterpriseDTO enterprise);

  /**
   * Blacklists an enterprise in the database.
   *
   * @param enterprise The enterprise to blacklist.
   * @return true if the enterprise was successfully blacklisted, false otherwise.
   */
  boolean updateOne(EnterpriseDTO enterprise);

  /**
   * Get the statistics of the enterprises.
   *
   * @return the statistics of the enterprises
   */
  Map<Integer, Map<String, Integer>> getStats();
}
