package com.weshare.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.UUID;

public record UserDTO(
    Integer id,
    String name,
    UUID groupId,
    @JsonIgnore String role
) implements Serializable {}
