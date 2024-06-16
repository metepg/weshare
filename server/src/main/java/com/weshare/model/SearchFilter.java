package com.weshare.model;

import java.util.Date;
import java.util.List;

public record SearchFilter(
        String description,
        List<Integer> categories,
        List<Date> range
) {

}
