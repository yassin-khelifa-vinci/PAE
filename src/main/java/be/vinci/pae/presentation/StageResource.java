package be.vinci.pae.presentation;

import be.vinci.pae.business.stage.StageDTO;
import be.vinci.pae.business.stage.StageUCC;
import be.vinci.pae.business.user.UserDTO;
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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * This class provides the API endpoints for managing stages. It includes methods for retrieving the
 * last stage of a user. The @Singleton annotation specifies that only one instance of this class
 * should be created. The @Path annotation specifies the base URI for the resource class.
 */
@Singleton
@Path("/stages")
public class StageResource {

  // The service for managing stages
  @Inject
  private StageUCC myStageUCC;

  /**
   * This method provides the API endpoint for getting the last stage of a user. It returns the last
   * stage of a user. If the user has no stages, it returns null. If the user is not found, it
   * throws a WebApplicationException with an appropriate response.
   *
   * @param request the request context
   * @return the last stage of the user
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public StageDTO getLastStage(@Context ContainerRequest request) {
    // Retrieve the user from the request context
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    // Get the id of the user
    int id = userDTO.getIdUser();

    // Return the last stage of the user
    return myStageUCC.getOne(id);
  }

  /**
   * This method provides the API endpoint for getting a stage of a student by their id. It returns
   * the stage of a student. If the student has no stages, it returns null. If the student is not
   * found, it throws a WebApplicationException with an appropriate response.
   *
   * @param idEtudiant the id of the student
   * @return the stage of the student
   */
  @GET
  @Path("{id}/getstage")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize({Role.TEACHER, Role.ADMINISTRATIVE})
  public StageDTO getStage(@PathParam("id") int idEtudiant) {
    return myStageUCC.getOne(idEtudiant);
  }

  /**
   * Provides the API endpoint for creating a new stage. This method creates a new stage using the
   * provided StageDTO object. If the provided data is invalid or missing, it throws a
   * WrongBodyDataException.
   *
   * @param stageDTO The StageDTO object containing the new stage information.
   * @param request  The HTTP request context.
   * @return The newly created StageDTO object.
   * @throws WrongBodyDataException If the provided data is invalid or missing.
   */
  @POST
  @Path("create")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public StageDTO createStage(StageDTO stageDTO, @Context ContainerRequest request) {

    UserDTO userDTO = (UserDTO) request.getProperty("user");
    stageDTO.setUserId(userDTO.getIdUser());
    if (stageDTO.getUserId() < 1 || stageDTO.getInternshipSupervisorId() < 1
        || stageDTO.getContact() < 1 || Util.checkEmptyString(stageDTO.getSignatureDate())) {
      throw new WrongBodyDataException("Invalid data");
    }

    return myStageUCC.createStage(stageDTO);
  }

  /**
   * Provides the API for changing the internship project.
   *
   * @param json    The JSON representation of the new internship project.
   * @param request The HTTP request context.
   * @return The StageDTO object after changing the internship project.
   * @throws WrongBodyDataException If the provided JSON data is invalid or missing.
   */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public StageDTO changeInternshipProject(ObjectNode json, @Context ContainerRequest request) {
    if (json.get("internshipProject").isNull()) {
      throw new WrongBodyDataException("Internship project can't be null");
    }
    int numVersion = json.get("numVersion").asInt();
    if (numVersion < 1) {
      throw new WrongBodyDataException("Wrong numVersion");
    }
    UserDTO user = (UserDTO) request.getProperty("user");
    int idUser = user.getIdUser();
    String internshipProject = json.get("internshipProject").asText();
    return myStageUCC.changeInternshipProject(idUser, internshipProject, numVersion);
  }

  /**
   * Provides the API endpoint for getting statistics about stages. This method retrieves a list of
   * students without a stage and a map of stage statistics.
   *
   * @return A map with the key being the school year and the value being a map with "withStage" and
   *     "withoutStage" as keys and the number of stages as values.
   */
  @GET
  @Path("/stats")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.TEACHER)
  public Map<String, Map<String, Integer>> getStats() {
    return myStageUCC.getStatsStages();
  }

}