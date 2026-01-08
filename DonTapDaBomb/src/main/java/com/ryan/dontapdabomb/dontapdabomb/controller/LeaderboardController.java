package com.ryan.dontapdabomb.dontapdabomb.controller;

import com.ryan.dontapdabomb.dontapdabomb.dto.LeaderboardDTO;
import com.ryan.dontapdabomb.dontapdabomb.service.ILeaderboardService;
import com.ryan.dontapdabomb.dontapdabomb.service.ILeaderboardSseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @Autowired
    private ILeaderboardService leaderboardService;
    @Autowired
    private ILeaderboardSseService leaderboardSseService;


    @GetMapping("/top10")
    public List<LeaderboardDTO> getTop10() {
        return leaderboardService.getTop10();
    }

    @GetMapping("/stream")
    public SseEmitter stream() {
        return leaderboardSseService.subscribe();
    }


}