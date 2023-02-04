package ch.zhaw.card2brain.controller;

import ch.zhaw.card2brain.dto.CardDto;
import ch.zhaw.card2brain.objectmapper.CardMapper;
import ch.zhaw.card2brain.objectmapper.CategoryMapper;
import ch.zhaw.card2brain.services.CardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/cards")
public class CardRestController {

    @Autowired
    private final CardService cardService;

    @Autowired
    private final CardMapper cardMapper;

    @Autowired
    CategoryMapper categoryMapper;


    public CardRestController(CardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }


    /**
     * This endpoint is used to add a card. It maps the incoming cardDto to a card and calls the cardService to add the card.
     * @param cardDto the cardDto to add
     * @return the added cardDto
     */

    // Add a Card
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/")
    public ResponseEntity<CardDto> addCard(@RequestBody CardDto cardDto) {
        CardDto response = cardMapper.mapToCardDto(cardService.addCard(cardMapper.mapToCard(cardDto)));
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    /**
     * This endpoint is used to update a card. It maps the incoming cardDto to a card and calls the cardService to update the card.
     * @param cardDto the cardDto to update
     * @return the updated cardDto
     */
    // Update a card
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = "/")
    public ResponseEntity<CardDto> updateCard(@RequestBody CardDto cardDto) {
        return new ResponseEntity<>(cardMapper.mapToCardDto(cardService.updateCard(cardMapper.mapToCard(cardDto))), HttpStatus.CREATED);
    }


    /**
     * This endpoint is used to get all cards of a category. It maps the incoming categoryId to a category and calls the cardService to get all cards of the category.
     * @param categoryId the id of the category
     * @return the list of cardDtos
     */


    // Get all cards to a category
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = {"/"})
    public ResponseEntity<List<CardDto>> getCardsByCategory(@RequestParam long categoryId) {
        //Get All Cards
        return new ResponseEntity<>(cardService.getAllCardsOfACategory(categoryMapper.fromIdToCategory(categoryId)).stream().map(cardMapper::mapToCardDto).collect(Collectors.toList()), HttpStatus.OK);

    }

    /**
     * This endpoint is used to delete a specific card by id, throws exception if card not exists.
     * @param cardId the id of the category
     */

    // delete a card
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(value = {"/"})
    public ResponseEntity<HttpStatus> deleteCard(@RequestParam long cardId) {
        cardService.deleteCard(cardMapper.mapFromIdToCard(cardId));
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
