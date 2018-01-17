<%@tag pageEncoding="UTF-8"%>
<%@attribute name="title" type="java.lang.String" required="false" %>
<%@attribute name="index" type="java.lang.Boolean" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if lt IE 7 ]><html class="ie ie6" lang="en"> <![endif]-->
<!--[if IE 7 ]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<html lang="en" xmlns="http://www.w3.org/1999/html"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">

    <meta http-equiv="Cache-Control" content="no-store" />
    <meta http-equiv="Pragma" content="no-cache" />
	
    <title>${title}</title>
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    <link rel="icon" href="favicon.ico" type="image/x-icon">
    <%@include file="/WEB-INF/views/common/import-css.jspf"%>
</head>
<body>
<embed id="AuthNoIE" type="application/x-dogauth" width=0 height=0 hidden="true"></embed>
<object id="AuthIE" name="AuthIE" width="0px" height="0px"
	style="background-color: blue;"
	codebase="DogAuth.CAB"
	classid="CLSID:05C384B0-F45D-46DB-9055-C72DC76176E3"> </object>
