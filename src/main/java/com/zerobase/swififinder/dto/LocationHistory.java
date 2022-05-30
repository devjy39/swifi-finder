package com.zerobase.swififinder.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LocationHistory {
    private int id;
    private double lat;
    private double lnt;
    private String dateTime;
}
