<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<!--
NOT IN USE
MAPPING MUST BE CHANGED
-->



<html>

<head>
<meta charset="UTF-8">
<title>Automated checklist analyser - analyse jsp</title>
<style>
<link href="<%=request.getContextPath()%>/lib/css/tooltip.css" rel="stylesheet" type="text/css">
</style>
</head>





<body>
<h1>Automated checklist analyser - analyse jsp</h1>
${param.id}


<div>
request.getContextPath() = <%= request.getContextPath()%>
</div>
<div>
request.getRemoteHost() = <%= request.getRemoteHost() %>
</div>

<div>
<a href="/lib/css/tooltip.css">
/lib/css/tooltip.css
</a>
</div>


<div class="tooltip_for_query">
<img src="<%=request.getContextPath()%>lib/img/glass.jpg" alt="Query">
<span class="tooltiptext"> ABC DEF GHI</span>
</div>






<div>
<!--***********FORM ACTION*************-->
<form action="/analyse?action=submit" method="get" id="checklist">




Checklist
<select name = "checklists" form="checklist" width = 100% tabindex="3">
            <option value = "TI" selected>TI (Default)</option>
            <option value = "2">2 (not impl.)</option>
            <option value = "3">3 (not impl.)</option>
</select>

<!-********************************MAIN BUTTON*********************-->
<button
class = "PON_filter"
type="submit"
title="Enter "
tabindex="4">
Analyse1
</button>
</form>


<div>
<a href="index.jsp">
index.jsp
</a>
</div>

</body>