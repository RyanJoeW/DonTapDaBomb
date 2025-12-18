package com.ryan.dontapdabomb.dontapdabomb.service;

import com.ryan.dontapdabomb.dontapdabomb.dto.LeaderboardDTO;
import com.ryan.dontapdabomb.dontapdabomb.entity.LeaderboardEntry;
import com.ryan.dontapdabomb.dontapdabomb.repository.LeaderboardEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService implements  ILeaderboardService {

    private final LeaderboardEntryRepository leaderboardEntryRepository;
    private final LeaderboardSseService leaderboardSseService;

    public LeaderboardService(LeaderboardEntryRepository leaderboardEntryRepository, LeaderboardSseService leaderboardSseService) {
        this.leaderboardEntryRepository = leaderboardEntryRepository;
        this.leaderboardSseService = leaderboardSseService;
    }

    public void addEntryAndNotify(LeaderboardEntry entry) {
        leaderboardEntryRepository.save(entry);

        List<LeaderboardDTO> top10 = leaderboardEntryRepository
                .findTop10ByOrderByScoreDesc()
                .stream()
                .map(e -> new LeaderboardDTO(
                        e.getUser().getName(),
                        e.getScore(),
                        e.getCreatedAt()
                ))
                .toList();

        leaderboardSseService.notifyClients(top10);
    }

    public List<LeaderboardDTO> getTop10() {
        return leaderboardEntryRepository
                .findTop10ByOrderByScoreDesc()
                .stream()
                .map(e -> new LeaderboardDTO(
                        e.getUser().getName(),
                        e.getScore(),
                        e.getCreatedAt()
                ))
                .toList();
    }
}