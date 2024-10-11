package be.vinci.pae.presentation;

import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.contact.ContactDTO.Status;
import be.vinci.pae.business.contact.ContactUCC;
import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.business.user.UserDTO.Role;
import be.vinci.pae.exception.WrongBodyDataException;
import be.vinci.pae.presentation.filters.Authorize;
import be.vinci.pae.utils.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * This class provides the API endpoints for the authentication status. It includes methods for
 * changing the status of a request. The @Singleton annotation specifies that only one instance of
 * this class should be created. The @Path annotation specifies the base URI for the resource
 * class.
 */
@Singleton
@Path("/contact")
public class ContactResource {

  // The object mapper for JSON
  ObjectMapper objectMapper = new ObjectMapper();
  // The service for authentication status
  @Inject
  private ContactUCC myContactUCC;

  /**
   * This method provides the API endpoint for accepting a contact. It accepts a contact and returns
   * the updated contact. If the acceptance is not successful, it throws a WebApplicationException
   * with an appropriate response.
   *
   * @param json    the JSON object containing the acceptance data
   * @param request the request context
   * @param id      the id of the contact to be accepted
   * @return the updated contact
   */
  @POST
  @Path("/{id}/accepted")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public ContactDTO acceptContact(ObjectNode json, @Context ContainerRequest request,
      @PathParam("id") int id) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    final int idUser = userDTO.getIdUser();
    int versionNumber = -1;
    if (json.has("versionNumber")) {
      versionNumber = json.get("versionNumber").asInt();
    }
    if (versionNumber < 0) {
      throw new WrongBodyDataException("Invalid version number");
    }
    return myContactUCC.acceptContact(id, idUser, versionNumber);
  }

  /**
   * This method provides the API endpoint for denying a contact. It denies a contact and returns
   * the updated contact. If the denial is not successful, it throws a WebApplicationException with
   * an appropriate response.
   *
   * @param request the request context
   * @param id      the id of the contact to be denied
   * @param json    the JSON object containing the denial data
   * @return the updated contact
   */
  @POST
  @Path("/{id}/turnedDown")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public ContactDTO turnDownContact(ObjectNode json, @Context ContainerRequest request,
      @PathParam("id") int id) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    final int idUser = userDTO.getIdUser();
    String reasonRefusal = null;
    int versionNumber = -1;

    if (json.has("versionNumber")) {
      versionNumber = json.get("versionNumber").asInt();
    }

    if (versionNumber < 0) {
      throw new WrongBodyDataException("Invalid version number");
    }

    if (json.has("reasonRefusal")) {
      reasonRefusal = json.get("reasonRefusal").asText();
    }
    if (Util.checkEmptyString(reasonRefusal)) {
      throw new WrongBodyDataException("Reason for refusal is required");
    }

    return myContactUCC.turnDownContact(id, idUser, reasonRefusal, versionNumber);
  }

  /**
   * This method provides the API endpoint for admitting a contact. It admits a contact and returns
   * the updated contact. If the admission is not successful, it throws a WebApplicationException
   * with an appropriate response.
   *
   * @param request the request context
   * @param id      the id of the contact to be admitted
   * @param json    the JSON object containing the admission data
   * @return the updated contact
   */
  @POST
  @Path("/{id}/admitted")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public ContactDTO admitContact(ObjectNode json, @Context ContainerRequest request,
      @PathParam("id") int id) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    final int idUser = userDTO.getIdUser();
    String meetingPlace = null;
    int versionNumber = -1;
    if (json.has("meetingPlace")) {
      meetingPlace = json.get("meetingPlace").asText();
    }
    if (Util.checkEmptyString(meetingPlace)) {
      throw new WrongBodyDataException("Meeting place is required");
    }

    if (!meetingPlace.equals("A distance") && !meetingPlace.equals("Dans l'entreprise")) {
      throw new WrongBodyDataException("Invalid meeting place");
    }
    if (json.has("versionNumber")) {
      versionNumber = json.get("versionNumber").asInt();
    }

    if (versionNumber < 0) {
      throw new WrongBodyDataException("Invalid version number");
    }

    return myContactUCC.admitContact(id, idUser, meetingPlace, versionNumber);
  }

  /**
   * This method provides the API endpoint for unsupervising a contact. It unsupervises a contact
   * and returns the updated contact. If the unsupervision is not successful, it throws a
   * WebApplicationException with an appropriate response.
   *
   * @param request the request context
   * @param id      the id of the contact to be unsupervised
   * @param json    the JSON object containing the unsupervision data
   * @return the updated contact
   */
  @POST
  @Path("/{id}/unsupervised")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public ContactDTO unsupervisedContact(ObjectNode json, @Context ContainerRequest request,
      @PathParam("id") int id) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    final int idUser = userDTO.getIdUser();
    int versionNumber = -1;
    if (json.has("versionNumber")) {
      versionNumber = json.get("versionNumber").asInt();
    }

    if (versionNumber < 0) {
      throw new WrongBodyDataException("Invalid version number");
    }
    System.out.println("id : " + id + " idUser : " + idUser + " versionNumber : " + versionNumber);
    return myContactUCC.unsupervisedContact(id, idUser, versionNumber);
  }

  /**
   * This method provides the API endpoint for getting the contact list of a user. It returns the
   * contacts of a user. If the contacts are not found, it throws a WebApplicationException with an
   * appropriate response.
   *
   * @param request the request context
   * @return the contacts of the user
   */
  @GET
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public ObjectNode getMyContacts(@Context ContainerRequest request) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    int idUser = userDTO.getIdUser();

    var contactList = myContactUCC.getMyContacts(idUser);
    Map<String, Integer> map = new HashMap<>();

    for (Status status : Status.values()) {
      String statsString = "number_" + status.toString().toLowerCase();
      map.put(statsString,
          (int) contactList.stream()
              .filter(contactDTO -> contactDTO.getContactStatus().equals(status))
              .count());
    }

    ObjectNode result = objectMapper.createObjectNode();
    result.set("contacts", objectMapper.valueToTree(contactList));
    result.set("statusCount", objectMapper.valueToTree(map));

    return result;
  }

  /**
   * Creates a new contact using the provided JSON data.
   *
   * @param json    The JSON object containing data for the new contact.
   * @param request The ContainerRequest object representing the HTTP request context.
   * @return The JSON object representing the newly created contact.
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public ContactDTO createContact(ObjectNode json, @Context ContainerRequest request) {
    if (json.get("idEnterprise").isNull()) {
      throw new WrongBodyDataException("idEnterprise can't be null");
    }
    int idEnterprise = json.get("idEnterprise").asInt();
    UserDTO user = (UserDTO) request.getProperty("user");
    int idUser = user.getIdUser();
    if (idEnterprise < 1) {
      throw new WrongBodyDataException("Wrong enterprise id");
    }

    return myContactUCC.createContact(idUser, idEnterprise);
  }

  /**
   * This method provides the API endpoint for getting the contact list of a specific user. It
   * returns the contacts of a user. If the user id is less than 1, it throws a
   * WrongBodyDataException.
   *
   * @param id The id of the user whose contacts are to be fetched.
   * @return The list of contacts of the user.
   * @throws WrongBodyDataException if the user id is less than 1.
   */
  @GET
  @Path("/{id}/contact")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<ContactDTO> getUserInfo(@PathParam("id") int id) {
    if (id < 1) {
      throw new WrongBodyDataException("Wrong user id");
    }
    return myContactUCC.getUserContacts(id);
  }

  /**
   * This method provides the API endpoint for getting the contacts of a specific enterprise. It
   * returns a list of ContactDTO objects representing the contacts of the enterprise.
   *
   * @param id The ID of the enterprise for which to get the contacts.
   * @return A list of ContactDTO objects representing the contacts of the specified enterprise.
   * @throws WrongBodyDataException If the provided enterprise ID is under 1.
   */
  @GET
  @Path("/enterprise/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.TEACHER)
  public List<ContactDTO> getEnterpriseContacts(@PathParam("id") int id) {
    if (id < 1) {
      throw new WrongBodyDataException("Wrong enterprise id");
    }
    return myContactUCC.getEnterpriseContacts(id);
  }
}
