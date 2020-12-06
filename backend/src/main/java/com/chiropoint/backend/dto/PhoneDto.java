package com.chiropoint.backend.dto;

import lombok.Data;

@Data
public class PhoneDto {

    private int number;

    private boolean telegram;
    private boolean whatsApp;

}
