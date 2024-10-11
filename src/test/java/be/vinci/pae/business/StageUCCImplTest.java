package be.vinci.pae.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.responsable.ResponsableStageDTO;
import be.vinci.pae.business.stage.StageDTO;
import be.vinci.pae.business.stage.StageUCC;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.dal.responsable.ResponsableStageDAO;
import be.vinci.pae.dal.stage.StageDAO;
import be.vinci.pae.exception.ForbiddenException;
import be.vinci.pae.exception.ObjectNotFoundException;
import be.vinci.pae.utils.ApplicationBinderTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class for StageUCCImpl.
 */
class StageUCCImplTest {

  // ServiceLocator instance for testing
  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());

  // Instance of StageUCC for testing
  private final StageUCC stageUCC = locator.getService(StageUCC.class);

  // Mocked StageDAO for testing
  private final StageDAO myStageDAO = locator.getService(StageDAO.class);

  // Mocked ContactDAO for testing
  private final ContactDAO myContactDAO = locator.getService(ContactDAO.class);

  private final ResponsableStageDAO myResponsableStageDAO = locator.getService(
      ResponsableStageDAO.class);
  // Instance of DomainFactory
  private final DomainFactory domainFactory = locator.getService(DomainFactory.class);
  // StageDTO instance for testing
  private StageDTO stage;

  // Contact instance for testing
  private ContactDTO contact;


  /**
   * This method sets up the necessary objects for testing. It is annotated with @BeforeEach to run
   * before each test case.
   */
  @BeforeEach
  void setUp() {

    // Initialization of a stage
    this.stage = domainFactory.getStage();
    this.contact = domainFactory.getContact();

  }

  /**
   * This test case checks the getOneStage functionality with a wrong id. It expects a null return
   * value.
   */
  @DisplayName("Test getOneStage method")
  @Test
  public void getOneStage() {
    Mockito.when(myStageDAO.getOne(1)).thenReturn(stage);
    assertEquals(stage, stageUCC.getOne(1));
  }

  /**
   * Test method for changing non-existent internship project. Verifies that attempting to change an
   * internship project that does not exist throws an ObjectNotFoundException.
   */
  @DisplayName("Test change non existent internship project")
  @Test
  public void changeWrongInternship() {
    Mockito.when(myStageDAO.getOne(1)).thenReturn(null);
    assertThrows(ObjectNotFoundException.class,
        () -> stageUCC.changeInternshipProject(1, "test", 1));
  }

  /**
   * Test method for changing other's internship project. Verifies that attempting to change the
   * internship project of another user throws a ForbiddenException.
   */
  @DisplayName("Test change other's internship project")
  @Test
  public void changeOthersInternship() {
    stage.setUserId(2);
    Mockito.when(myStageDAO.getOne(1)).thenReturn(stage);
    assertThrows(ForbiddenException.class,
        () -> stageUCC.changeInternshipProject(1, "test", 1));
  }

  /**
   * Test method for changing internship project successfully. Verifies that changing the internship
   * project of the logged-in user with correct parameters results in the expected change.
   */
  @DisplayName("Test change internship project all ok")
  @Test
  public void changeInternshipProjectOk() {
    stage.setUserId(1);
    stage.setIdStage(1);
    Mockito.when(myStageDAO.getOne(1)).thenReturn(stage);
    Mockito.when(myStageDAO.updateOne(stage)).thenReturn(true);
    assertEquals(stage.getIdStage(),
        stageUCC.changeInternshipProject(1, "test", 1).getIdStage());
  }

  /**
   * Test method for creating a stage with a non-existent contact. Verifies that attempting to
   * create a stage with a non-existent contact throws an ObjectNotFoundException.
   */
  @DisplayName("createStage bad contact")
  @Test
  public void createStageBadContact() {
    stage.setContact(1);
    Mockito.when(myContactDAO.getContact(stage.getContact())).thenReturn(null);
    assertThrows(ObjectNotFoundException.class, () -> stageUCC.createStage(stage));
  }

  /**
   * Test method for creating a stage with a contact that does not belong to the logged-in user.
   * Verifies that attempting to create a stage with a contact that does not belong to the logged-in
   * user throws a ForbiddenException.
   */
  @DisplayName("createStage not same id user")
  @Test
  public void createStageNotSameIdUser() {
    contact.setUserId(1);
    contact.setIdContact(1);
    stage.setUserId(2);
    stage.setContact(1);
    Mockito.when(myContactDAO.getContact(stage.getContact())).thenReturn(contact);
    assertThrows(ForbiddenException.class, () -> stageUCC.createStage(stage));
  }

  /**
   * Test method for creating a stage with a non-existent internship supervisor. Verifies that
   * attempting to create a stage with a non-existent internship supervisor throws an
   * ObjectNotFoundException.
   */
  @DisplayName("createStage bad internship supervisor")
  @Test
  public void createStageBadInternshipSupervisor() {
    contact.setUserId(1);
    contact.setIdContact(1);
    stage.setUserId(1);
    stage.setContact(1);
    stage.setInternshipSupervisorId(1);
    Mockito.when(myContactDAO.getContact(stage.getContact())).thenReturn(contact);
    Mockito.when(myStageDAO.createStage(stage)).thenReturn(stage);
    Mockito.when(myResponsableStageDAO.getOne(1)).thenReturn(null);
    assertThrows(ObjectNotFoundException.class, () -> stageUCC.createStage(stage));
  }

  /**
   * Test method for creating a stage successfully. Verifies that creating a stage with correct
   * parameters results in the expected stage creation.
   */
  @DisplayName("createStage all ok")
  @Test
  public void createStageAllOk() {
    contact.setUserId(1);
    contact.setIdContact(1);
    stage.setUserId(1);
    stage.setContact(1);
    stage.setInternshipSupervisorId(1);
    ResponsableStageDTO responsableStage = domainFactory.getResponsableStage();
    Mockito.when(myContactDAO.getContact(stage.getContact())).thenReturn(contact);
    Mockito.when(myStageDAO.createStage(stage)).thenReturn(stage);
    Mockito.when(myStageDAO.getOne(1)).thenReturn(stage);
    Mockito.when(myResponsableStageDAO.getOne(1)).thenReturn(responsableStage);
    assertEquals(stage.getIdStage(), stageUCC.createStage(stage).getIdStage());
  }

  /**
   * Test method for creating a stage with a non-existent contact. Verifies that attempting to
   * create a stage with a non-existent contact throws an ObjectNotFoundException.
   */
  @DisplayName("Test getStatsStages")
  @Test
  public void getStatsStages() {
    final Map<String, Map<String, Integer>> stats = Map.of("2023-2024",
        Map.of("withStage", 1, "withoutStage", 1),
        "2022-2023", Map.of("withStage", 1, "withoutStage", 1),
        "2021-2022", Map.of("withStage", 0, "withoutStage", 1), "allYears", Map.of("withStage", 2,
            "withoutStage", 1));
    final List<String> l = new ArrayList<>();
    l.add("2023-2024 null");
    l.add("2021-2022 2023-2024");
    final Map<String, Integer> m = new HashMap<>();
    m.put("2023-2024", 1);
    m.put("2022-2023", 1);
    Mockito.when(myStageDAO.getNumberStagesBySchoolYear()).thenReturn(m);
    Mockito.when(myStageDAO.getStudentsWithoutStage()).thenReturn(l);
    assertEquals(stats, stageUCC.getStatsStages());
  }
}
