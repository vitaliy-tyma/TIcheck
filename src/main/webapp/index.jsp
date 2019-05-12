<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>Automated checklist analyser</title>
<link href="/lib/css/TIcheck.css" rel="stylesheet" type="text/css">

<!--
CSS from BIRT. It breaks the mark-up - cause is not detected!!!
<link href="http://himdlxbirt01/birt/static/css/bootstrap.css?v=6" rel="stylesheet" type="text/css">

<link href="http://himdlxbirt01/birt/static/css/birtstyles.css?v=8" rel="stylesheet" type="text/css">
<script src="http://himdlxbirt01/birt/static/js/jquery.js" type="text/javascript"></script>

  <style type="text/css">
  </style>
-->


</head>



<body>
<table class="common">


<div class="float_container">
<tr>
<td>
<h2>Automated checklist analyser</h2>
<hr align="center" width="80%" size="2" color="#0000ff" />
</p>
</td>
</tr>
</div>






<tr>
<td>
<div class="PON_selector">


<fieldset>
<legend> Select PON name and iteration</legend>




<!--***********FORM ACTION*************-->
<form action="/analyse?action=submit" method="get" id="checklist">





<div>
<table class="PON_form">
<tr>

<td class = "left">
Request</td>

<td class = "center">
<input
type="text"
name="pon_name"
placeholder="Enter PON or it's part"
title="Query with &quot;LIKE&quot; or &quot;=&quot; statement will be used depending on &quot;Use LIKE...&quot; checkbox"
tabindex="1"
maxlength="30"
size="20"
autofocus/>
</td>

<td>
<input
type="checkbox"
name="use_query_like"
checked
title="Query for requested name will be used with LIKE + heading and tailing %"
tabindex="9">
Use "LIKE %...%"
</checkbox>
</td>
</tr>

<tr>
<td class = "left">
Iteration
</td>

<td class = "center">
<input
type="text"
name="pon_iteration" value="1"
placeholder="Enter PON's iteration"
title="Replace with the actual value (_R# will be added for query with =)"
maxlength="2"
tabindex="2"/>
</td>
</tr>

<tr>
<td class = "left">
Checklist
</td>
<td class = "center">
<!-- //TODO Generate automatically on the basis of existing files-->


<select
name = "checklists"
form="checklist"
width = 100%
tabindex="3">
            <option value = "TI" selected>TI (Default)</option>
            <option value = "TIwithNoNDS">TIwithNoNDS</option>
            <option value = "3">3 (not impl.)</option>
         </select>
</td>
</tr>

<tr>
<td class = "left">
Limit data output
</td>
<td class = "center">
<select
name = "limit"
form="checklist"
width = 100%
tabindex="4">
            <option value = "10">10</option>
            <option value = "100" selected>100 (Default)</option>
            <option value = "1000">1000</option>
            <option value = "10000">10000</option>
         </select>
</td>
</tr>

<tr>
<td class = "left">

</td>
<td class = "center"
<!-********************************MAIN BUTTON*********************-->
<button
class = "PON_filter"
type="submit"
title="Enter PON (or it's part) and press button to start TI analysis"
tabindex="5">
Analyse
</button>
</td>


<td class = "right">

<input
type="checkbox"
name="regression_check"

title="Unset to disable regression check despite of entered data"
tabindex="10">
Regression check
</checkbox>

</td>

</tr>


<tr>
<td class = "left">
(Optional) Regression
</td>

<td class = "center">
<input
type="text"
name="prev_pon_name"
placeholder="Previous PON/part"
title="Query with &quot;LIKE&quot; or &quot;=&quot; statement will be used depending on &quot;Use LIKE...&quot; checkbox"
maxlength="30"
size="20"

tabindex="11"/>
</td>

<td class = "right">
<input
type="checkbox"
name="use_query_like_for_prev"
checked

title="Query for requested name will be used with LIKE + heading and tailing %"
tabindex="12">
Use "LIKE %...%"
</checkbox>
</td>
</tr>

<tr>
<td class = "left">
(Optional) Iteration
</td>

<td class = "center">
<input
type="text"
name="prev_pon_iteration"
value="1"
title="Enter the previous PON iteration number (_R# will be added for query with =)"
maxlength="2"

tabindex="13"/>
</td>

</tr>

</table>

</div>

</form>

</fieldset>

</div>
</td>
</tr>





<tr>
<td>
Steps for TI are described in <a href="https://adc.luxoft.com/confluence/pages/viewpage.action?spaceKey=DBPROD&title=07.+TI"> DB production TI checklist</a><br/>
<font color=red>Not all steps are Implemented!!!</font>
Refer to the actual checklist log.
</td>
</tr>



<tr>
<td>
<hr align="center" width="80%" size="2" color="#ff0000" />
<p>
Testing area - to be used only after deploy:
<a href="http://172.30.136.166:8889/TIcheck/">http://172.30.136.166:8889/TIcheck/</a>
</p>
</td>
</tr>






</table>
</body>
</html>
