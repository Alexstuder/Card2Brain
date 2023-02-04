package ch.zhaw.card2brain.model;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**

 The BaseEntity class is an abstract class that serves as a base for all entities in the project.
 It contains the ID field, which is a primary key and is generated automatically.
 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
 */

@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity {

    /*
The ID field is used as a primary key and is generated automatically.
*/
    @Id
    @GeneratedValue
    @Getter
    @Setter
    @Column(name = "ID")
    protected Long id;

}
