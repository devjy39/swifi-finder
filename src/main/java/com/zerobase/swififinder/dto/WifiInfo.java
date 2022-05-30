package com.zerobase.swififinder.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WifiInfo {
    private double distance;
    private String id;
    private String wrdofc;
    private String name;
    private String adress1;
    private String adress2;
    private String instFloor;
    private String instType;
    private String instMby;
    private String svcSe;
    private String cmcwr;
    private int instYear;
    private String inOutDoor;
    private String remars;
    private double lat;
    private double lnt;
    private String workDateTime;
}
