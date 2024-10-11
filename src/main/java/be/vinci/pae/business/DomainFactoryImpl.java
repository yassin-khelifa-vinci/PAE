package be.vinci.pae.business;

import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.contact.ContactImpl;
import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.business.entreprise.EnterpriseImpl;
import be.vinci.pae.business.responsable.ResponsableStageDTO;
import be.vinci.pae.business.responsable.ResponsableStageImpl;
import be.vinci.pae.business.stage.StageDTO;
import be.vinci.pae.business.stage.StageImpl;
import be.vinci.pae.business.user.UserDTO;
import be.vinci.pae.business.user.UserImpl;

/**
 * This class implements the DomainFactory interface. It provides a concrete implementation for
 * creating domain objects.
 */
public class DomainFactoryImpl implements DomainFactory {

  /**
   * This method creates a new User object. It overrides the getUser method from the DomainFactory
   * interface. The implementation of this method initializes the User object with a new instance of
   * UserImpl.
   *
   * @return a new User object
   */
  @Override
  public UserDTO getUser() {
    return new UserImpl();
  }

  /**
   * This method creates a new Contact object. It overrides the getContact method from the
   * DomainFactory interface. The implementation of this method initializes the Contact object with
   * a new instance of ContactImpl.
   *
   * @return a new Contact object
   */
  @Override
  public ContactDTO getContact() {
    return new ContactImpl();
  }

  /**
   * This method creates a new Enterprise object. It overrides the getEnterprise method from the
   * DomainFactory interface. The implementation of this method initializes the Enterprise object
   * with a new instance of EnterpriseImpl.
   *
   * @return a new Enterprise object
   */
  @Override
  public EnterpriseDTO getEnterprise() {
    return new EnterpriseImpl();
  }

  /**
   * This method creates a new Stage object. It overrides the getStage method from the DomainFactory
   * interface. The implementation of this method initializes the Stage object with a new instance
   * of StageImpl.
   *
   * @return a new Stage object
   */
  @Override
  public StageDTO getStage() {
    return new StageImpl();
  }

  /**
   * This method creates a new ResponsableStage object. It overrides the getResponsableStage method
   * from the DomainFactory interface. The implementation of this method initializes the
   * ResponsableStage object with a new instance of ResponsableStageImpl.
   *
   * @return a new ResponsableStage object
   */
  @Override
  public ResponsableStageDTO getResponsableStage() {
    return new ResponsableStageImpl();
  }
}