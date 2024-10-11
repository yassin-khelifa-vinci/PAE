package be.vinci.pae.business.stage;

import be.vinci.pae.business.DomainFactory;
import be.vinci.pae.business.contact.ContactDTO;
import be.vinci.pae.business.responsable.ResponsableStageDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.contact.ContactDAO;
import be.vinci.pae.dal.responsable.ResponsableStageDAO;
import be.vinci.pae.dal.stage.StageDAO;
import be.vinci.pae.exception.ForbiddenException;
import be.vinci.pae.exception.ObjectNotFoundException;
import be.vinci.pae.utils.Util;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * StageUCCImpl is a class that implements the StageUCC interface. It provides the implementation
 * for the method to get a stage by its id. This class is responsible for handling business logic
 * related to Stage operations. It interacts with the Data Access Layer (DAL) to retrieve and
 * manipulate Stage data.
 */
public class StageUCCImpl implements StageUCC {

  // Instance of DALServices for performing common DAL operations
  @Inject
  private DALServices dalServices;

  //Instance of StageDAO for interacting with the database
  @Inject
  private StageDAO stageDAO;

  //Instance of DomainFactory for creating domain objects
  @Inject
  private DomainFactory domainFactory;

  //Instance of ResponsableStageDAO for interacting with the database
  @Inject
  private ResponsableStageDAO responsableStageDAO;

  //Instance of ContactUCC for interacting with the database
  @Inject
  private ContactDAO contactDAO;

  /**
   * This method is used to retrieve a stage by its id from the database.
   *
   * @param id The id of the stage to retrieve.
   * @return A Stage with the given id. If no stage is found, it returns null.
   */
  @Override
  public StageDTO getOne(int id) {
    try {
      dalServices.open();
      return stageDAO.getOne(id);
    } finally {
      dalServices.close();
    }
  }

  /**
   * This method is used to create a stage in the database.
   *
   * @param stageDTO The StageDTO object representing the stage to create.
   * @return A StageDTO object representing the created stage.
   */
  @Override
  public StageDTO createStage(StageDTO stageDTO) {
    try {
      dalServices.startTransaction();
      ContactDTO contactDTO = contactDAO.getContact(stageDTO.getContact());
      if (contactDTO == null) {
        throw new ObjectNotFoundException("Contact not found");
      }
      if (contactDTO.getUserId() != stageDTO.getUserId()) {
        throw new ForbiddenException("Can't create stage for other's contact");
      }
      ResponsableStageDTO responsableStage = responsableStageDAO.getOne(
          stageDTO.getInternshipSupervisorId());
      if (responsableStage == null) {
        throw new ObjectNotFoundException("Internship supervisor not found");
      }

      stageDTO.setInternshipSupervisorDTO(responsableStage);
      StageDTO newStage = stageDAO.createStage(stageDTO);

      dalServices.commit();

      return newStage;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Changes the internship project for a given user.
   *
   * @param idUser     The ID of the user whose internship project is to be changed.
   * @param text       The new text for the internship project.
   * @param numVersion The version number of the internship project.
   * @return The modified StageDTO object after changing the internship project.
   */
  @Override
  public StageDTO changeInternshipProject(int idUser, String text, int numVersion) {
    try {
      dalServices.startTransaction();
      StageDTO stageTemp = stageDAO.getOne(idUser);
      if (stageTemp == null) {
        throw new ObjectNotFoundException("Internship not found");
      }
      if (stageTemp.getUserId() != idUser) {
        throw new ForbiddenException("Can't modify other's internship");
      }
      stageTemp.setInternshipProject(text);
      stageTemp.setVersionNumber(numVersion + 1);
      stageDAO.updateOne(stageTemp);
      dalServices.commit();
      return stageTemp;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Retrieves a list of school years without a stage. This method opens a connection to the
   * database, retrieves a list of school years without a stage from the StageDAO, and then closes
   * the connection. If an exception occurs during this process, the connection is closed before the
   * exception is propagated.
   *
   * @return A list of school years without a stage.
   */
  @Override
  public Map<String, Map<String, Integer>> getStatsStages() {
    try {
      dalServices.open();
      Map<String, Map<String, Integer>> stats = new HashMap<>();
      List<String> yearsWithoutStage = stageDAO.getStudentsWithoutStage();
      Map<String, Integer> statsStages = stageDAO.getNumberStagesBySchoolYear();
      AtomicInteger withStage = new AtomicInteger();
      statsStages.forEach((key, value) -> {
        withStage.addAndGet(value);
        stats.put(key, new HashMap<>());
        stats.get(key).put("withStage", value);
        stats.get(key).put("withoutStage", 0);
      });
      AtomicInteger withoutStage = new AtomicInteger();
      yearsWithoutStage.forEach(year -> {
        if (year.split(" ")[1].equals("null")) {
          withoutStage.addAndGet(1);
        }
        List<String> l = getAllSchoolYears(year);
        l.forEach(y -> {
          if (!stats.containsKey(y)) {
            stats.put(y, new HashMap<>());
            stats.get(y).put("withStage", 0);
          }
          stats.get(y).put("withoutStage", stats.get(y).getOrDefault("withoutStage", 0) + 1);
        });
      });
      Map<String, Integer> allYears = new HashMap<>();
      allYears.put("withStage", withStage.get());
      allYears.put("withoutStage", withoutStage.get());
      stats.put("allYears", allYears);
      return stats;
    } finally {
      dalServices.close();
    }
  }

  /**
   * Generates a list of all school years from the given school year to the current school year.
   * This method takes a school year as a parameter, which is a string in the format "yyyy-yyyy".
   *
   * @param years The school year in the format "yyyy-yyyy".
   * @return A list of school years from the given school year to the current school year.
   */
  private List<String> getAllSchoolYears(String years) {
    List<String> schoolYears = new ArrayList<>();
    String[] parts = years.split(" ");
    int start = Integer.parseInt(parts[0].split("-")[0]);
    int end;
    boolean current = false;
    if (parts[1].equals("null")) {
      current = true;
      end = Integer.parseInt(Util.getCurrentSchoolYear().split("-")[0]);
    } else {
      end = Integer.parseInt(parts[1].split("-")[0]);
    }
    for (int i = start; i < end; i++) {
      schoolYears.add(i + "-" + (i + 1));
    }
    if (current) {
      schoolYears.add(Util.getCurrentSchoolYear());
    }
    return schoolYears;
  }
}
