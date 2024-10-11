package be.vinci.pae.business.responsable;

import java.util.List;

/**
 * ResponsableStageUCC interface. This interface provides methods for creating a new internship
 * supervisor.
 */
public interface ResponsableStageUCC {

  /**
   * Create a new internship supervisor.
   *
   * @param responsableStageDTO the new internship supervisor
   * @return the new internship supervisor
   */
  ResponsableStageDTO createResponsableStage(ResponsableStageDTO responsableStageDTO);

  /**
   * Get all the information of an internship supervisor.
   *
   * @return the internship supervisor
   */
  List<ResponsableStageDTO> getAllResponsableStage();
}
