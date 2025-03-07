package com.piotrba.prisoners.repo;

import com.piotrba.prisoners.entity.Prisoner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrisonersRepository extends JpaRepository<Prisoner, Long> {

    Optional<Prisoner> findByFirstNameAndLastName (String firstName, String lastName);
}
