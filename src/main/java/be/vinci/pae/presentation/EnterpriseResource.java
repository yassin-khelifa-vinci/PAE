package be.vinci.pae.presentation;

import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.business.entreprise.EnterpriseUCC;
import be.vinci.pae.business.user.UserDTO.Role;
import be.vinci.pae.exception.WrongBodyDataException;
import be.vinci.pae.presentation.filters.Authorize;
import be.vinci.pae.utils.Util;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides the API endpoints for the enterprise. It includes methods for getting the
 * information of an enterprise. The @Singleton annotation specifies that only one instance of this
 * class should be created. The @Path annotation specifies the base URI for the resource class.
 */
@Singleton
@Path("/enterprise")
public class EnterpriseResource {

  // The service for enterprise data
  @Inject
  private EnterpriseUCC enterpriseUCC;

  /**
   * This method provides the API endpoint for getting the information of an enterprise. It return
   * the information of an enterprise. If the id is not valid, it throws a WebApplicationException
   * with an appropriate response.
   *
   * @param id the id of the enterprise
   * @return the information of the enterprise
   */
  @GET
  @Path("/{id}/info")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public EnterpriseDTO getInformations(@PathParam("id") int id) {
    if (id <= 0) {
      throw new WebApplicationException("Invalid id must be positive", Response.Status.BAD_REQUEST);
    }
    return enterpriseUCC.getEnterpriseInfo(id);
  }

  /**
   * This method provides the API endpoint for getting all the enterprises. It returns all the
   * enterprises. If there are no enterprises, it returns an empty list.
   *
   * @return all the enterprises
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<EnterpriseDTO> getAll() {
    return enterpriseUCC.getAll();
  }

  /**
   * This method provides the API endpoint for adding a new enterprise. It requires the user to be
   * authenticated as a student. It validates the input data and throws a WrongBodyDataException if
   * any of the required fields are empty or if the email format is incorrect.
   *
   * @param enterprise the EnterpriseDTO object containing the data of the new enterprise
   * @return the EnterpriseDTO object of the newly added enterprise
   * @throws WrongBodyDataException if any of the required fields are empty or if the email format
   *                                is incorrect
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public EnterpriseDTO addEnterprise(EnterpriseDTO enterprise) {
    // Check if any of the required fields are empty
    if (Util.checkEmptyString(enterprise.getTradeName()) || Util.checkEmptyString(
        enterprise.getCity())
        || Util.checkEmptyString(enterprise.getStreet()) || Util.checkEmptyString(
        enterprise.getPostalCode())
        || Util.checkEmptyString(enterprise.getCountry()) || Util.checkEmptyString(
        enterprise.getStreetNumber())) {
      throw new WrongBodyDataException("All required fields must be filled");
    }
    // Check if the designation or phone number fields are empty
    if (enterprise.getDesignation() != null && Util.checkEmptyString(enterprise.getDesignation())
        || enterprise.getPhoneNumber() != null && Util.checkEmptyString(
        enterprise.getPhoneNumber())) {
      throw new WrongBodyDataException("Designation or phoneNumber cannot be empty");
    }

    if (enterprise.getEmail() != null) {
      // Validate the email format
      String emailRegex = "^[A-Za-z0-9_.+-]+@[A-Za-z0-9-]+\\.[A-Za-z0-9-.]+[A-Za-z]+$";
      Pattern pattern = Pattern.compile(emailRegex);
      Matcher matcher = pattern.matcher(enterprise.getEmail());
      if (!matcher.matches()) {
        throw new WrongBodyDataException("Wrong email format");
      }
    }
    // Add the new enterprise and return the EnterpriseDTO object of the newly added enterprise
    return enterpriseUCC.addEnterprise(enterprise);
  }

  /**
   * This method provides the API endpoint for blacklisting an enterprise. It requires the user to
   * be authenticated as a teacher. If the blacklist reason is empty or the version number is not
   * valid, it throws a WrongBodyDataException.
   *
   * @param json         the JSON object containing the blacklist reason and the version number
   * @param idEnterprise the id of the enterprise to be blacklisted
   * @return the updated EnterpriseDTO after the blacklisting operation
   * @throws WrongBodyDataException if the blacklist reason is empty or the version number is not
   *                                valid
   */
  @POST
  @Path("/{id}/blacklist")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.TEACHER)
  public EnterpriseDTO blacklistEnterprise(ObjectNode json, @PathParam("id") int idEnterprise) {
    // Check if the JSON object is null
    if (json.get("blacklistReason") == null || json.get("versionNumber") == null) {
      throw new WrongBodyDataException("Blacklist reason and version number are required");
    }

    // Extract the blacklist reason and the version number from the JSON object
    String blacklistReason = json.get("blacklistReason").asText();
    int versionNumber = json.get("versionNumber").asInt();

    // Check if the blacklist reason is empty
    if (Util.checkEmptyString(blacklistReason)) {
      throw new WrongBodyDataException("Blacklist reason cannot be empty");
    }
    // Check if the version number is not valid
    if (versionNumber < 0 || versionNumber == 0) {
      throw new WrongBodyDataException("Invalid version number");
    }

    return enterpriseUCC.blacklist(idEnterprise, blacklistReason, versionNumber);
  }

  /**
   * This method provides the API endpoint for getting the statistics related to enterprises. It
   * requires the user to be authenticated as a teacher. The statistics are represented as a Map
   * where the key is an Integer and the value is another Map. The inner Map's key is a String and
   * the value is an Integer. The method delegates the call to the enterpriseUCC's getStats method.
   *
   * @return a Map containing the statistics related to enterprises
   */
  @GET
  @Path("/stats")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.TEACHER)
  public Map<Integer, Map<String, Integer>> getStats() {
    return enterpriseUCC.getStats();
  }
}
