package com.chiropoint.backend.security;

import com.chiropoint.backend.domain.models.Administrator;
import com.chiropoint.backend.domain.models.OfficeWorker;
import com.chiropoint.backend.domain.models.Patient;
import com.chiropoint.backend.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    @Getter
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (user.getPerson() == null) {
            return authorities;
        }

        if (user.getPerson() instanceof Administrator) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else if (user.getPerson() instanceof Patient) {
            authorities.add(new SimpleGrantedAuthority("PATIENT"));
        } else if (user.getPerson() instanceof OfficeWorker) {
            ((OfficeWorker) user.getPerson()).getRoles().forEach(
                    role -> authorities.add(new SimpleGrantedAuthority(role.getLiteral()))
            );
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
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

}
