package com.chiropoint.backend.dto;

import lombok.Data;


@Data
public class AddressDto {

    private String street;
    private int number;

    private String block;
    private int aptNumber;

    private int zipCode;

    private String area;
    private String state;
    private String country;

}
