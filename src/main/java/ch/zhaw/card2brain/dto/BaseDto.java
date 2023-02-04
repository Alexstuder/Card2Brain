package ch.zhaw.card2brain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**

 The BaseEntity class is an abstract class for the ID of all DTO object,
 It contains the ID field, which can be null
 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
 */
@NoArgsConstructor
public abstract class BaseDto {
    /** The id field is used to identify the DTO. */
    @Getter
    @Setter
    public Long id;
}
