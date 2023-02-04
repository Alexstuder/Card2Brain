package ch.zhaw.card2brain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

/**
 The class representing a database entity of a category
 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
 */

@Entity(name = "Category")
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Category extends BaseEntity {

    @Getter
    @Setter
    @NonNull
    private String categoryName;

    @Getter
    @Setter
    @ManyToOne
    @NonNull
    private User owner;


}
