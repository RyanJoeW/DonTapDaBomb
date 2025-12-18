package com.ryan.dontapdabomb.dontapdabomb.repository;

import com.ryan.dontapdabomb.dontapdabomb.entity.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardEntryRepository extends JpaRepository<LeaderboardEntry, Long> {
    List<LeaderboardEntry> findTop10ByOrderByScoreDesc();
}