package be.vinci.pae.utils;

import be.vinci.pae.business.DomainFactory;
import be.vinci.pae.business.DomainFactoryImpl;
import be.vinci.pae.business.contact.ContactUCC;
import be.vinci.pae.business.contact.ContactUCCImpl;
import be.vinci.pae.business.entreprise.EnterpriseUCC;
import be.vinci.pae.business.entreprise.EnterpriseUCCImpl;
import be.vinci.pae.business.responsable.ResponsableStageUCC;
import be.vinci.pae.business.responsable.ResponsableStageUCCImpl;
import be.vinci.pae.business.stage.StageUCC;
import be.vinci.pae.business.stage.StageUCCImpl;
import be.vinci.pae.business.user.UserUCC;
import be.vinci.pae.business.user.UserUCCImpl;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.dal.enterprise.EnterpriseDAO;
import be.vinci.pae.dal.responsable.ResponsableStageDAO;
import be.vinci.pae.dal.stage.StageDAO;
import be.vinci.pae.dal.user.UserDAO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

/**
 * This class extends the AbstractBinder class and is used to bind interfaces to their
 * implementations. It is annotated with @Provider, which means it will be automatically discovered
 * by the JAX-RS runtime. The bindings are configured in the configure method.
 */
@Provider
public class ApplicationBinderTest extends AbstractBinder {

  /**
   * Configures the bindings. The DomainFactory interface is bound to the DomainFactoryImpl class.
   * The UserDAO interface is bound to the UserDAOImpl class. Both bindings are in Singleton scope,
   * which means only one instance of each class will be created.
   */
  @Override
  protected void configure() {
    bind(DomainFactoryImpl.class).to(DomainFactory.class).in(Singleton.class);

    bind(Mockito.mock(DALServices.class)).to(DALServices.class);

    bind(Mockito.mock(UserDAO.class)).to(UserDAO.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);

    bind(EnterpriseUCCImpl.class).to(EnterpriseUCC.class).in(Singleton.class);
    bind(Mockito.mock(EnterpriseDAO.class)).to(EnterpriseDAO.class);

    bind(Mockito.mock(StageDAO.class)).to(StageDAO.class);
    bind(StageUCCImpl.class).to(StageUCC.class).in(Singleton.class);

    bind(Mockito.mock(ContactDAO.class)).to(ContactDAO.class);
    bind(ContactUCCImpl.class).to(ContactUCC.class).in(Singleton.class);

    bind(Mockito.mock(ResponsableStageDAO.class)).to(ResponsableStageDAO.class);
    bind(ResponsableStageUCCImpl.class).to(ResponsableStageUCC.class).in(Singleton.class);
  }
}