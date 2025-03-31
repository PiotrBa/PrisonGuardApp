package com.piotrba.guards.repo;

import com.piotrba.guards.entity.Guard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuardsRepository extends JpaRepository<Guard, Long> {

    Optional<Guard> findByEmail(String email);
}