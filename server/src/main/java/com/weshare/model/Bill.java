package com.weshare.model;

import java.util.Date;

public record Bill(String owner,
                   String description,
                   double amount,
                   String category,
                   Date date,
                   double ownAmount,
                   boolean isPaid) {
}