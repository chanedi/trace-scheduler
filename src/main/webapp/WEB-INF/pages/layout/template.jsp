<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="resourcesPath" value="${pageContext.request.contextPath}/resources"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>
    <!-- Bootstrap -->
    <script src="${resourcesPath}/jquery/jquery-1.11.2.js"></script>
    <link href="${resourcesPath}/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="${resourcesPath}/bootstrap/js/bootstrap.js"></script>
    <!-- datatables -->
    <script src="${resourcesPath}/datatables/js/jquery.dataTables.js"></script>
    <link href="${resourcesPath}/datatables/css/jquery.dataTables.css" rel="stylesheet"/>

    <!-- other -->
    <script src="${resourcesPath}/datepick/WdatePicker.js"></script>
    <link href="${resourcesPath}/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>

    <script src="${resourcesPath}/js/zhiyi.js"></script>
    <link href="${resourcesPath}/css/zhiyi.css" rel="stylesheet">

</head>
<body>
<tiles:insertAttribute name="content"/>
</body>
</html>
