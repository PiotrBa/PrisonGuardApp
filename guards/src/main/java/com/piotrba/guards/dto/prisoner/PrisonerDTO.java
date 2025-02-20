package com.piotrba.guards.dto.prisoner;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrisonerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDateTime incarcerationDate;
    private LocalDateTime imprisonmentEndDate;
    @Enumerated(EnumType.STRING)
    private ImprisonmentRigourDTO imprisonmentRigour;
    private AddressDTO address;
}