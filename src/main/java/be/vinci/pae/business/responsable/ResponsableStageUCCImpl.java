package be.vinci.pae.business.responsable;

import be.vinci.pae.business.DomainFactory;
import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.enterprise.EnterpriseDAO;
import be.vinci.pae.dal.responsable.ResponsableStageDAO;
import be.vinci.pae.exception.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * ResponsableStageUCCImpl is a class that implements the ResponsableStageUCC interface. It provides
 * the implementation for the method to create a new internship supervisor and get all the
 * information of an internship supervisor. This class is responsible for handling business logic
 * related to internship supervisor operations. It interacts with the Data Access Layer (DAL) to
 * retrieve and manipulate internship supervisor data.
 */
public class ResponsableStageUCCImpl implements ResponsableStageUCC {

  // Instance of ResponsableStageDAO for interacting with the database
  @Inject
  ResponsableStageDAO responsableStageDAO;

  // Instance of EnterpriseDAO for interacting with the database
  @Inject
  EnterpriseDAO enterpriseDAO;

  // Instance of DALServices for performing common DAL operations
  @Inject
  DALServices dalServices;

  // Instance of DomainFactory for creating domain objects
  @Inject
  DomainFactory domainFactory;

  /**
   * Create a new internship supervisor.
   *
   * @param responsableStageDTO the new internship supervisor
   * @return the new internship supervisor
   */
  @Override
  public ResponsableStageDTO createResponsableStage(ResponsableStageDTO responsableStageDTO) {
    try {
      dalServices.startTransaction();
      EnterpriseDTO enterpriseDTO = enterpriseDAO.getOneById(responsableStageDTO.getEnterprise());
      if (enterpriseDTO == null) {
        throw new ObjectNotFoundException("wrong enterprise id");
      }
      responsableStageDTO.setEnterpriseDTO(enterpriseDTO);

      ResponsableStageDTO newResponsableStage = responsableStageDAO.createResponsableStage(
          responsableStageDTO);

      dalServices.commit();
      return newResponsableStage;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Get all the information of an internship supervisor.
   *
   * @return the internship supervisor
   */
  @Override
  public List<ResponsableStageDTO> getAllResponsableStage() {
    try {
      dalServices.open();

      return responsableStageDAO.getAll();
    } finally {
      dalServices.close();
    }
  }
}
