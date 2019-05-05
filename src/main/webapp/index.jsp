<html>

<head>
<title>Automated checklist analyser</title>
<!--
CSS from BIRT.
It breaks mark-up - cause is not detected!!!
<link href="http://himdlxbirt01/birt/static/css/bootstrap.css?v=6" rel="stylesheet" type="text/css">
<link href="http://himdlxbirt01/birt/static/css/birtstyles.css?v=8" rel="stylesheet" type="text/css">
<script src="http://himdlxbirt01/birt/static/js/jquery.js" type="text/javascript"></script>
-->

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

     select {
         width: 100%;
        }






  </style>
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
title="Query with &quot;LIKE&quot; or &quot;=&quot; statement will be used depending on Autocomplete checkbox"
tabindex="1"
maxlength="30"
size="20"
autofocus/>
</td>

<td>
<input
type="checkbox"
name="autocomplete_pon"
checked
title="Query for requested name will be used with LIKE + heading and tailing %"
tabindex="9">
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
maxlength="2"
tabindex="2"/>
</td>
</tr>

<tr>
<td class = "left">
Checklist
</td>
<td class = "center">
<select name = "checklists" form="checklist" width = 100% tabindex="3">
            <option value = "TI" selected>TI</option>
            <option value = "2">2 (not impl.)</option>
            <option value = "3">3 (not impl.)</option>
         </select>
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
tabindex="4">
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
title="Query with &quot;LIKE&quot; or &quot;=&quot; statement will be used depending on Autocomplete checkbox"
maxlength="30"
size="20"

tabindex="10"/>
</td>

<td class = "right">
<input
type="checkbox"
name="autocomplete_prev_pon"
checked

title="Query for requested name will be used with LIKE + heading and tailing %"
tabindex="11">
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
value="1"
title="Enter the previous PON iteration number"
maxlength="2"

tabindex="12"/>
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
