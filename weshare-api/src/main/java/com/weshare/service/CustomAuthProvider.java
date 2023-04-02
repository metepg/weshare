package com.weshare.service;

import com.weshare.model.User;
import com.weshare.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    final private IUserRepository repository;
    final private PasswordEncoder encoder;
    Logger logger = LoggerFactory.getLogger(CustomAuthProvider.class);

    public CustomAuthProvider(IUserRepository repository, PasswordEncoder encoder) {
        this.encoder = encoder;
        this.repository = repository;
    }

    /**
     * Get the username and password from authentication object and validate with password encoders matching method
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> student = repository.findByUsername(username);
        if (student.isEmpty()) {
            throw new BadCredentialsException("Väärä nimi tai salasana");
        }
        User loggedUser = student.get();

        if (encoder.matches(password, loggedUser.getPassword())) {
            logger.info("Successfully Authenticated the user");
            Set<String> role = loggedUser.getRoles();

            return new UsernamePasswordAuthenticationToken(username, password, getUserRoles(role));
        }
        else {
            throw new BadCredentialsException("Väärä nimi tai salasana");
        }
    }

    /**
     * An user can have more than one roles separated by ",". We are splitting each role separately
     *
     * @param userRoles
     * @return
     */
    private List<GrantedAuthority> getUserRoles(Set<String> userRoles) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for (String role : userRoles) {
            logger.info("Role: " + role);
            String fullRole = "ROLE_" + role;
            grantedAuthorityList.add(new SimpleGrantedAuthority(fullRole));
        }

        return grantedAuthorityList;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
