<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <title>TI checklist - analyse</title>
</head>





<body>
<section>
    <h3>TI checklist</h3>

    <jsp:useBean id="pon" scope="request" type="com.luxoft.falcon.model.Pon"/>


    <tr>

        <td>Name: ${pon.name} | Output: ${pon.output}</td>

        <td><a href="analyse?action=updateX">UpdateX</a></td>

    </tr>
</section>
</body>
</html>