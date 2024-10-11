package be.vinci.pae.presentation;

import be.vinci.pae.business.responsable.ResponsableStageDTO;
import be.vinci.pae.business.responsable.ResponsableStageUCC;
import be.vinci.pae.business.user.UserDTO.Role;
import be.vinci.pae.exception.WrongBodyDataException;
import be.vinci.pae.presentation.filters.Authorize;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * This class provides the API endpoints for the internship supervisor. It includes methods for
 * creating a new internship supervisor. The @Singleton annotation specifies that only one instance
 * of this class should be created. The @Path annotation specifies the base URI for the resource
 * class.
 */
@Singleton
@Path("/responsableStage")
public class ResponsableStageResource {

  // The service for internship supervisor
  @Inject
  private ResponsableStageUCC myResponsableStageUCC;

  /**
   * This method provides the API endpoint for creating a new internship supervisor. It accepts the
   * data for the new internship supervisor and returns the created internship supervisor. If the
   * creation is not successful, it throws a WebApplicationException with an appropriate response.
   *
   * @param responsableStageDTO the data for the new internship supervisor
   * @return the created internship supervisor
   */
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(Role.STUDENT)
  public ResponsableStageDTO createResponsableStage(ResponsableStageDTO responsableStageDTO) {

    if (responsableStageDTO.getLastName() == null || responsableStageDTO.getFirstName() == null
        || responsableStageDTO.getPhoneNumber() == null
        || responsableStageDTO.getEnterprise() < 1) {
      throw new WrongBodyDataException("Missing data in the request body");
    }

    return myResponsableStageUCC.createResponsableStage(responsableStageDTO);

  }

  /**
   * This method provides the API endpoint for getting an internship supervisor by its id. It
   * accepts the id of the internship supervisor and returns the internship supervisor. If the
   * internship supervisor is not found, it throws a WebApplicationException with an appropriate
   * response.
   *
   * @return the internship supervisor
   */
  @GET
  @Path("/all")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize({Role.STUDENT, Role.TEACHER})
  public List<ResponsableStageDTO> getResponsableStage() {
    return myResponsableStageUCC.getAllResponsableStage();
  }

}
