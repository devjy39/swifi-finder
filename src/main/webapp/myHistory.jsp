<%@ page import="com.zerobase.swififinder.service.LocationHistoryService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zerobase.swififinder.dto.LocationHistory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <link rel="stylesheet" type="text/css" href="css/stylecss.css">
    <script>
        function getDeleteById(historyId) {
            location.href = 'myHistory.jsp?id=' + historyId;

            <%
               String id = request.getParameter("id");
               if(id != null){
                   LocationHistoryService locationHistoryService = new LocationHistoryService();
                   locationHistoryService.historyDelete(id);
               }
            %>
        }
    </script>
</head>
<body>
  <h1>와이파이 정보 구하기</h1>
  <p>
    <a href="/">홈</a> |
    <a href="myHistory.jsp">위치 히스토리 목록</a> |
    <a href="index.jsp">Open API 와이파이 정보 가져오기</a>
  </p>
  <%
      LocationHistoryService locationHistoryService = new LocationHistoryService();
      List<LocationHistory> list = locationHistoryService.historySelect();
  %>

  <table id="customers">
      <tr>
          <th>ID</th>
          <th>X좌표</th>
          <th>Y좌표</th>
          <th>조회일자</th>
          <th>비고</th>
      </tr>
          <%
              if (list==null || list.isEmpty()) {
                  out.print("<td colspan=\"100%\" style=\"text-align: center;\">위치 히스토리 정보가 없습니다.</td>");
              } else {
                  for (LocationHistory history : list) {
                      System.out.println(history.getId());
                      out.write("<tr>\n");
                      out.write("<td>"+history.getId()+"</td>\n");
                      out.write("<td>"+history.getLat()+"</td>\n");
                      out.write("<td>"+history.getLnt()+"</td>\n");
                      out.write("<td>"+history.getDateTime()+"</td>\n");
                      out.write("<td style=\"text-align: center;\"><input type=\"button\" value=\"삭제\" onclick=\"getDeleteById("+history.getId()+")\"; /></td>\n");
                      out.write("</tr>\n");
                  }
              }
          %>
  </table>
</body>
</html>