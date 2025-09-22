package com.weshare.dto;

import com.weshare.model.BillEventType;

public record BillEvent(BillEventType type, BillDTO bill) {}
