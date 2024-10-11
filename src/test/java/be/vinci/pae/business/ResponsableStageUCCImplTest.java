package be.vinci.pae.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.business.responsable.ResponsableStageDTO;
import be.vinci.pae.business.responsable.ResponsableStageUCC;
import be.vinci.pae.dal.enterprise.EnterpriseDAO;
import be.vinci.pae.dal.responsable.ResponsableStageDAO;
import be.vinci.pae.exception.ObjectNotFoundException;
import be.vinci.pae.utils.ApplicationBinderTest;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class for ResponsableStageUCCImpl.
 */
public class ResponsableStageUCCImplTest {

  // ServiceLocator instance for testing
  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());

  // Instance of ResponsableStageUCC for testing
  private final DomainFactory domainFactory = locator.getService(DomainFactory.class);
  // Mocked ResponsableStageDAO for testing
  private final ResponsableStageDAO myResponsableStageDAO = locator.getService(
      ResponsableStageDAO.class);

  // Mocked EnterpriseDAO for testing
  private final EnterpriseDAO myEnterpriseDAO = locator.getService(EnterpriseDAO.class);
  private final ResponsableStageUCC responsableStageUCC = locator.getService(
      ResponsableStageUCC.class);
  // Instance of ResponsableStageUCC for testing
  private ResponsableStageDTO responsableStageDTO;
  // Instance of EnterpriseDTO for testing
  private EnterpriseDTO enterpriseDTO;

  /**
   * This method sets up the necessary objects for testing. It is annotated with @BeforeEach to run
   * before each test case.
   */
  @BeforeEach
  void setUp() {
    // Initialization of a ResponsableStageDTO
    this.responsableStageDTO = domainFactory.getResponsableStage();
    this.enterpriseDTO = domainFactory.getEnterprise();
  }

  /**
   * Test createResponsableStage method.
   */
  @DisplayName("Test createResponsableStage method bad enterprise id")
  @Test
  void testCreateResponsableStageBadEnterpriseId() {
    Mockito.when(myEnterpriseDAO.getOneById(1)).thenReturn(null);
    enterpriseDTO = myEnterpriseDAO.getOneById(1);
    assertThrows(ObjectNotFoundException.class, () -> {
      responsableStageUCC.createResponsableStage(responsableStageDTO);
    });
  }

  @DisplayName("Test createResponsableStage method ok")
  @Test
  void testCreateResponsableStageOk() {
    responsableStageDTO.setEnterprise(1);
    EnterpriseDTO enterpriseDTO1 = domainFactory.getEnterprise();
    enterpriseDTO1.setIdEnterprise(1);
    Mockito.when(myEnterpriseDAO.getOneById(1)).thenReturn(enterpriseDTO1);
    enterpriseDTO = myEnterpriseDAO.getOneById(responsableStageDTO.getEnterprise());
    Mockito.when(myResponsableStageDAO.createResponsableStage(responsableStageDTO))
        .thenReturn(responsableStageDTO);
    assertEquals(responsableStageDTO,
        responsableStageUCC.createResponsableStage(responsableStageDTO));
  }

  /**
   * Test createResponsableStage method.
   */
  @DisplayName("Test getAllResponsableStage method")
  @Test
  void testGetAllResponsableStage() {
    Mockito.when(myResponsableStageDAO.getAll()).thenReturn(null);
    assertNull(responsableStageUCC.getAllResponsableStage());
  }

}
