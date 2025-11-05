package com.ryan.dontapdabomb.dontapdabomb.repository;

import com.ryan.dontapdabomb.dontapdabomb.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    // we kunnen hier custom queries toevoegen als dat later nodig is
}