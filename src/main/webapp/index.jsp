<html>

<head>
<title>TI checklist web-interface</title>
<!--
CSS from BIRT.
It breaks mark-up - cause is not detected!!!
<link href="http://himdlxbirt01/birt/static/css/bootstrap.css?v=6" rel="stylesheet" type="text/css">
<link href="http://himdlxbirt01/birt/static/css/birtstyles.css?v=8" rel="stylesheet" type="text/css">
-->

<script src="http://himdlxbirt01/birt/static/js/jquery.js" type="text/javascript"></script>

  <style type="text/css">
   table.common {
    width: 90%; /* Table width */
    border: 1px solid black; /* Frame around table */
    margin: auto; /* Align table by the center of the page */
   }
   td {
    text-align: center; /* Align text by the center of the cell */
   }


   table.PON_form {
     border: 1px solid black; /* Frame around table */
     margin: auto; /* Align table by the center of the page */
     }
     td.left {
     text-align: right; /* Align text by the right edge of the cell */
     }
     td.center {
     text-align: center; /* Align text by the center of the cell */
     }
     td.right {
     text-align: left; /* Align text by the left edge of the cell */
     }

     button, input[type=submit] {
     width: 100%;
     color: #ffffff;
     text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
     background-color: #006dcc;
     border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
     background-image: linear-gradient(to bottom, #0088cc, #0044cc);
     border-radius: 4px;
     font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
     font-weight: normal;
     -webkit-appearance: button;
     border: 1px solid #bbbbbb;
     padding: 4px 14px;
     }


    body {
    min-height: 100%;
    overflow-y: scroll;
    min-width: 500px;
    background-color: #F5F6F7;
    }

     input[type=checkbox]{
     -webkit-transform: scale(2);
     }
  </style>
</head>



<body>
<table class="common">


<div class="float_container">
<tr>
<td>
<h2>Automated TI checklist</h2>

</p>
</td>
</tr>
</div>






<tr>
<td>
<div class="PON_selector">


<fieldset>
<legend> Select PON/iteration</legend>




<!--***********FORM ACTION*************-->
<form action="/analyse?action=submit" method="get">





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
title="% as wildcard is applied at the end"
tabindex="1"
autofocus/>
</td>

<td>
<input
type="checkbox"
name="autocomplete_pon"
checked
title="Query for requested name will be used with LIKE + heading and tailing %"
tabindex="4">
Autocomplete
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
title="Replace with the actual value"
tabindex="2"/>
</td>
</tr>

<tr>
<td class = "right">
</td>
<td class = "center"
<!-********************************MAIN BUTTON*********************-->
<button
class = "PON_filter"
type="submit"
title="Enter PON (or it's part) and press button to start TI analysis"
tabindex="3">
Analyse
</button>
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
title="Use % as wildcard"
tabindex="5"/>
</td>

<td class = "right">
<input
type="checkbox"
name="autocomplete_prev_pon"
checked
title="Query for requested name will be used with LIKE + heading and tailing %"
tabindex="7">
Autocomplete
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
placeholder="Previous PON iteration"
title="Enter the actual value"
tabindex="6"/>
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
<hr align="center" width="80%" size="2" color="#0000ff" />
</td>
</tr>

<tr>
<td>
Steps are described in <a href="https://adc.luxoft.com/confluence/pages/viewpage.action?spaceKey=DBPROD&title=07.+TI"> DB production TI checklist</a><br/>
Implemented not fully!!!
Refer to checklist log
</td>
</tr>

<tr>
<td>
<hr align="center" width="80%" size="2" color="#ff0000" />
</td>
</tr>
<tr>

<tr>
<td>
<p>Testing area - to be used only after deploy</p>
<a href="http://172.30.136.166:8889/TIcheck/">http://172.30.136.166:8889/TIcheck/</a>
</td>
</tr>






</table>
</body>
</html>
