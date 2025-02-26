package com.piotrba.prisoners.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prisoner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDateTime incarcerationDate;
    private LocalDateTime imprisonmentEndDate;
    @Enumerated(EnumType.STRING)
    private ImprisonmentRigour imprisonmentRigour;
    @Embedded
    private Address address;
    private Boolean active;
}
