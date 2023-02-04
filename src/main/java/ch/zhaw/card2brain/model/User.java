package ch.zhaw.card2brain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 The User class represents an user in the Card2Brain application.
 It is an entity class that uses JPA to map to the corresponding table in the database.
 It contains fields for the user's name, first name, email address and password.
 The email address is unique in the system.
 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @since 16.01.2023
 @version 1.0
 */
@Entity(name = "Card2Brain_User")
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class User extends BaseEntity implements UserDetails {

    /**

     The user's username.
     */
    @Getter
    @Setter
    @NonNull
    private String userName;
    /**

     The user's first name.
     */
    @Getter
    @Setter
    @NonNull
    private String firstName;
    /**

     The user's email address.
     */
    @Getter
    @Setter
    @NonNull
    @Column(unique=true)
    private String mailAddress;
    /**

     The user's password.
     */
    @Getter
    @Setter
    private String password;
    /**

     The user's role in the system.
     Admin / User
     */

    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Returns a collection of roles/authorities granted to the user.
     *
     * @return a collection of {@link SimpleGrantedAuthority} objects.
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    /**
     * Gets the user name of the User.
     *
     * @return the user name of the User
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name of the User.
     *
     * @param userName the new user name of the User
     */

    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * Returns the user's email address as their username.
     *
     * @return the user's email address.
     */
    @Override
    public String getUsername() {
        return mailAddress;
    }
    /**
     * Returns whether the user's account is expired or not.
     *
     * @return true, since the user's account is not expired.
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /**
     * Returns whether the user's account is locked or not.
     *
     * @return true, since the user's account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * Returns whether the user's credentials are expired or not.
     *
     * @return true, since the user's credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns whether the user is enabled or not.
     *
     * @return true, since the user is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
