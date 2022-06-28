package com.zerobase.swififinder.service;

import com.zerobase.swififinder.dto.WifiInfo;
import com.zerobase.swififinder.repository.WifiInfoRepository;

import java.util.List;

public class WifiInfoService {
    private final WifiInfoRepository wifiInfoRepository = new WifiInfoRepository();

    public int wifiInsert(List<WifiInfo> list) {
        return wifiInfoRepository.saveAll(list);
    }

    public List<WifiInfo> wifiOrderedSelect(String lat, String lnt, int limitCnt) {
        return wifiInfoRepository.findByOrderByDistance(lat, lnt, limitCnt);
    }

    public void cleanDbTable() {
        wifiInfoRepository.cleanTable();
    }
}