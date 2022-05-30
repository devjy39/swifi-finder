<%@ page import="com.zerobase.swififinder.service.LocationHistoryService" %>
<%@ page import="com.zerobase.swififinder.service.WifiInfoService" %>
<%@ page import="com.zerobase.swififinder.dto.WifiInfo" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <link rel="stylesheet" type="text/css" href="css/stylecss.css">
    <script>
        function getMyLocation(){
            navigator.geolocation.getCurrentPosition(
                position => {
                    ({
                        latitude: document.querySelector("#latitude").getAttributeNode("value").value,
                        longitude: document.querySelector("#longitude").getAttributeNode("value").value
                    } = position.coords);
                }
            )
        }

        function loadLocation() {
            location.href = 'index.jsp?lat=' + document.getElementById('latitude').value
                + '&lnt=' + document.getElementById('longitude').value;
        }
    </script>
</head>
<body>
    <h1>와이파이 정보 구하기</h1>
    <p>
        <a href="/">홈</a> |
        <a href="myHistory.jsp">위치 히스토리 목록</a> |
        <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
    </p>
    <p>
        <label>
            LAT:
            <input type="text" id="latitude" value=""/>,
        </label>
        <label>
            LNT:
            <input type="text" id="longitude" value=""/>
        </label>
        <input type="button" value="내 위치 가져오기" onclick="getMyLocation();"/>
        <input type="button" value="근처 WIFI 정보 보기" onclick="loadLocation();"/>
    </p>

    <table id="customers">
        <tr>
            <th>거리(Km)</th>
            <th>관리번호</th>
            <th>자치구</th>
            <th>와이파이명</th>
            <th>도로명주소</th>
            <th>상세주소</th>
            <th>설치위치(층)</th>
            <th>설치유형</th>
            <th>설치기관</th>
            <th>서비스구분</th>
            <th>망종류</th>
            <th>설치년도</th>
            <th>실내외구분</th>
            <th>WIFI접속환경</th>
            <th>X좌표</th>
            <th>Y좌표</th>
            <th>작업일자</th>
        </tr>
        <tr>
            <%
                String strLat = request.getParameter("lat");
                String strLnt = request.getParameter("lnt");

                if (strLat != null && strLnt != null) {
                    new LocationHistoryService().historyInsert(strLat, strLnt);

                    WifiInfoService wifiInfoService = new WifiInfoService();
                    List<WifiInfo> wifiList = wifiInfoService.wifiOrderedSelect(strLat, strLnt, 20);
                    for (WifiInfo wifiInfo : wifiList) {
                        out.write("<tr>");
                        out.write("<td>"+ wifiInfo.getDistance()+"</td>");
                        out.write("<td>"+ wifiInfo.getId()+"</td>");
                        out.write("<td>"+ wifiInfo.getWrdofc()+"</td>");
                        out.write("<td>"+ wifiInfo.getName()+"</td>");
                        out.write("<td>"+ wifiInfo.getAdress1() +"</td>");
                        out.write("<td>"+ wifiInfo.getAdress2() +"</td>");
                        out.write("<td>"+ wifiInfo.getInstFloor() +"</td>");
                        out.write("<td>"+ wifiInfo.getInstType() +"</td>");
                        out.write("<td>"+ wifiInfo.getInstMby() +"</td>");
                        out.write("<td>"+ wifiInfo.getSvcSe() +"</td>");
                        out.write("<td>"+ wifiInfo.getCmcwr() +"</td>");
                        out.write("<td>"+ wifiInfo.getInstYear() +"</td>");
                        out.write("<td>"+ wifiInfo.getInOutDoor() +"</td>");
                        out.write("<td>"+ wifiInfo.getRemars() +"</td>");
                        out.write("<td>"+ wifiInfo.getLat() +"</td>");
                        out.write("<td>"+ wifiInfo.getLnt()+"</td>");
                        out.write("<td>"+ wifiInfo.getWorkDateTime()+"</td>");
                        out.write("</tr>");
                    }
                } else {
                    out.print("<td colspan=\"100%\" style=\"text-align: center;\">위치 정보를 입력한 후에 조회해 주세요.</td>");
                }
            %>
        </tr>
    </table>
</body>
</html>