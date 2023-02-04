package ch.zhaw.card2brain.TestData;

import ch.zhaw.card2brain.dto.CardDto;
import ch.zhaw.card2brain.dto.CategoryDto;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;

import java.util.List;
import java.util.Objects;
/**

 Class to compare different data objects.
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */
public class TestDataCompare {

    /**

     Compares two CardDto objects and returns true if they are equal, false otherwise.
     @param expected the expected CardDto object
     @param actual the actual CardDto object
     @return true if expected and actual are equal, false otherwise
     */
    public static boolean COMPARE_CARD_DTO(CardDto expected, CardDto actual) {

        return Objects.equals(expected.getId(), actual.getId()) && expected.getAnswer().contentEquals(actual.getAnswer()) && expected.getQuestion().contentEquals(actual.getQuestion()) && Objects.equals(expected.getCategoryId(), actual.getCategoryId());
    }

    /**
     Compares two CategoryDto objects and returns true if they are equal, false otherwise.
     @param expected the expected CategoryDto object
     @param actual the actual CategoryDto object
     @return true if expected and actual are equal, false otherwise
     */
    public static boolean COMPARE_CATEGORY_DTO(CategoryDto expected, CategoryDto actual) {

        return Objects.equals(expected.getId(), actual.getId()) && expected.getCategoryName().contentEquals(actual.getCategoryName()) && Objects.equals(expected.getOwner(), actual.getOwner());
    }
    /**
     Compares two Category objects and returns true if they are equal, false otherwise.
     @param expected the expected Category object
     @param actual the actual Category object
     @return true if expected and actual are equal, false otherwise
     */
    public static boolean COMPARE_CATEGORY(Category expected, Category actual) {
        return Objects.equals(expected.getId(), actual.getId()) && expected.getCategoryName().contentEquals(actual.getCategoryName()) && COMPARE_USER(expected.getOwner(), actual.getOwner());
    }

    /**
     Compares two User objects and returns true if they are equal, false otherwise.
     @param expected the expected User object
     @param actual the actual User object
     @return true if expected and actual are equal, false otherwise
     */
    public static boolean COMPARE_USER(User expected, User actual) {
        return Objects.equals(expected.getId(), actual.getId()) && expected.getUserName().contentEquals(actual.getUserName()) && expected.getMailAddress().contentEquals(actual.getMailAddress()) && expected.getFirstName().contentEquals(actual.getFirstName());
    }

    /**

     Compares two lists of CardDto objects and returns true if they are equal, false otherwise.
     @param expectedList the expected list of CardDto objects
     @param actualList the actual list of CardDto objects
     @return true if expectedList and actualList are equal, false otherwise
     */
    public static boolean COMPARE_CARD_DTO_LISTS(List<CardDto> expectedList, List<CardDto> actualList) {

        int counter = 0;
        for (CardDto expectedDto : expectedList) {
            for (CardDto actualDto : actualList) {
                if (Objects.equals(expectedDto.getId(), actualDto.getId())) {
                    if (COMPARE_CARD_DTO(expectedDto, actualDto)) {
                        counter++;
                    } else {
                        System.out.println("Expected: " + expectedDto);
                        System.out.println("Actual: " + actualDto);
                        return false;
                    }
                }
            }
        }

        return expectedList.size() == counter;
    }

    /**
     Compares two lists of Category objects to determine if they contain the same categories.
     @param expectedList The list of expected Category objects.
     @param actualList The list of actual Category objects.
     @return true if the lists contain the same categories, false otherwise.
     */
    public static boolean COMPARE_CATEGORY_LISTS(List<Category> expectedList, List<Category> actualList) {

        int counter = 0;
        for (Category expected : expectedList) {
            for (Category actual : actualList) {
                if (Objects.equals(expected.getId(), actual.getId())) {
                    if (COMPARE_CATEGORY(expected, actual)) {
                        counter++;
                    } else {
                        System.out.println("Expected: " + expected);
                        System.out.println("Actual: " + actual);
                        return false;
                    }
                }
            }
        }

        return expectedList.size() == counter;
    }

}
