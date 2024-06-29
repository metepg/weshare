package com.weshare.dto;

import java.io.Serializable;
import java.util.UUID;

public record UserDTO(Integer id, String name, UUID groupId, String role) implements Serializable {}
