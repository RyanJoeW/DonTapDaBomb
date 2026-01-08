package com.ryan.dontapdabomb.dontapdabomb.service;

import com.ryan.dontapdabomb.dontapdabomb.dto.LeaderboardDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface ILeaderboardSseService {

    public SseEmitter subscribe();
    public void notifyClients(List<LeaderboardDTO> data);


}
