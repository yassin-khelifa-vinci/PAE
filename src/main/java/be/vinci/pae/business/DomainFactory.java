package be.vinci.pae.business;

import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.business.responsable.ResponsableStageDTO;
import be.vinci.pae.business.stage.StageDTO;
import be.vinci.pae.business.user.UserDTO;

/**
 * This interface provides a factory for creating domain objects. It includes a method for creating
 * User objects.
 */
public interface DomainFactory {

  /**
   * This method creates a new User object. The implementation of this method should initialize the
   * User object with default values.
   *
   * @return a new User object
   */
  UserDTO getUser();

  /**
   * This method creates a new Contact object. The implementation of this method should initialize
   * the Contact object with default values.
   *
   * @return a new Contact object
   */
  ContactDTO getContact();

  /**
   * This method creates a new Enterprise object. The implementation of this method should
   * initialize the Enterprise object with default values.
   *
   * @return a new Enterprise object
   */
  EnterpriseDTO getEnterprise();

  /**
   * This method creates a new Stage object. It overrides the getStage method from the DomainFactory
   * interface. The implementation of this method initializes the Stage object with a new instance
   * of StageImpl.
   *
   * @return a new Stage object
   */
  StageDTO getStage();

  /**
   * This method creates a new ResponsableStage object. It overrides the getResponsableStage method
   * from the DomainFactory interface. The implementation of this method initializes the
   * ResponsableStage object with a new instance of ResponsableStageImpl.
   *
   * @return a new ResponsableStage object
   */
  ResponsableStageDTO getResponsableStage();
}