package com.ryan.dontapdabomb.dontapdabomb.service;

import com.ryan.dontapdabomb.dontapdabomb.dto.LeaderboardDTO;
import com.ryan.dontapdabomb.dontapdabomb.entity.LeaderboardEntry;
import com.ryan.dontapdabomb.dontapdabomb.repository.LeaderboardEntryRepository;

import java.util.List;

public interface ILeaderboardService {

    public void addEntryAndNotify(LeaderboardEntry entry);
    public List<LeaderboardDTO> getTop10();
}
