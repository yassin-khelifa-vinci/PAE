package be.vinci.pae.utils;

// Importing necessary classes and interfaces

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
import be.vinci.pae.dal.DALBackServices;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.DALServicesImpl;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.dal.contact.ContactDAOImpl;
import be.vinci.pae.dal.enterprise.EnterpriseDAO;
import be.vinci.pae.dal.enterprise.EnterpriseDAOImpl;
import be.vinci.pae.dal.responsable.ResponsableStageDAO;
import be.vinci.pae.dal.responsable.ResponsableStageDAOImpl;
import be.vinci.pae.dal.stage.StageDAO;
import be.vinci.pae.dal.stage.StageDAOImpl;
import be.vinci.pae.dal.user.UserDAO;
import be.vinci.pae.dal.user.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * The ApplicationBinder class is responsible for binding interfaces to their implementations. It
 * extends the AbstractBinder class and is annotated with @Provider, which allows it to be
 * automatically discovered by the JAX-RS runtime. The bindings are configured in the configure
 * method.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  /**
   * The configure method is responsible for binding the interfaces to their implementations. The
   * DomainFactory interface is bound to the DomainFactoryImpl class. The UserDAO interface is bound
   * to the UserDAOImpl class. The UserUCC interface is bound to the UserUCCImpl class. The
   * DALBackServices interface is bound to the DALServicesImpl class. All bindings are in Singleton
   * scope, which means only one instance of each class will be created.
   */
  @Override
  protected void configure() {
    bind(DomainFactoryImpl.class).to(DomainFactory.class).in(Singleton.class);

    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);

    bind(DALServicesImpl.class).to(DALBackServices.class).to(DALServices.class).in(Singleton.class);

    bind(ContactDAOImpl.class).to(ContactDAO.class).in(Singleton.class);
    bind(ContactUCCImpl.class).to(ContactUCC.class).in(Singleton.class);

    bind(EnterpriseDAOImpl.class).to(EnterpriseDAO.class).in(Singleton.class);
    bind(EnterpriseUCCImpl.class).to(EnterpriseUCC.class).in(Singleton.class);

    bind(StageDAOImpl.class).to(StageDAO.class).in(Singleton.class);
    bind(StageUCCImpl.class).to(StageUCC.class).in(Singleton.class);

    bind(ResponsableStageDAOImpl.class).to(ResponsableStageDAO.class).in(Singleton.class);
    bind(ResponsableStageUCCImpl.class).to(ResponsableStageUCC.class).in(Singleton.class);
  }
}