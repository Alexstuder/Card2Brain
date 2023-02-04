package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.dto.InfoDto;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.util.HasLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * InfoServiceImpl is a class that implements the InfoService interface and provides methods for getting information about cards and categories for a given user.
 * This class also implements the HasLogger interface, which provides methods for logging information.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@Component
public class InfoServiceImpl implements InfoService, HasLogger {


    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CardService cardService;


    /**
     * This method returns a list of {@link InfoDto} containing information about the user's categories,
     * including the number of cards in each category and the number of cards to be repeated in each category.
     *
     * @param user The user for which the information is requested.
     * @return A list of {@link InfoDto} containing information about the user's categories.
     */

    @Override
    public List<InfoDto> getInfos(User user) {
        List<InfoDto> infoDtos = new ArrayList<>();
        categoryService.getAllCategoriesOfUser(user).forEach(category -> {
            infoDtos.add(new InfoDto(category.getCategoryName(), category.getId(), cardService.getAllCardsOfACategory(category).size(), cardService.getCardsForRepetition(category.getId()).size()));
        });
        for (InfoDto infoDto : infoDtos)
            getLogger().info("User requests Infos User :" + user.getMailAddress() + " Category :" + infoDto.getCategoryName() + " number of cards :" + infoDto.getNumberOfCards() + " cards to learn :" + infoDto.getToLearn());
        return infoDtos;
    }

}
