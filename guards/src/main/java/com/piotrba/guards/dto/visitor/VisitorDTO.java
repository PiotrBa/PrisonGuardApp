package com.piotrba.guards.dto.visitor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private AddressDTO address;
    private Long prisonerIdNumber;
    @Enumerated(EnumType.STRING)
    private RelationshipToPrisonerDTO relationshipToPrisoner;

}

