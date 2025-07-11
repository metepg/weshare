package com.weshare.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("eRole")
public final class ERole {

    // Add Role1 and Role2 as env_variables to application-dev.properties and application.properties
    @Value("${Role1}")
    public String role1;
    @Value("${Role2}")
    public String role2;

}
