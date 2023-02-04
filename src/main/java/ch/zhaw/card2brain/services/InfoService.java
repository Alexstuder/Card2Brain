package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.dto.InfoDto;
import ch.zhaw.card2brain.model.User;

import java.util.List;
/**
 * The InfoService interface defines the methods for retrieving information.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */

public interface InfoService {
    /**
     * Retrieves a list of information for a given user.
     *
     * @param user the user for which to retrieve information
     * @return a list of information represented as InfoDto objects
     */
    List<InfoDto> getInfos(User user);
}
