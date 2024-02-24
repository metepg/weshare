package com.weshare.service;

import com.weshare.model.Person;
import com.weshare.repository.PersonRepository;
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

    private final PersonRepository repository;
    private final PasswordEncoder encoder;
    Logger logger = LoggerFactory.getLogger(CustomAuthProvider.class);

    public CustomAuthProvider(PersonRepository repository, PasswordEncoder encoder) {
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

        Optional<Person> person = repository.findByUsername(username);
        if (person.isEmpty()) {
            throw new BadCredentialsException("Väärä nimi tai salasana");
        }
        Person loggedPerson = person.get();

        if (encoder.matches(password, loggedPerson.getPassword())) {
            logger.info("Successfully Authenticated the user");
            String role = loggedPerson.getRole();

            return new UsernamePasswordAuthenticationToken(username, password, getUserRoles(role));
        }
        else {
            throw new BadCredentialsException("Väärä nimi tai salasana");
        }
    }

    /**
     * An user can have more than one roles separated by ",". We are splitting each role separately
     *
     * @param personRole
     * @return
     */
    private List<GrantedAuthority> getUserRoles(String personRole) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        logger.info("Role: {}", personRole);
        String fullRole = "ROLE_" + personRole;
        grantedAuthorityList.add(new SimpleGrantedAuthority(fullRole));

        return grantedAuthorityList;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
