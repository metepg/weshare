package com.weshare.dto;

import java.time.LocalDate;

public record BillDTO(
    Integer id,
    double amount,
    double ownAmount,
    String description,
    LocalDate date,
    boolean paid,
    Integer categoryId,
    Integer ownerId,
    String ownerName
) {}
