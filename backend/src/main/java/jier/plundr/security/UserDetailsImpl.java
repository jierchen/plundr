package jier.plundr.security;

import jier.plundr.model.PlundrUser;
import jier.plundr.model.enums.UserType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class UserDetailsImpl implements UserDetails {

    private PlundrUser user;

    public UserDetailsImpl(PlundrUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        UserType userType = user.getType();

        if(userType == UserType.CUSTOMER) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if (userType == UserType.EMPLOYEE) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public PlundrUser getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public Boolean isCustomerUser() {
        return user.getType() == UserType.CUSTOMER;
    }
}
