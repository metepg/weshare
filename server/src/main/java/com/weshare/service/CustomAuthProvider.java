package com.weshare.service;

import com.weshare.model.User;
import com.weshare.repository.UserRepository;
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

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    Logger logger = LoggerFactory.getLogger(CustomAuthProvider.class);

    public CustomAuthProvider(UserRepository repository, PasswordEncoder encoder) {
        this.encoder = encoder;
        this.repository = repository;
    }

    /**
     * Get the username and password from the authentication object and validate with the password encoder's matching method.
     *
     * @param authentication the authentication request object containing the user's credentials
     * @return a fully authenticated object including credentials and authorities if successful
     * @throws AuthenticationException if authentication fails
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> user = repository.findByName(name);
        if (user.isEmpty()) {
            throw new BadCredentialsException("Väärä nimi tai salasana");
        }
        User loggedUser = user.get();

        if (encoder.matches(password, loggedUser.getPassword())) {
            logger.info("Successfully Authenticated the user");
            String role = loggedUser.getRole();

            return new UsernamePasswordAuthenticationToken(name, password, getUserRoles(role));
        }
        else {
            throw new BadCredentialsException("Väärä nimi tai salasana");
        }
    }

    /**
     * An user can have more than one role separated by ",". This method splits the roles and returns them as a list of granted authorities.
     *
     * @param userRole a string containing roles separated by commas
     * @return a list of granted authorities derived from the user roles
     */
    private List<GrantedAuthority> getUserRoles(String userRole) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        logger.info("Role: {}", userRole);
        String fullRole = "ROLE_" + userRole;
        grantedAuthorityList.add(new SimpleGrantedAuthority(fullRole));

        return grantedAuthorityList;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
