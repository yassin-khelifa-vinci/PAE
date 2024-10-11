package be.vinci.pae.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.business.entreprise.EnterpriseDTO;
import be.vinci.pae.business.entreprise.EnterpriseUCC;
import be.vinci.pae.dal.enterprise.EnterpriseDAO;
import be.vinci.pae.exception.ConflictException;
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
 * Unit tests for the EnterpriseUCCImpl class.
 */
class EnterpriseUCCImplTest {

  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());
  private final List<EnterpriseDTO> list = new ArrayList<>();
  private final EnterpriseUCC enterpriseUCC = locator.getService(EnterpriseUCC.class);
  private final DomainFactory domainFactory = locator.getService(DomainFactory.class);

  private final EnterpriseDAO enterpriseDAO = locator.getService(EnterpriseDAO.class);

  /**
   * Set up the test environment.
   */
  @BeforeEach
  void setup() {
    EnterpriseDTO enterprise1 = domainFactory.getEnterprise();
    EnterpriseDTO enterprise2 = domainFactory.getEnterprise();

    enterprise1.setIdEnterprise(1);

    enterprise2.setIdEnterprise(2);

    list.add(enterprise1);
    list.add(enterprise2);

    Mockito.when(enterpriseDAO.getAll()).thenReturn(list);

    Mockito.when(enterpriseDAO.getOneById(1)).thenReturn(list.get(0));
  }

  /**
   * Test for getAll() method.
   */
  @DisplayName("test getAll method")
  @Test
  void getAll() {
    assertEquals(list, enterpriseUCC.getAll());
  }

  /**
   * Test for getEnterpriseInfo() method.
   */
  @DisplayName("test getEnterpriseInfo method")
  @Test
  void getEnterpriseInfo() {
    assertEquals(list.get(0), enterpriseUCC.getEnterpriseInfo(1));
  }

  /**
   * Test with a good input for the blacklist method.
   */
  @DisplayName("Test blacklist method with good inputs")
  @Test
  void blacklist1() {
    final int version = 1;
    final int enterpriseID = 1;
    final String reason = "reason";

    // Create a mock EnterpriseDTO
    EnterpriseDTO blackEnterprise = domainFactory.getEnterprise();
    blackEnterprise.setIdEnterprise(enterpriseID);
    blackEnterprise.setBlacklisted(false);

    // Mock the behavior of myEnterpriseDAO
    Mockito.when(enterpriseDAO.getOneById(enterpriseID)).thenReturn(blackEnterprise);
    Mockito.when(enterpriseDAO.updateOne(blackEnterprise)).thenReturn(true);

    // Call the blacklist method
    EnterpriseDTO blacklistedEnterprise = enterpriseUCC.blacklist(enterpriseID, reason, version);

    // Verify the result
    assertTrue(blacklistedEnterprise.isBlacklisted());
    assertEquals(reason, blacklistedEnterprise.getBlacklistedReason());
    assertEquals(version + 1, blacklistedEnterprise.getVersionNumber());
  }

  /**
   * Test with an enterprise not found for the blacklist method.
   */
  @DisplayName("Test blacklist method with enterprise not found")
  @Test
  void blacklist2() {
    final int version = 1;
    final int enterpriseID = 1;
    final String reason = "reason";

    // Mock the behavior of myEnterpriseDAO
    Mockito.when(enterpriseDAO.getOneById(enterpriseID)).thenReturn(null);

    assertThrows(ObjectNotFoundException.class, () -> enterpriseUCC.blacklist(enterpriseID, reason,
        version));
  }

  /**
   * Test with an enterprise already blacklisted for the blacklist method.
   */
  @DisplayName("Test blacklist method with enterprise already blacklisted")
  @Test
  void blacklist3() {
    final int version = 1;
    final int enterpriseID = 1;
    final String reason = "reason";

    // Create a mock EnterpriseDTO
    EnterpriseDTO blackEnterprise = domainFactory.getEnterprise();
    blackEnterprise.setBlacklisted(true);

    // Mock the behavior of myEnterpriseDAO
    Mockito.when(enterpriseDAO.getOneById(enterpriseID)).thenReturn(blackEnterprise);

    assertThrows(ConflictException.class, () -> enterpriseUCC.blacklist(enterpriseID, reason,
        version));
  }

  /**
   * This test case checks the addEnterprise method with valid inputs.
   */
  @DisplayName("Test addEnterprise method with good inputs")
  @Test
  void testAddEnterprise1() {
    EnterpriseDTO enterprise = domainFactory.getEnterprise();
    enterprise.setEmail("goodEmail");
    enterprise.setTradeName("goodTradeName");

    Mockito.when(enterpriseDAO.getOneByEmail(enterprise.getEmail())).thenReturn(null);
    Mockito.when(enterpriseDAO.getOneByTradeNameAndDesignation(enterprise.getTradeName(),
        enterprise.getDesignation())).thenReturn(null);
    Mockito.when(enterpriseDAO.createOne(enterprise)).thenReturn(enterprise);

    assertEquals(enterprise, enterpriseUCC.addEnterprise(enterprise));
  }

  /**
   * This test case checks the addEnterprise method with an email that already exists.
   */
  @DisplayName("Test addEnterprise method with email already exists")
  @Test
  void testAddEnterprise2() {
    EnterpriseDTO enterprise = domainFactory.getEnterprise();
    enterprise.setEmail("EmailAlreadyExists");

    Mockito.when(enterpriseDAO.getOneByEmail(enterprise.getEmail())).thenReturn(enterprise);

    assertThrows(ConflictException.class, () -> enterpriseUCC.addEnterprise(enterprise));
  }

  /**
   * This test case checks the addEnterprise method with a trade name and designation that already
   * exist.
   */
  @DisplayName("Test addEnterprise method with trade name and designation already exists")
  @Test
  void testAddEnterprise3() {
    EnterpriseDTO enterprise = domainFactory.getEnterprise();
    enterprise.setEmail("goodEmail");
    enterprise.setTradeName("TradeNameAlreadyExists");

    Mockito.when(enterpriseDAO.getOneByEmail(enterprise.getEmail())).thenReturn(null);
    Mockito.when(enterpriseDAO.getOneByTradeNameAndDesignation(enterprise.getTradeName(),
        enterprise.getDesignation())).thenReturn(enterprise);

    assertThrows(ConflictException.class, () -> enterpriseUCC.addEnterprise(enterprise));
  }

  /**
   * This test case checks the addEnterprise method without an email.
   */
  @DisplayName("Test addEnterprise method without email")
  @Test
  void testAddEnterprise4() {
    EnterpriseDTO enterprise = domainFactory.getEnterprise();
    enterprise.setTradeName("goodTradeName");

    Mockito.when(enterpriseDAO.getOneByTradeNameAndDesignation(enterprise.getTradeName(),
        enterprise.getDesignation())).thenReturn(null);
    Mockito.when(enterpriseDAO.createOne(enterprise)).thenReturn(enterprise);

    assertEquals(enterprise, enterpriseUCC.addEnterprise(enterprise));
  }

  /**
   * This test case checks the getStats method.
   */
  @DisplayName("Test getStats method")
  @Test
  void testGetStats() {
    Map<Integer, Map<String, Integer>> stats = new HashMap<>();

    Mockito.when(enterpriseDAO.getStats()).thenReturn(stats);

    assertEquals(stats, enterpriseUCC.getStats());
  }
}
