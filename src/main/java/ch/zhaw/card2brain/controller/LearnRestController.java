package ch.zhaw.card2brain.controller;

import ch.zhaw.card2brain.dto.CardDto;
import ch.zhaw.card2brain.dto.LearnDto;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.objectmapper.CardMapper;
import ch.zhaw.card2brain.objectmapper.LearnMapper;
import ch.zhaw.card2brain.services.LearnService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RestController class for handling when the user is in the learning mode.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */

@RestController
@RequestMapping(value = "/api/learns")
public class LearnRestController {


    @Autowired
    private LearnService learnService;

    @Autowired
    private LearnMapper learnMapper;

    @Autowired
    private CardMapper cardMapper;

    /**

     This method is used to mark a card as learned with the information correct or false
     @param learnDto {@link LearnDto} object containing the details of the learned card.
     @return {@link ResponseEntity} with status OK.
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/")
    public ResponseEntity<?> cardLearned(@RequestBody LearnDto learnDto) {
        learnService.cardWasShown(learnMapper.toLearn(learnDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     This method is used to retrieve a list of cards to learn for a given category.
     @param categoryId the id of the category for which the cards to learn are requested.
     @return {@link ResponseEntity} with a list of {@link CardDto} objects and status OK.
     */
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/")
    public ResponseEntity<List<CardDto>> cardsToLearn(@RequestParam long categoryId) {
        return new ResponseEntity<>(learnService.cardsToLearn(learnMapper.getCardsToLearn(categoryId)).stream().map((Card card) -> cardMapper.mapToCardDto(card)).collect(Collectors.toList()), HttpStatus.OK);
    }

}
