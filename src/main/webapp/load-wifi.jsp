<%@ page import="com.zerobase.swififinder.util.ApiExplorer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <style>
        div {
            text-align: center;
        }
    </style>
</head>
<body>
    <%
        ApiExplorer apiExplorer = new ApiExplorer();
        int totalCnt = apiExplorer.openApiDataMigration();
    %>
    <div>
        <h1><%=totalCnt%>개의 WIFI 정보를 정삭적으로 저장하였습니다.</h1>
        <a href="/">홈으로 가기</a>
    </div>
</body>
</html>