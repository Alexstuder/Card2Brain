package ch.zhaw.card2brain.controller;


import ch.zhaw.card2brain.dto.CategoryDto;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.objectmapper.CategoryMapper;
import ch.zhaw.card2brain.services.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**

 The CategoryRestController class is responsible for handling HTTP requests for categories.
 It maps requests to the appropriate methods in the CategoryService and CategoryMapper classes.
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @version 1.0
 * @since 28-01-2023
 */

@RestController
@RequestMapping(value = "/api/categories")
public class CategoryRestController {


    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryMapper categoryMapper;


    /**
     Get all categories of a user based on the provided category ID.
     This endpoint is protected by bearerAuth security requirement.
     @param userId the ID of the category to retrieve the user's categories for
     @return a response entity containing the list of categories for the user and a status of OK
     */

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/")
    public ResponseEntity<List<CategoryDto>> getAllCategoriesOfUser(@RequestParam long userId) {
        List<CategoryDto> categories = categoryService.getAllCategoriesOfUser(categoryMapper.fromIdToUser(userId)).stream().map((Category category) -> categoryMapper.toDto(category)).collect(Collectors.toList());

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**

     Create a new category with the information provided in the request body.
     This endpoint is protected by bearerAuth security requirement.
     @param categoryDto the DTO representing the category to be created
     @return a response entity containing the DTO of the created category and a status of CREATED
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto returnDto = categoryMapper.toDto(categoryService.createCategory(categoryMapper.toCategory(categoryDto)));
        return new ResponseEntity<>(returnDto, HttpStatus.CREATED);
    }

/**

 Update an existing category with the information provided in the request body.
 This endpoint is protected by bearerAuth security requirement.
 @param categoryDto the DTO representing the updated category
 @return returnDto with HTTP status OK
*/

 @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = "/")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto returnDto = categoryMapper.toDto(categoryService.updateCateory(categoryMapper.toCategory(categoryDto)));
        return new ResponseEntity<>(returnDto , HttpStatus.OK);
    }


    /**

     Delete an existing category with the provided id.
     This endpoint is protected by bearerAuth security requirement.
     @param categoryId the id of the category to be deleted
     @return a response entity with a status of OK
     */

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(value = "/")
    public ResponseEntity<CategoryDto> deleteCategory(@RequestParam long categoryId) {
        categoryService.delete(categoryMapper.fromIdToCategory(categoryId));
        return new ResponseEntity<>(HttpStatus.OK);
    }








}
