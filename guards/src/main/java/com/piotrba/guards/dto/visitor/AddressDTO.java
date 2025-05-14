package com.piotrba.guards.dto.visitor;

import lombok.Data;

@Data
public class AddressDTO {

    private String firstLine;
    private String postCode;
    private String city;
}