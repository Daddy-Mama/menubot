package com.iwahare.impl;

import com.iwahare.enums.ReservedWordsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulingService {
    @Autowired
    private IDataBaseService dataBaseService;
    private final Integer timer = 600000;

    @Scheduled(fixedRate = 600000)
    public void reportCurrentTime() {
        dataBaseService.deleteOldReceipts();
    }
}
