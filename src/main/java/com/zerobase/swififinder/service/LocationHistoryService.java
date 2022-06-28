package com.zerobase.swififinder.service;

import com.zerobase.swififinder.dto.LocationHistory;
import com.zerobase.swififinder.repository.LocationHistoryRepository;

import java.util.List;

public class LocationHistoryService {
    private final LocationHistoryRepository locationHistoryRepository = new LocationHistoryRepository();

    public void historyInsert(String lat, String lnt) {
        if (locationHistoryRepository.save(lat, lnt) > 0) {
            System.out.println("history insert success");
        } else {
            System.out.println("history insert fail");
        }
    }

    public List<LocationHistory> historySelect() {
        List<LocationHistory> list = locationHistoryRepository.findAll();
        System.out.println(list.size() + "개 history 조회");

        return list;
    }

    public void historyDelete(String id) {
        if (locationHistoryRepository.deleteById(id) > 0) {
            System.out.println("history delete success");
        } else {
            System.out.println("history delete fail");
        }
    }

}
