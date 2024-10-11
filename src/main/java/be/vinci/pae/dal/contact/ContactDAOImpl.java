package be.vinci.pae.dal.contact;

import be.vinci.pae.business.DomainFactory;
import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.dal.DALBackServices;
import be.vinci.pae.dal.utils.Util;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.FatalException;
import be.vinci.pae.exception.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The ContactDAOImpl class implements the ContactDAO interface. It provides methods for changing
 * the status of a request, retrieving a specific contact based on the provided id, and returning
 * all the contacts of a request. This class is responsible for interacting with the database to
 * retrieve and update contact data. It uses the DomainFactory and DALBackServices to prepare and
 * execute SQL queries.
 */
public class ContactDAOImpl implements ContactDAO {

  // Instance of DALBackServices for performing common DAL operations
  @Inject
  private DALBackServices dalBackServices;

  // Instance of DomainFactory for creating domain objects
  @Inject
  private DomainFactory domainFactory;

  /**
   * Changes the status of a request.
   *
   * @param contactDTO    The contact to update
   * @param versionNumber The version number of the contact
   * @return true if the status change is successful, false otherwise
   */
  @Override
  public boolean changeStatus(ContactDTO contactDTO, int versionNumber) {
    String query = "UPDATE pae.contacts SET userId = ?, enterprise = ?, contactStatus = ?, "
        + "meetingPlace = ?, reasonForRefusal = ?, schoolYear = ?, versionNumber = ? "
        + "WHERE idContact = ? AND versionNumber = ?";

    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setInt(1, contactDTO.getUserId());
      ps.setInt(2, contactDTO.getEnterprise());
      ps.setString(3, contactDTO.getContactStatus().toString());
      ps.setString(4, contactDTO.getMeetingPlace());
      ps.setString(5, contactDTO.getReasonForRefusal());
      ps.setString(6, contactDTO.getSchoolYear());
      ps.setInt(7, versionNumber + 1);
      ps.setInt(8, contactDTO.getIdContact());
      ps.setInt(9, versionNumber);
      boolean result = ps.executeUpdate() == 1;
      if (!result) {
        if (getContact(contactDTO.getIdContact()) == null) {
          throw new ObjectNotFoundException("Contact not found");
        } else {
          throw new ConflictException("Version number is not correct");
        }
      }
      return true;
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }


  /**
   * Changes the status of all contacts for a request.
   *
   * @param contactDTO The contact to update
   * @return true if the status change is successful, false otherwise
   */
  public boolean changeAllStatus(ContactDTO contactDTO) {
    String query = "UPDATE pae.contacts "
        + "SET contactStatus = 'ON_HOLD', versionNumber = versionNumber + 1 "
        + "WHERE userId = ? AND idContact != ? "
        + "AND (contactStatus = 'STARTED' OR contactStatus = 'ADMITTED')";
    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setInt(1, contactDTO.getUserId());
      ps.setInt(2, contactDTO.getIdContact());
      return ps.executeUpdate() == 1;
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }


  /**
   * Retrieves a specific contact based on the provided id. This method prepares a SQL statement,
   * executes it, and returns a ContactDTO object. If no contact is found, it returns null.
   *
   * @param id The id of the request.
   * @return a ContactDTO object with the given id. If no contact is found, it returns null.
   * @throws FatalException if a SQLException occurs.
   */
  @Override
  public ContactDTO getContact(int id) {
    try (var ps = dalBackServices.getPreparedStatement(
        "SELECT * FROM pae.contacts WHERE idContact = ?")) {
      ps.setInt(1, id);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          return (ContactDTO) Util.fillInfos(rs, domainFactory.getContact());
        }
        return null;
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }


  /**
   * Returns all the contacts of a request. This method prepares a SQL statement, executes it, and
   * returns a list of ContactDTO objects. If no contacts are found, it returns an empty list.
   *
   * @param id The id of the request.
   * @return a list of ContactDTO objects for the request.
   * @throws FatalException if a SQLException occurs.
   */
  @Override
  public List<ContactDTO> getContactsByUser(int id) {
    String query = "SELECT CO.*, EN.* "
        + "FROM pae.contacts CO, pae.enterprises EN "
        + "WHERE EN.idEnterprise = CO.enterprise AND CO.userId = ?";

    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setInt(1, id);

      try (var rs = ps.executeQuery()) {
        List<ContactDTO> contacts = new ArrayList<>();
        while (rs.next()) {
          EnterpriseDTO myEnterprise = (EnterpriseDTO) Util.fillInfos(rs,
              domainFactory.getEnterprise());
          ContactDTO contactDTO = (ContactDTO) Util.fillInfos(rs, domainFactory.getContact());
          contactDTO.setEnterpriseDTO(myEnterprise);
          contacts.add(contactDTO);
        }
        return contacts;
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  /**
   * Retrieves the contact associated with the specified user, enterprise IDs, and school year.
   *
   * @param idUser       The user's ID.
   * @param idEnterprise The enterprise's ID.
   * @param schoolYear   The school year.
   * @return The ContactDTO representing the contact, or null if it doesn't exist.
   */
  @Override
  public ContactDTO getContactByUserAndByEnterprise(int idUser, int idEnterprise,
      String schoolYear) {
    String query = "SELECT * "
        + "FROM pae.contacts c "
        + "WHERE c.enterprise = ? AND c.userId = ? AND c.schoolYear = ?";
    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setInt(1, idEnterprise);
      ps.setInt(2, idUser);
      ps.setString(3, schoolYear);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          return (ContactDTO) Util.fillInfos(rs, domainFactory.getContact());
        }
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
    return null;
  }

  /**
   * Returns all the contacts of an enterprise.
   *
   * @param id the id of the enterprise
   * @return a list of contacts for the enterprise
   */
  @Override
  public List<ContactDTO> getContactsByEnterprise(int id) {
    String query = "SELECT CO.*, U.*, EN.* "
        + "FROM pae.contacts CO, pae.enterprises EN, pae.users U "
        + "WHERE CO.enterprise= EN.identerprise "
        + " AND CO.userid=U.iduser "
        + " AND CO.enterprise = ?";

    try (var ps = dalBackServices.getPreparedStatement(query)) {
      ps.setInt(1, id);

      try (var rs = ps.executeQuery()) {
        List<ContactDTO> contacts = new ArrayList<>();
        while (rs.next()) {
          ContactDTO contactDTO = (ContactDTO) Util.fillInfos(rs, domainFactory.getContact());

          EnterpriseDTO myEnterprise = (EnterpriseDTO) Util.fillInfos(rs,
              domainFactory.getEnterprise());
          contactDTO.setEnterpriseDTO(myEnterprise);

          UserDTO myUser = (UserDTO) Util.fillInfos(rs, domainFactory.getUser());
          contactDTO.setUserDTO(myUser);

          contacts.add(contactDTO);
        }
        return contacts;
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  /**
   * Creates a new contact using the provided ContactDTO.
   *
   * @param contact The ContactDTO object containing new contact information.
   * @return The newly created ContactDTO object, or null if creation fails.
   */
  @Override
  public ContactDTO createOne(ContactDTO contact) {
    String query =
        "INSERT INTO pae.contacts (userId, enterprise, contactStatus, schoolYear, versionNumber) "
            + "VALUES (?, ?, ?, ?, ?) RETURNING idContact";
    try (PreparedStatement newContact = dalBackServices.getPreparedStatement(query)) {
      newContact.setInt(1, contact.getUserId());
      newContact.setInt(2, contact.getEnterprise());
      newContact.setString(3, contact.getContactStatus().toString());
      newContact.setString(4, contact.getSchoolYear());
      newContact.setInt(5, contact.getVersionNumber());
      try (var rs = newContact.executeQuery()) {
        if (rs.next()) {
          contact.setIdContact(rs.getInt(1));
          return contact;
        }
        return null;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Retrieves the accepted contact for the specified user.
   *
   * @param idUser The user's ID.
   * @return The ContactDTO representing the accepted contact, or null if none exists.
   */
  @Override
  public ContactDTO getContactAccepted(int idUser) {
    String query = "SELECT * FROM pae.contacts c "
        + "WHERE c.userId = ? AND c.contactStatus = 'ACCEPTED'";
    try (PreparedStatement contact = dalBackServices.getPreparedStatement(query)) {
      contact.setInt(1, idUser);
      try (var rs = contact.executeQuery()) {
        if (rs.next()) {
          return (ContactDTO) Util.fillInfos(rs, domainFactory.getContact());
        }
        return null;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

}